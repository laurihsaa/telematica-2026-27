package pit2027;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class GestorAlquileresTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void filtraVehiculosPorTipoYCategoria() {
        GestorAlquileres gestor = new GestorAlquileres();

        assertEquals(3, gestor.obtenerVehiculos("Turismo").length);
        assertEquals(1, gestor.obtenerVehiculos("Motocicleta", Categoria.SUPERIOR).length);
        assertEquals(0, gestor.obtenerVehiculos("Avion").length);
    }

    @Test
    public void registraFiltraYDevuelveAlquileres() {
        GestorAlquileres gestor = new GestorAlquileres();
        Cliente cliente = gestor.obtenerClientes()[0];
        Vehiculo vehiculo = gestor.obtenerVehiculos("Turismo", Categoria.NORMAL)[0];
        Alquiler alquiler = new Alquiler(vehiculo, cliente,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2));

        gestor.agregarAlquiler(alquiler);
        assertFalse(vehiculo.isDisponible());
        assertEquals(1, gestor.obtenerAlquileres(cliente).length);
        assertFalse(gestor.esClienteFrecuente(cliente, 2));

        gestor.devolverAlquiler(alquiler);
        assertTrue(alquiler.isDevuelto());
        assertTrue(vehiculo.isDisponible());
    }

    @Test
    public void consideraFrecuenteAlClienteAlAlcanzarElUmbral() {
        GestorAlquileres gestor = new GestorAlquileres();
        Cliente cliente = gestor.obtenerClientes()[0];
        Vehiculo[] vehiculos = gestor.obtenerTodosLosVehiculos();

        assertFalse(gestor.esClienteFrecuente(cliente, 3));
        for (int i = 0; i < 3; i++) {
            gestor.agregarAlquiler(new Alquiler(vehiculos[i], cliente,
                    LocalDate.of(2026, 1, i + 1), LocalDate.of(2026, 1, i + 1)));
        }
        assertTrue(gestor.esClienteFrecuente(cliente, 3));
    }

    @Test(expected = IllegalStateException.class)
    public void noPermiteAlquilarDosVecesUnVehiculoNoDisponible() {
        GestorAlquileres gestor = new GestorAlquileres();
        Cliente cliente = gestor.obtenerClientes()[0];
        Vehiculo vehiculo = gestor.obtenerVehiculos("Turismo")[0];
        gestor.agregarAlquiler(new Alquiler(vehiculo, cliente,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1)));
        gestor.agregarAlquiler(new Alquiler(vehiculo, cliente,
                LocalDate.of(2026, 1, 2), LocalDate.of(2026, 1, 2)));
    }

    @Test
    public void guardaYRecuperaClientesVehiculosAlquileresYDisponibilidad() throws Exception {
        GestorAlquileres original = new GestorAlquileres();
        Cliente cliente = original.obtenerClientes()[0];
        Vehiculo vehiculo = original.obtenerVehiculos("Turismo", Categoria.NORMAL)[0];
        original.agregarAlquiler(new Alquiler(vehiculo, cliente,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 3),
                PoliticasDePrecios.Oferton));
        File fichero = temporaryFolder.newFile("datos.json");
        original.guardarDatos(fichero.getAbsolutePath());

        GestorAlquileres recuperado = new GestorAlquileres();
        recuperado.cargarDatos(fichero.getAbsolutePath());
        Alquiler[] alquileres = recuperado.obtenerAlquileres();

        assertEquals(original.obtenerClientes().length, recuperado.obtenerClientes().length);
        assertEquals(original.obtenerTodosLosVehiculos().length,
                recuperado.obtenerTodosLosVehiculos().length);
        assertEquals(1, alquileres.length);
        assertEquals(PoliticasDePrecios.Oferton, alquileres[0].getPoliticaPrecio());
        assertSame(recuperado.obtenerClientes()[0], alquileres[0].getCliente());
        assertSame(recuperado.obtenerVehiculos("Turismo", Categoria.NORMAL)[0],
                alquileres[0].getVehiculo());
        assertFalse(alquileres[0].getVehiculo().isDisponible());
        assertNotSame(original.obtenerClientes()[0], recuperado.obtenerClientes()[0]);
    }
}
