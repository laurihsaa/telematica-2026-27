package pit2027;

public class PrecioTemporadaAlta implements PoliticaPrecio {
    @Override
    public double calcularPrecio(int precioBaseDiario, long dias) {
        ValidacionPrecio.validar(precioBaseDiario, dias);
        return precioBaseDiario * 2.0 * dias;
    }
}
