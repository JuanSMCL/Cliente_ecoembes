package es.deusto.sd.ecoembes.client.proxies;

import es.deusto.sd.ecoembes.client.data.ContenedorRango;
import es.deusto.sd.ecoembes.client.data.Usuario;

import java.time.LocalDate;

public interface IServiceProxy {
    ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, long token);
    String logear(Usuario usuario);

}
