package es.deusto.sd.auctions.client.proxies;

import java.time.LocalDate;
import es.deusto.sd.auctions.client.data.ContenedorRango;

public interface IServiceProxy {
    ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, long token);

}
