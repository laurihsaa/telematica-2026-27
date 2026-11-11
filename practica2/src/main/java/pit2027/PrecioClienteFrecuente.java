package pit2027;

public class PrecioClienteFrecuente implements PoliticaPrecio {
    @Override
    public double calcularPrecio(int precioBaseDiario, long dias) {
        ValidacionPrecio.validar(precioBaseDiario, dias);
        long diasDescuentoInicial = Math.min(dias, 3);
        long diasDescuentoAdicional = Math.max(0, dias - 3);
        return precioBaseDiario * 0.80 * diasDescuentoInicial
                + precioBaseDiario * 0.60 * diasDescuentoAdicional;
    }
}
