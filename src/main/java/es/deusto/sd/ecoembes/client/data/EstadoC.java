package es.deusto.sd.ecoembes.client.data;

import java.time.LocalDate;

public record EstadoC(
        Long id,
        LocalDate fecha,
        NivelLlenado nivelLlenado,
        int capacidadActual
) {

}
