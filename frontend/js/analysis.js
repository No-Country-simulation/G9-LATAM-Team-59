import {
  buildCurrencyOptionsHtml,
  fetchBackend,
  getBackendSettings,
  getDefaultCurrency,
} from "./utils.js";

function getTransaccionesAnalisis() {
  const contenedor = document.getElementById("transaccionesAnalisis");
  if (!contenedor) return [];

  return Array.from(
    contenedor.getElementsByClassName("transaccion-analisis"),
  ).map((item) => {
    const descripcionEl = item.querySelector(".descripcion-analisis");
    const montoEl = item.querySelector(".monto-analisis");
    const monedaEl = item.querySelector(".moneda-analisis");
    return {
      descripcion: descripcionEl ? descripcionEl.value : "",
      monto: montoEl ? Number(montoEl.value || 0) : 0,
      moneda: monedaEl ? monedaEl.value : "USD",
    };
  });
}

export function agregarTransaccionAnalisis() {
  const contenedor = document.getElementById("transaccionesAnalisis");
  if (!contenedor) return;

  const div = document.createElement("div");
  div.className = "transaccion-analisis mb-3";
  div.innerHTML =
    '<label class="form-label">Descripción de la transacción</label>' +
    '<input type="text" class="descripcion-analisis form-control form-control-sm mb-3" style="border: 2px solid #64748b !important; box-shadow: inset 0 0 0 1px rgba(15, 23, 42, 0.04) !important;" />' +
    '<label class="form-label">Monto</label>' +
    '<input type="number" class="monto-analisis form-control form-control-sm mb-3" style="border: 2px solid #64748b !important; box-shadow: inset 0 0 0 1px rgba(15, 23, 42, 0.04) !important;" min="0" step="0.01" />' +
    '<label class="form-label">Moneda</label>' +
    `<select class="moneda-analisis currency-select form-select form-select-sm mb-3">${buildCurrencyOptionsHtml(getDefaultCurrency())}</select>` +
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
    const val = first.querySelector(".monto-analisis");
    const moneda = first.querySelector(".moneda-analisis");
    if (desc) desc.value = "";
    if (val) val.value = "";
    if (moneda) moneda.value = getDefaultCurrency();
  }
}

export function enviarDatos() {
  const ingresoEl = document.getElementById("ingreso");
  const monedaIngresoEl = document.getElementById("monedaIngresoMensual");
  const endeudamientoEl = document.getElementById("endeudamiento");
  const ahorroEl = document.getElementById("ahorro");

  const payload = {
    ingreso_mensual: Number(ingresoEl?.value || 0),
    moneda_ingreso_mensual: monedaIngresoEl?.value || getDefaultCurrency(),
    nivel_endeudamiento: Number(endeudamientoEl?.value || 0),
    frecuencia_ahorro: ahorroEl?.value || "Nula",
    transacciones: getTransaccionesAnalisis(),
  };

  const settings = getBackendSettings("/api/analisis-financiero");
  fetchBackend(settings.endpoint, payload, "resultadoAnalisis");
}

export function resetAnalisisForm() {
  const contenedor = document.getElementById("transaccionesAnalisis");
  if (contenedor) {
    contenedor.innerHTML = `
      <div class="transaccion-analisis mb-3">
        <label class="form-label">Descripción de la transacción</label>
        <input type="text" class="descripcion-analisis form-control form-control-sm mb-3" style="border: 2px solid #64748b !important; box-shadow: inset 0 0 0 1px rgba(15, 23, 42, 0.04) !important;" />

        <label class="form-label">Monto</label>
        <input type="number" class="monto-analisis form-control form-control-sm mb-3" style="border: 2px solid #64748b !important; box-shadow: inset 0 0 0 1px rgba(15, 23, 42, 0.04) !important;" min="0" step="0.01" />

        <label class="form-label">Moneda</label>
        <select class="moneda-analisis currency-select form-select form-select-sm mb-3">${buildCurrencyOptionsHtml(getDefaultCurrency())}</select>
        <hr />
      </div>
    `;
  }

  const monedaIngresoEl = document.getElementById("monedaIngresoMensual");
  if (monedaIngresoEl) {
    monedaIngresoEl.value = getDefaultCurrency();
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

export function mostrarResultadoAnalisis(responseData) {
  const result = document.getElementById("resultadoAnalisis");
  if (!result) return;

  const content = result.querySelector("div");
  if (!content) return;

  if (
    !responseData ||
    (typeof responseData === "object" && !Object.keys(responseData).length)
  ) {
    result.style.display = "none";
    content.textContent = "";
    return;
  }

  content.textContent = JSON.stringify(responseData, null, 2);
  result.style.display = "block";
  result.scrollIntoView({ behavior: "smooth", block: "center" });
}
