package com.dk.streamprocessor.controller;


import com.dk.streamprocessor.mapper.Mapper;
import com.dk.streamprocessor.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            Model model
    ) {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        var pageResult = (decision != null && !decision.isBlank())
                ? alertRepository.findByDecisionIgnoreCase(decision, pageable)
                : alertRepository.findAll(pageable);

        var alerts = pageResult
                .getContent()
                .stream()
                .map(Mapper::toDto)
                .toList();

        model.addAttribute("alerts", alerts);
        model.addAttribute("selectedDecision", decision);
        model.addAttribute("page", pageResult);
        return "alerts";
    }
}
