package es.deusto.sd.ecoembes.client.data;

import java.util.List;

public record ContenedorRango(
        long id,
        double lon,
        double lat,
        String codPostal,
        int capacidadMax,
        List<EstadoC>estados
)
{ }
