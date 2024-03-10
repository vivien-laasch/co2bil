package de.vlaasch.co2bil.exceptions;

public class EnergySourceNotFoundException extends Exception {
    
    public EnergySourceNotFoundException() {
        super();
    }

    public EnergySourceNotFoundException(String message) {
        super(message);
    }
}
