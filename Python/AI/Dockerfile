FROM python:3.13-alpine AS builder
WORKDIR /install

RUN apk add --no-cache build-base

COPY requirements.txt .
RUN pip install --upgrade pip && \
	pip wheel --no-cache-dir --wheel-dir /wheels -r requirements.txt

FROM python:3.13-alpine
WORKDIR /app

ENV PYTHONDONTWRITEBYTECODE=1 \
	PYTHONUNBUFFERED=1

COPY --from=builder /wheels /wheels
COPY requirements.txt .

RUN pip install --no-cache-dir --no-index --find-links=/wheels -r requirements.txt \
	&& rm -rf /wheels /root/.cache/pip

COPY . .

EXPOSE 8082

CMD [ "python", "-m", "uvicorn", "ai:app", "--host", "0.0.0.0", "--port", "8082" ]
