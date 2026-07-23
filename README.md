# 🚀 FinanceAI

**FinanceAI** es una solución inteligente para analizar la salud financiera de un usuario a partir de sus transacciones e indicadores clave.  
El proyecto clasifica gastos, evalúa perfiles financieros y genera recomendaciones personalizadas a través de una **API REST**.

---

## 🎯 Objetivo

Construir un **MVP funcional** que permita:

- ✅ Clasificar transacciones en categorías financieras.
- 📊 Analizar el perfil financiero del usuario.
- 💡 Generar recomendaciones de mejora.
- 🌐 Exponer los resultados mediante **endpoints REST**.
- ☁️ Integrar el flujo con **Ciencia de Datos** y al menos un servicio de **OCI**.

---

## 🧩 Alcance del MVP

El MVP se enfoca en un flujo simple de extremo a extremo:

1. El usuario ingresa sus datos financieros.
2. El frontend envía la información al backend.
3. El backend valida, procesa y orquesta el análisis.
4. El módulo de **Ciencia de Datos** clasifica y predice el perfil financiero.
5. El backend responde con un **JSON unificado** que incluye el análisis y las recomendaciones.

---

## 🏗️ Arquitectura general

| Capa | Tecnología | Función |
|------|------------|---------|
| 🖥️ **Frontend** | Interfaz simple | Ingreso de datos y visualización de resultados. |
| ⚙️ **Backend** | Spring Boot (API REST) | Recepción, validación y orquestación del flujo. |
| 🧠 **Ciencia de Datos** | Python | Clasificación de transacciones y predicción de perfil financiero. |
| ☁️ **OCI** | Oracle Cloud Infrastructure | Despliegue y/o almacenamiento en la nube. |

---

## 📌 Endpoints principales

### 🔹 Clasificación de transacciones

- **Método:** `POST`  
- **Ruta:** `/api/clasificar-transacciones`  
- **Descripción:** Recibe un conjunto de transacciones y devuelve la categoría financiera correspondiente.

---

### 🔹 Análisis financiero

- **Método:** `POST`  
- **Ruta:** `/api/analisis-financiero`  
- **Descripción:** Recibe datos financieros del usuario, procesa la información y devuelve:
  - Perfil financiero
  - Probabilidad
  - Resumen de gastos
  - Recomendaciones personalizadas

---

## 🛠️ Tecnologías utilizadas

- Java + Spring Boot  
- Python (pandas, scikit-learn, etc.)  
- Oracle Cloud Infrastructure (OCI)  
- HTML/CSS/JS (Frontend básico)  
- Maven / Gradle  
- Postman (pruebas de API)

---
