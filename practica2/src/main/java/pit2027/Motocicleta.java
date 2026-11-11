package pit2027;

import org.json.JSONObject;

public class Motocicleta extends Vehiculo {
    private final int cilindrada;

    public Motocicleta(String matricula, Categoria categoria, int annio, int cilindrada) {
        super(matricula, categoria, annio);
        if (cilindrada <= 0) {
            throw new IllegalArgumentException("La cilindrada debe ser positiva");
        }
        this.cilindrada = cilindrada;
    }

    Motocicleta(JSONObject json) {
        this(json.getString("matricula"),
                Categoria.valueOf(json.getString("categoria")),
                json.getInt("annio"),
                json.getInt("cilindrada"));
        setDisponible(json.optBoolean("disponible", true));
    }

    @Override
    public String getTipo() {
        return "Motocicleta";
    }

    @Override
    public int getPrecioBase() {
        return precioPorCategoria();
    }

    public int getCilindrada() {
        return cilindrada;
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject().put("cilindrada", cilindrada);
    }
}
