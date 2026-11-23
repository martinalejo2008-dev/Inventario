package negocio;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ValidadorProducto {

    public static void validarTextoObligatorio(String valor, String nombreCampo) throws DatoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException("ERROR: El campo '" + nombreCampo + "' es obligatorio.");
        }
    }

    public static int parsearEnteroObligatorio(String entrada, String nombreCampo) throws DatoInvalidoException {
        validarTextoObligatorio(entrada, nombreCampo);
        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("ERROR: El campo '" + nombreCampo + "' debe ser un numero entero valido.");
        }
    }

    public static double parsearDecimalObligatorio(String entrada, String nombreCampo) throws DatoInvalidoException {
        validarTextoObligatorio(entrada, nombreCampo);
        try {
            return Double.parseDouble(entrada.trim());
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("ERROR: El campo '" + nombreCampo + "' debe ser un numero valido.");
        }
    }

    public static LocalDate parsearFechaObligatoria(String entrada, String nombreCampo) throws DatoInvalidoException {
        validarTextoObligatorio(entrada, nombreCampo);
        try {
            return LocalDate.parse(entrada.trim());
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException("ERROR: El campo '" + nombreCampo + "' debe tener formato AAAA-MM-DD.");
        }
    }

    public static void validarNoNegativo(int valor, String nombreCampo) throws DatoInvalidoException {
        if (valor < 0) {
            throw new DatoInvalidoException("ERROR: El campo '" + nombreCampo + "' no puede ser negativo.");
        }
    }

    public static void validarPositivo(double valor, String nombreCampo) throws DatoInvalidoException {
        if (valor <= 0) {
            throw new DatoInvalidoException("ERROR: El campo '" + nombreCampo + "' debe ser mayor que cero.");
        }
    }

    public static void validarFechas(LocalDate fechaIngreso, LocalDate fechaVencimiento) throws DatoInvalidoException {
        if (fechaVencimiento == null) {
            throw new DatoInvalidoException("ERROR: El campo 'fecha de vencimiento' es obligatorio.");
        }
        if (fechaIngreso != null && !fechaVencimiento.isAfter(fechaIngreso)) {
            throw new DatoInvalidoException("ERROR: La fecha de vencimiento debe ser posterior a la fecha de ingreso.");
        }
    }
}