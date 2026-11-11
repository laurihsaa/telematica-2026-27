package pit2027;

import java.util.Objects;
import org.json.JSONObject;

public class Cliente {
    private final String nombre;
    private final String dni;

    public Cliente(String nombre, String dni) {
        this.nombre = requireText(nombre, "El nombre");
        this.dni = requireText(dni, "El DNI");
    }

    public Cliente(String jsonString) {
        this(new JSONObject(jsonString));
    }

    Cliente(JSONObject json) {
        this(json.getString("nombre"), json.getString("dni"));
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field + " no puede ser null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " no puede estar vacío");
        }
        return value;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public JSONObject toJSONObject() {
        return new JSONObject().put("nombre", nombre).put("dni", dni);
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("Juan Pérez", "12345678A");
        System.out.println(FactoriaClientes.getCliente(cliente.toString()));
    }
}
