package es.deusto.sd.auctions.client.data;

import java.time.LocalDate;

public record ContenedorZona(
        String codigoPostal,
        LocalDate fecha
)
{ }
