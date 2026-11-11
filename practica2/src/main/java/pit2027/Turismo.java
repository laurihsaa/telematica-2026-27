package pit2027;

import org.json.JSONObject;

public class Turismo extends Vehiculo {
    private final int plazas;
    private final double capacidadMaletero;

    public Turismo(String matricula, Categoria categoria, int annio, int plazas, double capacidadMaletero) {
        super(matricula, categoria, annio);
        if (plazas <= 0 || !Double.isFinite(capacidadMaletero) || capacidadMaletero < 0) {
            throw new IllegalArgumentException("Las características del turismo no son válidas");
        }
        this.plazas = plazas;
        this.capacidadMaletero = capacidadMaletero;
    }

    Turismo(JSONObject json) {
        this(json.getString("matricula"),
                Categoria.valueOf(json.getString("categoria")),
                json.getInt("annio"),
                json.getInt("plazas"),
                json.getDouble("capacidadMaletero"));
        setDisponible(json.optBoolean("disponible", true));
    }

    @Override
    public String getTipo() {
        return "Turismo";
    }

    @Override
    public int getPrecioBase() {
        return precioPorCategoria();
    }

    public int getPlazas() {
        return plazas;
    }

    public double getCapacidadMaletero() {
        return capacidadMaletero;
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject()
                .put("plazas", plazas)
                .put("capacidadMaletero", capacidadMaletero);
    }
}
