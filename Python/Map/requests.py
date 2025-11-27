from fastapi import FastAPI
from fastapi.responses import FileResponse
from dotenv import load_dotenv
from pydantic import BaseModel
from typing import List
import os

load_dotenv()
latest_points = None

app = FastAPI()

class PointItem(BaseModel):
    name: str
    coords: List[float]

class RouteRequest(BaseModel):
    points: List[PointItem]

class RouteResponse(BaseModel):
    type: str
    points: List[PointItem]
    params: dict

@app.get("/")
async def read_root():
    return FileResponse("Map.html")

@app.get("/api/config/maps-key")
def get_maps_key():
    return {"key": os.getenv("YANDEX_MAPS_API_KEY")}

@app.post("/api/route/latest")
def save_route(payload: RouteRequest):
    global latest_points
    latest_points = payload.points
    return {"message": "Маршрут сохранён"}

# GET: получить последний сохранённый маршрут
@app.get("/api/route/latest", response_model=RouteResponse)
def get_route():
    # Если ещё ничего не сохраняли — возвращаем пустой маршрут
    points_to_use = latest_points or []
    return RouteResponse(
        type="walking",
        points=points_to_use,
        params={"routingMode": "pedestrian"}
    )