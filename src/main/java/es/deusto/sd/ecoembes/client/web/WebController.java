package es.deusto.sd.ecoembes.client.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import es.deusto.sd.ecoembes.client.data.ContenedorRango;
import es.deusto.sd.ecoembes.client.data.Usuario;
import es.deusto.sd.ecoembes.client.proxies.HTTPServiceProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;


@Controller

public class WebController {

    private final HTTPServiceProxy proxy;
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpClient httpClient = HttpClient.newHttpClient();
    private String token;

    public WebController(HTTPServiceProxy proxy) {

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
