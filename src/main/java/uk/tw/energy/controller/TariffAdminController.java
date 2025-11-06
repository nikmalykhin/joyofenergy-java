package uk.tw.energy.controller;

import uk.tw.energy.domain.Tariff;
import uk.tw.energy.service.TariffService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/tariffs")
public class TariffAdminController {

    private final TariffService tariffService;

    public TariffAdminController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    public Tariff createTariff(@RequestBody Tariff tariff) {
        // Manual validation can be added here if needed
        return tariffService.save(tariff);
    }

    @GetMapping
    public List<Tariff> getAllTariffs() {
        return tariffService.findAll();
    }
}
