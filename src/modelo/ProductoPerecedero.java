package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import negocio.DatoInvalidoException;
import negocio.ValidadorProducto;

public class ProductoPerecedero extends Producto {
    private LocalDate fechaVencimiento;
    private double descuentoPorVencer;

    public ProductoPerecedero(String codigo, String nombre, int cantidad, int stockMinimo,
                              double precioCompra, double precioVenta, LocalDate fechaIngreso,
                              Proveedor proveedor, LocalDate fechaVencimiento,
                              double descuentoPorVencer) throws DatoInvalidoException {
        super(codigo, nombre, cantidad, stockMinimo, precioCompra, precioVenta, fechaIngreso, proveedor);
        setFechaVencimiento(fechaVencimiento);
        setDescuentoPorVencer(descuentoPorVencer);
    }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) throws DatoInvalidoException {
        ValidadorProducto.validarFechas(getFechaIngreso(), fechaVencimiento);
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getDescuentoPorVencer() { return descuentoPorVencer; }
    public void setDescuentoPorVencer(double descuentoPorVencer) throws DatoInvalidoException {
        if (descuentoPorVencer < 0 || descuentoPorVencer > 100) {
            throw new DatoInvalidoException("ERROR: El descuento debe estar entre 0 y 100.");
        }
        this.descuentoPorVencer = descuentoPorVencer;
    }

    public long diasParaVencer() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    public boolean estaVencido() {
        return LocalDate.now().isAfter(fechaVencimiento);
    }

    @Override
    public double calcularPrecioFinal() {
        if (diasParaVencer() <= 3 && !estaVencido()) {
            double factor = (descuentoPorVencer > 1) ? (descuentoPorVencer / 100.0) : descuentoPorVencer;
            return getPrecioVenta() * (1.0 - factor);
        }
        return getPrecioVenta();
    }

    @Override
    public String mostrarInfo() {
        return super.mostrarInfo() + String.format(" | Tipo: Perecedero | Vence: %s (%d dias)",
                fechaVencimiento, diasParaVencer());
    }
}