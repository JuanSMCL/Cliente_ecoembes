package es.deusto.sd.auctions.client.data;

import java.time.LocalDate;

public record EstadoC(
        Long id,
        LocalDate fecha,
        NivelLlenado nivelLlenado,
        int capacidadActual
) {

}
