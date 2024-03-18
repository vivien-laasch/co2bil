# Energy Balance Calculator API

This repository contains a Java/Spring Boot application for calculating energy balance and retrieving energy source master data. It provides the following endpoints:

- `/api/v1/energy/balance`: Calculates the energy balance based on the provided energy consumption data. *(POST)*
- `/api/v1/energy/sources`: Retrieves the master data of energy sources. *(GET)*

## Prerequisites and Execution

- Java 17 or higher is required to run the application.
- The project is not prebuilt.
- Execute the application by running `./gradlew bootRun`.
- Gradle wrapper is included, so pre-installation of Gradle is not required.

## Request Body Format

For the `/api/v1/energy/balance` endpoint, the client should send a POST request with a JSON body containing a list of energy entries. Each entry should include the following fields:

| Field                     | Data Type | Validation                                         |
|---------------------------|-----------|----------------------------------------------------|
| Description               | Text      | - Required                                         |
| Energy Source ID          | Text      | - Required                                         |
| Consumption               | Decimal   | - Up to 5 decimal places<br>- Required             |
| Individual Emission Factor| Decimal   | - Up to 5 decimal places (optional)                |

Example request body:
```json
{
  "entries": [
    {
      "id": "2001",
      "description": "Standort Berlin",
      "consumption": 2050.81863
    },
    {
      "id": "2001",
      "description": "Standort München",
      "consumption": 1078.3699
    },
    {
      "id": "2006",
      "description": "B APL 001",
      "consumption": 10050
    },
    {
      "id": "1000",
      "description": "Standort Ketsch",
      "consumption": 3770
    },
    {
      "id": "1004",
      "description": "Standort Sandhausen",
      "consumption": 3000,
      "emissionFactor": 0.04
    }
  ]
}
```

## CO2 Balance Calculation

Based on the energy data provided, the CO2 balance is calculated and returned to the client as a response.

The CO2 balance calculation is performed using another system's endpoint, which provides an overview of all possible energy sources, including name, conversion factor, and emission factor per energy source. Mapping is done based on the energy source ID from the request.

Example response format:
```json
[
  {
    "label": "Heizöl leicht (Standort Berlin)",
    "energy": 22671.8,
    "co2": 6.44
  },
  {
    "label": "Heizöl leicht (Standort München)",
    "energy": 11921.38,
    "co2": 3.39
  },
  {
    "label": "Benzin (B APL 001)",
    "energy": 90098.25,
    "co2": 20.94
  },
  {
    "label": "Strommix (Deutschland) (Standort Ketsch)",
    "energy": 3770.0,
    "co2": 1.67
  },
  {
    "label": "Andere Elektrizität (erneuerbar, eingekauft) (Standort Sandhausen)",
    "energy": 3000.0,
    "co2": 0.12
  }
]
```

## Configuration

The external API URL for energy source master data is configurable in the `application.properties` file. By default, it is configured as follows.
```properties
external.api.energySourcesApiUrl=https://applied-coding-challenge.s3.eu-central-1.amazonaws.com/backend/energy-sources.json
```
