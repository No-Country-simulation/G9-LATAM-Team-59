FROM python:3.11-slim

WORKDIR /app

ENV PYTHONUNBUFFERED=1
ENV PYTHONDONTWRITEBYTECODE=1

# Instalar dependencias necesarias para los modelos scikit-learn y pandas
RUN pip install --no-cache-dir fastapi uvicorn pandas scikit-learn==1.6.1 joblib

# Copiar el codigo y los artefactos .pkl
COPY . .

EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
