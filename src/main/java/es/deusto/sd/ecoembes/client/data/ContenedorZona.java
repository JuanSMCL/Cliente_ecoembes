package es.deusto.sd.ecoembes.client.data;

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
