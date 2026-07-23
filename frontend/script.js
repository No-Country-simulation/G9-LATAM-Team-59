const USE_BACKEND = false; // Cambia a true cuando quieras enviar al backend real
const BACKEND_BASE_URL = "localhost:3000";
const BACKEND_PATH_ANALISIS = "/api/analisis-financiero";
const BACKEND_PATH_CLASIFICACION = "/api/clasificar-transacciones";

// Datos de prueba
const DATOS_PRUEBA_CLASIFICACION = [
  { descripcion: "Supermercado Carrefour", valor: 420.0 },
  { descripcion: "Carga de nafta YPF", valor: 300.0 },
  { descripcion: "Suscripción Netflix", valor: 40.0 },
];

const DATOS_PRUEBA_ANALISIS = {
  ingreso: 4500.0,
  endeudamiento: 25.0,
  ahorro: "Media",
  transacciones: [
    { descripcion: "Supermercado Carrefour", valor: 420.0 },
    { descripcion: "Carga de nafta YPF", valor: 300.0 },
    { descripcion: "Suscripción Netflix", valor: 40.0 },
  ],
};

function cargarDatosPruebaClasificacion() {
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

function cargarDatosPruebaJSONClasificacion() {
  const jsonArea = document.getElementById("jsonPuro");
  if (jsonArea) {
    jsonArea.value = JSON.stringify(DATOS_PRUEBA_CLASIFICACION, null, 2);
    autoResize(jsonArea);
  }
}

function cargarDatosPruebaAnalisis() {
  resetAnalisisForm();
  document.getElementById("ingreso").value = DATOS_PRUEBA_ANALISIS.ingreso;
  document.getElementById("endeudamiento").value =
    DATOS_PRUEBA_ANALISIS.endeudamiento;
  document.getElementById("ahorro").value = DATOS_PRUEBA_ANALISIS.ahorro;

  const contenedor = document.getElementById("transaccionesAnalisis");
  contenedor.innerHTML = "";
  DATOS_PRUEBA_ANALISIS.transacciones.forEach(function (item) {
    const div = document.createElement("div");
    div.className = "transaccion-analisis";
    div.innerHTML =
      "<label>Descripción de la transacción</label>" +
      '<input type="text" class="descripcion-analisis form-control form-control-sm mb-3" value="' +
      (item.descripcion || "").replace(/"/g, "&quot;") +
      '" />' +
      "<label>Valor</label>" +
      '<input type="number" class="valor-analisis form-control form-control-sm numeric-only" inputmode="numeric" pattern="[0-9]*" value="' +
      item.valor +
      '" />' +
      "<hr />";
    contenedor.appendChild(div);
    div.querySelectorAll("input.numeric-only").forEach(applyNumericMask);
  });
}

function cargarDatosPruebaJSONAnalisis() {
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (jsonArea) {
    const payload = {
      ingreso_mensual: DATOS_PRUEBA_ANALISIS.ingreso,
      nivel_endeudamiento: DATOS_PRUEBA_ANALISIS.endeudamiento,
      frecuencia_ahorro: DATOS_PRUEBA_ANALISIS.ahorro,
      transacciones: DATOS_PRUEBA_ANALISIS.transacciones,
    };
    jsonArea.value = JSON.stringify(payload, null, 2);
    autoResize(jsonArea);
  }
}

function enviarDatos() {
  const ingreso = document.getElementById("ingreso").value;
  const endeudamiento = document.getElementById("endeudamiento").value;
  const ahorro = document.getElementById("ahorro").value;

  const items = document.querySelectorAll(
    "#transaccionesAnalisis .transaccion-analisis",
  );
  const transacciones = [];

  items.forEach(function (item) {
    const descripcionEl = item.querySelector(".descripcion-analisis");
    const valorEl = item.querySelector(".valor-analisis");
    const descripcion = descripcionEl ? descripcionEl.value : "";
    const valor = valorEl ? Number(valorEl.value || 0) : 0;
    transacciones.push({ descripcion: descripcion, valor: valor });
  });

  const datos = {
    ingreso_mensual: Number(ingreso),
    nivel_endeudamiento: Number(endeudamiento),
    frecuencia_ahorro: ahorro,
    transacciones: transacciones,
  };

  console.log("JSON a enviar (analisis):", datos);

  const settings = getBackendSettings("/api/analisis-financiero");
  if (settings.useBackend) {
    fetchBackend(settings.endpoint, datos, "resultadoAnalisis");
    return;
  }

  const mockResponse = crearMockResponseAnalisis(transacciones);
  console.log("Response (HTTP 200 OK)");
  console.log(mockResponse);
  mostrarResultadoAnalisis(mockResponse);
}

function crearMockResponseAnalisis(transacciones) {
  const mockResponse = {
    perfil_financiero: "En observación",
    probabilidad: 0.82,
    resumen_gastos: {
      alimentacion: 0,
      transporte: 0,
      entretenimiento: 0,
    },
    recomendaciones: [
      "Monitorear los gastos recurrentes de entretenimiento",
      "Aumentar la reserva financiera mensual",
    ],
  };

  transacciones.forEach(function (item) {
    const desc = (item.descripcion || "").toLowerCase();
    const val = Number(item.valor || 0);
    if (/(supermercado|carrefour|mercad|almacen|super)/.test(desc)) {
      mockResponse.resumen_gastos.alimentacion += val;
    } else if (/(nafta|ypf|gasolina|gasol|estacion)/.test(desc)) {
      mockResponse.resumen_gastos.transporte += val;
    } else if (
      /(netflix|suscripci[oó]n|streaming|entretenimiento|spotify|disney|prime)/.test(
        desc,
      )
    ) {
      mockResponse.resumen_gastos.entretenimiento += val;
    } else {
      mockResponse.resumen_gastos.entretenimiento += val;
    }
  });

  mockResponse.resumen_gastos.alimentacion = Number(
    mockResponse.resumen_gastos.alimentacion.toFixed(2),
  );
  mockResponse.resumen_gastos.transporte = Number(
    mockResponse.resumen_gastos.transporte.toFixed(2),
  );
  mockResponse.resumen_gastos.entretenimiento = Number(
    mockResponse.resumen_gastos.entretenimiento.toFixed(2),
  );

  return mockResponse;
}

function mostrarResultadoAnalisis(mockResponse) {
  const resultContainer = document.getElementById("resultadoAnalisis");
  const resultEl = resultContainer
    ? resultContainer.querySelector(".bg-light")
    : null;
  if (!resultContainer || !resultEl) return;
  if (
    !mockResponse ||
    (typeof mockResponse === "object" && !Object.keys(mockResponse).length)
  ) {
    resultContainer.style.display = "none";
    resultEl.textContent = "";
    return;
  }
  resultEl.textContent = JSON.stringify(mockResponse, null, 2);
  resultContainer.style.display = "block";
  resultEl.scrollIntoView({ behavior: "smooth", block: "center" });
}

function mostrarResultadoClasificacion(mockResponse) {
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

function getBackendSettings(defaultPath) {
  const endpoint = buildBackendUrl(BACKEND_BASE_URL, defaultPath);
  return { useBackend: USE_BACKEND, endpoint };
}

function buildBackendUrl(baseUrl, path) {
  if (!baseUrl) {
    return path;
  }
  const normalizedBase = baseUrl.replace(/\/+$/, "");
  if (/^https?:\/\//i.test(normalizedBase)) {
    return normalizedBase + path;
  }
  return "http://" + normalizedBase + path;
}

function parseResponseBody(response) {
  return response.text().then(function (text) {
    try {
      return JSON.parse(text);
    } catch (e) {
      return text;
    }
  });
}

function fetchBackend(endpoint, payload, resultId) {
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

  if (!resultInnerEl)
    return Promise.reject(new Error("Elemento de resultado no encontrado"));

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

function resetAnalisisForm() {
  document.getElementById("ingreso").value = "";
  document.getElementById("endeudamiento").value = "";
  document.getElementById("ahorro").value = "Nula";
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (jsonArea) {
    jsonArea.value = "";
    autoResize(jsonArea);
  }
  const resultContainer = document.getElementById("resultadoAnalisis");
  const resultEl = resultContainer
    ? resultContainer.querySelector(".bg-light")
    : null;
  if (resultContainer) {
    resultContainer.style.display = "none";
  }
  if (resultEl) {
    resultEl.textContent = "";
  }

  const contenedor = document.getElementById("transaccionesAnalisis");
  if (contenedor) {
    contenedor.innerHTML = `
      <div class="transaccion-analisis">
        <label>Descripción de la transacción</label>
        <input type="text" class="descripcion-analisis form-control form-control-sm" />

        <br />

        <label>Valor</label>
        <input type="number" class="valor-analisis form-control form-control-sm" />
        <br />
        <hr />
      </div>
    `;
  }
}

function resetJSONPuroAnalisis() {
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (jsonArea) {
    jsonArea.value = "";
    autoResize(jsonArea);
  }
  const resultContainer = document.getElementById("resultadoAnalisis");
  const resultEl = resultContainer
    ? resultContainer.querySelector(".bg-light")
    : null;
  if (resultContainer) {
    resultContainer.style.display = "none";
  }
  if (resultEl) {
    resultEl.textContent = "";
  }
}

function enviarJSONPuroAnalisis() {
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (!jsonArea) {
    console.error("No se encontró el campo de JSON puro de análisis.");
    return;
  }

  let parsed;
  try {
    parsed = JSON.parse(jsonArea.value);
  } catch (e) {
    console.error("JSON inválido:", e.message);
    const resultContainer = document.getElementById("resultadoAnalisis");
    const resultEl = resultContainer
      ? resultContainer.querySelector(".bg-light")
      : null;
    if (resultContainer) {
      resultContainer.style.display = "block";
    }
    if (resultEl) {
      resultEl.style.color = "#c82333";
      resultEl.textContent = "JSON inválido: " + e.message;
    }
    return;
  }

  const settings = getBackendSettings("/api/analisis-financiero");
  if (settings.useBackend) {
    fetchBackend(settings.endpoint, parsed, "resultadoAnalisis");
    return;
  }

  console.log("JSON a enviar crudo (analisis):", parsed);

  const transacciones = Array.isArray(parsed.transacciones)
    ? parsed.transacciones
    : [];
  const mockResponse = crearMockResponseAnalisis(transacciones);
  console.log("Response (HTTP 200 OK)");
  console.log(mockResponse);
  mostrarResultadoAnalisis(mockResponse);
}

function resetJSONPuroAnalisis() {
  const jsonArea = document.getElementById("jsonPuroAnalisis");
  if (jsonArea) {
    jsonArea.value = "";
    autoResize(jsonArea);
  }
  const resultContainer = document.getElementById("resultadoAnalisis");
  const resultEl = resultContainer
    ? resultContainer.querySelector(".bg-light")
    : null;
  if (resultContainer) {
    resultContainer.style.display = "none";
  }
  if (resultEl) {
    resultEl.textContent = "";
  }
}

function agregarTransaccionAnalisis() {
  const contenedor = document.getElementById("transaccionesAnalisis");

  const div = document.createElement("div");
  div.className = "transaccion-analisis";

  div.innerHTML =
    "<label>Descripción de la transacción</label>" +
    '<input type="text" class="descripcion-analisis form-control form-control-sm" />' +
    "<br />" +
    "<label>Valor</label>" +
    '<input type="number" class="valor-analisis form-control form-control-sm numeric-only" inputmode="numeric" pattern="[0-9]*" />' +
    "<br />" +
    "<hr />";

  contenedor.appendChild(div);
  div.querySelectorAll("input.numeric-only").forEach(applyNumericMask);
}

function eliminarTransaccionAnalisis() {
  const contenedor = document.getElementById("transaccionesAnalisis");
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

// Añade una nueva transacción (grupo de inputs)
function agregarTransaccion() {
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

// Elimina la última transacción; si sólo queda una, la limpia
function eliminarTransaccion() {
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

// Recolecta todas las transacciones y muestra (o envía) el JSON
function enviarTransacciones() {
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

// Envia el JSON que el usuario escribió en el textarea `jsonPuro`
function enviarJSONPuro() {
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

function resetClasificacionForm() {
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

function resetJSONPuro() {
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

function applyNumericMask(input) {
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

function setupNumericOnlyInputs() {
  document.querySelectorAll("input.numeric-only").forEach(applyNumericMask);
}

document.addEventListener("DOMContentLoaded", function () {
  setupNumericOnlyInputs();
});
if (document.readyState !== "loading") {
  setupNumericOnlyInputs();
}

// Ajusta la altura del textarea al contenido
function autoResize(el) {
  if (!el) return;
  el.style.height = "auto";
  el.style.height = el.scrollHeight + "px";
}

function clasificarTransaccion() {
  let descripcion = document.getElementById("descripcion").value;

  let valor = document.getElementById("valor").value;

  const datos = [
    {
      descripcion: descripcion,

      valor: Number(valor),
    },
  ];

  console.log("JSON enviado al Backend:");
  console.log(datos);
  console.log(descripcion, valor);

  // Mock temporal mientras Backend no está disponible

  /*
  fetch("http://localhost:8080/api/clasificar-transacciones", {

    method: "POST",

    headers: {
      "Content-Type": "application/json",
    },

    body: JSON.stringify(datos),

  })

  .then((response) => response.json())

  .then((data) => {
    console.log(data);
  })

  .catch((error) => {
    console.log(error);
  });
  */

  // Simulación de respuesta del Backend

  const respuestaBackend = {
    alimentacion: Number(valor),
  };

  console.log("Respuesta simulada del Backend:");
  console.log(respuestaBackend);

  function agregarObjeto() {
    const contenedor = document.getElementById("objetos");

    const input = document.createElement("input");
    input.type = "text";
    input.placeholder = "Nuevo objeto";

    contenedor.appendChild(input);
  }
}
