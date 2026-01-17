package es.deusto.sd.ecoembes.client.proxies;

import es.deusto.sd.ecoembes.client.data.ContenedorRango;
import es.deusto.sd.ecoembes.client.data.PlantaDTOCap;
import es.deusto.sd.ecoembes.client.data.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface IServiceProxy {
    ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, String token);
    String logear(Usuario usuario);
    boolean logout(String token);
    List<PlantaDTOCap> consultarCapacidadPlantas(LocalDate fecha, long token);
}
