package es.deusto.sd.ecoembes.client.proxies;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.deusto.sd.ecoembes.client.data.ContenedorRango;

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
    public ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, long token) {
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
}
