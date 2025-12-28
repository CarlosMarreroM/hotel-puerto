package org.docencia.hotel.validation;

import java.util.Objects;

/**
 * Clase utilitaria para validaciones de argumentos.
 */
public final class Guard {
    private Guard() {}

    /**
     * Verifica que una cadena no sea nula ni este en blanco.
     * 
     * @param value     Cadena a verificar
     * @param fieldName Nombre del campo para el mensaje de error
     * @return La cadena si es valida
     * @throws NullPointerException     Si la cadena es nula
     * @throws IllegalArgumentException Si la cadena esta en blanco
     */
    public static String requireNonBlank(String value, String fieldName) {
        if (value == null) {
            throw new NullPointerException(fieldName + " must not be null");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    /**
     * Verifica que un objeto no sea nulo.
     * 
     * @param <T>       Tipo del objeto
     * @param value     Objeto a verificar
     * @param fieldName Nombre del campo para el mensaje de error
     * @return El objeto si no es nulo
     * @throws NullPointerException Si el objeto es nulo
     */
    public static <T> T requireNonNull(T value, String fieldName) {
        return Objects.requireNonNull(value, fieldName + " must not be null");
    }
}

