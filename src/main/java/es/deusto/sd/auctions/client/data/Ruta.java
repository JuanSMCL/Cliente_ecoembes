package es.deusto.sd.auctions.client.data;

import java.time.LocalDate;
import java.util.List;

public record Ruta(
        String nombre,
        LocalDate fecha,
        List<Long> contenedoresIds
) {
}
