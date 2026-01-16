package es.deusto.sd.auctions.client.proxies;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.deusto.sd.auctions.client.data.ContenedorRango;
import es.deusto.sd.auctions.client.data.Usuario;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IServiceProxy {
    ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, long token);
    String logear(Usuario usuario) throws IOException, InterruptedException;
}
