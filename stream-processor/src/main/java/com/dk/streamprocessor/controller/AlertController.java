package com.dk.streamprocessor.controller;

import com.dk.streamprocessor.dto.AlertDto;
import com.dk.streamprocessor.mapper.Mapper;
import com.dk.streamprocessor.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;

    @GetMapping
    public List<AlertDto> getAllAlerts() {
        return alertRepository.findAll()
                .stream()
                .map(Mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertDto> getAlert(@PathVariable("id") Long id) {
        return alertRepository.findById(id)
                .map(Mapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-tx/{txId}")
    public List<AlertDto> getAlertsByTx(@PathVariable("txId") String txId) {
        return alertRepository.findByTxId(txId)
                .stream()
                .map(Mapper::toDto)
                .toList();
    }

}
