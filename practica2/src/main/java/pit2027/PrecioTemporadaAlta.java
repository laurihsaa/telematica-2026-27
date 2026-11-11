package pit2027;

public class PrecioTemporadaAlta implements PoliticaPrecio {
    @Override
    public double calcularPrecio(int precioBaseDiario, long dias) {
        return precioBaseDiario * 2.0 * dias;
    }
}
