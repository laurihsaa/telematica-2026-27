package pit2027;

import java.util.Objects;

public final class FactoriaClientes {
    private FactoriaClientes() {
    }

    public static Cliente getCliente(String jsonString) {
        Objects.requireNonNull(jsonString, "El JSON del cliente no puede ser null");
        return new Cliente(jsonString);
    }
}
