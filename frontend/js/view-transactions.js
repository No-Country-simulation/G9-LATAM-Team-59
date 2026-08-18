import { getAuthHeaders } from "./auth.js";
import { getBackendSettings, parseResponseBody } from "./utils.js";

let transaccionesActuales = [];

function pad(num) {
  return String(num).padStart(2, "0");
}

function getCurrentMonthRange() {
  const today = new Date();
  const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
  const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
  const desde =
    firstDay.getFullYear() +
    "-" +
    pad(firstDay.getMonth() + 1) +
    "-" +
    pad(firstDay.getDate());
  const hasta =
    lastDay.getFullYear() +
    "-" +
    pad(lastDay.getMonth() + 1) +
    "-" +
    pad(lastDay.getDate());
  return { desde, hasta };
}

function getValue(id) {
  const el = document.getElementById(id);
  return el ? el.value : "";
}

function setValue(id, value) {
  const el = document.getElementById(id);
  if (el) el.value = value;
}

function formatMonto(value) {
  const num = Number(value);
  if (!Number.isFinite(num)) return "-";
  return num.toFixed(2);
}

function escapeHtml(text) {
  return String(text ?? "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/\"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function mostrarMensaje(texto, tipo = "success") {
  const alerta = document.getElementById("mensajeTransacciones");
  if (!alerta) return;
  alerta.className = `alert alert-${tipo} mt-3`;
  alerta.textContent = texto;
  alerta.style.display = "block";
}

function limpiarMensaje() {
  const alerta = document.getElementById("mensajeTransacciones");
  if (!alerta) return;
  alerta.textContent = "";
  alerta.className = "alert mt-3";
  alerta.style.display = "none";
}

function actualizarContador() {
  const el = document.getElementById("contadorTransacciones");
  if (!el) return;
  const total = transaccionesActuales.length;
  el.textContent = `${total} ${total === 1 ? "transacción" : "transacciones"}`;
}

function renderTabla() {
  const body = document.getElementById("tablaTransaccionesBody");
  const estado = document.getElementById("estadoTransacciones");
  if (!body || !estado) return;

  body.innerHTML = "";

  if (!transaccionesActuales.length) {
    estado.textContent =
      "No se encontraron transacciones para el rango seleccionado.";
    actualizarContador();
    return;
  }

  estado.textContent = "";

  transaccionesActuales.forEach((tx) => {
    const tr = document.createElement("tr");
    tr.setAttribute("data-id", String(tx.id));
    tr.innerHTML =
      `<td>${escapeHtml(tx.id)}</td>` +
      `<td>${escapeHtml(tx.descripcion)}</td>` +
      `<td>${escapeHtml(formatMonto(tx.monto))}</td>` +
      `<td>${escapeHtml(tx.moneda || "USD")}</td>` +
      '<td class="text-end">' +
      `<button type="button" class="btn btn-outline-danger btn-sm" onclick="eliminarTransaccionDesdeVista(${Number(tx.id)})">Eliminar</button>` +
      "</td>";
    body.appendChild(tr);
  });

  actualizarContador();
}

function construirEndpointConFechas(baseEndpoint, desde, hasta) {
  const query = new URLSearchParams();
  if (desde) query.append("desde", desde);
  if (hasta) query.append("hasta", hasta);
  const queryString = query.toString();
  return queryString ? `${baseEndpoint}?${queryString}` : baseEndpoint;
}

function validarRango(desde, hasta) {
  if (desde && hasta && desde > hasta) {
    throw new Error(
      "La fecha 'desde' no puede ser mayor que la fecha 'hasta'.",
    );
  }
}

export function inicializarVistaTransacciones() {
  const desdeEl = document.getElementById("desdeTransacciones");
  if (!desdeEl) return;

  const { desde, hasta } = getCurrentMonthRange();
  if (!getValue("desdeTransacciones")) setValue("desdeTransacciones", desde);
  if (!getValue("hastaTransacciones")) setValue("hastaTransacciones", hasta);
  verTransaccionesFiltradas();
}

export function limpiarFiltrosTransacciones() {
  const { desde, hasta } = getCurrentMonthRange();
  setValue("desdeTransacciones", desde);
  setValue("hastaTransacciones", hasta);
  limpiarMensaje();
  verTransaccionesFiltradas();
}

export function verTransaccionesFiltradas() {
  const estado = document.getElementById("estadoTransacciones");
  const desde = getValue("desdeTransacciones");
  const hasta = getValue("hastaTransacciones");

  try {
    validarRango(desde, hasta);
  } catch (error) {
    mostrarMensaje(error.message, "danger");
    return;
  }

  if (estado) estado.textContent = "Cargando transacciones...";
  limpiarMensaje();

  const settings = getBackendSettings("/api/transacciones");
  const endpoint = construirEndpointConFechas(settings.endpoint, desde, hasta);

  fetch(endpoint, {
    method: "GET",
    headers: {
      ...getAuthHeaders(),
    },
  })
    .then((response) => {
      return parseResponseBody(response).then((body) => {
        if (!response.ok) {
          const message =
            typeof body === "string" ? body : JSON.stringify(body);
          throw new Error(`HTTP ${response.status}: ${message}`);
        }
        return body;
      });
    })
    .then((data) => {
      transaccionesActuales = Array.isArray(data)
        ? data
        : Array.isArray(data?.transacciones)
          ? data.transacciones
          : [];
      renderTabla();
    })
    .catch((error) => {
      transaccionesActuales = [];
      renderTabla();
      mostrarMensaje(
        `No se pudieron obtener transacciones. ${error.message}`,
        "danger",
      );
    });
}

export function eliminarTransaccionDesdeVista(id) {
  const txId = Number(id);
  if (!Number.isFinite(txId)) {
    mostrarMensaje("ID de transacción inválido.", "danger");
    return;
  }

  const tx = transaccionesActuales.find((item) => Number(item.id) === txId);
  const descripcion = tx?.descripcion ? `\n${tx.descripcion}` : "";
  const confirmed = window.confirm(
    `¿Seguro que deseas eliminar la transacción #${txId}?${descripcion}`,
  );
  if (!confirmed) {
    mostrarMensaje("Eliminación cancelada.", "warning");
    return;
  }

  const settings = getBackendSettings(`/api/transacciones/${txId}`);

  fetch(settings.endpoint, {
    method: "DELETE",
    headers: {
      ...getAuthHeaders(),
    },
  })
    .then((response) => {
      if (response.status === 204) {
        return null;
      }
      return parseResponseBody(response).then((body) => {
        if (!response.ok) {
          const message =
            typeof body === "string" ? body : JSON.stringify(body);
          throw new Error(`HTTP ${response.status}: ${message}`);
        }
        return body;
      });
    })
    .then(() => {
      transaccionesActuales = transaccionesActuales.filter(
        (tx) => Number(tx.id) !== txId,
      );
      renderTabla();
      mostrarMensaje("Eliminado exitosamente.", "success");
    })
    .catch((error) => {
      mostrarMensaje(
        `No se pudo eliminar la transacción. ${error.message}`,
        "danger",
      );
    });
}
