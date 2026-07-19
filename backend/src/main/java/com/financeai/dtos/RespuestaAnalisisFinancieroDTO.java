package com.financeai.dtos;

import java.util.List;
import java.util.Map;

public class RespuestaAnalisisFinancieroDTO {

    private String perfil_financiero;
    private Double probabilidad;
    private Map<String, Double> resumen_gastos;
    private List<String> recomendaciones;

    public RespuestaAnalisisFinancieroDTO() {}

    public RespuestaAnalisisFinancieroDTO(String perfil_financiero, Double probabilidad,
                                          Map<String, Double> resumen_gastos, List<String> recomendaciones) {
        this.perfil_financiero = perfil_financiero;
        this.probabilidad = probabilidad;
        this.resumen_gastos = resumen_gastos;
        this.recomendaciones = recomendaciones;
    }

    public String getPerfil_financiero() { return perfil_financiero; }
    public void setPerfil_financiero(String perfil_financiero) { this.perfil_financiero = perfil_financiero; }

    public Double getProbabilidad() { return probabilidad; }
    public void setProbabilidad(Double probabilidad) { this.probabilidad = probabilidad; }

    public Map<String, Double> getResumen_gastos() { return resumen_gastos; }
    public void setResumen_gastos(Map<String, Double> resumen_gastos) { this.resumen_gastos = resumen_gastos; }

    public List<String> getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(List<String> recomendaciones) { this.recomendaciones = recomendaciones; }

}
