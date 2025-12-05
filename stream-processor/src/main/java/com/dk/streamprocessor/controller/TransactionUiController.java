package com.dk.streamprocessor.controller;


import com.dk.streamprocessor.mapper.Mapper;
import com.dk.streamprocessor.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TransactionUiController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/ui/transactions")
    public String transactionsPage(@RequestParam(value = "accountId", required = false) String accountId, Model model) {
        var entities = (accountId != null && !accountId.isBlank())
                ? transactionRepository.findByAccountId(accountId)
                : transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "eventTime"));

        var txs = entities.stream()
                .map(Mapper::toDto)
                .toList();

        model.addAttribute("transactions", txs);
        assert accountId != null;
        model.addAttribute("accountId", accountId);

        return "transactions";

    }
}
