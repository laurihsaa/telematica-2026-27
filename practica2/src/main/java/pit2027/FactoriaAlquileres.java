package pit2027;

import java.time.LocalDate;
import java.util.Objects;
import org.json.JSONObject;

public final class FactoriaAlquileres {
    private FactoriaAlquileres() {
    }

    public static Alquiler getAlquiler(String jsonString) {
        Objects.requireNonNull(jsonString, "El JSON del alquiler no puede ser null");
        return getAlquiler(new JSONObject(jsonString));
    }

    static Alquiler getAlquiler(JSONObject json) {
        Cliente cliente = FactoriaClientes.getCliente(json.getJSONObject("cliente").toString());
        Vehiculo vehiculo = FactoriaVehiculos.getVehiculo(json.getJSONObject("vehiculo"));
        return getAlquiler(json, cliente, vehiculo);
    }

    static Alquiler getAlquiler(JSONObject json, Cliente cliente, Vehiculo vehiculo) {
        return new Alquiler(
                vehiculo,
                cliente,
                LocalDate.parse(json.getString("fechaInicio")),
                LocalDate.parse(json.getString("fechaFin")),
                PoliticasDePrecios.valueOf(json.getString("politicaPrecio")),
                json.optBoolean("devuelto", false));
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("Ana", "12345678A");
        Vehiculo vehiculo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Alquiler original = new Alquiler(vehiculo, cliente,
                LocalDate.now(), LocalDate.now().plusDays(2), PoliticasDePrecios.Normal);
        Alquiler recuperado = getAlquiler(original.toString());
        System.out.println("Original: " + original);
        System.out.println("Recuperado: " + recuperado);
    }
}
