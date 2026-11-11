# Práctica 2: alquiler de vehículos

Proyecto Maven con los seis ejercicios del enunciado: pruebas unitarias, estrategias
de precios, factorías de clientes/vehículos/alquileres y políticas, persistencia JSON
del gestor y operaciones de alquiler y devolución.

## Compilar y probar

Con Maven instalado:

```text
mvn test
```

La aplicación de consola se inicia ejecutando `pit2027.GestorAlquileres`.
El fichero JSON de persistencia contiene las colecciones de clientes, vehículos y
alquileres; al recuperarlo se vuelven a enlazar los alquileres con las instancias
registradas del cliente y del vehículo.

Las tarifas base aplican a las tres subclases los valores por categoría que aparecen
en el ejemplo del enunciado para Turismo: 50 (Normal), 80 (Especial) y 100 (Superior).
Las estrategias de precio pueden probarse por separado con `pit2027.FactoriaPoliticas`.
