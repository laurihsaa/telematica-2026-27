package pit2027;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import org.junit.Test;
import org.json.JSONObject;

public class FactoriaTest {
    @Test
    public void clienteSeSerializaYSeRecupera() {
        Cliente cliente = FactoriaClientes.getCliente(
                new Cliente("Laura", "12345678A").toString());

        assertEquals("Laura", cliente.getNombre());
        assertEquals("12345678A", cliente.getDni());
    }

    @Test
    public void factoriaRecuperaTodosLosTiposDeVehiculo() {
        Vehiculo[] originales = {
            new Turismo("T1", Categoria.NORMAL, 2020, 5, 450.0),
            new Motocicleta("M1", Categoria.SUPERIOR, 2021, 500),
            new VehiculoIndustrial("I1", Categoria.ESPECIAL, 2022, 2400, 3)
        };
        originales[1].setDisponible(false);

        for (Vehiculo original : originales) {
            Vehiculo recuperado = FactoriaVehiculos.getVehiculo(original.toString());
            assertEquals(original.getTipo(), recuperado.getTipo());
            assertEquals(original.getMatricula(), recuperado.getMatricula());
            assertEquals(original.getCategoria(), recuperado.getCategoria());
            assertEquals(original.isDisponible(), recuperado.isDisponible());
            assertEquals(original.getPrecioBase(), recuperado.getPrecioBase());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoriaRechazaUnTipoDeVehiculoDesconocido() {
        FactoriaVehiculos.getVehiculo(new JSONObject()
                .put("tipo", "Barco")
                .toString());
    }

    @Test
    public void alquilerSeSerializaYSeRecuperaConSusReferenciasYPolitica() {
        Cliente cliente = new Cliente("Ana", "A1");
        Vehiculo vehiculo = new Turismo("T1", Categoria.NORMAL, 2024, 5, 400.0);
        Alquiler original = new Alquiler(vehiculo, cliente,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 3),
                PoliticasDePrecios.ClienteFrecuente);
        Alquiler recuperado = FactoriaAlquileres.getAlquiler(original.toString());

        assertEquals("A1", recuperado.getCliente().getDni());
        assertEquals("T1", recuperado.getVehiculo().getMatricula());
        assertEquals(PoliticasDePrecios.ClienteFrecuente, recuperado.getPoliticaPrecio());
        assertEquals(3, recuperado.getDiasAlquiler());
        assertEquals(120.0, recuperado.calcularPrecioAlquiler(), 0.0);
        assertFalse(recuperado.isDevuelto());
    }

    @Test
    public void losAlquileresDevueltosMantienenSuEstadoAlDeserializarse() {
        Alquiler alquiler = new Alquiler(
                new Motocicleta("M1", Categoria.NORMAL, 2020, 200),
                new Cliente("Ana", "A1"),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 1));
        alquiler.marcarDevuelto();

        assertTrue(FactoriaAlquileres.getAlquiler(alquiler.toString()).isDevuelto());
    }
}
