# Display Cost of Last Week's Usage

## Description 
As an electricity consumer I want to be able to view my usage cost of the last week so that I can monitor my spending

## Acceptance Criteria:
- Given I have a smart meter ID with price plan attached to it and usage data stored, when I request the usage cost then I am shown the correct cost of last week's usage
- Given I have a smart meter ID without a price plan attached to it and usage data stored, when I request the usage cost then an error message is displayed

## How to calculate usage cost
Unit of meter readings : kW (KiloWatt) <br>
Unit of Time : Hour (h) <br>
Unit of Energy Consumed : kW * Hour = kWh <br>
Unit of Tariff : $ per kWh (ex 0.2 $ per kWh) <br>
<br>
To calculate the usage cost for a duration (D) in which lets assume we have captured N electricity readings (er1,er2,er3....erN)
<br>
Average reading in KW = (er1.reading + er2.reading + ..... erN.Reading)/N <br>
Usage time in hours = Duration(D) in hours <br>
Energy consumed in kWh = average reading * usage time <br>
Cost = tariff unit prices * energy consumed <br>

## Price calculation example
### Meter Readings
| Timestamp (IS0 8601) | Power Reading (in kW) |
| - | - |
| 2024-10-19T09:25:00Z | 1.101 |
| 2024-10-20T10:00:00Z | 0.994 |
| 2024-10-21T16:58:00Z | 0.503 |
| 2024-10-22T13:20:00Z | 1.065 |
| 2024-10-23T10:40:00Z | 0.213 |
| 2024-10-24T11:00:00Z | 0.24 |
| 2024-10-25T15:28:00Z | 0.598 |
| 2024-10-26T03:45:00Z | 0.001 |
| 2024-10-26T09:26:00Z | 0.506 |
| 2024-10-27T12:46:00Z | 1.011 |
| 2024-10-28T15:14:00Z | 1.201 |
| 2024-10-29T07:10:00Z | 0.009 |
| 2024-10-30T09:54:00Z | 0.202 |

Listed above are 13 power readings (in kW) from timestamps over 12 days, representing the power consumed at the given timestamp. <br>
Timestamps are in IS0 8601 format (in UTC), which can be copied into a text editor and parsed easily in most major programming languages.
### Usage Price Calculation (as per User Story 2345)
| Metric | Formula | Value | Comment |
| - | - | - | - |
| Avg Power (kW) | Avg of readings | 0.588 | Assuming constant power usage by averaging the readings is a simplification for the sake of the exercise. |
| Elapsed Time (Hours) || 264.4833333 | We measure absolute time that has passed from the first to the last reading. |
| Energy Consumed (kwH) | (Elapsed Time * Avg Power) | 155.5162 | Power usage is assumed constant, so multiply the average power by the time elapsed to find the energy consumed. |
| Price per kWh | Price Plan Specific | £0.29 | We assume PowerDale is in the UK, so price is in GBP. |
| **Total Cost (Rounded)** | **(Energy Consumed * Price per kWh)** | **£45.10** | Simply multiply the energy usage by the unit price rate. |

Unit price is more or less realistic for UK in 2024. <br>
The actual consumption would be very high in comparison to real world family house usage - a result of averaging.