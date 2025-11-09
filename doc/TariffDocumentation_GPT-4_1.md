# Tariff Feature Overview

## Feature Summary

The **Tariff** feature enables Administrators to create and configure new electricity tariffs within the system. This allows the business to store, advertise, and apply tariffs to users who may benefit from them. The feature supports both **Flat Rate** and **Time of Use** tariffs, ensuring flexible pricing models and robust validation for all required fields.

---

## API Endpoints

### 1. `POST /admin/tariffs`

**Description:**  
Creates a new electricity tariff (either Flat Rate or Time of Use) and saves it in the system.

**Example cURL Request:**
```bash
curl -X POST http://localhost:8080/admin/tariffs \
  -H "Content-Type: application/json" \
  -d '{
    "tariffName": "Green Saver 2025",
    "supplierName": "EcoPower Ltd",
    "tariffType": "FLAT_RATE",
    "unitRateKwh": 0.15,
    "standingChargePerDay": 0.25,
    "validFrom": "2025-01-01"
  }'
```

**Example JSON Request Body (Flat Rate):**
```json
{
  "tariffName": "Green Saver 2025",
  "supplierName": "EcoPower Ltd",
  "tariffType": "FLAT_RATE",
  "unitRateKwh": 0.15,
  "standingChargePerDay": 0.25,
  "validFrom": "2025-01-01"
}
```

**Example JSON Request Body (Time of Use):**
```json
{
  "tariffName": "Night Owl 2025",
  "supplierName": "EcoPower Ltd",
  "tariffType": "TIME_OF_USE",
  "standingChargePerDay": 0.30,
  "validFrom": "2025-01-01",
  "hourlyRates": {
    "0": 0.10,
    "1": 0.10,
    "2": 0.10,
    "3": 0.10,
    "4": 0.10,
    "5": 0.10,
    "6": 0.12,
    "7": 0.15,
    "8": 0.18,
    "9": 0.20,
    "10": 0.20,
    "11": 0.20,
    "12": 0.20,
    "13": 0.20,
    "14": 0.20,
    "15": 0.20,
    "16": 0.20,
    "17": 0.22,
    "18": 0.25,
    "19": 0.25,
    "20": 0.22,
    "21": 0.18,
    "22": 0.15,
    "23": 0.12
  }
}
```

**Example JSON Success Response:**
```json
{
  "tariffName": "Green Saver 2025",
  "supplierName": "EcoPower Ltd",
  "tariffType": "FLAT_RATE",
  "unitRateKwh": 0.15,
  "standingChargePerDay": 0.25,
  "validFrom": "2025-01-01",
  "hourlyRates": null
}
```

---

### 2. `GET /admin/tariffs`

**Description:**  
Retrieves a list of all tariffs currently stored in the system.

**Example cURL Request:**
```bash
curl -X GET http://localhost:8080/admin/tariffs
```

**Example JSON Success Response:**
```json
[
  {
    "tariffName": "Green Saver 2025",
    "supplierName": "EcoPower Ltd",
    "tariffType": "FLAT_RATE",
    "unitRateKwh": 0.15,
    "standingChargePerDay": 0.25,
    "validFrom": "2025-01-01",
    "hourlyRates": null
  },
  {
    "tariffName": "Night Owl 2025",
    "supplierName": "EcoPower Ltd",
    "tariffType": "TIME_OF_USE",
    "unitRateKwh": null,
    "standingChargePerDay": 0.30,
    "validFrom": "2025-01-01",
    "hourlyRates": {
      "0": 0.10,
      "1": 0.10,
      "2": 0.10,
      "3": 0.10,
      "4": 0.10,
      "5": 0.10,
      "6": 0.12,
      "7": 0.15,
      "8": 0.18,
      "9": 0.20,
      "10": 0.20,
      "11": 0.20,
      "12": 0.20,
      "13": 0.20,
      "14": 0.20,
      "15": 0.20,
      "16": 0.20,
      "17": 0.22,
      "18": 0.25,
      "19": 0.25,
      "20": 0.22,
      "21": 0.18,
      "22": 0.15,
      "23": 0.12
    }
  }
]
```

---

## Notes

- All required fields must be provided according to the tariff type.
- Validation errors will be returned if required fields are missing or values are invalid (e.g., negative prices).
- See the User Story and Acceptance Criteria for further business rules.