package es.deusto.sd.ecoembes.client.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import es.deusto.sd.ecoembes.client.data.ContenedorRango;
import es.deusto.sd.ecoembes.client.data.PlantaDTOCap;
import es.deusto.sd.ecoembes.client.data.RutaDTO;
import es.deusto.sd.ecoembes.client.data.Usuario;
import es.deusto.sd.ecoembes.client.proxies.IServiceProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.List;


@Controller

public class WebController {

    private final IServiceProxy proxy;
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpClient httpClient = HttpClient.newHttpClient();
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
            @RequestParam long contenedorId,
            @RequestParam LocalDate fechaIni,
            @RequestParam LocalDate fechaFin,
            Model model){

       if (token != null){
           ContenedorRango resultado = proxy.consultarEstadoContenedor(contenedorId, fechaIni, fechaFin, token);

           // Esto para el HTML
           model.addAttribute("contenedorFecha", resultado);

           return "contenedores";
       }else{
           return "redirect:/index";
       }
    }

    @GetMapping("/login")
    public String mostrarLogin(
    ) {


        return "index";
    }

    @PostMapping("/login")
    public String logear(@RequestParam("username") String email,
                         @RequestParam("password") String password,
                         Model model)
    {
        Usuario usuario = new Usuario(email, password);
        try {
            this.token = proxy.logear(usuario);
            return "menu";
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "index";
        }

    }

    @PostMapping("/rutas")
    public String crearRuta(
        @RequestParam("camion") Long camionId,
        @RequestParam("plantaReciclaje") String plantaReciclaje,
        @RequestParam("contenedores") List<Long> contenedoresId,
        Model model
    ){

        RutaDTO ruta = new RutaDTO(contenedoresId, camionId, plantaReciclaje);
        try {
            proxy.crearRuta(token, ruta);
            model.addAttribute("successMessage", "Ruta creada correctamente");
            model.addAttribute("rutaCreada", true);

        } catch (Exception e){
            model.addAttribute("errorMessage", "Error al crear la ruta");
        }

        List<String> plantasReciclaje = proxy.getPlantas();
        model.addAttribute("plantasReciclaje", plantasReciclaje);

        return "plantas";


    }

    @GetMapping("/menu")
    public String mostrarIndex(Model model){
        System.out.println("token" + token);
        if(token == null){
            System.out.println("ver login");
            return "redirect:/index";
        }

        return "menu";
    }



    @PostMapping("/logout/{token}")
    public String logear(@PathVariable("token") String token,
                         Model model)
    {
        try {
            boolean resultado = proxy.logout(token);
            if (resultado){
                model.addAttribute("successMessage", "Logout successful.");
                return "index";
            }else {
                model.addAttribute("errorMessage", "Token not found");
                return "menu";
            }

        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "menu";
        }

    }

    @GetMapping("/plantas")
    public String verPlantas(Model model) {
        model.addAttribute("plantasReciclaje", proxy.getPlantas());
        return "plantas";
    }

    @GetMapping("/plantas/consultar")
    public String consultaPlantaCapacidad(
            @RequestParam LocalDate plantaFecha,

            // Hay que conseguir esto del login
            @ModelAttribute("token") long token,
            Model model){

        List<PlantaDTOCap> resultado = proxy.consultarCapacidadPlantas(plantaFecha, token);

        // Esto para el HTML
        model.addAttribute("plantas", resultado);

        // Nombre del html
        return "plantas";
    }




}
