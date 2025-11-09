# Electricity Tariff Management Feature

## Feature Summary

The Electricity Tariff Management feature enables system administrators to create, configure, and manage electricity tariffs within the system. This feature addresses the business need for administrators to define new tariff structures that can later be advertised or applied to users who may benefit from specific pricing models.

The system supports two types of electricity tariffs:
- **FLAT_RATE**: A simple tariff with a single unit rate per kWh
- **TIME_OF_USE**: A dynamic tariff with 24 distinct hourly rates for different times of day

## API Endpoints

### Create New Tariff

**Endpoint:** `POST /admin/tariffs`

**Description:** Creates a new electricity tariff in the system with validation for required fields and business rules.

**cURL Example:**
```bash
curl -X POST http://localhost:8080/admin/tariffs \
  -H "Content-Type: application/json" \
  -d '{
    "tariffName": "Economy Standard Rate",
    "supplierName": "British Energy Co",
    "tariffType": "FLAT_RATE",
    "unitRateKwh": 0.25,
    "standingChargePerDay": 0.45,
    "validFrom": "2024-01-01"
  }'
```

**JSON Request Body (FLAT_RATE Example):**
```json
{
  "tariffName": "Economy Standard Rate",
  "supplierName": "British Energy Co",
  "tariffType": "FLAT_RATE",
  "unitRateKwh": 0.25,
  "standingChargePerDay": 0.45,
  "validFrom": "2024-01-01"
}
```

**JSON Request Body (TIME_OF_USE Example):**
```json
{
  "tariffName": "Smart Time Saver",
  "supplierName": "Green Energy Ltd",
  "tariffType": "TIME_OF_USE",
  "standingChargePerDay": 0.50,
  "validFrom": "2024-01-01",
  "hourlyRates": {
    "0": 0.15, "1": 0.15, "2": 0.15, "3": 0.15, "4": 0.15, "5": 0.15,
    "6": 0.20, "7": 0.25, "8": 0.30, "9": 0.28, "10": 0.26, "11": 0.24,
    "12": 0.22, "13": 0.24, "14": 0.26, "15": 0.28, "16": 0.30, "17": 0.35,
    "18": 0.40, "19": 0.38, "20": 0.35, "21": 0.30, "22": 0.25, "23": 0.20
  }
}
```

**JSON Success Response:**
```json
{
  "tariffName": "Economy Standard Rate",
  "supplierName": "British Energy Co",
  "tariffType": "FLAT_RATE",
  "unitRateKwh": 0.25,
  "standingChargePerDay": 0.45,
  "validFrom": "2024-01-01",
  "hourlyRates": null
}
```

### Retrieve All Tariffs

**Endpoint:** `GET /admin/tariffs`

**Description:** Returns a list of all configured tariffs in the system.

**cURL Example:**
```bash
curl -X GET http://localhost:8080/admin/tariffs \
  -H "Accept: application/json"
```

**JSON Success Response:**
```json
[
  {
    "tariffName": "Economy Standard Rate",
    "supplierName": "British Energy Co",
    "tariffType": "FLAT_RATE",
    "unitRateKwh": 0.25,
    "standingChargePerDay": 0.45,
    "validFrom": "2024-01-01",
    "hourlyRates": null
  },
  {
    "tariffName": "Smart Time Saver",
    "supplierName": "Green Energy Ltd",
    "tariffType": "TIME_OF_USE",
    "unitRateKwh": null,
    "standingChargePerDay": 0.50,
    "validFrom": "2024-01-01",
    "hourlyRates": {
      "0": 0.15, "1": 0.15, "2": 0.15, "3": 0.15,
      "4": 0.15, "5": 0.15, "6": 0.20, "7": 0.25,
      "8": 0.30, "9": 0.28, "10": 0.26, "11": 0.24,
      "12": 0.22, "13": 0.24, "14": 0.26, "15": 0.28,
      "16": 0.30, "17": 0.35, "18": 0.40, "19": 0.38,
      "20": 0.35, "21": 0.30, "22": 0.25, "23": 0.20
    }
  }
]
```

## Validation Rules

The system enforces the following validation rules:
- **Required fields**: tariffName, supplierName, tariffType, standingChargePerDay, validFrom
- **FLAT_RATE tariffs**: Must include unitRateKwh field
- **TIME_OF_USE tariffs**: Must include exactly 24 hourly rates (hours 0-23)
- **Negative values**: Prices and charges cannot be negative
- **Field constraints**: Names must not be empty or null