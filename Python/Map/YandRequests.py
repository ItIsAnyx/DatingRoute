from fastapi import FastAPI
from fastapi.responses import FileResponse
from dotenv import load_dotenv
from pydantic import BaseModel
from typing import List, Optional
import os
import requests

load_dotenv()
app = FastAPI()
YANDEX_KEY = os.getenv("YANDEX_MAPS_API_KEY")
GEOCODER_KEY = os.getenv("GEOCODER_KEY")

if not YANDEX_KEY:
    print("⚠ WARNING: YANDEX_MAPS_API_KEY not set in .env")

# Храним последний маршрут в памяти
latest_points: Optional[List[dict]] = None

class PointItem(BaseModel):
    name: str
    coords: List[float]  # [lat, lon]

class RouteResponse(BaseModel):
    type: str
    points: List[PointItem]
    params: dict

class GeocodeRequest(BaseModel):
    query: str

class GeocodeResponse(BaseModel):
    name: Optional[str]
    coords: List[float]

@app.get("/")
async def read_root():
    return FileResponse("Map.html")

@app.get("/api/config/maps-key")
def get_maps_key():
    return {"key": YANDEX_KEY}

@app.post("/api/route/latest")
def save_route(payload: List[PointItem]):
    global latest_points
    latest_points = payload  # сохраняем список PointItem
    return {"message": "Маршрут сохранён"}

@app.get("/api/route/latest", response_model=RouteResponse)
def get_route():
    points_to_use = latest_points or []
    return RouteResponse(
        type="walking",
        points=points_to_use,
        params={"routingMode": "pedestrian"}
    )

@app.post("/api/route/geocode", response_model=GeocodeResponse)
def geocode_point_api(payload: GeocodeRequest):
    result = geocode_point(payload.query)
    if not result:
        return {"name": payload.query, "coords": []}
    return result

def geocode_point(query: str):
    url = "https://geocode-maps.yandex.ru/v1/"
    params = {
        "apikey": GEOCODER_KEY,
        "geocode": query,
        "format": "json",
    }

    r = requests.get(url, params=params)
    print(r.url)
    print(r.text)

    try:
        data = r.json()
        members = data["response"]["GeoObjectCollection"]["featureMember"]

        if not members:
            return {"name": None, "coords": []}

        geo = members[0]["GeoObject"]
        pos = geo["Point"]["pos"]   # "30.315868 59.939095"
        lon, lat = map(float, pos.split())

        return {
            "name": geo["name"],
            "coords": [lat, lon]
        }

    except Exception as e:
        print("GEOCODING ERROR:", e)
        return {"name": None, "coords": []}
