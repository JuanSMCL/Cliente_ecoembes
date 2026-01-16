package es.deusto.sd.ecoembes.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.deusto.sd.ecoembes.client.proxies.HTTPServiceProxy;
import es.deusto.sd.ecoembes.client.proxies.IServiceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootApplication(scanBasePackages = {"es.deusto.sd.ecoembes.client"})
public class WebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebClientApplication.class, args);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public IServiceProxy serviceProxy(HttpClient httpClient, ObjectMapper objectMapper) {
        return new HTTPServiceProxy(httpClient, objectMapper);
    }
}