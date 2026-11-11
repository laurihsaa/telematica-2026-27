package pit2027;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class GestorAlquileres {
    private final List<Alquiler> alquileres = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Vehiculo> vehiculos = new ArrayList<>();

    public GestorAlquileres() {
        inicializarClientes();
        inicializarVehiculos();
    }

    private void inicializarClientes() {
        clientes.add(new Cliente("Clara Videncia", "001"));
        clientes.add(new Cliente("Aitor Menta", "002"));
        clientes.add(new Cliente("Lola Mento", "003"));
        clientes.add(new Cliente("Armando Guerra", "004"));
        clientes.add(new Cliente("Ana Lítica", "005"));
        clientes.add(new Cliente("Carmelo Cotón", "006"));
    }

    private void inicializarVehiculos() {
        vehiculos.add(new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0));
        vehiculos.add(new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200));
        vehiculos.add(new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2));
        vehiculos.add(new Turismo("TSA2024", Categoria.SUPERIOR, 2024, 5, 550.0));
        vehiculos.add(new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500));
        vehiculos.add(new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3));
        vehiculos.add(new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.0));
        vehiculos.add(new Motocicleta("MEA2026", Categoria.ESPECIAL, 2026, 1000));
        vehiculos.add(new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5));
    }

    public Vehiculo[] obtenerVehiculos(String tipo) {
        Objects.requireNonNull(tipo, "El tipo de vehículo no puede ser null");
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(vehiculo);
            }
        }
        return resultado.toArray(new Vehiculo[0]);
    }

    public Vehiculo[] obtenerVehiculos(String tipo, Categoria categoria) {
        Objects.requireNonNull(tipo, "El tipo de vehículo no puede ser null");
        Objects.requireNonNull(categoria, "La categoría no puede ser null");
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getTipo().equalsIgnoreCase(tipo)
                    && vehiculo.getCategoria() == categoria) {
                resultado.add(vehiculo);
            }
        }
        return resultado.toArray(new Vehiculo[0]);
    }

    public void agregarAlquiler(Alquiler alquiler) {
        Objects.requireNonNull(alquiler, "El alquiler no puede ser null");
        if (!clientes.contains(alquiler.getCliente())) {
            throw new IllegalArgumentException("El cliente no pertenece a este gestor");
        }
        if (!vehiculos.contains(alquiler.getVehiculo())) {
            throw new IllegalArgumentException("El vehículo no pertenece a este gestor");
        }
        if (!alquiler.getVehiculo().isDisponible()) {
            throw new IllegalStateException("El vehículo no está disponible");
        }
        alquileres.add(alquiler);
        alquiler.getVehiculo().setDisponible(false);
    }

    public Alquiler[] obtenerAlquileres() {
        return alquileres.toArray(new Alquiler[0]);
    }

    public Alquiler[] obtenerAlquileres(Cliente cliente) {
        Objects.requireNonNull(cliente, "El cliente no puede ser null");
        List<Alquiler> resultado = new ArrayList<>();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente().getDni().equals(cliente.getDni())) {
                resultado.add(alquiler);
            }
        }
        return resultado.toArray(new Alquiler[0]);
    }

    public boolean esClienteFrecuente(Cliente cliente, int umbral) {
        Objects.requireNonNull(cliente, "El cliente no puede ser null");
        if (umbral < 0) {
            throw new IllegalArgumentException("El umbral no puede ser negativo");
        }
        return obtenerAlquileres(cliente).length >= umbral;
    }

    public void devolverAlquiler(Alquiler alquiler) {
        Objects.requireNonNull(alquiler, "El alquiler no puede ser null");
        if (!alquileres.contains(alquiler)) {
            throw new IllegalArgumentException("El alquiler no está registrado en este gestor");
        }
        alquiler.marcarDevuelto();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getMatricula().equals(alquiler.getVehiculo().getMatricula())) {
                vehiculo.setDisponible(true);
            }
        }
        alquiler.getVehiculo().setDisponible(true);
    }

    public Cliente[] obtenerClientes() {
        return clientes.toArray(new Cliente[0]);
    }

    public Vehiculo[] obtenerTodosLosVehiculos() {
        return vehiculos.toArray(new Vehiculo[0]);
    }

    public void guardarDatos(String nombreFichero) throws IOException {
        Path ruta = Paths.get(Objects.requireNonNull(nombreFichero, "La ruta no puede ser null"));
        JSONObject datos = new JSONObject()
                .put("clientes", serializarClientes())
                .put("vehiculos", serializarVehiculos())
                .put("alquileres", serializarAlquileres());
        Files.write(ruta, datos.toString(2).getBytes(StandardCharsets.UTF_8));
    }

    public void cargarDatos(String nombreFichero) throws IOException {
        Path ruta = Paths.get(Objects.requireNonNull(nombreFichero, "La ruta no puede ser null"));
        String contenido = new String(Files.readAllBytes(ruta), StandardCharsets.UTF_8);
        JSONObject datos = new JSONObject(contenido);

        List<Cliente> nuevosClientes = new ArrayList<>();
        Map<String, Cliente> clientesPorDni = new HashMap<>();
        JSONArray jsonClientes = datos.getJSONArray("clientes");
        for (int i = 0; i < jsonClientes.length(); i++) {
            Cliente cliente = FactoriaClientes.getCliente(jsonClientes.getJSONObject(i).toString());
            if (clientesPorDni.put(cliente.getDni(), cliente) != null) {
                throw new IllegalArgumentException("DNI duplicado en el fichero: " + cliente.getDni());
            }
            nuevosClientes.add(cliente);
        }

        List<Vehiculo> nuevosVehiculos = new ArrayList<>();
        Map<String, Vehiculo> vehiculosPorMatricula = new HashMap<>();
        JSONArray jsonVehiculos = datos.getJSONArray("vehiculos");
        for (int i = 0; i < jsonVehiculos.length(); i++) {
            Vehiculo vehiculo = FactoriaVehiculos.getVehiculo(jsonVehiculos.getJSONObject(i).toString());
            if (vehiculosPorMatricula.put(vehiculo.getMatricula(), vehiculo) != null) {
                throw new IllegalArgumentException(
                        "Matrícula duplicada en el fichero: " + vehiculo.getMatricula());
            }
            nuevosVehiculos.add(vehiculo);
        }

        List<Alquiler> nuevosAlquileres = new ArrayList<>();
        Set<String> vehiculosOcupados = new HashSet<>();
        JSONArray jsonAlquileres = datos.getJSONArray("alquileres");
        for (int i = 0; i < jsonAlquileres.length(); i++) {
            JSONObject jsonAlquiler = jsonAlquileres.getJSONObject(i);
            String dni = jsonAlquiler.getJSONObject("cliente").getString("dni");
            String matricula = jsonAlquiler.getJSONObject("vehiculo").getString("matricula");
            Cliente cliente = clientesPorDni.get(dni);
            Vehiculo vehiculo = vehiculosPorMatricula.get(matricula);
            if (cliente == null || vehiculo == null) {
                throw new IllegalArgumentException(
                        "El alquiler hace referencia a un cliente o vehículo inexistente");
            }
            Alquiler alquiler = FactoriaAlquileres.getAlquiler(jsonAlquiler, cliente, vehiculo);
            if (!alquiler.isDevuelto()) {
                if (!vehiculosOcupados.add(matricula)) {
                    throw new IllegalArgumentException(
                            "Hay más de un alquiler activo para el vehículo: " + matricula);
                }
                if (vehiculo.isDisponible()) {
                    throw new IllegalArgumentException(
                            "Un vehículo con un alquiler activo no puede figurar disponible: " + matricula);
                }
            }
            nuevosAlquileres.add(alquiler);
        }

        clientes.clear();
        clientes.addAll(nuevosClientes);
        vehiculos.clear();
        vehiculos.addAll(nuevosVehiculos);
        alquileres.clear();
        alquileres.addAll(nuevosAlquileres);
    }

    private JSONArray serializarClientes() {
        JSONArray resultado = new JSONArray();
        for (Cliente cliente : clientes) {
            resultado.put(cliente.toJSONObject());
        }
        return resultado;
    }

    private JSONArray serializarVehiculos() {
        JSONArray resultado = new JSONArray();
        for (Vehiculo vehiculo : vehiculos) {
            resultado.put(vehiculo.toJSONObject());
        }
        return resultado;
    }

    private JSONArray serializarAlquileres() {
        JSONArray resultado = new JSONArray();
        for (Alquiler alquiler : alquileres) {
            resultado.put(alquiler.toJSONObject());
        }
        return resultado;
    }

    public Cliente identificarCliente() {
        while (true) {
            System.out.println("Clientes:");
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                System.out.println(i + " -> " + cliente.getNombre() + " (" + cliente.getDni() + ")");
            }
            System.out.println("Número de cliente (-1 para volver):");
            int indice = KeyBoard.readInt();
            if (indice == -1) {
                return null;
            }
            if (indice >= 0 && indice < clientes.size()) {
                return clientes.get(indice);
            }
            System.out.println("Número de cliente inválido.");
        }
    }

    public Vehiculo seleccionarVehiculo(Cliente cliente) {
        Objects.requireNonNull(cliente, "El cliente no puede ser null");
        String tipo;
        while (true) {
            System.out.println("Tipo: 1) Turismo  2) Motocicleta  3) Vehículo industrial");
            int opcion = KeyBoard.readInt();
            if (opcion == 1) {
                tipo = "Turismo";
                break;
            } else if (opcion == 2) {
                tipo = "Motocicleta";
                break;
            } else if (opcion == 3) {
                tipo = "VehiculoIndustrial";
                break;
            }
            System.out.println("Tipo de vehículo inválido.");
        }

        while (true) {
            System.out.println("Categoría: 1) Normal  2) Especial  3) Superior  4) Cualquiera");
            int opcion = KeyBoard.readInt();
            Categoria categoria = null;
            if (opcion == 1) {
                categoria = Categoria.NORMAL;
            } else if (opcion == 2) {
                categoria = Categoria.ESPECIAL;
            } else if (opcion == 3) {
                categoria = Categoria.SUPERIOR;
            } else if (opcion != 4) {
                System.out.println("Categoría inválida.");
                continue;
            }

            List<Vehiculo> disponibles = new ArrayList<>();
            Vehiculo[] candidatos = categoria == null
                    ? obtenerVehiculos(tipo)
                    : obtenerVehiculos(tipo, categoria);
            for (Vehiculo vehiculo : candidatos) {
                if (vehiculo.isDisponible()) {
                    disponibles.add(vehiculo);
                }
            }
            if (disponibles.isEmpty()) {
                System.out.println("No hay vehículos disponibles con esos criterios.");
                return null;
            }

            for (int i = 0; i < disponibles.size(); i++) {
                System.out.println(i + " -> " + disponibles.get(i));
            }
            System.out.println("Seleccione un vehículo:");
            int indice = KeyBoard.readInt();
            if (indice >= 0 && indice < disponibles.size()) {
                return disponibles.get(indice);
            }
            System.out.println("Selección inválida.");
        }
    }

    public void menuPrincipal() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\nGestión de alquileres");
            System.out.println("1) Alquilar  2) Devolver  3) Historial");
            System.out.println("4) Guardar  5) Recuperar  0) Salir");
            int opcion = KeyBoard.readInt();
            switch (opcion) {
                case 1:
                    gestionarNuevoAlquiler();
                    break;
                case 2:
                    gestionarDevolucion();
                    break;
                case 3:
                    Cliente cliente = identificarCliente();
                    if (cliente != null) {
                        for (Alquiler alquiler : obtenerAlquileres(cliente)) {
                            System.out.println(alquiler);
                        }
                    }
                    break;
                case 4:
                    guardarDesdeConsola();
                    break;
                case 5:
                    cargarDesdeConsola();
                    break;
                case 0:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void gestionarNuevoAlquiler() {
        Cliente cliente = identificarCliente();
        if (cliente == null) {
            return;
        }
        Vehiculo vehiculo = seleccionarVehiculo(cliente);
        if (vehiculo == null) {
            return;
        }
        System.out.println("Número de días del alquiler:");
        int dias = KeyBoard.readInt();
        if (dias <= 0) {
            System.out.println("La duración debe ser positiva.");
            return;
        }

        PoliticasDePrecios politica = esClienteFrecuente(cliente, 3)
                ? PoliticasDePrecios.ClienteFrecuente
                : PoliticasDePrecios.Normal;
        LocalDate inicio = LocalDate.now();
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(dias - 1), politica);
        agregarAlquiler(alquiler);
        System.out.println("Alquiler realizado con tarifa " + politica
                + ". Precio: " + alquiler.calcularPrecioAlquiler());
    }

    private void gestionarDevolucion() {
        Cliente cliente = identificarCliente();
        if (cliente == null) {
            return;
        }
        List<Alquiler> activos = new ArrayList<>();
        for (Alquiler alquiler : obtenerAlquileres(cliente)) {
            if (!alquiler.isDevuelto()) {
                activos.add(alquiler);
            }
        }
        if (activos.isEmpty()) {
            System.out.println("No hay alquileres pendientes de devolución.");
            return;
        }
        for (int i = 0; i < activos.size(); i++) {
            System.out.println(i + " -> " + activos.get(i));
        }
        System.out.println("Seleccione el alquiler que desea devolver:");
        int indice = KeyBoard.readInt();
        if (indice < 0 || indice >= activos.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        devolverAlquiler(activos.get(indice));
        System.out.println("Vehículo devuelto.");
    }

    private void guardarDesdeConsola() {
        System.out.println("Fichero de destino:");
        try {
            guardarDatos(KeyBoard.readString());
            System.out.println("Datos guardados.");
        } catch (IOException exception) {
            System.err.println("No se pudieron guardar los datos: " + exception.getMessage());
        }
    }

    private void cargarDesdeConsola() {
        System.out.println("Fichero que desea recuperar:");
        try {
            cargarDatos(KeyBoard.readString());
            System.out.println("Datos recuperados.");
        } catch (IOException | RuntimeException exception) {
            System.err.println("No se pudieron recuperar los datos: " + exception.getMessage());
        }
    }

    public void listarAlquileres() {
        for (Alquiler alquiler : alquileres) {
            System.out.println(alquiler);
        }
    }

    public void listarVehiculos() {
        for (Vehiculo vehiculo : vehiculos) {
            System.out.println(vehiculo);
        }
    }

    public static void main(String[] args) {
        new GestorAlquileres().menuPrincipal();
    }
}
