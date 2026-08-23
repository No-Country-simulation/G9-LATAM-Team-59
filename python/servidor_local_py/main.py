from contextlib import asynccontextmanager
import re
import sys
import warnings
from typing import Optional

from custom_models import ClasificadorConUmbral
from fastapi import FastAPI, HTTPException
import joblib
import pandas as pd
from pydantic import BaseModel

# Silenciar advertencias de versión en consola
warnings.filterwarnings("ignore", category=UserWarning, module="sklearn")

# Mapeamos la clase al módulo __main__ para la deserialización
sys.modules["__main__"].ClasificadorConUmbral = ClasificadorConUmbral


# --- FUNCIÓN DE LIMPIEZA DE TEXTO ---
def limpiar_texto(texto: str) -> str:
    if not isinstance(texto, str):
        return ""
    texto = texto.lower()  # Pasar a minúsculas
    texto = re.sub(r"[^\w\s]", "", texto)  # Eliminar puntuación
    texto = re.sub(r"\d+", "", texto)  # Eliminar números
    texto = re.sub(r"\s+", " ", texto).strip()  # Normalizar espacios
    return texto


# Variables globales para almacenar los modelos
modelo_transacciones = None
modelo_perfil = None
encoder_perfil = None

MAPA_AHORRO = {"Baja": 0, "Media": 1, "Alta": 2}


# Carga de modelos al iniciar el servidor
@asynccontextmanager
async def lifespan(app: FastAPI):
    global modelo_transacciones, modelo_perfil, encoder_perfil
    try:
        modelo_transacciones = joblib.load("modelo_clasificador_transacciones.pkl")
        modelo_perfil = joblib.load("modelo_perfiles_conductual_optimo.pkl")
        encoder_perfil = joblib.load("label_encoder_perfil_conductual.pkl")
        print("\n✅ ¡Todos los modelos e hiperparámetros mejorados fueron cargados exitosamente!")
    except Exception as e:
        print(f"\n❌ Error al cargar los modelos: {e}")
    yield


app = FastAPI(
    title="FinanceAI - API de Modelos ML Mejorados",
    description="Servidor local de IA con soporte para modelos NLP y evaluación conductual de salud financiera.",
    version="2.0.0",
    lifespan=lifespan,
)


# --- DTOs DE ENTRADA (COMPATIBLES CON BACKEND JAVA) ---
class TransaccionInput(BaseModel):
    descripcion: str


class PerfilInput(BaseModel):
    ingreso_mensual: float
    nivel_endeudamiento: float
    frecuencia_ahorro: str
    gasto_total: float
    ratio_gasto_ingreso: Optional[float] = None
    volatilidad_gasto: Optional[float] = None
    meses_cobertura: Optional[float] = None


# --- ENDPOINTS ---
@app.get("/")
def home():
    return {
        "status": "online",
        "mensaje": "Servidor Local de IA (Modelos Mejorados) activo y listo para el backend Java.",
        "modelos_cargados": modelo_transacciones is not None and modelo_perfil is not None,
    }


@app.post("/api/clasificacion")
def predict_transaccion(data: TransaccionInput):
    if not modelo_transacciones:
        return {"categoria": "Alimentación (Mock)"}

    texto_limpio = limpiar_texto(data.descripcion)
    details = modelo_transacciones.get_prediction_details([texto_limpio])
    categoria = details[0]["categoria_final"]

    return {
        "categoria": categoria
    }


@app.post("/api/analisis")
def predict_perfil(data: PerfilInput):
    if not modelo_perfil or not encoder_perfil:
        return {
            "perfil_financiero": "En riesgo (Mock)",
            "probabilidad": {"En riesgo": 84.5, "En observación": 12.3, "Saludable": 3.2},
        }

    ahorro_clean = data.frecuencia_ahorro.capitalize()
    ahorro_encoded = MAPA_AHORRO.get(ahorro_clean, 1)

    ratio_gasto = data.ratio_gasto_ingreso
    if ratio_gasto is None:
        ratio_gasto = data.gasto_total / data.ingreso_mensual if data.ingreso_mensual > 0 else 0.0

    volatilidad = data.volatilidad_gasto
    if volatilidad is None:
        volatilidad = 0.15

    cobertura = data.meses_cobertura
    if cobertura is None:
        cobertura = (
            max(0.0, (data.ingreso_mensual - data.gasto_total) / (data.gasto_total + 1e-5))
            if data.gasto_total > 0
            else 2.5
        )

    df_input = pd.DataFrame(
        [
            {
                "ingreso_mensual": data.ingreso_mensual,
                "nivel_endeudamiento": data.nivel_endeudamiento,
                "frecuencia_ahorro_encoded": ahorro_encoded,
                "gasto_total": data.gasto_total,
                "ratio_gasto_ingreso": ratio_gasto,
                "volatilidad_gasto": volatilidad,
                "meses_cobertura": cobertura,
            }
        ]
    )

    pred_num = modelo_perfil.predict(df_input)
    perfil_texto = str(encoder_perfil.inverse_transform(pred_num)[0])

    probs = modelo_perfil.predict_proba(df_input)[0]
    clases = encoder_perfil.classes_
    prob_dict = {str(clase): round(float(prob) * 100, 1) for clase, prob in zip(clases, probs)}

    return {
        "perfil_financiero": perfil_texto,
        "probabilidad": prob_dict,
    }