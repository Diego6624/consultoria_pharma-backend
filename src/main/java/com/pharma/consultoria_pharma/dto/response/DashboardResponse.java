package com.pharma.consultoria_pharma.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private long totalConsultas;
    private long totalArticulos;
    private long totalServicios;
    private long totalUbicaciones;
    private long totalCategorias;
    private long consultasEstaSemana;
    private long consultasSinRevisarHoy;
    private ConsultaResponse ultimaConsulta;
    private NoticiaResponse ultimoArticulo;
    private List<ConsultaResponse> consultasRecientes;
    private List<NoticiaResponse> articulosRecientes;
}
