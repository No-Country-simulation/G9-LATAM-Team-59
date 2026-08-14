from contextlib import asynccontextmanager
import re  # <-- AGREGADO
import sys
from typing import List
import warnings

# 1. IMPORTANTE: Importar la clase personalizada antes de cargar los pkl
from custom_models import ClasificadorConUmbral
from fastapi import FastAPI, HTTPException
import joblib
import pandas as pd
from pydantic import BaseModel, Field

# Silenciar advertencias de versión en consola
warnings.filterwarnings("ignore", category=UserWarning, module="sklearn")

# Mapeamos la clase al módulo __main__ para la deserialización
sys.modules["__main__"].ClasificadorConUmbral = ClasificadorConUmbral


# --- FUNCIÓN DE LIMPIEZA DE TEXTO (AGREGADA) ---
def limpiar_texto(texto: str) -> str:
    if not isinstance(texto, str):
        return ""
    texto = texto.lower()  # Pasar a minúsculas
    texto = re.sub(
        r"[^\w\s]", "", texto
    )  # Eliminar puntuación / caracteres especiales
    texto = re.sub(r"\d+", "", texto)  # Eliminar números (ej: '4022')
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
        modelo_transacciones = joblib.load(
            "modelo_clasificador_transacciones.pkl"
        )
        modelo_perfil = joblib.load("modelo_perfiles_conductual_optimo.pkl")
        encoder_perfil = joblib.load("label_encoder_perfil_conductual.pkl")
        print("\n✅ ¡Todos los modelos de IA fueron cargados exitosamente!")
    except Exception as e:
        print(f"\n❌ Error al cargar los modelos: {e}")
    yield


app = FastAPI(
    title="API Local de Pruebas - Asistente de Salud Financiera",
    description="Servidor local para simular la IA en entorno de producción.",
    version="1.0.0",
    lifespan=lifespan,
)


# --- SCHEMAS DE ENTRADA Y SALIDA (Pydantic) ---
class TransaccionRequest(BaseModel):
    descripcion: str = Field(
        ..., example="Starbucks Providencia 4022", description="Texto del movimiento"
    )


class PerfilRequest(BaseModel):
    ingreso_mensual: float = Field(..., example=3500.0)
    nivel_endeudamiento: float = Field(..., example=30.5)
    frecuencia_ahorro: str = Field(
        ..., example="Media", description="Valores permitidos: Baja, Media, Alta"
    )
    gasto_total: float = Field(..., example=2625.0)
    ratio_gasto_ingreso: float = Field(..., example=0.75)
    volatilidad_gasto: float = Field(
        ..., example=0.15, description="Ejemplo: 0.15 para 15%"
    )
    meses_cobertura: float = Field(..., example=2.5)


# --- ENDPOINTS DE LA API ---


@app.get("/")
def home():
    return {
        "status": "online",
        "mensaje": "Servidor Local de IA activo y listo para pruebas.",
        "docs": "Visita http://127.0.0.1:8000/docs para probar los endpoints.",
    }


# Endpoint 1: Clasificación de Transacciones (NLP)
@app.post("/predict-transaction")
def predecir_transaccion(data: TransaccionRequest):
    if not modelo_transacciones:
        raise HTTPException(status_code=500, detail="Modelo NLP no cargado.")

    # 1. Limpiar el texto igual que en el entrenamiento
    texto_limpio = limpiar_texto(data.descripcion)

    # 2. Inferencia sobre el texto limpio
    resultado = modelo_transacciones.get_prediction_details([texto_limpio])

    return {
        "input_original": data.descripcion,
        "input_limpio": texto_limpio,
        "resultado": resultado[0],
    }


# Endpoint 2: Evaluación de Perfil Financiero (Conductual)
@app.post("/predict-profile", summary="Evaluar Perfil de Salud Financiera")
def predecir_perfil(data: PerfilRequest):
    if not modelo_perfil or not encoder_perfil:
        raise HTTPException(
            status_code=500, detail="Modelos de perfil conductual no cargados."
        )

    # Validar mapeo de ahorro
    ahorro_clean = data.frecuencia_ahorro.capitalize()
    if ahorro_clean not in MAPA_AHORRO:
        raise HTTPException(
            status_code=400,
            detail=f"frecuencia_ahorro debe ser uno de: {list(MAPA_AHORRO.keys())}",
        )

    # Construir DataFrame en el orden estricto de 7 variables
    df_input = pd.DataFrame(
        [
            {
                "ingreso_mensual": data.ingreso_mensual,
                "nivel_endeudamiento": data.nivel_endeudamiento,
                "frecuencia_ahorro_encoded": MAPA_AHORRO[ahorro_clean],
                "gasto_total": data.gasto_total,
                "ratio_gasto_ingreso": data.ratio_gasto_ingreso,
                "volatilidad_gasto": data.volatilidad_gasto,
                "meses_cobertura": data.meses_cobertura,
            }
        ]
    )

    # Inferencia
    pred_num = modelo_perfil.predict(df_input)
    perfil_texto = encoder_perfil.inverse_transform(pred_num)[0]

    return {"perfil_financiero": perfil_texto, "clase_numerica": int(pred_num[0])}