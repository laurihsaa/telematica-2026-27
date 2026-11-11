package pit2027;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PoliticaPrecioTest {
    @Test
    public void precioNormalMultiplicaBasePorDias() {
        assertEquals(200.0, new PrecioNormal().calcularPrecio(50, 4), 0.0);
    }

    @Test
    public void temporadaAltaDuplicaPrecioDiario() {
        assertEquals(400.0, new PrecioTemporadaAlta().calcularPrecio(50, 4), 0.0);
    }

    @Test
    public void ofertaAplicaDescuentoDiarioYDescuentoAdicionalDesdeOchoDias() {
        assertEquals(245.0, new PrecioOferton().calcularPrecio(50, 7), 0.0);
        assertEquals(252.0, new PrecioOferton().calcularPrecio(50, 8), 0.0);
    }

    @Test
    public void clienteFrecuenteAplicaDosTramosDeDescuento() {
        assertEquals(120.0, new PrecioClienteFrecuente().calcularPrecio(50, 3), 0.0);
        assertEquals(150.0, new PrecioClienteFrecuente().calcularPrecio(50, 4), 0.0);
    }

    @Test
    public void factoriaCreaLaEstrategiaSolicitada() {
        assertEquals(200.0, FactoriaPoliticas.getPolitica(PoliticasDePrecios.Normal)
                .calcularPrecio(50, 4), 0.0);
        assertEquals(800.0, FactoriaPoliticas.getPolitica(PoliticasDePrecios.TemporadaAlta)
                .calcularPrecio(50, 8), 0.0);
        assertEquals(270.0, FactoriaPoliticas.getPolitica(PoliticasDePrecios.ClienteFrecuente)
                .calcularPrecio(50, 8), 0.0);
        assertEquals(252.0, FactoriaPoliticas.getPolitica(PoliticasDePrecios.Oferton)
                .calcularPrecio(50, 8), 0.0);
        assertEquals(252.0, FactoriaPoliticas.getPolitica(" ofertON ")
                .calcularPrecio(50, 8), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoriaRechazaUnaPoliticaDesconocida() {
        FactoriaPoliticas.getPolitica("Desconocida");
    }

    @Test
    public void todasLasPoliticasRechazanDuracionesNoPositivas() {
        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            try {
                FactoriaPoliticas.getPolitica(politica).calcularPrecio(50, 0);
                org.junit.Assert.fail("Se debe rechazar una duración de cero: " + politica);
            } catch (IllegalArgumentException expected) {
                assertEquals("La duración del alquiler debe ser positiva", expected.getMessage());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaUnPrecioBaseNegativo() {
        new PrecioNormal().calcularPrecio(-1, 1);
    }
}
