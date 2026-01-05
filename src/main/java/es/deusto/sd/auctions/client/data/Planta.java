package es.deusto.sd.auctions.client.data;

// Este es para el endpoint de listar plantas
public record Planta(
        long id,
        String nombre,
        String ubicacion
) {
}
