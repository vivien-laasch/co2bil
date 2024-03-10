package de.vlaasch.co2bil.exceptions;

public class InvalidEnergyUsageException extends Exception {

    public InvalidEnergyUsageException() {
        super();
    }

    public InvalidEnergyUsageException(String message) {
        super(message);
    }
}
