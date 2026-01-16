package es.deusto.sd.ecoembes.client.proxies;

import java.time.LocalDate;
import es.deusto.sd.ecoembes.client.data.ContenedorRango;

public interface IServiceProxy {
    ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, long token);

}
