package uk.tw.energy.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import uk.tw.energy.domain.Tariff;

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
