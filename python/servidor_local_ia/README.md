# 🚀 Servidor Local de Pruebas - API Asistente de Salud Financiera

Este proyecto contiene un **Mock Server local** desarrollado con **FastAPI** que expone los modelos de Inteligencia Artificial y Machine Learning para el Asistente de Salud Financiera.

Su objetivo es permitir al equipo de desarrollo (backend/frontend) simular peticiones en tiempo real, verificar esquemas de entrada/salida y probar la integración antes del despliegue en producción.

---

## 📁 Estructura del Proyecto

```text
servidor_local_ia/
├── custom_models.py                          # Clases personalizadas de los modelos
├── label_encoder_perfil_conductual.pkl       # Encoder para categorías de perfil
├── modelo_clasificador_transacciones.pkl     # Modelo NLP (TF-IDF + Clasificador)
├── modelo_perfiles_conductual_optimo.pkl     # Modelo de evaluación conductual
├── server.py                                 # Código principal de la API en FastAPI
├── requirements.txt                          # Dependencias del proyecto
└── README.md                                 # Documentación del servidor
```

---

## 🛠️ Requisitos Previos

* **Python 3.10+**
* **Git Bash** (o terminal Linux/macOS)

---

## ⚙️ Instalación y Configuración Local

1. **Clonar o descargar la carpeta `servidor_local_ia`** y posicionarse en ella:
   ```bash
   cd servidor_local_ia
   ```

2. **Crear y activar un entorno virtual (recomendado):**
   ```bash
   # Crear el entorno virtual
   python -m venv venv

   # Activar en Git Bash / Linux / macOS
   source venv/Scripts/activate     # En Windows (Git Bash)
   # source venv/bin/activate       # En Linux / macOS
   ```

3. **Instalar dependencias:**
   ```bash
   pip install -r requirements.txt
   ```

---

## 🚀 Iniciar el Servidor

Ejecuta el siguiente comando para levantar el servidor en modo desarrollo con recarga automática:

```bash
uvicorn server:app --reload
```

Si todo inicia correctamente, verás en la consola:
```text
✅ ¡Todos los modelos de IA fueron cargados exitosamente!
INFO:     Uvicorn running on http://127.0.0.1:8000 (Press CTRL+C to quit)
```

---

## 📖 Documentación Interactiva (Swagger UI)

Una vez iniciado el servidor, abre tu navegador e ingresa a:

👉 **`[http://127.0.0.1:8000/docs](http://127.0.0.1:8000/docs)`**

Desde esta interfaz podrás probar todos los *endpoints* haciendo clic en **"Try it out"** y ejecutando peticiones JSON de prueba.

---

## 🔌 Endpoints Disponibles

### 1. `GET /`
* **Descripción:** Comprobación de estado (*Health Check*).
* **Respuesta:** Estado del servidor y link a la documentación.

---

### 2. `POST /predict-transaction`
* **Descripción:** Clasifica el texto de una transacción bancaria en categorías financieras (`Alimentación`, `Ocio`, `Transporte`, `Salud`, `Vivienda`, `Educación`, `Servicios`).
* **Request Body:**
  ```json
  {
    "descripcion": "Starbucks Providencia 4022"
  }
  ```
* **Response Body (Ejemplo):**
  ```json
  {
    "input_original": "Starbucks Providencia 4022",
    "input_limpio": "starbucks providencia",
    "resultado": {
      "categoria_final": "Ocio",
      "confianza": 0.965,
      "es_clasificacion_dudosa": false
    }
  }
  ```

---

### 3. `POST /predict-profile`
* **Descripción:** Evalúa las variables financieras del usuario y predice su perfil de salud financiera.
* **Request Body:**
  ```json
  {
    "ingreso_mensual": 3500.0,
    "nivel_endeudamiento": 30.5,
    "frecuencia_ahorro": "Media",
    "gasto_total": 2625.0,
    "ratio_gasto_ingreso": 0.75,
    "volatilidad_gasto": 0.15,
    "meses_cobertura": 2.5
  }
  ```
* **Response Body (Ejemplo):**
  ```json
  {
    "perfil_financiero": "En observación",
    "clase_numerica": 0
  }
  ```

---

## 💡 Notas Técnicas
* **Manejo de Transacciones Dudosas:** Si la confianza del modelo NLP es menor al 40% (0.40), la respuesta clasificará el movimiento como `"Otros"` y activará la bandera `"es_clasificacion_dudosa": true`.
* **Frecuencia de Ahorro:** Acepta únicamente los valores `"Baja"`, `"Media"` o `"Alta"`.