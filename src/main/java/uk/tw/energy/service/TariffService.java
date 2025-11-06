package uk.tw.energy.service;

import org.springframework.stereotype.Service;
import uk.tw.energy.domain.Tariff;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

@Service
public class TariffService {
    private final Map<String, Tariff> tariffs = new ConcurrentHashMap<>();

    public Tariff save(Tariff tariff) {
        tariffs.put(tariff.getTariffName(), tariff);
        return tariff;
    }

    public List<Tariff> findAll() {
        return new ArrayList<>(tariffs.values());
    }
}
