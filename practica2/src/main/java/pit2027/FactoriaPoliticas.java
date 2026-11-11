package pit2027;

import java.util.Objects;

public final class FactoriaPoliticas {
    private FactoriaPoliticas() {
    }

    public static PoliticaPrecio getPolitica(PoliticasDePrecios politica) {
        Objects.requireNonNull(politica, "La política de precios no puede ser null");
        switch (politica) {
            case Normal:
                return new PrecioNormal();
            case TemporadaAlta:
                return new PrecioTemporadaAlta();
            case ClienteFrecuente:
                return new PrecioClienteFrecuente();
            case Oferton:
                return new PrecioOferton();
            default:
                throw new IllegalArgumentException("Política de precios desconocida: " + politica);
        }
    }

    public static PoliticaPrecio getPolitica(String nombre) {
        Objects.requireNonNull(nombre, "El nombre de la política no puede ser null");
        try {
            return getPolitica(PoliticasDePrecios.valueOf(nombre));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Política de precios desconocida: " + nombre, exception);
        }
    }

    public static void main(String[] args) {
        int baseDiaria = 50;
        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            double precio = getPolitica(politica).calcularPrecio(baseDiaria, 8);
            System.out.println(politica + ": " + precio);
        }
    }
}
