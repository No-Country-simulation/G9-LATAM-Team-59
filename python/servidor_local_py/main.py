from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import pandas as pd

app = FastAPI(
    title="FinanceAI - API de Modelos ML",
    description="Servidor local para pruebas de integración con Backend Java",
    version="1.0.0"
)

# Intentar cargar los artefactos .pkl
try:
    tfidf_vec = joblib.load('tfidf_vectorizer.pkl')
    nlp_model = joblib.load('modelo_clasificador_transacciones.pkl')
    ahorro_encoder = joblib.load('label_encoder_frecuencia_ahorro_volatiles.pkl')
    scaler = joblib.load('scaler_perfiles_volatiles.pkl')
    risk_model = joblib.load('modelo_clasificador_perfiles_rf_volatiles.pkl')
    MODELS_LOADED = True
    print("✅ Modelos y transformadores .pkl cargados correctamente.")
except Exception as e:
    MODELS_LOADED = False
    print(f"⚠️ No se encontraron todos los artefactos .pkl ({e}).")
    print("⚠️ El servidor responderá con datos de prueba (Mock).")

# DTOs de entrada
class TransaccionInput(BaseModel):
    descripcion: str

class PerfilInput(BaseModel):
    ingreso_mensual: float
    nivel_endeudamiento: float
    frecuencia_ahorro: str
    gasto_total: float

# Endpoints
@app.post("/api/clasificacion")
def predict_transaccion(data: TransaccionInput):
    if MODELS_LOADED:
        vec_input = tfidf_vec.transform([data.descripcion])
        categoria = str(nlp_model.predict(vec_input)[0])
    else:
        categoria = "Alimentación (Mock)"

    return {
        "categoria": categoria
    }

@app.post("/api/analisis")
def predict_perfil(data: PerfilInput):
    if MODELS_LOADED:
        ratio_gasto = data.gasto_total / data.ingreso_mensual if data.ingreso_mensual > 0 else 0.0
        ahorro_cod = ahorro_encoder.transform([data.frecuencia_ahorro])[0]
        
        df_raw = pd.DataFrame([{
            'ingreso_mensual': data.ingreso_mensual,
            'nivel_endeudamiento': data.nivel_endeudamiento,
            'frecuencia_ahorro_encoded': ahorro_cod,
            'gasto_total': data.gasto_total,
            'ratio_gasto_ingreso': ratio_gasto
        }])
        
        df_scaled = pd.DataFrame(scaler.transform(df_raw), columns=df_raw.columns)
        perfil = str(risk_model.predict(df_scaled)[0])
        probs = risk_model.predict_proba(df_scaled)[0]
        prob_dict = {clase: round(float(prob) * 100, 1) for clase, prob in zip(risk_model.classes_, probs)}
    else:
        perfil = "En riesgo (Mock)"
        prob_dict = {"En riesgo": 84.5, "En observación": 12.3, "Saludable": 3.2}

    return {
        "perfil_financiero": perfil,
        "probabilidad": prob_dict
    }