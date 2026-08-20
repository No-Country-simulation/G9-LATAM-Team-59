import { autoResize } from "./utils.js";

const AUTH_STORAGE_KEY = "smartwallet_auth";
const RUTAS_PROTEGIDAS = [
  "analisis_historico.html",
  "registrar_transaccion.html",
  "ver_transacciones.html",
];

function obtenerRutaActual() {
  return window.location.pathname.split("/").pop() || "index.html";
}

export function esRutaProtegidaActual() {
  return RUTAS_PROTEGIDAS.includes(obtenerRutaActual());
}

function normalizarSesion(authData) {
  if (!authData) return null;

  const username =
    authData?.username ||
    authData?.userName ||
    authData?.name ||
    authData?.user?.username ||
    authData?.user?.name ||
    null;

  return {
    ...authData,
    username,
  };
}

export function guardarSesion(authData) {
  if (!authData) return;
  const sesionNormalizada = normalizarSesion(authData);
  localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(sesionNormalizada));
}

export function obtenerSesion() {
  try {
    const data = localStorage.getItem(AUTH_STORAGE_KEY);
    return data ? normalizarSesion(JSON.parse(data)) : null;
  } catch (error) {
    console.error(error);
    return null;
  }
}

export function limpiarSesion() {
  localStorage.removeItem(AUTH_STORAGE_KEY);
}

export function esAutenticado() {
  const sesion = obtenerSesion();
  return Boolean(sesion?.token);
}

export function getAuthHeaders(extraHeaders = {}) {
  const sesion = obtenerSesion();
  return {
    ...extraHeaders,
    ...(sesion?.token
      ? { Authorization: `${sesion.tipo || "Bearer"} ${sesion.token}` }
      : {}),
  };
}

function escapeHtml(text) {
  return String(text ?? "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/\"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function obtenerNombreVisible(sesion) {
  return sesion?.username || sesion?.userName || sesion?.name || "Usuario";
}

function obtenerBotonEnvio() {
  return document.querySelector("form button[type='submit']");
}

function setBotonEnvioCargando(isLoading, texto = "Procesando...") {
  const boton = obtenerBotonEnvio();
  if (!boton) return;

  boton.disabled = isLoading;
  boton.dataset.originalText = boton.dataset.originalText || boton.textContent;

  if (isLoading) {
    boton.textContent = texto;
    boton.setAttribute("aria-busy", "true");
  } else {
    boton.textContent = boton.dataset.originalText || "Enviar";
    boton.removeAttribute("aria-busy");
  }
}

export function renderHeader() {
  const container = document.getElementById("site-header");
  if (!container) return;

  const sesion = obtenerSesion();
  const isLoggedIn = Boolean(sesion?.token);
  const nombreVisible = obtenerNombreVisible(sesion);

  container.innerHTML = `
    <nav class="nav">
      <a class="nav-link brand-link" href="../html/index.html">SW</a>
      <a class="nav-link" href="../html/clasificacion.html">Clasificación</a>
      <a class="nav-link" href="../html/analisis.html">Análisis</a>
      ${
        isLoggedIn
          ? `
            <a class="nav-link" href="../html/analisis_historico.html">Histórico</a>
            <a class="nav-link" href="../html/registrar_transaccion.html">Registrar transacción</a>
            <a class="nav-link" href="../html/ver_transacciones.html">Ver transacciones</a>
          `
          : ""
      }
      ${
        isLoggedIn
          ? `
            <div class="ms-auto d-flex align-items-center gap-2">
              <span class="nav-link text-white mb-0">${escapeHtml(nombreVisible)}</span>
              <button class="btn btn-outline-light btn-sm" type="button" onclick="cerrarSesion()">Cerrar sesión</button>
            </div>
          `
          : `
            <div class="ms-auto d-flex align-items-center gap-2">
              <a class="nav-link auth-link" href="../html/registrar_cuenta.html">Registrar</a>
              <a class="nav-link auth-link" href="../html/iniciar_sesion.html">Login</a>
            </div>
          `
      }
    </nav>
  `;
}

function aplicarVisibilidadRutasProtegidas() {
  const isLoggedIn = esAutenticado();
  document.querySelectorAll("[data-protected='true']").forEach((el) => {
    el.style.display = isLoggedIn ? "" : "none";
  });
}

export function actualizarNavbarAuth() {
  renderHeader();
  aplicarVisibilidadRutasProtegidas();
}

export function cerrarSesion() {
  limpiarSesion();
  actualizarNavbarAuth();
  if (esRutaProtegidaActual()) {
    window.location.href = "iniciar_sesion.html";
  } else {
    window.location.reload();
  }
}

export function protegerRuta() {
  const rutaActual = obtenerRutaActual();
  const rutasPublicasAuth = ["iniciar_sesion.html", "registrar_cuenta.html"];

  if (rutasPublicasAuth.includes(rutaActual) && esAutenticado()) {
    window.location.href = "index.html";
    return false;
  }

  if (esRutaProtegidaActual() && !esAutenticado()) {
    window.location.href = "iniciar_sesion.html";
    return false;
  }

  return true;
}

export function inicializarAuthFrontend() {
  const puedeContinuar = protegerRuta();
  if (!puedeContinuar) {
    return;
  }
  actualizarNavbarAuth();
}

export function limpiarMensaje() {
  const alerta = document.getElementById("mensaje-alerta");
  if (!alerta) return;

  alerta.textContent = "";
  alerta.className = "alert mt-3";
  alerta.style.display = "none";
}

export function mostrarMensaje(mensaje, tipo = "danger") {
  const alerta = document.getElementById("mensaje-alerta");
  if (!alerta) return;

  alerta.className = `alert alert-${tipo} mt-3`;
  alerta.textContent = mensaje;
  alerta.style.display = "block";
  alerta.setAttribute("role", "alert");
}

export function registrarCuenta() {
  limpiarMensaje();
  setBotonEnvioCargando(true, "Registrando...");

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const username = document.getElementById("username").value;

  const datos = {
    email,
    password,
    username,
  };

  console.log("JSON enviado al Backend:", datos);

  fetch("/api/auth/registrar-cuenta", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datos),
  })
    .then(async (response) => {
      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(
          errorData?.message ||
            errorData?.mensaje ||
            "No se pudo completar el registro.",
        );
      }
      const messageBody = await response.json();
      const message =
        messageBody?.message || "Cuenta registrada correctamente.";
      if (messageBody?.token) {
        guardarSesion(messageBody);
        const nombreUsuario =
          messageBody?.username ||
          messageBody?.userName ||
          messageBody?.name ||
          "Usuario";
        mostrarMensaje(`Hola, ${nombreUsuario}`, "success");
        setBotonEnvioCargando(true, "Redirigiendo...");
        window.setTimeout(() => {
          window.location.href = "index.html";
        }, 400);
      } else {
        mostrarMensaje(message, "success");
        setBotonEnvioCargando(true, "Redirigiendo...");
        window.setTimeout(() => {
          window.location.href = "iniciar_sesion.html";
        }, 400);
      }
    })
    .catch((error) => {
      console.log(error);
      setBotonEnvioCargando(false);
      mostrarMensaje(
        error.message || "Ocurrió un error al registrar la cuenta.",
      );
    });
}

export function iniciarSesion() {
  limpiarMensaje();
  setBotonEnvioCargando(true, "Ingresando...");

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const datos = {
    email,
    password,
  };

  console.log("JSON enviado al Backend:", datos);

  fetch("/api/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datos),
  })
    .then(async (response) => {
      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(
          errorData?.message ||
            errorData?.mensaje ||
            "No se pudo iniciar sesión.",
        );
      }
      return response.json();
    })
    .then((data) => {
      console.log(data);
      if (data?.token) {
        guardarSesion(data);
        const nombreUsuario =
          data?.username || data?.userName || data?.name || "Usuario";
        mostrarMensaje(`Hola, ${nombreUsuario}`, "success");
        setBotonEnvioCargando(true, "Redirigiendo...");
        window.setTimeout(() => {
          window.location.href = "index.html";
        }, 400);
      } else {
        throw new Error("No se recibió un token de autenticación.");
      }
    })
    .catch((error) => {
      console.log(error);
      setBotonEnvioCargando(false);
      mostrarMensaje(error.message || "No se pudo iniciar sesión.");
    });
}
