package pit2027;

import java.util.Objects;
import org.json.JSONObject;

public final class FactoriaVehiculos {
    private FactoriaVehiculos() {
    }

    public static Vehiculo getVehiculo(String jsonString) {
        Objects.requireNonNull(jsonString, "El JSON del vehículo no puede ser null");
        return getVehiculo(new JSONObject(jsonString));
    }

    static Vehiculo getVehiculo(JSONObject json) {
        String tipo = json.getString("tipo");
        switch (tipo) {
            case "Turismo":
                return new Turismo(json);
            case "Motocicleta":
                return new Motocicleta(json);
            case "VehiculoIndustrial":
                return new VehiculoIndustrial(json);
            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido: " + tipo);
        }
    }

    public static void main(String[] args) {
        Vehiculo original = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Vehiculo recuperado = getVehiculo(original.toString());
        System.out.println(original);
        System.out.println(recuperado);
    }
}
