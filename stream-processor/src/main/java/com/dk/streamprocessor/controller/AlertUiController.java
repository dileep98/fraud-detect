package com.dk.streamprocessor.controller;


import com.dk.streamprocessor.mapper.Mapper;
import com.dk.streamprocessor.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AlertUiController {

    private final AlertRepository alertRepository;

    @GetMapping("/ui/alerts")
    public String alertsPage(Model model) {
        var alerts = alertRepository.findAll()
                .stream()
                .map(Mapper::toDto)
                .toList();

        model.addAttribute("alerts", alerts);
        return "alerts"; // templates/alerts.html
    }
}
