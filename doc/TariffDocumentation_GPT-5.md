# Tariff Feature Overview

## 1. Feature Summary
Enables Administrators to create and manage electricity tariffs so they can be persisted and later advertised or applied to end users. Supports two tariff types: FLAT\_RATE (single unit price) and TIME\_OF\_USE (24 hourly rates). Provides validation to prevent missing required fields and negative pricing inputs.

### Core Business Value
Standardizes tariff creation, ensuring consistent data (names, supplier, pricing, validity date) and preparing the system for future matching of users to optimal tariffs.

## 2. API Endpoints

### POST `/admin/tariffs`
Creates a new tariff (FLAT\_RATE or TIME\_OF\_USE).

#### Validation Highlights
- `tariffName`, `supplierName`, `tariffType`, `standingChargePerDay`, `validFrom` required.
- FLAT\_RATE requires `unitRateKwh`.
- TIME\_OF\_USE requires exactly 24 `hourlyRates` (keys 0–23).
- Prices must be non\-negative.

#### Example cURL (FLAT\_RATE)
```bash
curl -X POST https://api.example.com/admin/tariffs \
  -H "Content-Type: application/json" \
  -d '{
    "tariffName": "Eco Saver 2025",
    "supplierName": "GreenEnergy Ltd",
    "tariffType": "FLAT_RATE",
    "unitRateKwh": 0.285,
    "standingChargePerDay": 0.42,
    "validFrom": "2025-01-01"
  }'
```

#### Example JSON Request Body (TIME\_OF\_USE)
```json
{
  "tariffName": "Flex Peak 2025",
  "supplierName": "GridSmart",
  "tariffType": "TIME_OF_USE",
  "standingChargePerDay": 0.38,
  "validFrom": "2025-02-01",
  "hourlyRates": {
    "0": 0.19, "1": 0.18, "2": 0.17, "3": 0.17, "4": 0.18, "5": 0.20,
    "6": 0.24, "7": 0.26, "8": 0.27, "9": 0.25, "10": 0.23, "11": 0.22,
    "12": 0.23, "13": 0.24, "14": 0.25, "15": 0.26, "16": 0.30, "17": 0.34,
    "18": 0.36, "19": 0.35, "20": 0.32, "21": 0.28, "22": 0.24, "23": 0.21
  }
}
```

#### Example JSON Success Response (FLAT\_RATE)
```json
{
  "tariffName": "Eco Saver 2025",
  "supplierName": "GreenEnergy Ltd",
  "tariffType": "FLAT_RATE",
  "unitRateKwh": 0.285,
  "standingChargePerDay": 0.42,
  "validFrom": "2025-01-01",
  "hourlyRates": null
}
```

#### Example JSON Validation Error
```json
{
  "error": "Unit Rate (kWh) is required for a Flat Rate tariff."
}
```

---

### GET `/admin/tariffs`
Returns all stored tariffs.

#### Example cURL
```bash
curl -X GET https://api.example.com/admin/tariffs
```

#### Example JSON Success Response (Mixed)
```json
[
  {
    "tariffName": "Eco Saver 2025",
    "supplierName": "GreenEnergy Ltd",
    "tariffType": "FLAT_RATE",
    "unitRateKwh": 0.285,
    "standingChargePerDay": 0.42,
    "validFrom": "2025-01-01",
    "hourlyRates": null
  },
  {
    "tariffName": "Flex Peak 2025",
    "supplierName": "GridSmart",
    "tariffType": "TIME_OF_USE",
    "unitRateKwh": null,
    "standingChargePerDay": 0.38,
    "validFrom": "2025-02-01",
    "hourlyRates": {
      "0": 0.19, "1": 0.18, "2": 0.17, "3": 0.17, "4": 0.18, "5": 0.20,
      "6": 0.24, "7": 0.26, "8": 0.27, "9": 0.25, "10": 0.23, "11": 0.22,
      "12": 0.23, "13": 0.24, "14": 0.25, "15": 0.26, "16": 0.30, "17": 0.34,
      "18": 0.36, "19": 0.35, "20": 0.32, "21": 0.28, "22": 0.24, "23": 0.21
    }
  }
]
```

## 3. Notes
- Persistence is in\-memory (ConcurrentHashMap) pending future storage integration.
- Extendable for update/delete endpoints in subsequent iterations.
- All dates use ISO\-8601 format (YYYY\-MM\-DD).