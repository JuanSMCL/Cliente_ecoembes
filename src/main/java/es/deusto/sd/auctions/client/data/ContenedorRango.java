package es.deusto.sd.auctions.client.data;

import java.time.LocalDate;

public record ContenedorRango(
        long id,
        LocalDate fIni,
        LocalDate fFin
)
{ }
