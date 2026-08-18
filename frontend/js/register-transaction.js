import { esAutenticado } from "./auth.js";
import {
  fetchBackend,
  getBackendSettings,
  getDefaultCurrency,
} from "./utils.js";

function getValue(id) {
  const el = document.getElementById(id);
  return el ? el.value : "";
}

function setValue(id, value) {
  const el = document.getElementById(id);
  if (el) el.value = value;
}

function mostrarErrorRegistro(message) {
  const result = document.getElementById("resultadoRegistroTransaccion");
  const content = result ? result.querySelector(".bg-light") : null;
  if (!result || !content) return;

  result.style.display = "block";
  content.style.color = "#c82333";
  content.textContent = "Error: " + message;
  result.scrollIntoView({ behavior: "smooth", block: "center" });
}

function validarPayload(payload) {
  if (!payload.descripcion || !payload.descripcion.trim()) {
    throw new Error("La descripción es obligatoria.");
  }
  if (!Number.isFinite(payload.monto) || payload.monto <= 0) {
    throw new Error("El monto debe ser mayor a 0.");
  }
}

export function resetRegistroTransaccionForm() {
  setValue("descripcionTransaccion", "");
  setValue("montoTransaccion", "");
  setValue("monedaTransaccion", getDefaultCurrency());

  const result = document.getElementById("resultadoRegistroTransaccion");
  const content = result ? result.querySelector(".bg-light") : null;
  if (result) result.style.display = "none";
  if (content) {
    content.textContent = "";
    content.style.color = "#212529";
  }
}

export function registrarTransaccion() {
  if (!esAutenticado()) {
    mostrarErrorRegistro(
      "Debes iniciar sesión para registrar una transacción.",
    );
    return;
  }

  const payload = {
    descripcion: getValue("descripcionTransaccion"),
    monto: Number(getValue("montoTransaccion") || 0),
    moneda: getValue("monedaTransaccion") || getDefaultCurrency(),
  };

  try {
    validarPayload(payload);
  } catch (error) {
    mostrarErrorRegistro(error.message);
    return;
  }

  const settings = getBackendSettings("/api/transacciones");
  fetchBackend(settings.endpoint, payload, "resultadoRegistroTransaccion");
}
