package com.exception;

/**
 * Bir Agent icin izin verilmeyen bir durum gecisi (ornegin OFFLINE -> BUSY)
 * denendiginde firlatilir. State Pattern'in gecis kurallarinin ihlalini temsil eder.
 */
public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}
