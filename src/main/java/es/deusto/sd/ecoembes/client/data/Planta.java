package es.deusto.sd.ecoembes.client.data;

import java.time.LocalDate;

// Este es para el endpoint de listar plantas
public record Planta(
        double lon,
        double lat,
        String nombre,
        LocalDate fecha,
        int capacidad
) {
}
