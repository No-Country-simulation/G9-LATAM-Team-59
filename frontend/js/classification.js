import {
  autoResize,
  fetchBackend,
  getBackendSettings,
  applyNumericMask,
} from "./utils.js";

export const DATOS_PRUEBA_CLASIFICACION = [
  { descripcion: "Supermercado Carrefour", valor: 420.0 },
  { descripcion: "Carga de nafta YPF", valor: 300.0 },
  { descripcion: "Suscripción Netflix", valor: 40.0 },
];

export function cargarDatosPruebaClasificacion() {
  resetClasificacionForm();
  const contenedor = document.getElementById("transacciones");
  contenedor.innerHTML = "";
  DATOS_PRUEBA_CLASIFICACION.forEach(function (item) {
    const div = document.createElement("div");
    div.className = "transaccion";
    div.innerHTML =
      "<label>Descripción de la transacción</label>" +
      '<input type="text" class="descripcion form-control form-control-sm mb-3" value="' +
      (item.descripcion || "").replace(/"/g, "&quot;") +
      '" />' +
      "<label>Valor de la transacción</label>" +
      '<input type="number" class="valor form-control form-control-sm numeric-only" inputmode="numeric" pattern="[0-9]*" value="' +
      item.valor +
      '" />' +
      "<hr />";
    contenedor.appendChild(div);
    div.querySelectorAll("input.numeric-only").forEach(applyNumericMask);
  });
}

export function cargarDatosPruebaJSONClasificacion() {
  const jsonArea = document.getElementById("jsonPuro");
  if (jsonArea) {
    jsonArea.value = JSON.stringify(DATOS_PRUEBA_CLASIFICACION, null, 2);
    autoResize(jsonArea);
  }
}

export function agregarTransaccion() {
  const contenedor = document.getElementById("transacciones");
  const div = document.createElement("div");
  div.className = "transaccion";
  div.innerHTML =
    "<label>Descripción de la transacción</label>" +
    '<input type="text" class="descripcion form-control form-control-sm mb-3" />' +
    "<label>Valor de la transacción</label>" +
    '<input type="number" class="valor form-control form-control-sm mb-3 numeric-only" inputmode="numeric" pattern="[0-9]*" />' +
    "<hr />";

  contenedor.appendChild(div);
  div.querySelectorAll("input.numeric-only").forEach(applyNumericMask);
}

export function eliminarTransaccion() {
  const contenedor = document.getElementById("transacciones");
  const items = contenedor.getElementsByClassName("transaccion");
  if (items.length > 1) {
    contenedor.removeChild(items[items.length - 1]);
  } else if (items.length === 1) {
    const first = items[0];
    const desc = first.querySelector(".descripcion");
    const val = first.querySelector(".valor");
    if (desc) desc.value = "";
    if (val) val.value = "";
  }
}

export function enviarTransacciones() {
  const items = document.querySelectorAll("#transacciones .transaccion");
  const transacciones = [];
  items.forEach(function (item) {
    const descripcionEl = item.querySelector(".descripcion");
    const valorEl = item.querySelector(".valor");
    const descripcion = descripcionEl ? descripcionEl.value : "";
    const valor = valorEl ? Number(valorEl.value || 0) : 0;
    transacciones.push({ descripcion: descripcion, valor: valor });
  });

  const payload = transacciones;
  console.log("JSON a enviar (array):", payload);

  const settings = getBackendSettings("/api/clasificar-transacciones");
  if (settings.useBackend) {
    fetchBackend(settings.endpoint, payload, "resultadoClasificacion");
    return;
  }

  const mockResponse = {};
  let alimentacion = 0;
  let transporte = 0;

  payload.forEach(function (item) {
    const desc = (item.descripcion || "").toLowerCase();
    const val = Number(item.valor || 0);
    if (/(supermercado|carrefour|mercad|almacen|super)/.test(desc)) {
      alimentacion += val;
    } else if (/(nafta|ypf|gasolina|gasol|estacion)/.test(desc)) {
      transporte += val;
    } else {
      mockResponse.otros = (mockResponse.otros || 0) + val;
    }
  });

  if (alimentacion > 0) mockResponse.alimentacion = alimentacion;
  if (transporte > 0) mockResponse.transporte = transporte;

  console.log("Response (HTTP 200 OK)");
  console.log(mockResponse);

  mostrarResultadoClasificacion(mockResponse);

  const contenedor = document.getElementById("transacciones");
  if (contenedor) {
    contenedor.innerHTML = `
      <div class="transaccion">
        <label>Descripción de la transacción</label>
        <input type="text" class="descripcion form-control form-control-sm mb-3" />

        <label>Valor de la transacción</label>
        <input type="number" class="valor form-control form-control-sm numeric-only" inputmode="numeric" pattern="[0-9]*" />
        <hr />
      </div>
    `;
  }
}

export function enviarJSONPuro() {
  const jsonArea = document.getElementById("jsonPuro");
  if (!jsonArea) {
    console.error("No se encontró el campo de JSON puro.");
    return;
  }

  let parsed;
  try {
    parsed = JSON.parse(jsonArea.value);
  } catch (e) {
    console.error("JSON inválido:", e.message);
    const resultContainer = document.getElementById(
      "resultadoContainerClasificacion",
    );
    const resultEl = document.getElementById("resultadoClasificacion");
    if (resultContainer) {
      resultContainer.style.display = "block";
    }
    if (resultEl) {
      resultEl.style.color = "#c82333";
      resultEl.textContent = "JSON inválido: " + e.message;
      resultEl.style.display = "block";
    }
    return;
  }

  const settings = getBackendSettings("/api/clasificar-transacciones");
  if (settings.useBackend) {
    fetchBackend(settings.endpoint, parsed, "resultadoClasificacion");
    return;
  }

  const transacciones = Array.isArray(parsed)
    ? parsed
    : Array.isArray(parsed.transacciones)
      ? parsed.transacciones
      : [];

  console.log("JSON a enviar (array):", transacciones);

  const mockResponse = {};
  let alimentacion = 0;
  let transporte = 0;

  transacciones.forEach(function (item) {
    const desc = ((item.descripcion || "") + "").toLowerCase();
    const val = Number(item.valor || 0);
    if (/(supermercado|carrefour|mercad|almacen|super)/.test(desc)) {
      alimentacion += val;
    } else if (/(nafta|ypf|gasolina|gasol|estacion)/.test(desc)) {
      transporte += val;
    } else {
      mockResponse.otros = (mockResponse.otros || 0) + val;
    }
  });

  if (alimentacion > 0) mockResponse.alimentacion = alimentacion;
  if (transporte > 0) mockResponse.transporte = transporte;

  console.log("Response (HTTP 200 OK)");
  console.log(mockResponse);
  mostrarResultadoClasificacion(mockResponse);
}

export function resetClasificacionForm() {
  const contenedor = document.getElementById("transacciones");
  if (contenedor) {
    contenedor.innerHTML = `
      <div class="transaccion">
        <label>Descripción de la transacción</label>
        <input type="text" class="descripcion form-control form-control-sm mb-3" />

        <label>Valor de la transacción</label>
        <input type="number" class="valor form-control form-control-sm numeric-only" inputmode="numeric" pattern="[0-9]*" />
        <hr />
      </div>
    `;
  }

  const resultContainer = document.getElementById(
    "resultadoContainerClasificacion",
  );
  const resultEl = document.getElementById("resultadoClasificacion");
  if (resultContainer) {
    resultContainer.style.display = "none";
  }
  if (resultEl) {
    resultEl.style.display = "none";
    resultEl.textContent = "";
  }
}

export function resetJSONPuro() {
  const jsonArea = document.getElementById("jsonPuro");
  if (jsonArea) {
    jsonArea.value = "";
    autoResize(jsonArea);
  }

  const resultContainer = document.getElementById(
    "resultadoContainerClasificacion",
  );
  const resultEl = document.getElementById("resultadoClasificacion");
  if (resultContainer) {
    resultContainer.style.display = "none";
  }
  if (resultEl) {
    resultEl.style.display = "none";
    resultEl.textContent = "";
  }
}

export function mostrarResultadoClasificacion(mockResponse) {
  const resultContainer = document.getElementById(
    "resultadoContainerClasificacion",
  );
  const resultEl = document.getElementById("resultadoClasificacion");
  if (!resultEl || !resultContainer) return;
  if (
    !mockResponse ||
    (typeof mockResponse === "object" && !Object.keys(mockResponse).length)
  ) {
    resultContainer.style.display = "none";
    resultEl.style.display = "none";
    resultEl.textContent = "";
    return;
  }
  resultEl.textContent = JSON.stringify(mockResponse, null, 2);
  resultEl.style.display = "block";
  resultEl.style.color = "#212529";
  resultContainer.style.display = "block";
  resultEl.scrollIntoView({ behavior: "smooth", block: "center" });
}
