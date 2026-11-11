package pit2027;

import org.json.JSONObject;

public class VehiculoIndustrial extends Vehiculo {
    private final int capacidadCarga;
    private final int numeroEjes;

    public VehiculoIndustrial(String matricula, Categoria categoria, int annio,
            int capacidadCarga, int numeroEjes) {
        super(matricula, categoria, annio);
        if (capacidadCarga <= 0 || numeroEjes <= 0) {
            throw new IllegalArgumentException("La carga y los ejes deben ser positivos");
        }
        this.capacidadCarga = capacidadCarga;
        this.numeroEjes = numeroEjes;
    }

    VehiculoIndustrial(JSONObject json) {
        this(json.getString("matricula"),
                Categoria.valueOf(json.getString("categoria")),
                json.getInt("annio"),
                json.getInt("capacidadCarga"),
                json.getInt("numeroEjes"));
        setDisponible(json.optBoolean("disponible", true));
    }

    @Override
    public String getTipo() {
        return "VehiculoIndustrial";
    }

    @Override
    public int getPrecioBase() {
        return precioPorCategoria();
    }

    public int getCapacidadCarga() {
        return capacidadCarga;
    }

    public int getNumeroEjes() {
        return numeroEjes;
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject()
                .put("capacidadCarga", capacidadCarga)
                .put("numeroEjes", numeroEjes);
    }
}
