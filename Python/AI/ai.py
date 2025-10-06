from fastapi import FastAPI, Header, HTTPException
from pydantic import BaseModel
from dotenv import load_dotenv
import os

load_dotenv()
AI_SECRET_KEY = os.getenv("AI_SECRET_KEY")

app = FastAPI(title="Простая проверка сервиса на передачу тензора")

# Опциональная Pydantic-модель для POST-запроса
class RouteRequest(BaseModel):
	user_request: str

# Функция проверки ключа
def verify_key(api_key: str):
	if api_key != AI_SECRET_KEY:
		raise HTTPException(status_code=403, detail="Forbidden: invalid key")

@app.post("/response")
def get_test_route(payload: RouteRequest, api_key: str = Header(...)):
    verify_key(api_key)
    return {
        "response": payload.user_request,
        "message": "Конечно! Вот пример маршрута: сначала посетите Храм Христа Спасителя, затем отправляйтесь в ресторан Dr. Zhivago.",
        "route": {
            "mode": "car",
            "total_distance_km": 3.2,
            "estimated_time_min": 12,
            "points": [
                {
                    "name": "Храм Христа Спасителя",
                    "type": "temple",
                    "coordinates": [55.744639, 37.605523],
                },
                {
                    "name": "Ресторан Dr. Zhivago",
                    "type": "restaurant",
                    "coordinates": [55.752220, 37.610636],
                }
            ]
        },
        "alternatives": [
            {
                "for": "restaurant",
                "name": "Ресторан Белуга",
                "coordinates": [55.751874, 37.611495],
                "reason": "основное место закрыто"
            }
        ]
    }

@app.post("/response/create")
def new_chat(payload: RouteRequest, api_key: str = Header(...)):
    verify_key(api_key)
    return {
        "chat_name": "Музей, парк и ужин в итальянском ресторане",
        "response": payload.user_request,
        "message": f"Вот подходящий маршрут по вашему запросу: '{payload.user_request}'. "
            "Начните с Музея изобразительных искусств, затем прогуляйтесь по Парку Горького "
            "и закончите ужином в итальянском ресторане «Pasta Mia».",
        "route": {
            "mode": "walk",
            "total_distance_km": 1.7,
            "estimated_time_min": 30,
            "points": [
                {
                    "name": "Государственный музей изобразительных искусств им. Пушкина",
                    "type": "temple",
                    "coordinates": [55.7363, 37.6042],
                },
                {
                    "name": "Парк Горького",
                    "type": "park",
                    "coordinates": [55.752220, 37.610636],
                },
                {
                    "name": "Ресторан Pasta Mia",
                    "type": "restaurant",
                    "coordinates": [55.7289, 37.5955]
                }
            ]
        },
        "alternatives": [
            {
                "for": "restaurant",
                "name": "Ресторан Белуга",
                "coordinates": [55.751874, 37.611495],
                "reason": "основное место закрыто"
            }
        ]
    }