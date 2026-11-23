package negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import modelo.Proveedor;

public class GestorInventario {
    private List<Producto> productos;
    private int capacidadMaxima;

    public GestorInventario(int capacidadMaxima) {
        this.productos = new ArrayList<>();
        this.capacidadMaxima = capacidadMaxima;
    }

    public void registrar(Producto producto) throws DatoInvalidoException {
        if (productos.size() >= capacidadMaxima) {
            throw new DatoInvalidoException("ERROR: La bodega ha alcanzado su capacidad maxima (" + capacidadMaxima + " productos).");
        }
        if (buscarPorCodigoInterno(producto.getCodigo()) != null) {
            throw new DatoInvalidoException("ERROR: El codigo '" + producto.getCodigo() + "' ya existe en el inventario.");
        }
        productos.add(producto);
    }

    private Producto buscarPorCodigoInterno(String codigo) {
        if (codigo == null) return null;
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarPorCodigo(String codigo) throws DatoInvalidoException {
        ValidadorProducto.validarTextoObligatorio(codigo, "codigo");
        Producto encontrado = buscarPorCodigoInterno(codigo);
        if (encontrado == null) {
            throw new DatoInvalidoException("ERROR: Producto no encontrado.");
        }
        return encontrado;
    }

    public void actualizarCantidad(String codigo, int nuevaCantidad) throws DatoInvalidoException {
        Producto p = buscarPorCodigo(codigo);
        p.setCantidad(nuevaCantidad);
    }

    public void eliminar(String codigo) throws DatoInvalidoException {
        Producto p = buscarPorCodigo(codigo);
        productos.remove(p);
    }

    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }

    public List<Producto> alertasStockBajo() {
        List<Producto> alertas = new ArrayList<>();
        for (Producto p : productos) {
            if (p.necesitaReposicion()) {
                alertas.add(p);
            }
        }
        return alertas;
    }

    public List<ProductoPerecedero> alertasPorVencer(int umbralDias) throws DatoInvalidoException {
        ValidadorProducto.validarNoNegativo(umbralDias, "umbral en dias");
        List<ProductoPerecedero> alertas = new ArrayList<>();
        for (Producto p : productos) {
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero pp = (ProductoPerecedero) p;
                if (pp.diasParaVencer() <= umbralDias) {
                    alertas.add(pp);
                }
            }
        }
        return alertas;
    }

    public double valorTotalInventario() {
        double total = 0.0;
        for (Producto p : productos) {
            total += p.calcularValorInventario();
        }
        return total;
    }

    public int cargarDatosEjemplo() throws DatoInvalidoException {
        productos.clear();
        LocalDate hoy = LocalDate.now();
        Proveedor provLacteos = new Proveedor("PR1", "Lacteos", "0991234567");
        Proveedor provFerreteria = new Proveedor("PR2", "Ferreteria", "0987654321");

        registrar(new ProductoPerecedero("P001", "Yogurt", 10, 2, 0.50, 1.00,
                hoy.minusDays(2), provLacteos, hoy.plusDays(3), 30.0));

        registrar(new ProductoPerecedero("P002", "Leche", 12, 3, 0.70, 1.20,
                hoy.minusDays(1), provLacteos, hoy.plusDays(10), 20.0));

        registrar(new ProductoNoPerecedero("P003", "Martillo", 2, 5, 5.00, 8.50,
                hoy.minusDays(5), provFerreteria, 12, "Ferreteria"));

        registrar(new ProductoNoPerecedero("P004", "Cuaderno", 20, 5, 1.00, 2.00,
                hoy.minusDays(5), provFerreteria, 0, "Papeleria"));

        return productos.size();
    }
}