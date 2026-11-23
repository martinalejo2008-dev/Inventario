package interfaz;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import modelo.Proveedor;
import negocio.DatoInvalidoException;
import negocio.GestorInventario;
import negocio.ValidadorProducto;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GestorInventario gestor = new GestorInventario(100);

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        int opcion = -1;
        System.out.println("=== SISTEMA DE INVENTARIO - SUPER PAQUITA ===");

        while (opcion != 0) {
            mostrarMenu();
            System.out.print("Opcion: ");
            String entrada = scanner.nextLine();

            try {
                opcion = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Ingrese un numero de opcion valido.");
                continue;
            }

            try {
                switch (opcion) {
                    case 1:
                        registrarProducto();
                        break;
                    case 2:
                        listarProductos();
                        break;
                    case 3:
                        buscarProducto();
                        break;
                    case 4:
                        actualizarCantidad();
                        break;
                    case 5:
                        eliminarProducto();
                        break;
                    case 6:
                        mostrarAlertasVencimiento();
                        break;
                    case 7:
                        mostrarAlertasStockBajo();
                        break;
                    case 8:
                        mostrarValorTotal();
                        break;
                    case 9:
                        int cargados = gestor.cargarDatosEjemplo();
                        System.out.println("OK: datos de ejemplo cargados (" + cargados + " productos).");
                        break;
                    case 0:
                        System.out.println("Hasta pronto.");
                        break;
                    default:
                        System.out.println("ERROR: Opcion no reconocida.");
                }
            } catch (DatoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Registrar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto por codigo");
        System.out.println("4. Actualizar cantidad");
        System.out.println("5. Eliminar producto");
        System.out.println("6. Alertas de vencimiento");
        System.out.println("7. Alertas de stock bajo");
        System.out.println("8. Valor total del inventario");
        System.out.println("9. Cargar datos de ejemplo");
        System.out.println("0. Salir");
    }

    private static void registrarProducto() throws DatoInvalidoException {
        System.out.println("\n--- Registrar producto ---");
        System.out.print("Tipo (1 = Perecedero, 2 = No perecedero): ");
        int tipo = ValidadorProducto.parsearEnteroObligatorio(scanner.nextLine(), "tipo");
        if (tipo != 1 && tipo != 2) {
            throw new DatoInvalidoException("ERROR: El tipo debe ser 1 o 2.");
        }

        System.out.print("Codigo: ");
        String codigo = scanner.nextLine();
        ValidadorProducto.validarTextoObligatorio(codigo, "codigo");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        ValidadorProducto.validarTextoObligatorio(nombre, "nombre");

        System.out.print("Cantidad: ");
        int cantidad = ValidadorProducto.parsearEnteroObligatorio(scanner.nextLine(), "cantidad");
        ValidadorProducto.validarNoNegativo(cantidad, "cantidad");

        System.out.print("Stock minimo: ");
        int stockMinimo = ValidadorProducto.parsearEnteroObligatorio(scanner.nextLine(), "stock minimo");
        ValidadorProducto.validarNoNegativo(stockMinimo, "stock minimo");

        System.out.print("Precio de compra: ");
        double precioCompra = ValidadorProducto.parsearDecimalObligatorio(scanner.nextLine(), "precio de compra");
        ValidadorProducto.validarPositivo(precioCompra, "precio de compra");

        System.out.print("Precio de venta: ");
        double precioVenta = ValidadorProducto.parsearDecimalObligatorio(scanner.nextLine(), "precio de venta");
        ValidadorProducto.validarPositivo(precioVenta, "precio de venta");

        System.out.print("Proveedor (Enter para omitir): ");
        String nombreProv = scanner.nextLine().trim();
        Proveedor proveedor = nombreProv.isEmpty() ? null : new Proveedor("PR-GEN", nombreProv, "N/A");

        LocalDate fechaIngreso = LocalDate.now();
        Producto nuevoProducto;

        if (tipo == 1) {
            System.out.print("Fecha de vencimiento (AAAA-MM-DD): ");
            LocalDate fechaVenc = ValidadorProducto.parsearFechaObligatoria(scanner.nextLine(), "fecha de vencimiento");
            ValidadorProducto.validarFechas(fechaIngreso, fechaVenc);

            System.out.print("Descuento por vencer (%): ");
            double descuento = ValidadorProducto.parsearDecimalObligatorio(scanner.nextLine(), "descuento por vencer");

            nuevoProducto = new ProductoPerecedero(codigo, nombre, cantidad, stockMinimo,
                    precioCompra, precioVenta, fechaIngreso, proveedor, fechaVenc, descuento);
        } else {
            System.out.print("Categoria: ");
            String categoria = scanner.nextLine();
            ValidadorProducto.validarTextoObligatorio(categoria, "categoria");

            System.out.print("Garantia en meses: ");
            int garantia = ValidadorProducto.parsearEnteroObligatorio(scanner.nextLine(), "garantia en meses");

            nuevoProducto = new ProductoNoPerecedero(codigo, nombre, cantidad, stockMinimo,
                    precioCompra, precioVenta, fechaIngreso, proveedor, garantia, categoria);
        }

        gestor.registrar(nuevoProducto);
        System.out.println("OK: producto registrado correctamente.");
    }

    private static void listarProductos() {
        System.out.println("\n--- Listado de productos ---");
        List<Producto> lista = gestor.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }
        for (Producto p : lista) {
            System.out.println(p.mostrarInfo());
        }
    }

    private static void buscarProducto() throws DatoInvalidoException {
        System.out.println("\n--- Buscar producto ---");
        System.out.print("Codigo: ");
        String codigo = scanner.nextLine();
        Producto p = gestor.buscarPorCodigo(codigo);
        System.out.println("OK: " + p.mostrarInfo());
    }

    private static void actualizarCantidad() throws DatoInvalidoException {
        System.out.println("\n--- Actualizar cantidad ---");
        System.out.print("Codigo: ");
        String codigo = scanner.nextLine();
        System.out.print("Nueva cantidad: ");
        int nuevaCantidad = ValidadorProducto.parsearEnteroObligatorio(scanner.nextLine(), "cantidad");
        gestor.actualizarCantidad(codigo, nuevaCantidad);
        System.out.println("OK: cantidad actualizada correctamente.");
    }

    private static void eliminarProducto() throws DatoInvalidoException {
        System.out.println("\n--- Eliminar producto ---");
        System.out.print("Codigo: ");
        String codigo = scanner.nextLine();
        gestor.eliminar(codigo);
        System.out.println("OK: producto eliminado correctamente.");
    }

    private static void mostrarAlertasVencimiento() throws DatoInvalidoException {
        System.out.println("\n--- Alertas de vencimiento ---");
        System.out.print("Umbral en dias: ");
        int umbral = ValidadorProducto.parsearEnteroObligatorio(scanner.nextLine(), "umbral en dias");
        List<ProductoPerecedero> alertas = gestor.alertasPorVencer(umbral);
        if (alertas.isEmpty()) {
            System.out.println("No hay productos proximos a vencer en ese rango.");
            return;
        }
        for (ProductoPerecedero pp : alertas) {
            System.out.printf("ALERTA: %s (%s) | Dias restantes: %d | Precio final: $%.2f%n",
                    pp.getNombre(), pp.getCodigo(), pp.diasParaVencer(), pp.calcularPrecioFinal());
        }
    }

    private static void mostrarAlertasStockBajo() {
        System.out.println("\n--- Alertas de stock bajo ---");
        List<Producto> alertas = gestor.alertasStockBajo();
        if (alertas.isEmpty()) {
            System.out.println("No hay productos con stock bajo.");
            return;
        }
        for (Producto p : alertas) {
            System.out.printf("REPONER: %s (%s) | Cantidad: %d | Stock minimo: %d%n",
                    p.getNombre(), p.getCodigo(), p.getCantidad(), p.getStockMinimo());
        }
    }

    private static void mostrarValorTotal() {
        System.out.printf("\n--- Valor total del inventario: $%.2f ---%n", gestor.valorTotalInventario());
    }
}