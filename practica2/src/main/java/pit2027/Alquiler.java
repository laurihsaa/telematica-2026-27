package pit2027;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import org.json.JSONObject;

public class Alquiler {
    private final Vehiculo vehiculo;
    private final Cliente cliente;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final PoliticasDePrecios politicaPrecio;
    private final PoliticaPrecio estrategiaPrecio;
    private boolean devuelto;

    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin) {
        this(vehiculo, cliente, fechaInicio, fechaFin, PoliticasDePrecios.Normal);
    }

    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio,
            LocalDate fechaFin, PoliticasDePrecios politicaPrecio) {
        this.vehiculo = Objects.requireNonNull(vehiculo, "El vehículo no puede ser null");
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser null");
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser null");
        this.fechaFin = Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser null");
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
        this.politicaPrecio = Objects.requireNonNull(politicaPrecio, "La política no puede ser null");
        this.estrategiaPrecio = FactoriaPoliticas.getPolitica(politicaPrecio);
        this.devuelto = false;
    }

    Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio,
            LocalDate fechaFin, PoliticasDePrecios politicaPrecio, boolean devuelto) {
        this(vehiculo, cliente, fechaInicio, fechaFin, politicaPrecio);
        this.devuelto = devuelto;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public PoliticasDePrecios getPoliticaPrecio() {
        return politicaPrecio;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public long getDiasAlquiler() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    public double calcularPrecioAlquiler() {
        return estrategiaPrecio.calcularPrecio(vehiculo.getPrecioBase(), getDiasAlquiler());
    }

    public double calcularPrecioAlquiler(LocalDate inicio, LocalDate fin) {
        Objects.requireNonNull(inicio, "La fecha de inicio no puede ser null");
        Objects.requireNonNull(fin, "La fecha de fin no puede ser null");
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        return estrategiaPrecio.calcularPrecio(vehiculo.getPrecioBase(), dias);
    }

    void marcarDevuelto() {
        if (devuelto) {
            throw new IllegalStateException("El alquiler ya está devuelto");
        }
        devuelto = true;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("vehiculo", vehiculo.toJSONObject())
                .put("cliente", cliente.toJSONObject())
                .put("fechaInicio", fechaInicio.toString())
                .put("fechaFin", fechaFin.toString())
                .put("politicaPrecio", politicaPrecio.name())
                .put("devuelto", devuelto);
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    public static void main(String[] args) {
        Vehiculo vehiculo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Juan Pérez", "123456789");
        Alquiler alquiler = new Alquiler(vehiculo, cliente,
                LocalDate.now(), LocalDate.now().plusDays(2), PoliticasDePrecios.Oferton);
        System.out.println(alquiler);
        System.out.println("Precio: " + alquiler.calcularPrecioAlquiler());
    }
}
