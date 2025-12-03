package com.dk.streamprocessor.controller;

import com.dk.streamprocessor.dto.TransactionDto;
import com.dk.streamprocessor.mapper.Mapper;
import com.dk.streamprocessor.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionQueryController {

    // TODO has to rewrite whole class check history
    private final TransactionRepository transactionRepository;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(Mapper::toDto)
                .toList();
    }

    @GetMapping("/{txId}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable("txId") String txId) {
        return transactionRepository.findById(txId)
                .map(Mapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/accounts/{accountId}")
    public List<TransactionDto> getTransactionsByAccount(@PathVariable("accountId") String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .stream()
                .map(Mapper::toDto)
                .toList();
    }
}
