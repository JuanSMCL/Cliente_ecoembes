package es.deusto.sd.ecoembes.client.proxies;

import es.deusto.sd.ecoembes.client.data.ContenedorCreacion;
import es.deusto.sd.ecoembes.client.data.ContenedorRango;
import es.deusto.sd.ecoembes.client.data.ContenedorZona;
import es.deusto.sd.ecoembes.client.data.PlantaDTOCap;
import es.deusto.sd.ecoembes.client.data.RutaDTO;
import es.deusto.sd.ecoembes.client.data.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface IServiceProxy {
    ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, String token);
    String logear(Usuario usuario);
    boolean logout(String token);
    List<PlantaDTOCap> consultarCapacidadPlantas(LocalDate fecha, String token);
    List<String> getPlantas();
    void crearRuta(String token, RutaDTO ruta);
    void crearContenedor(String token, ContenedorCreacion contenedor);
    List<ContenedorZona> consultarEstadoContenedoresZona(String codPostal,LocalDate fecha,String token);


}
