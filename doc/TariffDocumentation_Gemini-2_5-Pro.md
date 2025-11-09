# Feature: Tariff Management

### Feature Summary

This feature allows an Administrator to create and manage electricity tariffs within the system. The primary goal is to enable the configuration of new tariffs, which can then be stored and made available to users. Tariffs can be of two types: `FLAT_RATE` (a single price per kWh) or `TIME_OF_USE` (a different price for each hour of the day).

### API Endpoints

---

#### Create a New Tariff

Creates a new electricity tariff. The structure of the request body depends on the `tariffType`. For `FLAT_RATE`, `unitRateKwh` is required. For `TIME_OF_USE`, a map of 24 `hourlyRates` is required.

*   **Endpoint:** `POST /admin/tariffs`

*   **cURL Example:**
    ```bash
    curl -X POST http://localhost:8080/admin/tariffs \
    -H "Content-Type: application/json" \
    -d '{
      "tariffName": "Green Energy Saver",
      "supplierName": "EcoPower Inc.",
      "tariffType": "FLAT_RATE",
      "unitRateKwh": 0.15,
      "standingChargePerDay": 0.25,
      "validFrom": "2025-01-01",
      "hourlyRates": null
    }'
    ```

*   **Example JSON Request Body (`FLAT_RATE`):**
    ```json
    {
      "tariffName": "Green Energy Saver",
      "supplierName": "EcoPower Inc.",
      "tariffType": "FLAT_RATE",
      "unitRateKwh": 0.15,
      "standingChargePerDay": 0.25,
      "validFrom": "2025-01-01",
      "hourlyRates": null
    }
    ```

*   **Example JSON Success Response:**
    ```json
    {
      "tariffName": "Green Energy Saver",
      "supplierName": "EcoPower Inc.",
      "tariffType": "FLAT_RATE",
      "unitRateKwh": 0.15,
      "standingChargePerDay": 0.25,
      "validFrom": "2025-01-01",
      "hourlyRates": null
    }
    ```

---

#### Retrieve All Tariffs

Retrieves a list of all currently configured tariffs in the system.

*   **Endpoint:** `GET /admin/tariffs`

*   **cURL Example:**
    ```bash
    curl -X GET http://localhost:8080/admin/tariffs
    ```

*   **Example JSON Success Response:**
    ```json
    [
      {
        "tariffName": "Green Energy Saver",
        "supplierName": "EcoPower Inc.",
        "tariffType": "FLAT_RATE",
        "unitRateKwh": 0.15,
        "standingChargePerDay": 0.25,
        "validFrom": "2025-01-01",
        "hourlyRates": null
      },
      {
        "tariffName": "Night Owl Saver",
        "supplierName": "PowerGrid Corp",
        "tariffType": "TIME_OF_USE",
        "unitRateKwh": null,
        "standingChargePerDay": 0.30,
        "validFrom": "2025-02-01",
        "hourlyRates": {
          "0": 0.08, "1": 0.08, "2": 0.08, "3": 0.08, "4": 0.08, "5": 0.08,
          "6": 0.12, "7": 0.12, "8": 0.18, "9": 0.18, "10": 0.18, "11": 0.18,
          "12": 0.20, "13": 0.20, "14": 0.20, "15": 0.20, "16": 0.22, "17": 0.22,
          "18": 0.22, "19": 0.22, "20": 0.15, "21": 0.15, "22": 0.10, "23": 0.10
        }
      }
    ]
    ```