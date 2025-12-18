package com.dk.streamprocessor.controller;


import com.dk.streamprocessor.mapper.Mapper;
import com.dk.streamprocessor.repository.TransactionRepository;
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
public class TransactionUiController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/ui/transactions")
    public String transactionsPage(
            @RequestParam(value = "accountId", required = false) String accountId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "2") int size,
            Model model
    ) {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "eventTime"));

        var pageResult = (accountId != null && !accountId.isBlank())
                ? transactionRepository.findByAccountId(accountId, pageable)
                : transactionRepository.findAll(pageable);

        var txs = pageResult
                .getContent()
                .stream()
                .map(Mapper::toDto)
                .toList();

        model.addAttribute("transactions", txs);
        model.addAttribute("accountId", accountId);
        model.addAttribute("page", pageResult);

        return "transactions";

    }
}
