package modelo;

import java.time.LocalDate;
import negocio.DatoInvalidoException;
import negocio.ValidadorProducto;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private int cantidad;
    private int stockMinimo;
    private double precioCompra;
    private double precioVenta;
    private LocalDate fechaIngreso;
    private Proveedor proveedor;

    public Producto(String codigo, String nombre, int cantidad, int stockMinimo,
                    double precioCompra, double precioVenta, LocalDate fechaIngreso,
                    Proveedor proveedor) throws DatoInvalidoException {
        setCodigo(codigo);
        setNombre(nombre);
        setCantidad(cantidad);
        setStockMinimo(stockMinimo);
        setPrecioCompra(precioCompra);
        setPrecioVenta(precioVenta);
        setFechaIngreso(fechaIngreso);
        setProveedor(proveedor);
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) throws DatoInvalidoException {
        ValidadorProducto.validarTextoObligatorio(codigo, "codigo");
        this.codigo = codigo.trim();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) throws DatoInvalidoException {
        ValidadorProducto.validarTextoObligatorio(nombre, "nombre");
        this.nombre = nombre.trim();
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) throws DatoInvalidoException {
        ValidadorProducto.validarNoNegativo(cantidad, "cantidad");
        this.cantidad = cantidad;
    }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) throws DatoInvalidoException {
        ValidadorProducto.validarNoNegativo(stockMinimo, "stock minimo");
        this.stockMinimo = stockMinimo;
    }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) throws DatoInvalidoException {
        ValidadorProducto.validarPositivo(precioCompra, "precio de compra");
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) throws DatoInvalidoException {
        ValidadorProducto.validarPositivo(precioVenta, "precio de venta");
        this.precioVenta = precioVenta;
    }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) throws DatoInvalidoException {
        if (fechaIngreso == null) {
            throw new DatoInvalidoException("ERROR: El campo 'fecha de ingreso' es obligatorio.");
        }
        this.fechaIngreso = fechaIngreso;
    }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public double calcularValorInventario() {
        return cantidad * precioCompra;
    }

    public boolean necesitaReposicion() {
        return cantidad <= stockMinimo;
    }

    public abstract double calcularPrecioFinal();

    public String mostrarInfo() {
        String prov = (proveedor != null) ? proveedor.toString() : "N/A";
        return String.format("[%s] %s | Cant: %d (Min: %d) | Compra: $%.2f | Venta: $%.2f | Final: $%.2f | Prov: %s",
                codigo, nombre, cantidad, stockMinimo, precioCompra, precioVenta, calcularPrecioFinal(), prov);
    }
}