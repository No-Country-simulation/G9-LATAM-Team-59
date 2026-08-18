import { esRutaProtegidaActual, getAuthHeaders } from "./auth.js";

export const BACKEND_BASE_URL = "localhost:8080";
export const BACKEND_PATH_ANALISIS = "/api/analisis-financiero";
export const BACKEND_PATH_CLASIFICACION = "/api/clasificar-transacciones";
const CURRENCIES_STORAGE_KEY = "smartwallet_supported_currencies";

let supportedCurrencies = [];
let currenciesLoadError = "";
let currenciesBootstrapPromise = null;

export function buildBackendUrl(baseUrl, path) {
  if (!baseUrl) {
    return path;
  }
  const normalizedBase = baseUrl.replace(/\/+$/, "");
  if (/^https?:\/\//i.test(normalizedBase)) {
    return normalizedBase + path;
  }
  return "http://" + normalizedBase + path;
}

export function getBackendSettings(defaultPath) {
  const endpoint = buildBackendUrl(BACKEND_BASE_URL, defaultPath);
  return { endpoint };
}

export function parseResponseBody(response) {
  return response.text().then(function (text) {
    try {
      return JSON.parse(text);
    } catch (e) {
      return text;
    }
  });
}

function normalizeCurrencies(payload) {
  let raw = [];

  if (Array.isArray(payload)) {
    raw = payload;
  } else if (payload && typeof payload === "object") {
    if (Array.isArray(payload.monedas)) {
      raw = payload.monedas;
    } else if (Array.isArray(payload.currencies)) {
      raw = payload.currencies;
    } else if (Array.isArray(payload.data)) {
      raw = payload.data;
    }
  }

  const normalized = raw
    .map((item) => {
      if (typeof item === "string") {
        const code = item.trim().toUpperCase();
        return code ? { code, label: code } : null;
      }
      if (item && typeof item === "object") {
        const code = (
          item.iso_code ||
          item.codigo ||
          item.code ||
          item.moneda ||
          item.currency ||
          ""
        )
          .toString()
          .trim()
          .toUpperCase();
        if (!code) return null;
        const label = (item.name || item.nombre || code).toString().trim();
        return { code, label: label || code };
      }
      return null;
    })
    .filter(Boolean);

  const dedup = new Map();
  normalized.forEach((currency) => {
    if (!dedup.has(currency.code)) {
      dedup.set(currency.code, currency);
    }
  });
  return Array.from(dedup.values());
}

function persistSupportedCurrencies(currencies) {
  try {
    localStorage.setItem(CURRENCIES_STORAGE_KEY, JSON.stringify(currencies));
  } catch (error) {
    console.error(error);
  }
}

function loadCachedSupportedCurrencies() {
  try {
    const raw = localStorage.getItem(CURRENCIES_STORAGE_KEY);
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    return normalizeCurrencies(parsed);
  } catch (error) {
    console.error(error);
    return [];
  }
}

export function getSupportedCurrencies() {
  return supportedCurrencies.map((item) => item.code);
}

export function getDefaultCurrency() {
  if (supportedCurrencies.some((item) => item.code === "USD")) return "USD";
  return supportedCurrencies[0]?.code || "";
}

export function buildCurrencyOptionsHtml(selectedCurrency) {
  if (!supportedCurrencies.length) {
    const message = currenciesLoadError || "No hay monedas disponibles";
    return `<option value="" selected>${message}</option>`;
  }

  const selected = (selectedCurrency || getDefaultCurrency()).toUpperCase();
  return supportedCurrencies
    .map((currency) => {
      const isSelected = currency.code === selected;
      return `<option value="${currency.code}"${isSelected ? " selected" : ""}>${currency.code} - ${currency.label}</option>`;
    })
    .join("");
}

export function applyCurrencyOptionsToSelects(root = document) {
  root.querySelectorAll("select.currency-select").forEach((select) => {
    const preferred =
      select.getAttribute("data-default") ||
      select.value ||
      getDefaultCurrency();
    const normalized = String(preferred || "")
      .trim()
      .toUpperCase();
    const selected = supportedCurrencies.some(
      (item) => item.code === normalized,
    )
      ? normalized
      : getDefaultCurrency();
    select.innerHTML = buildCurrencyOptionsHtml(selected);
    select.disabled = supportedCurrencies.length === 0;
  });
}

export function bootstrapSupportedCurrencies() {
  if (currenciesBootstrapPromise) {
    return currenciesBootstrapPromise;
  }

  const cached = loadCachedSupportedCurrencies();
  if (cached.length) {
    supportedCurrencies = cached;
  }
  applyCurrencyOptionsToSelects();

  const endpoint = buildBackendUrl(BACKEND_BASE_URL, "/api/monedas");

  currenciesBootstrapPromise = fetch(endpoint, {
    method: "GET",
    headers: {
      ...getRequestAuthHeaders(),
    },
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      return parseResponseBody(response);
    })
    .then((body) => {
      const currencies = normalizeCurrencies(body);
      if (!currencies.length) {
        throw new Error(
          "La respuesta de /api/monedas no contiene monedas válidas.",
        );
      }
      currenciesLoadError = "";
      supportedCurrencies = currencies;
      persistSupportedCurrencies(currencies);
      return supportedCurrencies;
    })
    .catch((error) => {
      supportedCurrencies = [];
      currenciesLoadError = "Error cargando monedas";
      console.error(error);
      throw error;
    })
    .finally(() => {
      applyCurrencyOptionsToSelects();
    });

  return currenciesBootstrapPromise;
}

export function autoResize(el) {
  if (!el) return;
  el.style.height = "auto";
  el.style.height = el.scrollHeight + "px";
}

export function applyNumericMask(input) {
  if (!input) return;
  input.addEventListener("input", function () {
    this.value = this.value.replace(/[^0-9]/g, "");
  });
  input.addEventListener("keydown", function (event) {
    const allowed = [
      "Backspace",
      "Delete",
      "ArrowLeft",
      "ArrowRight",
      "Tab",
      "Enter",
    ];
    if (allowed.includes(event.key)) return;
    if (event.ctrlKey || event.metaKey) return;
    if (!/^[0-9]$/.test(event.key)) {
      event.preventDefault();
    }
  });
}

export function setupNumericOnlyInputs() {
  document.querySelectorAll("input.numeric-only").forEach(applyNumericMask);
}

function getRequestAuthHeaders() {
  return esRutaProtegidaActual() ? getAuthHeaders() : {};
}

function getApiErrorMessage(body) {
  if (typeof body === "string") {
    return body;
  }
  if (body && typeof body === "object") {
    if (typeof body.message === "string" && body.message.trim()) {
      return body.message;
    }
    if (
      body.error &&
      typeof body.error === "object" &&
      typeof body.error.message === "string" &&
      body.error.message.trim()
    ) {
      return body.error.message;
    }
    return JSON.stringify(body);
  }
  return "";
}

export function fetchBackend(endpoint, payload, resultId) {
  let resultEl, resultContainer, resultInnerEl;

  if (resultId === "resultadoClasificacion") {
    resultContainer = document.getElementById(
      "resultadoContainerClasificacion",
    );
    resultEl = document.getElementById(resultId);
    resultInnerEl = resultEl;
  } else if (resultId === "resultadoAnalisis") {
    resultContainer = document.getElementById(resultId);
    resultInnerEl = resultContainer
      ? resultContainer.querySelector(".bg-light")
      : null;
  } else {
    resultContainer = document.getElementById(resultId);
    resultEl = resultContainer;
    resultInnerEl = resultContainer
      ? resultContainer.querySelector(".bg-light") || resultContainer
      : null;
  }

  if (!resultInnerEl) {
    return Promise.reject(new Error("Elemento de resultado no encontrado"));
  }

  if (resultContainer) {
    resultContainer.style.display = "none";
  } else if (resultEl) {
    resultEl.style.display = "none";
  }
  if (resultInnerEl) {
    resultInnerEl.style.display = "none";
    resultInnerEl.textContent = "";
  }

  return fetch(endpoint, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...getRequestAuthHeaders(),
    },
    body: JSON.stringify(payload),
  })
    .then(function (response) {
      return parseResponseBody(response).then(function (body) {
        if (!response.ok) {
          const message = getApiErrorMessage(body);
          throw new Error(message || `HTTP ${response.status}`);
        }
        return body;
      });
    })
    .then(function (data) {
      if (resultContainer) {
        resultContainer.style.display = "block";
      } else if (resultEl) {
        resultEl.style.display = "block";
      }
      if (resultInnerEl) {
        resultInnerEl.style.display = "block";
        resultInnerEl.style.color = "#212529";
        resultInnerEl.textContent = JSON.stringify(data, null, 2);
      }
      if (resultInnerEl) {
        resultInnerEl.scrollIntoView({ behavior: "smooth", block: "center" });
      }
      return data;
    })
    .catch(function (error) {
      if (resultContainer) {
        resultContainer.style.display = "block";
      } else if (resultEl) {
        resultEl.style.display = "block";
      }
      if (resultInnerEl) {
        resultInnerEl.style.display = "block";
        resultInnerEl.style.color = "#c82333";
        resultInnerEl.textContent = "Error: " + error.message;
      }
      if (resultInnerEl) {
        resultInnerEl.scrollIntoView({ behavior: "smooth", block: "center" });
      }
      console.error(error);
      return null;
    });
}
