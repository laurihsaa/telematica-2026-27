package pit2027;

public class PrecioOferton implements PoliticaPrecio {
    @Override
    public double calcularPrecio(int precioBaseDiario, long dias) {
        ValidacionPrecio.validar(precioBaseDiario, dias);
        double precio = precioBaseDiario * 0.70 * dias;
        return dias > 7 ? precio * 0.90 : precio;
    }
}
