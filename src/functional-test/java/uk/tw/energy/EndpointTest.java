package uk.tw.energy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import uk.tw.energy.builders.MeterReadingsBuilder;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.MeterReadings;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class EndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static HttpEntity<MeterReadings> toHttpEntity(MeterReadings meterReadings) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(meterReadings, headers);
    }

    @Test
    public void shouldStoreReadings() {
        MeterReadings meterReadings =
                new MeterReadingsBuilder().generateElectricityReadings().build();
        HttpEntity<MeterReadings> entity = toHttpEntity(meterReadings);

        ResponseEntity<String> response = restTemplate.postForEntity("/readings/store", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    public void givenMeterIdShouldReturnAMeterReadingAssociatedWithMeterId() {
        String smartMeterId = "alice";
        List<ElectricityReading> data = List.of(
                new ElectricityReading(Instant.parse("2024-04-26T00:00:10.00Z"), new BigDecimal(10)),
                new ElectricityReading(Instant.parse("2024-04-26T00:00:20.00Z"), new BigDecimal(20)),
                new ElectricityReading(Instant.parse("2024-04-26T00:00:30.00Z"), new BigDecimal(30)));
        populateReadingsForMeter(smartMeterId, data);

        ResponseEntity<ElectricityReading[]> response =
                restTemplate.getForEntity("/readings/read/" + smartMeterId, ElectricityReading[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.asList(response.getBody())).isEqualTo(data);
    }

    @Test
    public void shouldCalculateAllPrices() {
        String smartMeterId = "bob";
        List<ElectricityReading> data = List.of(
                new ElectricityReading(Instant.parse("2024-04-26T00:00:10.00Z"), new BigDecimal(10)),
                new ElectricityReading(Instant.parse("2024-04-26T00:00:20.00Z"), new BigDecimal(20)),
                new ElectricityReading(Instant.parse("2024-04-26T00:00:30.00Z"), new BigDecimal(30)));
        populateReadingsForMeter(smartMeterId, data);

        ResponseEntity<CompareAllResponse> response =
                restTemplate.getForEntity("/price-plans/compare-all/" + smartMeterId, CompareAllResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(new CompareAllResponse(
                        Map.of("price-plan-0", 36000, "price-plan-1", 7200, "price-plan-2", 3600), null));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void givenMeterIdAndLimitShouldReturnRecommendedCheapestPricePlans() {
        String smartMeterId = "jane";
        List<ElectricityReading> data = List.of(
                new ElectricityReading(Instant.parse("2024-04-26T00:00:10.00Z"), new BigDecimal(10)),
                new ElectricityReading(Instant.parse("2024-04-26T00:00:20.00Z"), new BigDecimal(20)),
                new ElectricityReading(Instant.parse("2024-04-26T00:00:30.00Z"), new BigDecimal(30)));
        populateReadingsForMeter(smartMeterId, data);

        ResponseEntity<Map[]> response =
                restTemplate.getForEntity("/price-plans/recommend/" + smartMeterId + "?limit=2", Map[].class);

        assertThat(response.getBody()).containsExactly(Map.of("price-plan-2", 3600), Map.of("price-plan-1", 7200));
    }

    @SuppressWarnings({"rawtypes", "DataFlowIssue"})
    @Test
    public void shouldCompareByDayOfWeek() {
        String smartMeterId = "test-meter-day-compare";
        List<ElectricityReading> mondayReadings = List.of(
                new ElectricityReading(Instant.parse("2024-11-25T10:00:00Z"), new BigDecimal(15)),
                new ElectricityReading(Instant.parse("2024-11-25T12:00:00Z"), new BigDecimal(25)));
        List<ElectricityReading> tuesdayReadings = List.of(
                new ElectricityReading(Instant.parse("2024-11-26T10:00:00Z"), new BigDecimal(20)),
                new ElectricityReading(Instant.parse("2024-11-26T12:00:00Z"), new BigDecimal(30)));

        List<ElectricityReading> allReadings = new ArrayList<>();
        allReadings.addAll(mondayReadings);
        allReadings.addAll(tuesdayReadings);

        populateReadingsForMeter(smartMeterId, allReadings);

        ResponseEntity<Map> response =
                restTemplate.getForEntity("/price-plans/compare-by-day/" + smartMeterId, Map.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).containsKeys("MONDAY", "TUESDAY");

        Map<String, Object> mondayCosts =
                (Map<String, Object>) response.getBody().get("MONDAY");
        assertThat(mondayCosts).containsKeys("price-plan-0", "price-plan-1", "price-plan-2");

        Map<String, Object> tuesdayCosts =
                (Map<String, Object>) response.getBody().get("TUESDAY");
        assertThat(tuesdayCosts).containsKeys("price-plan-0", "price-plan-1", "price-plan-2");
    }

    @SuppressWarnings({"rawtypes", "DataFlowIssue"})
    @Test
    public void shouldRankLowestPlansByDayOfWeek() {
        String smartMeterId = "test-meter-day-rank";
        List<ElectricityReading> wednesdayReadings = List.of(
                new ElectricityReading(Instant.parse("2024-11-27T10:00:00Z"), new BigDecimal(12)),
                new ElectricityReading(Instant.parse("2024-11-27T12:00:00Z"), new BigDecimal(22)));
        List<ElectricityReading> thursdayReadings = List.of(
                new ElectricityReading(Instant.parse("2024-11-28T10:00:00Z"), new BigDecimal(18)),
                new ElectricityReading(Instant.parse("2024-11-28T12:00:00Z"), new BigDecimal(28)));

        List<ElectricityReading> allReadings = new ArrayList<>();
        allReadings.addAll(wednesdayReadings);
        allReadings.addAll(thursdayReadings);

        populateReadingsForMeter(smartMeterId, allReadings);

        ResponseEntity<Map> response =
                restTemplate.getForEntity("/price-plans/rank-by-day/" + smartMeterId + "?limit=2", Map.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).containsKeys("WEDNESDAY", "THURSDAY");

        List<Map<String, Object>> wednesdayRanking =
                (List<Map<String, Object>>) response.getBody().get("WEDNESDAY");
        assertThat(wednesdayRanking).hasSize(2);
        assertThat(wednesdayRanking.get(0)).containsKey("planName");
        assertThat(wednesdayRanking.get(0)).containsKey("cost");

        List<Map<String, Object>> thursdayRanking =
                (List<Map<String, Object>>) response.getBody().get("THURSDAY");
        assertThat(thursdayRanking).hasSize(2);
        assertThat(thursdayRanking.get(0)).containsKey("planName");
        assertThat(thursdayRanking.get(0)).containsKey("cost");
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void shouldReturnNotFoundWhenNoReadingsForDayOfWeek() {
        String nonExistentMeterId = "non-existent-meter";

        ResponseEntity<Map> compareResponse =
                restTemplate.getForEntity("/price-plans/compare-by-day/" + nonExistentMeterId, Map.class);
        assertThat(compareResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<Map> rankResponse =
                restTemplate.getForEntity("/price-plans/rank-by-day/" + nonExistentMeterId + "?limit=2", Map.class);
        assertThat(rankResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void populateReadingsForMeter(String smartMeterId, List<ElectricityReading> data) {
        MeterReadings readings = new MeterReadings(smartMeterId, data);

        HttpEntity<MeterReadings> entity = toHttpEntity(readings);
        restTemplate.postForEntity("/readings/store", entity, String.class);
    }

    record CompareAllResponse(Map<String, Integer> pricePlanComparisons, String pricePlanId) {}
}
