package es.deusto.sd.ecoembes.client.proxies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import es.deusto.sd.ecoembes.client.data.ContenedorCreacion;
import es.deusto.sd.ecoembes.client.data.ContenedorRango;
import es.deusto.sd.ecoembes.client.data.ContenedorZona;
import es.deusto.sd.ecoembes.client.data.PlantaDTOCap;
import es.deusto.sd.ecoembes.client.data.RutaDTO;
import es.deusto.sd.ecoembes.client.data.Usuario;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

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
    public ContenedorRango consultarEstadoContenedor(Long id, LocalDate fIni, LocalDate fFin, String token) {
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



    @Override
    public boolean logout(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/logout/" + token))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> Boolean.parseBoolean(response.body());
                case 401 -> throw new RuntimeException("Unauthorized: Token inválido o expirado");
                default -> throw new RuntimeException(
                        "Logout failed with status code: " + response.statusCode()
                );
            };

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ha habido un error al procesar la petición de logout", e);
        }
    }



    @Override
    public List<PlantaDTOCap> consultarCapacidadPlantas(LocalDate fecha, String token) {
        try {
            String url = BASE_URL + "/capacidadPlantas"
                    + "?fecha=" + fecha
                    + "&token=" + token;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            return switch (response.statusCode()) {
                case 200 -> objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<PlantaDTOCap>>() {}
                );
                case 404 -> throw new RuntimeException("No se encontraron plantas para la fecha indicada");
                case 400 -> throw new RuntimeException("Petición incorrecta: " + response.body());
                default -> throw new RuntimeException(
                        "Error al consultar capacidad de plantas. Código: " + response.statusCode()
                );
            };

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ha habido un error al procesar la petición", e);
        }
    }



    @Override
    public List<String> getPlantas() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/plantas"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<String> plantas = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {});
            System.out.println("Plantas recibidas: " + plantas);
            return plantas;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void crearRuta(String token, RutaDTO ruta) {
        try {
            System.out.println("RUTA: " + ruta);
            System.out.println("Token: " + token);
            String json = objectMapper.writeValueAsString(ruta);
            System.out.printf("Json " + json);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/ruta?token=" + token))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());
            if (response.statusCode() != 200) {
                throw new RuntimeException(response.body());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    //Funcion 3: Crear contenedor
    @Override
    public void crearContenedor(String token, ContenedorCreacion contenedor) {
        try {
            String json = objectMapper.writeValueAsString(contenedor);
            System.out.println("Json" + json);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/contenedor?token=" + token))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Code" +response.statusCode());
            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new RuntimeException("Error al crear el contenedor: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al crear el contenedor", e);
        }
    }
    //Funcion 5
    @Override
    public List<ContenedorZona> consultarEstadoContenedoresZona(
            String codPostal,
            LocalDate fecha,
            String token
    ) {
        try {
            String url = BASE_URL + "/contenedor"
                    + "?codPostal=" + codPostal
                    + "&fecha=" + fecha
                    + "&token=" + token;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Service");
            System.out.println(response.statusCode() + response.body());
            return switch (response.statusCode()) {
                case 200 -> objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<ContenedorZona>>() {}
                );
                case 404 -> throw new RuntimeException("No hay contenedores en esa zona");
                default -> throw new RuntimeException(
                        "Error al consultar contenedores por zona. Código: " + response.statusCode()
                );
            };

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al consultar contenedores por zona", e);
        }
    }


}
