package com.dk.streamprocessor.controller;

import com.dk.streamprocessor.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping({"/", "/ui/dashboard"})
    public String dashboard(Model model) {
        var stats = dashboardService.computeStats();
        model.addAttribute("stats", stats);
        return "dashboard";   // templates/dashboard.html
    }
}