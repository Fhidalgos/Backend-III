package cl.duoc.ms_cuentas.client;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClienteClient {

    private final RestClient restClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public ClienteClient(
            RestClient.Builder restClientBuilder,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {

        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8091")
                .build();

        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public boolean existeCliente(
            Long clienteId,
            String authorizationHeader) {

        return circuitBreakerFactory
                .create("msClientes")
                .run(
                        () -> consultarCliente(
                                clienteId,
                                authorizationHeader
                        ),
                        throwable -> {
                            System.out.println(
                                    "RESILIENCE4J FALLBACK - "
                                    + throwable.getClass().getSimpleName()
                            );

                            return false;
                        }
                );
    }

    private boolean consultarCliente(
            Long clienteId,
            String authorizationHeader) {

        restClient.get()
                .uri("/api/clientes/{id}", clienteId)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .toBodilessEntity();

        return true;
    }
}