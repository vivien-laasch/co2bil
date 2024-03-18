package de.vlaasch.co2bil.exceptions;

public class ExternalEnergySourcesNotFoundException extends Exception {
    public ExternalEnergySourcesNotFoundException() {
        super();
    }

    public ExternalEnergySourcesNotFoundException(String message) {
        super(message);
    }
}
