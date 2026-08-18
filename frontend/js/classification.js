import {
  buildCurrencyOptionsHtml,
  fetchBackend,
  getBackendSettings,
  getDefaultCurrency,
} from "./utils.js";

export function agregarTransaccion() {
  const contenedor = document.getElementById("transacciones");
  const div = document.createElement("div");
  div.className = "transaccion mb-3";
  div.innerHTML =
    '<label class="form-label">Descripción de la transacción</label>' +
    '<input type="text" class="descripcion form-control form-control-sm mb-3" style="border: 2px solid #64748b !important; box-shadow: inset 0 0 0 1px rgba(15, 23, 42, 0.04) !important;" />' +
    '<label class="form-label">Monto de la transacción</label>' +
    '<input type="number" class="monto form-control form-control-sm mb-3" style="border: 2px solid #64748b !important; box-shadow: inset 0 0 0 1px rgba(15, 23, 42, 0.04) !important;" min="0" step="0.01" />' +
    '<label class="form-label">Moneda</label>' +
    `<select class="moneda currency-select form-select form-select-sm mb-3">${buildCurrencyOptionsHtml(getDefaultCurrency())}</select>` +
    "<hr />";

  contenedor.appendChild(div);
}

export function eliminarTransaccion() {
  const contenedor = document.getElementById("transacciones");
  const items = contenedor.getElementsByClassName("transaccion");
  if (items.length > 1) {
    contenedor.removeChild(items[items.length - 1]);
  } else if (items.length === 1) {
    const first = items[0];
    const desc = first.querySelector(".descripcion");
    const val = first.querySelector(".monto");
    const moneda = first.querySelector(".moneda");
    if (desc) desc.value = "";
    if (val) val.value = "";
    if (moneda) moneda.value = getDefaultCurrency();
  }
}

export function enviarTransacciones() {
  const items = document.querySelectorAll("#transacciones .transaccion");
  const transacciones = [];

  items.forEach(function (item) {
    const descripcionEl = item.querySelector(".descripcion");
    const montoEl = item.querySelector(".monto");
    const monedaEl = item.querySelector(".moneda");
    const descripcion = descripcionEl ? descripcionEl.value : "";
    const monto = montoEl ? Number(montoEl.value || 0) : 0;
    const moneda = monedaEl ? String(monedaEl.value || "") : "";

    transacciones.push({ descripcion, monto, moneda });
  });

  const payload = { transacciones };
  const settings = getBackendSettings("/api/clasificar-transacciones");
  fetchBackend(settings.endpoint, payload, "resultadoClasificacion");
}

export function resetClasificacionForm() {
  const contenedor = document.getElementById("transacciones");
  if (contenedor) {
    contenedor.innerHTML = `
      <div class="transaccion mb-3">
        <label class="form-label">Descripción de la transacción</label>
        <input type="text" class="descripcion form-control form-control-sm mb-3" />

        <label class="form-label">Monto de la transacción</label>
        <input type="number" class="monto form-control form-control-sm mb-3" min="0" step="0.01" />

        <label class="form-label">Moneda</label>
        <select class="moneda currency-select form-select form-select-sm mb-3">${buildCurrencyOptionsHtml(getDefaultCurrency())}</select>
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

export function mostrarResultadoClasificacion(responseData) {
  const resultContainer = document.getElementById(
    "resultadoContainerClasificacion",
  );
  const resultEl = document.getElementById("resultadoClasificacion");
  if (!resultEl || !resultContainer) return;
  if (
    !responseData ||
    (typeof responseData === "object" && !Object.keys(responseData).length)
  ) {
    resultContainer.style.display = "none";
    resultEl.style.display = "none";
    resultEl.textContent = "";
    return;
  }
  resultEl.textContent = JSON.stringify(responseData, null, 2);
  resultEl.style.display = "block";
  resultEl.style.color = "#212529";
  resultContainer.style.display = "block";
  resultEl.scrollIntoView({ behavior: "smooth", block: "center" });
}
