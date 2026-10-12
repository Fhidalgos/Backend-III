package cl.duoc.ms_clientes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.ms_clientes.dto.ClienteRequest;
import cl.duoc.ms_clientes.dto.ClienteResponse;
import cl.duoc.ms_clientes.model.Cliente;
import cl.duoc.ms_clientes.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponse> listarClientes() {

        return clienteRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ClienteResponse buscarPorId(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cliente no encontrado con ID: " + id
                        )
                );

        return convertirAResponse(cliente);
    }

    public ClienteResponse crearCliente(ClienteRequest request) {

        if (clienteRepository.existsByRut(request.getRut())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con ese RUT"
            );
        }

        if (clienteRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con ese correo"
            );
        }

        Cliente cliente = new Cliente();

        cliente.setRut(request.getRut());
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());

        Cliente guardado = clienteRepository.save(cliente);

        return convertirAResponse(guardado);
    }

    public ClienteResponse actualizarCliente(
            Long id,
            ClienteRequest request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cliente no encontrado con ID: " + id
                        )
                );

        cliente.setRut(request.getRut());
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());

        Cliente actualizado = clienteRepository.save(cliente);

        return convertirAResponse(actualizado);
    }

    public void eliminarCliente(Long id) {

        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Cliente no encontrado con ID: " + id
            );
        }

        clienteRepository.deleteById(id);
    }

    private ClienteResponse convertirAResponse(Cliente cliente) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getRut(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getTelefono()
        );
    }
}