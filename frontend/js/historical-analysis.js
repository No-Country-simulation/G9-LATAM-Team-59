import { esAutenticado } from "./auth.js";
import {
  fetchBackend,
  getBackendSettings,
  getDefaultCurrency,
} from "./utils.js";

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

function construirEndpointConFechas(baseEndpoint, desde, hasta) {
  const query = new URLSearchParams();
  if (desde) query.append("desde", desde);
  if (hasta) query.append("hasta", hasta);
  const queryString = query.toString();
  return queryString ? `${baseEndpoint}?${queryString}` : baseEndpoint;
}

function validarRangoFechas(desde, hasta) {
  if (desde && hasta && desde > hasta) {
    throw new Error(
      "La fecha 'desde' no puede ser mayor que la fecha 'hasta'.",
    );
  }
}

function mostrarErrorHistorico(message) {
  const result = document.getElementById("resultadoAnalisisHistorico");
  const content = result ? result.querySelector(".bg-light") : null;
  if (!result || !content) return;

  result.style.display = "block";
  content.style.color = "#c82333";
  content.textContent = "Error: " + message;
  result.scrollIntoView({ behavior: "smooth", block: "center" });
}

export function inicializarAnalisisHistorico() {
  const ingresoEl = document.getElementById("ingresoHistorico");
  if (!ingresoEl) return;

  const { desde, hasta } = getCurrentMonthRange();
  if (!getValue("desdeHistorico")) setValue("desdeHistorico", desde);
  if (!getValue("hastaHistorico")) setValue("hastaHistorico", hasta);
}

export function resetAnalisisHistoricoForm() {
  setValue("ingresoHistorico", "");
  setValue("monedaIngresoHistorico", getDefaultCurrency());
  setValue("endeudamientoHistorico", "");
  setValue("ahorroHistorico", "Media");

  const { desde, hasta } = getCurrentMonthRange();
  setValue("desdeHistorico", desde);
  setValue("hastaHistorico", hasta);

  const result = document.getElementById("resultadoAnalisisHistorico");
  const content = result ? result.querySelector(".bg-light") : null;
  if (result) result.style.display = "none";
  if (content) {
    content.style.color = "#212529";
    content.textContent = "";
  }
}

export function enviarAnalisisHistoricoMisTransacciones() {
  if (!esAutenticado()) {
    mostrarErrorHistorico(
      "Debes iniciar sesión para analizar tus transacciones históricas.",
    );
    return;
  }

  const payload = {
    ingreso_mensual: Number(getValue("ingresoHistorico") || 0),
    moneda_ingreso_mensual:
      getValue("monedaIngresoHistorico") || getDefaultCurrency(),
    nivel_endeudamiento: Number(getValue("endeudamientoHistorico") || 0),
    frecuencia_ahorro: getValue("ahorroHistorico") || "Media",
  };

  const desde = getValue("desdeHistorico");
  const hasta = getValue("hastaHistorico");

  try {
    validarRangoFechas(desde, hasta);
  } catch (error) {
    mostrarErrorHistorico(error.message);
    return;
  }

  const settings = getBackendSettings(
    "/api/analisis-financiero/mis-transacciones",
  );
  const endpoint = construirEndpointConFechas(settings.endpoint, desde, hasta);
  fetchBackend(endpoint, payload, "resultadoAnalisisHistorico");
}
