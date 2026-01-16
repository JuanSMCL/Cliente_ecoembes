package es.deusto.sd.ecoembes.client.proxies;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        this.objectMapper = objectMapper;
    }

    @Override
    public es.deusto.sd.ecoembes.client.data.ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, long token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/contenedores/"+ id
                            +"?fechaIni="+ fIni
                            + "&fechaFin=" + fFin
                            + "&token=" + token))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                return objectMapper.readValue(
                        response.body(),
                        ContenedorRango.class);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String logear(Usuario usuario)  {
        try{
            String json = objectMapper.writeValueAsString(usuario);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create((BASE_URL + "/login")))
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
