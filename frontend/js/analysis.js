import { autoResize, fetchBackend, getBackendSettings } from "./utils.js";

export const DATOS_PRUEBA_ANALISIS = {
  ingreso_mensual: 5000,
  nivel_endeudamiento: 25,
  frecuencia_ahorro: "Media",
  transacciones: [
    { descripcion: "Supermercado Carrefour", valor: 420.0 },
    { descripcion: "Carga de nafta YPF", valor: 300.0 },
    { descripcion: "Suscripción Netflix", valor: 40.0 },
  ],
};

function getTransaccionesAnalisis() {
  const contenedor = document.getElementById("transaccionesAnalisis");
  if (!contenedor) return [];

  return Array.from(
    contenedor.getElementsByClassName("transaccion-analisis"),
  ).map((item) => {
    const descripcionEl = item.querySelector(".descripcion-analisis");
    const valorEl = item.querySelector(".valor-analisis");
    return {
      descripcion: descripcionEl ? descripcionEl.value : "",
      valor: valorEl ? Number(valorEl.value || 0) : 0,
    };
  });
}

export function cargarDatosPruebaAnalisis() {
  resetAnalisisForm();
  const ingresoEl = document.getElementById("ingreso");
  const endeudamientoEl = document.getElementById("endeudamiento");
  const ahorroEl = document.getElementById("ahorro");
  const contenedor = document.getElementById("transaccionesAnalisis");

  if (ingresoEl) ingresoEl.value = DATOS_PRUEBA_ANALISIS.ingreso_mensual;
  if (endeudamientoEl) {
    endeudamientoEl.value = DATOS_PRUEBA_ANALISIS.nivel_endeudamiento;
  }
  if (ahorroEl) ahorroEl.value = DATOS_PRUEBA_ANALISIS.frecuencia_ahorro;

  if (contenedor) {
    contenedor.innerHTML = "";
    DATOS_PRUEBA_ANALISIS.transacciones.forEach((item) => {
      const div = document.createElement("div");
      div.className = "transaccion-analisis mb-3";
      div.innerHTML =
        '<label class="form-label">Descripción de la transacción</label>' +
        '<input type="text" class="descripcion-analisis form-control form-control-sm mb-3" value="' +
        (item.descripcion || "").replace(/"/g, "&quot;") +
        '" />' +
        '<label class="form-label">Valor</label>' +
        '<input type="number" class="valor-analisis form-control form-control-sm mb-3 numeric-only" inputmode="numeric" pattern="[0-9]*" value="' +
        (item.valor ?? "") +
        '" />' +
        "<hr />";
      contenedor.appendChild(div);
    });
  }
}

export function cargarDatosPruebaJSONAnalisis() {
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (jsonArea) {
    jsonArea.value = JSON.stringify(DATOS_PRUEBA_ANALISIS, null, 2);
    autoResize(jsonArea);
  }
}

export function agregarTransaccionAnalisis() {
  const contenedor = document.getElementById("transaccionesAnalisis");
  if (!contenedor) return;

  const div = document.createElement("div");
  div.className = "transaccion-analisis mb-3";
  div.innerHTML =
    '<label class="form-label">Descripción de la transacción</label>' +
    '<input type="text" class="descripcion-analisis form-control form-control-sm mb-3" />' +
    '<label class="form-label">Valor</label>' +
    '<input type="number" class="valor-analisis form-control form-control-sm mb-3 numeric-only" inputmode="numeric" pattern="[0-9]*" />' +
    "<hr />";
  contenedor.appendChild(div);
}

export function eliminarTransaccionAnalisis() {
  const contenedor = document.getElementById("transaccionesAnalisis");
  if (!contenedor) return;

  const items = contenedor.getElementsByClassName("transaccion-analisis");
  if (items.length > 1) {
    contenedor.removeChild(items[items.length - 1]);
  } else if (items.length === 1) {
    const first = items[0];
    const desc = first.querySelector(".descripcion-analisis");
    const val = first.querySelector(".valor-analisis");
    if (desc) desc.value = "";
    if (val) val.value = "";
  }
}

export function enviarDatos() {
  const ingresoEl = document.getElementById("ingreso");
  const endeudamientoEl = document.getElementById("endeudamiento");
  const ahorroEl = document.getElementById("ahorro");

  const payload = {
    ingreso_mensual: Number(ingresoEl?.value || 0),
    nivel_endeudamiento: Number(endeudamientoEl?.value || 0),
    frecuencia_ahorro: ahorroEl?.value || "Nula",
    transacciones: getTransaccionesAnalisis(),
  };

  const settings = getBackendSettings("/api/analisis-financiero");
  if (settings.useBackend) {
    fetchBackend(settings.endpoint, payload, "resultadoAnalisis");
    return;
  }

  const mockResponse = {
    resumen:
      "Tu perfil muestra un equilibrio razonable entre ingresos y gastos.",
    riesgo: payload.nivel_endeudamiento > 35 ? "Alto" : "Moderado",
    ahorro_recomendado: Math.max(0, payload.ingreso_mensual * 0.2),
    gastos_totales: payload.transacciones.reduce(
      (acc, item) => acc + Number(item.valor || 0),
      0,
    ),
    recomendaciones: [
      "Prioriza un fondo de emergencia mensual.",
      "Reduce gastos recurrentes que no aporten valor.",
    ],
  };

  mostrarResultadoAnalisis(mockResponse);
}

export function crearMockResponseAnalisis() {
  return {
    resumen:
      "Tu perfil muestra un equilibrio razonable entre ingresos y gastos.",
    riesgo: "Moderado",
    ahorro_recomendado: 1000,
    recomendaciones: ["Ahorra un porcentaje fijo cada mes."],
  };
}

export function enviarJSONPuroAnalisis() {
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (!jsonArea) return;

  let parsed;
  try {
    parsed = JSON.parse(jsonArea.value);
  } catch (error) {
    mostrarResultadoAnalisis({ error: `JSON inválido: ${error.message}` });
    return;
  }

  const settings = getBackendSettings("/api/analisis-financiero");
  if (settings.useBackend) {
    fetchBackend(settings.endpoint, parsed, "resultadoAnalisis");
    return;
  }

  mostrarResultadoAnalisis({
    resumen: "Datos recibidos desde JSON puro.",
    payload: parsed,
  });
}

export function resetAnalisisForm() {
  const contenedor = document.getElementById("transaccionesAnalisis");
  if (contenedor) {
    contenedor.innerHTML = `
      <div class="transaccion-analisis mb-3">
        <label class="form-label">Descripción de la transacción</label>
        <input type="text" class="descripcion-analisis form-control form-control-sm mb-3" />

        <label class="form-label">Valor</label>
        <input type="number" class="valor-analisis form-control form-control-sm mb-3 numeric-only" inputmode="numeric" pattern="[0-9]*" />
        <hr />
      </div>
    `;
  }

  const result = document.getElementById("resultadoAnalisis");
  if (result) {
    result.style.display = "none";
    result.querySelector("div")?.remove();
    const content = document.createElement("div");
    content.className = "p-3 rounded-3 bg-light shadow-sm";
    content.style.whiteSpace = "pre-wrap";
    result.appendChild(content);
  }
}

export function resetJSONPuroAnalisis() {
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (jsonArea) {
    jsonArea.value = "";
    autoResize(jsonArea);
  }

  const result = document.getElementById("resultadoAnalisis");
  if (result) {
    result.style.display = "none";
    result.querySelector("div")?.remove();
    const content = document.createElement("div");
    content.className = "p-3 rounded-3 bg-light shadow-sm";
    content.style.whiteSpace = "pre-wrap";
    result.appendChild(content);
  }
}

export function mostrarResultadoAnalisis(mockResponse) {
  const result = document.getElementById("resultadoAnalisis");
  if (!result) return;

  const content = result.querySelector("div");
  if (!content) return;

  if (
    !mockResponse ||
    (typeof mockResponse === "object" && !Object.keys(mockResponse).length)
  ) {
    result.style.display = "none";
    content.textContent = "";
    return;
  }

  content.textContent = JSON.stringify(mockResponse, null, 2);
  result.style.display = "block";
  result.scrollIntoView({ behavior: "smooth", block: "center" });
}
