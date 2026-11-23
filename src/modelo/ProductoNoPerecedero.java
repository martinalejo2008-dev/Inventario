package modelo;

import java.time.LocalDate;
import negocio.DatoInvalidoException;
import negocio.ValidadorProducto;

public class ProductoNoPerecedero extends Producto {
    private int garantiaMeses;
    private String categoria;

    public ProductoNoPerecedero(String codigo, String nombre, int cantidad, int stockMinimo,
                                double precioCompra, double precioVenta, LocalDate fechaIngreso,
                                Proveedor proveedor, int garantiaMeses,
                                String categoria) throws DatoInvalidoException {
        super(codigo, nombre, cantidad, stockMinimo, precioCompra, precioVenta, fechaIngreso, proveedor);
        setGarantiaMeses(garantiaMeses);
        setCategoria(categoria);
    }

    public int getGarantiaMeses() { return garantiaMeses; }
    public void setGarantiaMeses(int garantiaMeses) throws DatoInvalidoException {
        ValidadorProducto.validarNoNegativo(garantiaMeses, "garantia en meses");
        this.garantiaMeses = garantiaMeses;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) throws DatoInvalidoException {
        ValidadorProducto.validarTextoObligatorio(categoria, "categoria");
        this.categoria = categoria.trim();
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecioVenta();
    }

    @Override
    public String mostrarInfo() {
        return super.mostrarInfo() + String.format(" | Tipo: No Perecedero | Cat: %s | Garantia: %d meses",
                categoria, garantiaMeses);
    }
}