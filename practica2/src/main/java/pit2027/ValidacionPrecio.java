package pit2027;

final class ValidacionPrecio {
    private ValidacionPrecio() {
    }

    static void validar(int precioBaseDiario, long dias) {
        if (precioBaseDiario < 0) {
            throw new IllegalArgumentException("El precio base diario no puede ser negativo");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("La duración del alquiler debe ser positiva");
        }
    }
}
