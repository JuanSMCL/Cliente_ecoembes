package es.deusto.sd.auctions.client.data;

import java.time.LocalDate;
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
