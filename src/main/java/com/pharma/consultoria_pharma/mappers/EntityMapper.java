package com.pharma.consultoria_pharma.mappers;

import com.pharma.consultoria_pharma.dto.request.ConsultaRequest;
import com.pharma.consultoria_pharma.dto.request.NoticiaRequest;
import com.pharma.consultoria_pharma.dto.request.ServicioRequest;
import com.pharma.consultoria_pharma.dto.request.UbicacionRequest;
import com.pharma.consultoria_pharma.dto.response.*;
import com.pharma.consultoria_pharma.entities.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityMapper {

    // --- Categoría ---
    CategoriaResponse toCategoriaResponse(Categoria categoria);

    Categoria toCategoria(com.pharma.consultoria_pharma.dto.request.CategoriaRequest request);

    // --- Noticia ---
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    NoticiaResponse toNoticiaResponse(Noticia noticia);

    @Mapping(target = "idNoticia", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Noticia toNoticia(NoticiaRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateNoticia(NoticiaRequest request, @MappingTarget Noticia noticia);

    // --- Servicio ---
    ServicioResponse toServicioResponse(Servicio servicio);

    @Mapping(target = "idServicio", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Servicio toServicio(ServicioRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateServicio(ServicioRequest request, @MappingTarget Servicio servicio);

    // --- Consulta ---
    @Mapping(source = "servicio.idServicio", target = "idServicio")
    ConsultaResponse toConsultaResponse(Consulta consulta);

    @Mapping(target = "idConsulta", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    Consulta toConsulta(ConsultaRequest request);

    // --- Ubicación ---
    UbicacionResponse toUbicacionResponse(Ubicacion ubicacion);

    @Mapping(target = "idUbicacion", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Ubicacion toUbicacion(UbicacionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUbicacion(UbicacionRequest request, @MappingTarget Ubicacion ubicacion);

    // --- Usuario ---
    @Mapping(source = "rol.nombre", target = "rol")
    @Mapping(source = "creadoPor.nombre", target = "creadoPor")
    UsuarioResponse toUsuarioResponse(Usuario usuario);
}
