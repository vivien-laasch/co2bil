package de.vlaasch.co2bil.exceptions;

public class NoEnergySourcesFoundException extends Exception {
    public NoEnergySourcesFoundException() {
        super();
    }

    public NoEnergySourcesFoundException(String message) {
        super(message);
    }
}
