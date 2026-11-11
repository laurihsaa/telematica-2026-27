package pit2027;

import java.util.Objects;
import org.json.JSONObject;

public abstract class Vehiculo {
    private final String matricula;
    private final Categoria categoria;
    private final int annio;
    private boolean disponible;

    protected Vehiculo(String matricula, Categoria categoria, int annio) {
        Objects.requireNonNull(matricula, "La matrícula no puede ser null");
        if (matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("La matrícula no puede estar vacía");
        }
        this.matricula = matricula;
        this.categoria = Objects.requireNonNull(categoria, "La categoría no puede ser null");
        if (annio < 1886) {
            throw new IllegalArgumentException("El año del vehículo no es válido: " + annio);
        }
        this.annio = annio;
        this.disponible = true;
    }

    protected Vehiculo(JSONObject json) {
        this(json.getString("matricula"),
                Categoria.valueOf(json.getString("categoria")),
                json.getInt("annio"));
        this.disponible = json.optBoolean("disponible", true);
    }

    public abstract String getTipo();

    public abstract int getPrecioBase();

    public String getMatricula() {
        return matricula;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getAnnio() {
        return annio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    protected int precioPorCategoria() {
        switch (categoria) {
            case NORMAL:
                return 50;
            case ESPECIAL:
                return 80;
            case SUPERIOR:
                return 100;
            default:
                throw new IllegalStateException("Categoría no soportada: " + categoria);
        }
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("tipo", getTipo())
                .put("precioBase", getPrecioBase())
                .put("categoria", categoria.name())
                .put("matricula", matricula)
                .put("annio", annio)
                .put("disponible", disponible);
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }
}
