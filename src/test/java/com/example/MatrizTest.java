package com.example;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MatrizTest {

    @Test
    public void constructorRechazaArrayNulo() {
        assertIllegalArgumentException(null);
    }

    @Test
    public void constructorRechazaArraySinFilas() {
        assertIllegalArgumentException(new double[0][]);
    }

    @Test
    public void constructorRechazaFilaNula() {
        assertIllegalArgumentException(new double[][] {
                { 1.0 },
                null,
                { 2.0 }
        });
    }

    @Test
    public void constructorRechazaMatrizSinColumnas() {
        assertIllegalArgumentException(new double[][] {
                {},
                {}
        });
    }

    @Test
    public void constructorConUnaFilaConservaLosValores() {
        Matriz matriz = new Matriz(new double[][] {
                { 1.5, -2.0, 0.0 }
        });

        assertArrayEquals(new int[] { 1, 3 }, matriz.getDimension());
        assertEquals(1.5, matriz.getElemento(0, 0), 0.0);
        assertEquals(-2.0, matriz.getElemento(0, 1), 0.0);
        assertEquals(0.0, matriz.getElemento(0, 2), 0.0);
    }

    @Test
    public void constructorConUnaColumnaConservaLosValores() {
        Matriz matriz = new Matriz(new double[][] {
                { 1.0 },
                { -3.5 },
                { 0.0 }
        });

        assertArrayEquals(new int[] { 3, 1 }, matriz.getDimension());
        assertEquals(1.0, matriz.getElemento(0, 0), 0.0);
        assertEquals(-3.5, matriz.getElemento(1, 0), 0.0);
        assertEquals(0.0, matriz.getElemento(2, 0), 0.0);
    }

    @Test
    public void constructorConMatrizRectangularConservaDimensionesYValores() {
        Matriz matriz = new Matriz(new double[][] {
                { 1.0, 2.0 },
                { 3.0, 4.0 }
        });

        assertArrayEquals(new int[] { 2, 2 }, matriz.getDimension());
        assertEquals(1.0, matriz.getElemento(0, 0), 0.0);
        assertEquals(2.0, matriz.getElemento(0, 1), 0.0);
        assertEquals(3.0, matriz.getElemento(1, 0), 0.0);
        assertEquals(4.0, matriz.getElemento(1, 1), 0.0);
    }

    @Test
    public void constructorRellenaConCerosLasFilasMasCortas() {
        Matriz matriz = new Matriz(new double[][] {
                { 1.0, 2.0, 3.0 },
                { 4.0 },
                { 5.0, 6.0 }
        });

        assertArrayEquals(new int[] { 3, 3 }, matriz.getDimension());
        assertEquals(0.0, matriz.getElemento(1, 1), 0.0);
        assertEquals(0.0, matriz.getElemento(1, 2), 0.0);
        assertEquals(0.0, matriz.getElemento(2, 2), 0.0);
    }

    @Test
    public void constructorPermiteFilasVaciasSiHayAlgunaColumna() {
        Matriz matriz = new Matriz(new double[][] {
                {},
                { 7.0, 8.0 },
                {}
        });

        assertArrayEquals(new int[] { 3, 2 }, matriz.getDimension());
        assertEquals(0.0, matriz.getElemento(0, 0), 0.0);
        assertEquals(0.0, matriz.getElemento(0, 1), 0.0);
        assertEquals(7.0, matriz.getElemento(1, 0), 0.0);
        assertEquals(8.0, matriz.getElemento(1, 1), 0.0);
        assertEquals(0.0, matriz.getElemento(2, 0), 0.0);
        assertEquals(0.0, matriz.getElemento(2, 1), 0.0);
    }

    @Test
    public void constructorCopiaLosDatosDeEntrada() {
        double[][] valores = {
                { 1.0, 2.0 },
                { 3.0 }
        };
        Matriz matriz = new Matriz(valores);

        valores[0][0] = 99.0;
        valores[1][0] = 88.0;

        assertEquals(1.0, matriz.getElemento(0, 0), 0.0);
        assertEquals(3.0, matriz.getElemento(1, 0), 0.0);
    }

    @Test
    public void sumarInstanciaSumaMatricesDeIgualDimension() {
        Matriz matriz = new Matriz(new double[][] {
                { 1.0, 2.0 },
                { -3.0, 4.5 }
        });
        Matriz resultado = matriz.sumar(new Matriz(new double[][] {
                { 5.0, -2.0 },
                { 3.0, 0.5 }
        }));

        assertArrayEquals(new int[] { 2, 2 }, resultado.getDimension());
        assertEquals(6.0, resultado.getElemento(0, 0), 0.0);
        assertEquals(0.0, resultado.getElemento(0, 1), 0.0);
        assertEquals(0.0, resultado.getElemento(1, 0), 0.0);
        assertEquals(5.0, resultado.getElemento(1, 1), 0.0);
    }

    @Test
    public void sumarInstanciaDevuelveNullParaArgumentoNulo() {
        Matriz matriz = new Matriz(new double[][] { { 1.0 } });

        assertNull(matriz.sumar(null));
    }

    @Test
    public void sumarInstanciaDevuelveNullParaDimensionesDistintas() {
        Matriz matriz = new Matriz(new double[][] { { 1.0, 2.0 } });

        assertNull(matriz.sumar(new Matriz(new double[][] { { 1.0 }, { 2.0 } })));
    }

    @Test
    public void sumarEstaticoSumaMatricesDeIgualDimension() {
        Matriz resultado = Matriz.sumar(
                new Matriz(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } }),
                new Matriz(new double[][] { { 5.0, 6.0 }, { 7.0, 8.0 } }));

        assertArrayEquals(new int[] { 2, 2 }, resultado.getDimension());
        assertEquals(6.0, resultado.getElemento(0, 0), 0.0);
        assertEquals(8.0, resultado.getElemento(0, 1), 0.0);
        assertEquals(10.0, resultado.getElemento(1, 0), 0.0);
        assertEquals(12.0, resultado.getElemento(1, 1), 0.0);
    }

    @Test
    public void sumarEstaticoDevuelveNullSiUnaMatrizEsNula() {
        Matriz matriz = new Matriz(new double[][] { { 1.0 } });

        assertNull(Matriz.sumar(null, matriz));
        assertNull(Matriz.sumar(matriz, null));
    }

    @Test
    public void sumarEstaticoDevuelveNullParaDimensionesDistintas() {
        Matriz primera = new Matriz(new double[][] { { 1.0, 2.0 } });
        Matriz segunda = new Matriz(new double[][] { { 1.0 }, { 2.0 } });

        assertNull(Matriz.sumar(primera, segunda));
    }

    private void assertIllegalArgumentException(double[][] valores) {
        try {
            new Matriz(valores);
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // El constructor debe rechazar esta entrada.
        }
    }
}
