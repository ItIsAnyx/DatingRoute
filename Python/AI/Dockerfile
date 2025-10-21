FROM python:3.12.1 AS builder
WORKDIR /install

RUN apt-get update && apt-get install -y --no-install-recommends build-essential \
	&& rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --upgrade pip && \
	pip wheel --no-cache-dir --wheel-dir /wheels -r requirements.txt


FROM python:3.12.1
WORKDIR /app

ENV PYTHONDONTWRITEBYTECODE=1 \
	PYTHONUNBUFFERED=1 \
	TRANSFORMERS_CACHE=/root/.cache/huggingface \
	HF_HOME=/root/.cache/huggingface

COPY --from=builder /wheels /wheels
COPY requirements.txt .

RUN pip install --no-cache-dir --no-index --find-links=/wheels -r requirements.txt \
	&& rm -rf /wheels /root/.cache/pip

COPY . .

EXPOSE 8082

CMD [ "python", "-m", "uvicorn", "ai:app", "--host", "0.0.0.0", "--port", "8082" ]