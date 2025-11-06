### User Story: Add New Electricity Tariff
As an Administrator, I want to create and configure a new electricity tariff in the system, so that it can be stored and later advertised or applied to users who may benefit from it.

### Acceptance Criteria (AC) - Revised
#### AC 1: (Happy Path) Successfully create a FLAT_RATE tariff
Given I am an Administrator
When I submit a new tariff form with all required fields for a FLAT_RATE (e.g., name, supplier, type, a single unitRateKwh, standingChargePerDay, validFrom)
Then a new tariff is successfully saved in the system
And I receive a confirmation message
And the new tariff is retrievable from the admin "Tariff List" (i.e., it's in the system's active collection).

#### AC 2: (HappyPath) Successfully create a TIME_OF_USE tariff
Given I am an Administrator
When I submit a new tariff form with all required fields for TIME_OF_USE (e.g., name, supplier, type, 24 distinct hourly rates, standingChargePerDay, validFrom)
Then a new tariff is successfully saved in the system, including all associated hourly rates
And I receive a confirmation message
And the new tariff is retrievable from the admin "Tariff List."

#### AC 3: (Validation) Fail to create a tariff with invalid prices
Given I am an Administrator
When I attempt to submit the form with a negative value for unitRateKwh or standingChargePerDay
Then the form is not submitted
And a validation error is displayed: "Prices and charges cannot be negative."

#### AC 4: (Validation) Fail to create a tariff with conflicting type and rate data
Given I am an Administrator
When I select tariffType = FLAT_RATE but I leave the unitRateKwh field empty
Then the form is not submitted
And a validation error is displayed: "Unit Rate (kWh) is required for a Flat Rate tariff."
(This also applies to a TIME_OF_USE tariff submitted without the required 24 hourly rates).

#### AC 5: (Validation) Fail to create a tariff with missing required fields
Given I am an Administrator
When I attempt to submit the form without a tariffName or supplierName
Then the form is not submitted
And a validation error is displayed indicating the missing field (e.g., "Tariff Name is required.").
