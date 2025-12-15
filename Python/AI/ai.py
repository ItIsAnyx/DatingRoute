from fastapi import FastAPI, Header, HTTPException, Query
from typing import Optional
from pydantic import BaseModel
from dotenv import load_dotenv
import os
from transformers import pipeline
from json_converter import JSONConverter

# Загрузка переменных из .env
load_dotenv()
AI_SECRET_KEY = os.getenv("AI_SECRET_KEY")
HF_MODEL = os.getenv("HF_MODEL")
DEVICE = os.getenv("DEVICE")
MAX_CONTEXT_TOKENS = int(os.getenv("MAX_CONTEXT_TOKENS"))

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

class SummarizeRequest(BaseModel):
    context: list

class ResultSummarization(BaseModel):
    points: list

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
            # Пробуем распределить pipeline по устройствам и использовать квантование
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
    if not context or not isinstance(context, list):
        return []

    if len(context) > 2:
        try:
            prompt_text = pipe.tokenizer.apply_chat_template(context, tokenize=False, add_generation_prompt=False)
            tokens = pipe.tokenizer(prompt_text, return_tensors="pt")
            print(f"Токены в контексте: {tokens.input_ids.shape[1]}")

            removed = 0
            while tokens.input_ids.shape[1] > MAX_CONTEXT_TOKENS:
                context = context[2:]
                removed += 2
                prompt_text = pipe.tokenizer.apply_chat_template(context, tokenize=False, add_generation_prompt=False)
                tokens = pipe.tokenizer(prompt_text, return_tensors="pt")

            if removed > 0:
                print(f"[context] Удалено {removed} старых сообщений, токенов теперь: {tokens.input_ids.shape[1]}")

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
def get_answer(payload: MessageRequest, 
               test: bool = Query(False),
               messages=None, 
               api_key: str = Header(..., alias="AI_SECRET_KEY"), 
               generation_kwargs=None):
    verify_key(api_key)

    if (test):
        return MessageRequest(message="Вот созданный мною маршрут:", context=list())

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
                        """You are a helpful assistant which helps the user to make a route for a walk. On topics that have nothing to do with walks or the principles
                        of your work, answer that it is not your competence. Always respond in the same language as the user's message.
                        - NEVER use unescaped double quotes (") inside it.
                        - For place names, use «...», '...', or no quotes.
                        - Format example: "1. «Исаакиевский собор»"
                        
                        Conversation examples:
                        User: Как ты думаешь, сначала покушать в ресторане или сходить на экскурсию?
                        Assistant: Я думаю, что если вы только начали свою прогулку, то лучше сначала сходить на экскурсию, и после неё зайти в ресторан.
            
                        User: Откуда взялась поговорка 'Все дороги ведут в Рим'?
                        Assistant: Кажется, ваш запрос не касается планировки маршрутов. Если вы планируете прогуляться по Риму, то напишите свои пожелания, и я помогу составить вам маршрут.
            
                        User: Я хочу сходить в пышечную на Невском, которая закрывается в 20:00. Сейчас уже 19:30, мне идти до пышечной 10 минут, и там обычно долгие очереди. Как ты думаешь, я успею зайти в пышечную?
                        Assistant: Мне кажется, вы не успеете сходить в пышечную на Невском, однако я могу попробовать найти ближайшие к вам пышечные, которые не закроются до 22:00. Подобрать вам варианты?
                        """
                     },
                    {"role": "user", "content": user_text},
                ]
            else:
                messages = [
                    {"role": "system", "content":
                        """You are a helpful assistant which helps the user to make a route for a walk. On topics that have nothing to do with walks or the principles
                        of your work, answer that it is not your competence. Always respond in the same language as the user's message.
                        - NEVER use unescaped double quotes (") inside it.
                        - For book/movie titles, use «...», '...', or no quotes.
                        - Format example: "1. «Исаакиевский собор»"
                        
                        Conversation examples:
                        User: Как ты думаешь, сначала покушать в ресторане или сходить на экскурсию?
                        Assistant: Я думаю, что если вы только начали свою прогулку, то лучше сначала сходить на экскурсию, и после неё зайти в ресторан.
            
                        User: Откуда взялась поговорка 'Все дороги ведут в Рим'?
                        Assistant: Кажется, ваш запрос не касается планировки маршрутов. Если вы планируете прогуляться по Риму, то напишите свои пожелания, и я помогу составить вам маршрут.
            
                        User: Я хочу сходить в пышечную на Невском, которая закрывается в 20:00. Сейчас уже 19:30, мне идти до пышечной 10 минут, и там обычно долгие очереди. Как ты думаешь, я успею зайти в пышечную?
                        Assistant: Мне кажется, вы не успеете сходить в пышечную на Невском, однако я могу попробовать найти ближайшие к вам пышечные, которые не закроются до 22:00. Подобрать вам варианты?
                        """
                     },
                ] + context + [{"role": "user", "content": user_text}]

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
def get_chat_title(payload: MessageTitleRequest, 
                   api_key: str = Header(..., alias="AI_SECRET_KEY"), 
                   test: bool = Query(False)):
    verify_key(api_key)

    if (test):
        return MessageTitleResponse(title="Короткий маршрут по...", message="Вот построенный мною маршрут...", context=list())
    
    messages = [
        {
            "role": "system",
            "content": """You are a strict JSON generator assistant which helps the user to make a route for a walk. On topics that have nothing to do with walks or the principles
            of your work, answer that it is not your competence.

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
            10. Always respond in the same language as the user's message.

            Examples:
            User: Как ты думаешь, сначала покушать в ресторане или сходить на экскурсию?
            Assistant: {"title": "Ресторан или экскурсия", "message": "Я думаю, что если вы только начали свою прогулку, то лучше сначала сходить на экскурсию, и после неё зайти в ресторан."}

            User: Откуда взялась поговорка 'Все дороги ведут в Рим'?
            Assistant: {"title": "Истоки поговорки", "message": "Кажется, ваш запрос не касается планировки маршрутов. Если вы планируете прогуляться по Риму, то напишите свои пожелания, и я помогу составить вам маршрут."}

            User: Я хочу сходить в пышечную на Невском, которая закрывается в 20:00. Сейчас уже 19:30, мне идти до пышечной 10 минут, и там обычно долгие очереди. Как ты думаешь, я успею зайти в пышечную?
            Assistant: {"title": "Поздний поход в пышечную", "message": "Мне кажется, вы не успеете сходить в пышечную на Невском, однако я могу попробовать найти ближайшие к вам пышечные, которые не закроются до 22:00. Подобрать вам варианты?"}

            Now respond in this exact format for the next message.
            """
        },
        {"role": "user", "content": payload.message}
    ]
    generation_kwargs = {
        "max_new_tokens": 500,
        "temperature": 0.5,
        "do_sample": True,
    }
    request = MessageRequest(message=payload.message, context=[])
    result = get_answer(request, test, messages, api_key, generation_kwargs)
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
                generation_kwargs = {
                    "max_new_tokens": 50,
                    "temperature": 0.3,
                    "do_sample": True,
                }
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

@app.post("/api/response/summarize", response_model=ResultSummarization)
def summarize(payload: SummarizeRequest, api_key = Header(..., alias="AI_SECRET_KEY")):
    verify_key(api_key)
    messages = ([
    {
        "role": "system",
        "content": (
        "You extract the actual final list of route points from a dialogue.\n"
            "Rules:\n"
            "1. Only include places the user still wants to visit in the end.\n"
            "2. If the user says they don't want a place, remove it completely.\n"
            "3. If the user changes their mind, use the newest preference.\n"
            "4. A route point is: place, address, street, building, park, cafe, museum.\n"
            "5. Ignore assistant suggestions unless the user confirmed them.\n"
            "6. Output only the final route as a semicolon-separated list."
    )
    }] + payload.context +
    [
        {
            "role": "user",
            "content": "List all route points discussed in the conversation. Only the final actual route."
        }
    ])

    generation_kwargs = {
        "max_new_tokens": 350,
        "temperature": 0.1,
        "do_sample": True,
    }

    out = pipe(messages, **generation_kwargs)
    print(out)
    # Пробуем извлечь текст
    try:
        if isinstance(out, list) and len(out) > 0:
            first = out[0]
            print(first)
            text = first['generated_text'][-1]['content'] or first.get("generated_text") or first.get("text") or str(first) or str(first[0])
        else:
            text = str(out)

        # Защита от эхо, если модель вернула prompt + ответ. Тогда prompt удалим из начала ответа
        if text.strip().startswith("Summarize the text by building a list of the points on my route that we discussed. Reply in the language of the previous messages.".strip()):
            text = text.strip()[len("Summarize the text by building a list of the points on my route that we discussed. Reply in the language of the previous messages.".strip()):].strip()

        # Если пустой текст - ставим заглушку
        if not text:
            text = "..."

        text = text.split(";")
        for element in range(len(text)):
            text[element] = text[element].strip()
        return {"points": text}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Generation error: {e}")