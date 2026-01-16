package es.deusto.sd.ecoembes.client.proxies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.deusto.sd.ecoembes.client.data.ContenedorRango;
import es.deusto.sd.ecoembes.client.data.Usuario;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class HTTPServiceProxy implements IServiceProxy {
    private static final String BASE_URL = "http://localhost:8080/ecoembes";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HTTPServiceProxy(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;

        // Solución de CHATGPT ante error de fechas de Estado en la función 4
            // Registrar módulo para Java 8 Time y configurar fechas ISO
        this.objectMapper = objectMapper.copy(); // Copiar para no modificar otros usos
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Func 4: Consultar estado contenedor
    @Override
    public es.deusto.sd.ecoembes.client.data.ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/contenedor/"+ id
                            +"?fIni="+ fIni
                            + "&fFin=" + fFin
                            + "&token=" + token))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 ->  objectMapper.readValue(response.body(),
                        ContenedorRango.class);
                case 404 -> throw new RuntimeException("Contenedor no encontrado");
                default -> throw new RuntimeException("Consulta de contenedor por fecha ha fallado, code: " + response.statusCode());
            };

        }catch(Exception e){
            throw new RuntimeException("Ha habido un error al procesar la peticion", e);
        }
    }

    // Func 1: Login
    @Override
    public String logear(Usuario usuario)  {
        try{
            String json = objectMapper.writeValueAsString(usuario);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create((BASE_URL + "/login")))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("Login failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e){
            throw new RuntimeException("Ha habido un error al procesar la peticion");
        }


    }
}
