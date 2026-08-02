import {
  guardarSesion,
  obtenerSesion,
  limpiarSesion,
  esAutenticado,
  getAuthHeaders,
  actualizarNavbarAuth,
  cerrarSesion,
  protegerRuta,
  inicializarAuthFrontend,
  limpiarMensaje,
  mostrarMensaje,
  registrarCuenta,
  iniciarSesion,
} from "./auth.js";
import {
  DATOS_PRUEBA_CLASIFICACION,
  cargarDatosPruebaClasificacion,
  cargarDatosPruebaJSONClasificacion,
  agregarTransaccion,
  eliminarTransaccion,
  enviarTransacciones,
  enviarJSONPuro,
  resetClasificacionForm,
  resetJSONPuro,
  mostrarResultadoClasificacion,
} from "./classification.js";
import {
  DATOS_PRUEBA_ANALISIS,
  cargarDatosPruebaAnalisis,
  cargarDatosPruebaJSONAnalisis,
  enviarDatos,
  crearMockResponseAnalisis,
  mostrarResultadoAnalisis,
  resetAnalisisForm,
  resetJSONPuroAnalisis,
  enviarJSONPuroAnalisis,
  agregarTransaccionAnalisis,
  eliminarTransaccionAnalisis,
} from "./analysis.js";
import {
  autoResize,
  setupNumericOnlyInputs,
  getBackendSettings,
  buildBackendUrl,
  parseResponseBody,
  fetchBackend,
  applyNumericMask,
} from "./utils.js";

window.guardarSesion = guardarSesion;
window.obtenerSesion = obtenerSesion;
window.limpiarSesion = limpiarSesion;
window.esAutenticado = esAutenticado;
window.getAuthHeaders = getAuthHeaders;
window.actualizarNavbarAuth = actualizarNavbarAuth;
window.cerrarSesion = cerrarSesion;
window.protegerRuta = protegerRuta;
window.inicializarAuthFrontend = inicializarAuthFrontend;
window.limpiarMensaje = limpiarMensaje;
window.mostrarMensaje = mostrarMensaje;
window.registrarCuenta = registrarCuenta;
window.iniciarSesion = iniciarSesion;
window.cargarDatosPruebaClasificacion = cargarDatosPruebaClasificacion;
window.cargarDatosPruebaJSONClasificacion = cargarDatosPruebaJSONClasificacion;
window.agregarTransaccion = agregarTransaccion;
window.eliminarTransaccion = eliminarTransaccion;
window.enviarTransacciones = enviarTransacciones;
window.enviarJSONPuro = enviarJSONPuro;
window.resetClasificacionForm = resetClasificacionForm;
window.resetJSONPuro = resetJSONPuro;
window.mostrarResultadoClasificacion = mostrarResultadoClasificacion;
window.cargarDatosPruebaAnalisis = cargarDatosPruebaAnalisis;
window.cargarDatosPruebaJSONAnalisis = cargarDatosPruebaJSONAnalisis;
window.enviarDatos = enviarDatos;
window.crearMockResponseAnalisis = crearMockResponseAnalisis;
window.mostrarResultadoAnalisis = mostrarResultadoAnalisis;
window.resetAnalisisForm = resetAnalisisForm;
window.resetJSONPuroAnalisis = resetJSONPuroAnalisis;
window.enviarJSONPuroAnalisis = enviarJSONPuroAnalisis;
window.agregarTransaccionAnalisis = agregarTransaccionAnalisis;
window.eliminarTransaccionAnalisis = eliminarTransaccionAnalisis;
window.autoResize = autoResize;
window.setupNumericOnlyInputs = setupNumericOnlyInputs;
window.getBackendSettings = getBackendSettings;
window.buildBackendUrl = buildBackendUrl;
window.parseResponseBody = parseResponseBody;
window.fetchBackend = fetchBackend;
window.applyNumericMask = applyNumericMask;

window.addEventListener("DOMContentLoaded", function () {
  setupNumericOnlyInputs();
  inicializarAuthFrontend();
});

if (document.readyState !== "loading") {
  setupNumericOnlyInputs();
  inicializarAuthFrontend();
}
