from fastapi import FastAPI, Header, HTTPException, Query
from typing import Optional
from pydantic import BaseModel
from dotenv import load_dotenv
import os
from openai import OpenAI
from json_converter import JSONConverter

# Загрузка переменных из .env
load_dotenv()
AI_SECRET_KEY = os.getenv("AI_SECRET_KEY")
AI_BACKEND_KEY = os.getenv("AI_BACKEND_KEY")

app = FastAPI(title="Простые запрос-ответы к нейросети")
client = OpenAI(api_key=AI_SECRET_KEY, base_url="https://api.deepseek.com")

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
    if not AI_BACKEND_KEY:
        raise HTTPException(
            status_code=500,
            detail="Server misconfiguration: AI_BACKEND_KEY is not set."
        )
    if api_key != AI_BACKEND_KEY:
        raise HTTPException(status_code=403, detail="Forbidden: invalid key")

@app.on_event("startup")
def load_model():
    print("Запущена модель")

def call_llm(messages):
    out = client.chat.completions.create(model="deepseek-chat", messages=messages)
    return out.choices[0].message.content

def trim_context(context: list, keep_last=8):
    if len(context) <= keep_last + 1:
        return context

    first_user = next(
        (m for m in context if m["role"] == "user"),
        None
    )

    tail = context[-keep_last:]
    return ([first_user] if first_user else []) + tail

# Метод для проверки, загружена ли модель
@app.get("/api/health")
def health():
    return {"status": "ok",
            "model_loaded": client is not None,
            }

"""
Главный endpoint - отправляет запрос к нейросети и возвращает ответ.
Принимает JSON { "message": "..." } и возвращает { "message": "..." }
Требует заголовок x-api-key для аутентификации.
"""
@app.post("/api/response", response_model=MessageRequest)
def get_answer(payload: MessageRequest, 
               test: bool = Query(False),
               api_key: str = Header(..., alias="AI_BACKEND_KEY")):
    verify_key(api_key)

    if (test):
        return MessageRequest(message="Вот созданный мною маршрут:", context=list())

    context = payload.context

    # Вызов модели
    user_text = payload.message
    try:
        if len(context) == 0:
            messages = [
                {"role": "system", "content":
                    """You help users plan walking routes and suggest places to visit.
                    You may recommend cafes, restaurants, attractions, parks and streets.
                    If the topic is unrelated to places or routes, say it is outside your competence.
                    Reply in the user's language."""
                 },
                {"role": "user", "content": user_text},
            ]
        else:
            messages = [
                {"role": "system", "content":
                    """You help users plan walking routes and suggest places to visit.
                    You may recommend cafes, restaurants, attractions, parks and streets.
                    If the topic is unrelated to places or routes, say it is outside your competence.
                    Reply in the user's language."""
                 },
            ] + context + [{"role": "user", "content": user_text}]

        # Вызов модели для получения ответа
        text_response = call_llm(messages)

        final_context = context + [{"role": "user", "content": payload.message}, {"role": "assistant", "content": text_response}]
        final_context = trim_context(final_context)
        return {"message": text_response, "context": final_context}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Generation error: {e}")


@app.post("/api/response/create", response_model=MessageTitleResponse)
def get_chat_title(payload: MessageTitleRequest, 
                   api_key: str = Header(..., alias="AI_BACKEND_KEY"),
                   test: bool = Query(False)):
    verify_key(api_key)

    if (test):
        return MessageTitleResponse(title="Короткий маршрут по...", message="Вот построенный мною маршрут...", context=list())
    
    messages = [
        {
            "role": "system",
            "content": """
            Return a JSON object:
            {
              "title": "3–6 word title",
              "message": "Answer to the user"
            }
            No extra text. Same language as user.
            Answer about routes or places (cafes, attractions, walking spots).
            If the topic is unrelated, say it is outside your competence.
            """
        },
        {"role": "user", "content": payload.message}
    ]
    result = call_llm(messages)

    try:
        converter = JSONConverter(result)
        json_result = converter.convert_to_json()
        if converter.check_exception() is not None:
            try:
                prompt = [
                    {"role": "system",
                     "content": "You are a helpful assistant, who comes up with short title for user's request (3–6 words). Now make a short title for user's request."},
                    {"role": "user", "content": payload.message}
                ]
                out = call_llm(prompt)

                converter = JSONConverter(out)
                json_result = converter.convert_to_json()

            except Exception as e:
                raise HTTPException(status_code=500,
                                    detail=f"Error with trying to make a title with AI for not valid JSON response: {e}")

        final_context = [{"role": "user", "content": payload.message},
                         {"role": "assistant", "content": json_result["message"]}]

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Convert to JSON error: {e}")

    print(f"\njson_result:\n{json_result}\ncontext: {final_context}")
    json_result["context"] = final_context
    return json_result

@app.post("/api/response/summarize", response_model=ResultSummarization)
def summarize(payload: SummarizeRequest, api_key = Header(..., alias="AI_BACKEND_KEY")):
    verify_key(api_key)
    messages = ([
    {
        "role": "system",
        "content": """
        Extract final route points from the dialogue.
        Keep only confirmed places.
        If no route was planned, output nothing.
        Output a semicolon-separated list only.
        """
    }] + payload.context +
    [
        {
            "role": "user",
            "content": "List the final route points."
        }
    ])

    out = client.chat.completions.create(model="deepseek-chat", messages=messages, max_tokens=400)
    print(out)
    # Пробуем извлечь текст
    try:
        text_response = out.choices[0].message.content

        text = text_response.split(";")
        for element in range(len(text)):
            text[element] = text[element].strip()
        return {"points": text}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Generation error: {e}")