from fastapi import FastAPI, Header, HTTPException
from typing import Optional
from pydantic import BaseModel
from dotenv import load_dotenv
import os
from transformers import pipeline
from huggingface_hub import login

# Загрузка переменных из .env
load_dotenv()
AI_SECRET_KEY = os.getenv("AI_SECRET_KEY")
HF_MODEL = os.getenv("HF_MODEL")
DEVICE = os.getenv("DEVICE", "cpu")
HF_TOKEN = os.getenv("HF_TOKEN")

app = FastAPI(title="Простые запрос-ответы к нейросети")

# Опциональные Pydantic-модели для POST-запросов
class MessageRequest(BaseModel):
    message: str

class MessageTitleRequest(BaseModel):
    title: str
    message: str

# Авторизация в Hugging Face
if HF_TOKEN:
    try:
        login(token=HF_TOKEN)
        print("[auth] Авторизация Hugging Face прошла успешно.")
    except Exception as e:
        print(f"[auth] Ошибка при авторизации HF: {e}")
else:
    print("[auth] Переменная HF_TOKEN не найдена. Модель может быть недоступна.")

# Функция проверки ключа
def verify_key(api_key: Optional[str]):
    if not AI_SECRET_KEY:
        # безопасности ради: если AI_SECRET_KEY не задан, запрещаем доступ
        raise HTTPException(status_code=500, detail="Server misconfiguration: AI_SECRET_KEY is not set.")
    if not api_key or api_key != AI_SECRET_KEY:
        raise HTTPException(status_code=403, detail="Forbidden: invalid key")

# Загрузка модели при старте приложения
pipe = None
@app.on_event("startup")
def load_model():
    global pipe
    print(f"[startup] Загрузка модели {HF_MODEL} на устройстве {DEVICE}...")
    try:
        if DEVICE.lower() == "cpu":
            pipe = pipeline("text-generation", model=HF_MODEL, device=-1)
        elif DEVICE.lower() == "auto":
            # Пробуем распределить pipeline по устройствам
            pipe = pipeline("text-generation", model=HF_MODEL, device_map="auto")
        else:
            try:
                # Пробуем использовать конкретный GPU id (например 0)
                gpu_id = int(DEVICE)
                pipe = pipeline("text-generation", model=HF_MODEL, device=gpu_id)
            except Exception:
                # Возвращаемся на CPU
                pipe = pipeline("text-generation", model=HF_MODEL, device=-1)
        print("[startup] Модель успешно загружена.")
    except Exception as e:
        pipe = None
        print("[startup] Не удалось загрузить модель:", e)

# Метод для проверки, загружена ли модель
@app.get("/api/health")
def health():
    return {"status": "ok",
            "model_loaded": pipe is not None,
            "model": HF_MODEL}


"""
Главный endpoint - отправляет запрос к нейросети и возвращает ответ.
Принимает JSON { "message": "..." } и возвращает { "message": "..." }
Требует заголовок x-api-key для аутентификации.
"""
@app.post("/api/response", response_model=MessageRequest)
def get_answer(payload: MessageRequest, messages=None, api_key: str = Header(..., alias="AI_SECRET_KEY"), generation_kwargs=None):
    verify_key(api_key)

    if pipe is None:
        raise HTTPException(status_code=503, detail="Model is not loaded. Check server logs or try GET /api/health")

    # Вызов модели
    user_text = payload.message
    try:
        # Параметры генерации
        if generation_kwargs == None:
            generation_kwargs = {
                "max_new_tokens": 150,
                "do_sample": True,
                "temperature": 0.7,
                # "top_p": 0.9,
                # "repetition_penalty": 1.05
            }
        if messages == None:
            messages = [
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": user_text},
            ]

        # Вызов pipeline для получения ответа
        out = pipe(messages, **generation_kwargs)

        # Пробуем извлечь текст
        if isinstance(out, list) and len(out) > 0:
            first = out[0]
            print(first)
            text = first['generated_text'][-1]['content'] or first.get("generated_text") or first.get("text") or str(first) or str(first[0])
        else:
            text = str(out)

        # Защита от эхо, если модель вернула prompt + ответ. Тогда prompt удалим из начала ответа
        if text.strip().startswith(user_text.strip()):
            text = text.strip()[len(user_text.strip()):].strip()

        # Если пустой текст - ставим заглушку

        if not text:
            text = "..."

        return {"message": text}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Generation error: {e}")


@app.post("/api/response/create", response_model=MessageTitleRequest)
def get_chat_title(payload: MessageRequest, api_key: str = Header(..., alias="AI_SECRET_KEY")):
    verify_key(api_key)
    messages = [
        {"role": "system", "content": (
            "Generate a short (3–6 words) title for the user's message. "
            "Return only the title, no explanations or greetings."
            "Example:\n"
            "User: I like reading books. Can you recommend some?\n"
            "Title: Book Recommendations\n\n"
            "Now generate a title for the next message."
        )},
        {"role": "user", "content": payload.message}
    ]
    generation_kwargs = {
        "max_new_tokens": 25,
        "do_sample": False,
        # "repetition_penalty": 1.05
    }
    # Два запроса к нейронке. Один - для генерации названия чата, другой - для генерации ответа на запрос пользователя
    chat_name = get_answer(payload, messages, api_key, generation_kwargs)
    print(f"Chat name success: ", chat_name)
    answer = get_answer(payload, api_key=api_key)
    print(f"Answer success: ", answer)

    result = {"title": chat_name["message"], "message": answer["message"]}
    print(result)
    return result