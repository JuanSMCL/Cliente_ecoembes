package es.deusto.sd.auctions.client.data;

import java.time.LocalDate;

public record ContenedorZona(
         Long id,
         double lon,
         double lat,
         String codPostal,
         int capacidadMax,
         int capacidadActual,
         NivelLlenado nivelLlenado
)
{ }
