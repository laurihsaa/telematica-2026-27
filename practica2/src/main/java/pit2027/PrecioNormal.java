package pit2027;

public class PrecioNormal implements PoliticaPrecio {
    @Override
    public double calcularPrecio(int precioBaseDiario, long dias) {
        return precioBaseDiario * (double) dias;
    }
}
