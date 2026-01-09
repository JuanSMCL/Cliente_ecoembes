package es.deusto.sd.auctions.client.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Func 4: Consulta del uso/estado de un contenedor por fecha
    @GetMapping("/")
    public String consultaContenedorPorFecha(Model model){
        return "";
    }


}
