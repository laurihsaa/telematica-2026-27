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

## Ramas de la práctica

Los ejercicios se desarrollaron en ramas y se fusionaron en `main`:

- `ejercicio-2-estrategia`: políticas de precio y sus pruebas.
- `ejercicio-3-factoria-json`: factorías de clientes, vehículos y alquileres.
- `ejercicio-4-factoria-politicas`: factoría de políticas y pruebas de selección.
- `ejercicio-5-6-gestor-json`: operaciones del gestor, persistencia y pruebas.

Para comprobar la entrega, ejecuta `mvn test` desde esta carpeta.

## Mejoras verificadas

Las ramas `mejora-estrategias-entradas` y `mejora-integridad-gestor` añaden
validación de precios, protección frente a referencias ajenas y rechazo de
ficheros con alquileres activos duplicados. Las pruebas también verifican
que una recuperación inválida no modifica los datos existentes.
