from fastapi import FastAPI, Header, HTTPException
from typing import Optional
from pydantic import BaseModel
from dotenv import load_dotenv
import os
from transformers import pipeline
from huggingface_hub import login
from json_converter import JSONConverter

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
    context: list

class MessageTitleRequest(BaseModel):
    message: str

class MessageTitleResponse(BaseModel):
    title: str
    message: str
    context: list

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

def reload_context(context: list):
    # Добавить проверку на соответствие формату контекста
    if len(context) > 0:
        try:
            prompt_text = pipe.tokenizer.apply_chat_template(context, tokenize=False, add_generation_prompt=False)
            tokens = pipe.tokenizer(prompt_text, return_tensors="pt")
            print(f"Токены в контексте: {tokens.input_ids.shape[1]}")
            return context
        except Exception as e:
            print(f"[Ошибка] Во время чтения контекста произошла ошибка: {e}")
            return context
    else:
        return context

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
    context = reload_context(payload.context)
    if pipe is None:
        raise HTTPException(status_code=503, detail="Model is not loaded. Check server logs or try GET /api/health")

    # Вызов модели
    user_text = payload.message
    try:
        # Параметры генерации
        if generation_kwargs == None:
            generation_kwargs = {
                "max_new_tokens": 450,
                "do_sample": True,
                "temperature": 0.7,
                # "top_p": 0.9,
                # "repetition_penalty": 1.05
            }
        if messages == None:
            if len(context) == 0:
                messages = [
                    {"role": "system", "content":
                        """You are a helpful assistant. 
                        - NEVER use unescaped double quotes (") inside it.
                        - For book/movie titles, use «...», '...', or no quotes.
                        - Example: "message": "1. «1984» by George Orwell"
                        """
                     },
                    {"role": "user", "content": user_text},
                ]
            else:
                messages = context + [
                    {"role": "system", "content":
                        """You are a helpful assistant. 
                        - NEVER use unescaped double quotes (") inside it.
                        - For book/movie titles, use «...», '...', or no quotes.
                        - Example: "message": "1. «1984» by George Orwell"
                        """
                     },
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

        final_context = context + [{"role": "user", "content": payload.message}, {"role": "assistant", "content": text}]
        final_context = reload_context(final_context)
        return {"message": text, "context": final_context}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Generation error: {e}")


@app.post("/api/response/create", response_model=MessageTitleResponse)
def get_chat_title(payload: MessageTitleRequest, api_key: str = Header(..., alias="AI_SECRET_KEY")):
    verify_key(api_key)
    messages = [
        {
            "role": "system",
            "content": """You are a strict JSON generator assistant.

            Your only task is to output a valid JSON object with the following two keys:
            {
              "title": "Short title for user's request (3–6 words)",
              "message": "Answer to the user's question"
            }

            Rules:
            1. ALWAYS return a valid JSON object, not plain text.
            2. DO NOT add explanations, greetings, markdown, or commentary.
            3. DO NOT include extra fields, quotes outside the object, or prose before/after JSON.
            4. The 'title' must summarize the user's request in 3–6 words.
            5. The 'message' must directly answer the question, in the same language the user used.
            6. If you are unsure, still output valid JSON with reasonable placeholders.
            7. If the user's input contains a proverb, riddle, or abstract topic — still output JSON in the same format.
            8. IMPORTANT: The "message" value must be a valid JSON string.
               - NEVER use unescaped double quotes (") inside it.
               - For book/movie titles, use «...», '...', or no quotes.
               - Example: "message": "1. «1984» by George Orwell"
            9. Don't add ```json to your answer. Start your answer right away with {.

            Examples:
            User: Сколько будет 2+2?
            Assistant: {"title": "Математическая задача", "message": "4"}

            User: Откуда взялась поговорка 'Все дороги ведут в Рим'?
            Assistant: {"title": "Истоки поговорки", "message": "Она появилась в Древнем Риме, так как действительно все дороги в то время вели в Рим."}

            User: Распиши поэтапно решение. Сколько будет 2.73^2+(879*2)-2!*3
            Assistant: {"title": "Решение математических уравнений", "message": "1. 2.73^2 = 7,4529\n2. 879 * 2 = 1758\n3. 2! = 2\n4. 2 * 3 = 6\n5. 7,4529 - 1758 = -1750,5471\n6. -1750,5471 - 6 = -1756,5471\nВ результате будет: -1756,5471."}

            Now respond in this exact format for the next message.
            """
        },
        {"role": "user", "content": payload.message}
    ]
    generation_kwargs = {
        "max_new_tokens": 150,
        "temperature": 0.5,
        "do_sample": True,
    }
    request = MessageRequest(message=payload.message, context=[])
    result = get_answer(request, messages, api_key, generation_kwargs)
    print(f"\nAnswer success: ", result)

    try:
        converter = JSONConverter(result["message"])
        json_result = converter.convert_to_json()
        if converter.check_exception() is not None:
            try:
                prompt = [
                    {"role": "system",
                     "content": "You are a helpful assistant, who comes up with short title for user's request (3–6 words). Now make a short title for user's request."},
                    {"role": "user", "content": payload.message}
                ]
                out = pipe(prompt, **generation_kwargs)
                # Пробуем извлечь текст
                if isinstance(out, list) and len(out) > 0:
                    first = out[0]
                    text = first['generated_text'][-1]['content'] or first.get("generated_text") or first.get(
                        "text") or str(first) or str(first[0])
                else:
                    text = str(out)

                user_text = result["message"]
                # Защита от эхо, если модель вернула prompt + ответ. Тогда prompt удалим из начала ответа
                if text.strip().startswith(user_text.strip()):
                    text = text.strip()[len(user_text.strip()):].strip()

                # Если пустой текст - ставим заглушку

                if not text:
                    text = "..."

                tojson = '{"title": "' + text + '", "message": "' + result["message"] + '"}'
                converter = JSONConverter(tojson)
                json_result = converter.convert_to_json()

            except Exception as e:
                raise HTTPException(status_code=500,
                                    detail=f"Error with trying to make a title with AI for not valid JSON response: {e}")

        final_context = [{"role": "user", "content": payload.message},
                         {"role": "assistant", "content": json_result["message"]}]
        context = reload_context(final_context)

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Convert to JSON error: {e}")

    print(f"\njson_result:\n{json_result}\ncontext: {context}")
    json_result["context"] = context
    return json_result