from fastapi import FastAPI, Header, HTTPException
from pydantic import BaseModel
from dotenv import load_dotenv
import torch
import os

# Загрузка переменных из .env
load_dotenv()
AI_SECRET_KEY = os.getenv("AI_SECRET_KEY")

app = FastAPI(title="Простая проверка сервиса на передачу тензора")

# Опциональная Pydantic-модель для POST-запроса
class TestRequest(BaseModel):
	note: str = "no note"

# Функция проверки ключа
def verify_key(api_key: str):
	if api_key != AI_SECRET_KEY:
		raise HTTPException(status_code=403, detail="Forbidden: invalid key")

# Простой GET, возвращающий текст и тензор в формате JSON
@app.get("/test/check_response")
def check_response_get(api_key: str = Header(...)):
	verify_key(api_key)
	example = torch.tensor([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
	return {
	"message": "GET запрос успешно обработан",
	"tensor": example.tolist(),
	"tensor_shape": example.shape
	}

""" 
Простой POST, который принимает JSON вида {"note": "какое-то сообщение"}.
Возвращает текст с подтверждением и случайный тензор 2x3.
"""
@app.post(f"/test/check_response")
def check_response_post(payload: TestRequest, api_key: str = Header(...)):
	verify_key(api_key)
	example = torch.rand(size=(2, 3))
	return {
		"message": f"POST запрос успешно принят получено сообщение: {payload.note}",
		"tensor": example.tolist(),
		"tensor_shape": example.shape
	}