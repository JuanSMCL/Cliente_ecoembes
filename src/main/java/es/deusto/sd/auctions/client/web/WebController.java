package es.deusto.sd.auctions.client.web;

import es.deusto.sd.auctions.client.data.ContenedorRango;
import es.deusto.sd.auctions.client.data.Usuario;
import es.deusto.sd.auctions.client.proxies.IServiceProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller

public class WebController {

    private final IServiceProxy proxy;
    private String token;

    public WebController(IServiceProxy proxy) {
        this.proxy = proxy;
    }

    // Metodo temporal para ver el html por primera vez
    @GetMapping("/contenedores")
    public String verContenedores() {
        return "contenedores";
    }


    // Func 4: Consulta del uso/estado de un contenedor por fecha
        // Lo que haya entre los "" tiene que coincidir con lo que ponga en el html
    @GetMapping("/contenedores/consultarFecha")
    public String consultaContenedorPorFecha(
            @RequestParam long id,
            @RequestParam LocalDate fIni,
            @RequestParam LocalDate fFin,

            // Hay que conseguir esto del login
            @ModelAttribute("token") long token,
            Model model){

        ContenedorRango resultado = proxy.consultarEstadoContenedor(id, fIni, fFin, token);

        // Esto para el HTML
        model.addAttribute("contenedorFecha", resultado);

        // Nombre del html
        return "contenedor";
    }

    @GetMapping("/login")
    public String mostrarLogin(
    ) {


        return "login";
    }

    @PostMapping("/login")
    public String logear(@RequestParam("username") String email,
                         @RequestParam("password") String password,
                         Model model)
    {
        Usuario usuario = new Usuario(email, password);
        try {
            this.token = proxy.logear(usuario);
            return "index";
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }


    }


}
