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
  agregarTransaccion,
  eliminarTransaccion,
  enviarTransacciones,
  resetClasificacionForm,
  mostrarResultadoClasificacion,
} from "./classification.js";
import {
  enviarDatos,
  mostrarResultadoAnalisis,
  resetAnalisisForm,
  agregarTransaccionAnalisis,
  eliminarTransaccionAnalisis,
} from "./analysis.js";
import {
  inicializarAnalisisHistorico,
  resetAnalisisHistoricoForm,
  enviarAnalisisHistoricoMisTransacciones,
} from "./historical-analysis.js";
import {
  resetRegistroTransaccionForm,
  registrarTransaccion,
} from "./register-transaction.js";
import {
  inicializarVistaTransacciones,
  limpiarFiltrosTransacciones,
  verTransaccionesFiltradas,
  eliminarTransaccionDesdeVista,
} from "./view-transactions.js";
import {
  bootstrapSupportedCurrencies,
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
window.agregarTransaccion = agregarTransaccion;
window.eliminarTransaccion = eliminarTransaccion;
window.enviarTransacciones = enviarTransacciones;
window.resetClasificacionForm = resetClasificacionForm;
window.mostrarResultadoClasificacion = mostrarResultadoClasificacion;
window.enviarDatos = enviarDatos;
window.mostrarResultadoAnalisis = mostrarResultadoAnalisis;
window.resetAnalisisForm = resetAnalisisForm;
window.agregarTransaccionAnalisis = agregarTransaccionAnalisis;
window.eliminarTransaccionAnalisis = eliminarTransaccionAnalisis;
window.inicializarAnalisisHistorico = inicializarAnalisisHistorico;
window.resetAnalisisHistoricoForm = resetAnalisisHistoricoForm;
window.enviarAnalisisHistoricoMisTransacciones =
  enviarAnalisisHistoricoMisTransacciones;
window.resetRegistroTransaccionForm = resetRegistroTransaccionForm;
window.registrarTransaccion = registrarTransaccion;
window.inicializarVistaTransacciones = inicializarVistaTransacciones;
window.limpiarFiltrosTransacciones = limpiarFiltrosTransacciones;
window.verTransaccionesFiltradas = verTransaccionesFiltradas;
window.eliminarTransaccionDesdeVista = eliminarTransaccionDesdeVista;
window.autoResize = autoResize;
window.setupNumericOnlyInputs = setupNumericOnlyInputs;
window.getBackendSettings = getBackendSettings;
window.buildBackendUrl = buildBackendUrl;
window.parseResponseBody = parseResponseBody;
window.fetchBackend = fetchBackend;
window.applyNumericMask = applyNumericMask;
window.bootstrapSupportedCurrencies = bootstrapSupportedCurrencies;

window.addEventListener("DOMContentLoaded", function () {
  bootstrapSupportedCurrencies().catch(() => {});
  setupNumericOnlyInputs();
  inicializarAuthFrontend();
  inicializarAnalisisHistorico();
  inicializarVistaTransacciones();
});

if (document.readyState !== "loading") {
  bootstrapSupportedCurrencies().catch(() => {});
  setupNumericOnlyInputs();
  inicializarAuthFrontend();
  inicializarAnalisisHistorico();
  inicializarVistaTransacciones();
}
