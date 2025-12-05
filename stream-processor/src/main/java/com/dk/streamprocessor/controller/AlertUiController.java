package com.dk.streamprocessor.controller;


import com.dk.streamprocessor.mapper.Mapper;
import com.dk.streamprocessor.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AlertUiController {

    private final AlertRepository alertRepository;

    @GetMapping("/ui/alerts")
    public String alertsPage(
            @RequestParam(value = "decision", required = false) String decision,
            Model model
    ) {
        var stream = alertRepository.findAll().stream();

        if (decision != null && !decision.isBlank()) {
            String d = decision.toUpperCase();
            stream = stream.filter(a -> d.equals(a.getDecision()));
        }

        var alerts = stream
                .map(Mapper::toDto)
                .toList();

        model.addAttribute("alerts", alerts);
        model.addAttribute("selectedDecision", decision);
        return "alerts";
    }
}
