import { getAuthHeaders } from "./auth.js";

export const USE_BACKEND = false;
export const BACKEND_BASE_URL = "localhost:3000";
export const BACKEND_PATH_ANALISIS = "/api/analisis-financiero";
export const BACKEND_PATH_CLASIFICACION = "/api/clasificar-transacciones";

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
  return { useBackend: USE_BACKEND, endpoint };
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
    resultInnerEl.textContent = "";
  }

  return fetch(endpoint, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...getAuthHeaders(),
    },
    body: JSON.stringify(payload),
  })
    .then(function (response) {
      return parseResponseBody(response).then(function (body) {
        if (!response.ok) {
          const message =
            typeof body === "string" ? body : JSON.stringify(body);
          throw new Error(`HTTP ${response.status}: ${message}`);
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
        resultInnerEl.style.color = "#c82333";
        resultInnerEl.textContent = "Error: " + error.message;
      }
      if (resultInnerEl) {
        resultInnerEl.scrollIntoView({ behavior: "smooth", block: "center" });
      }
      console.error(error);
      throw error;
    });
}
