package com.mmdevelopement.crm.domain.financial.controller;

import com.mmdevelopement.crm.domain.financial.entity.dto.BankAccountDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.TransferDto;
import com.mmdevelopement.crm.domain.financial.service.BankAccountService;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/financial/account")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;


    @GetMapping("/")
    public List<BankAccountDto> createAccount() {
        return bankAccountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public BankAccountDto getAccountById(@PathVariable Integer id) {
        return bankAccountService.getAccountById(id);
    }

    @PostMapping("/")
    public BankAccountDto saveAccount(@RequestBody BankAccountDto bankAccountDto) {
        return bankAccountService.saveAccount(bankAccountDto);
    }

    @PutMapping("/")
    public BankAccountDto updateAccount(@RequestBody BankAccountDto bankAccountDto) {
        if (bankAccountDto.getId() == null) {
            throw new BadRequestException("Account id is required for update");
        }

        return bankAccountService.saveAccount(bankAccountDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Integer id) {
        bankAccountService.deleteAccount(id);
    }

    @GetMapping("/transfer/")
    public List<TransferDto> getAllTransfers() {
        return bankAccountService.getAllTransfers();
    }

    @GetMapping("/transfer/{id}")
    public TransferDto getTransferById(@PathVariable Integer id) {
        return bankAccountService.getTransferById(id);
    }

    @PostMapping("/transfer/")
    public TransferDto saveTransfer(@RequestBody TransferDto transferDto) {
        return bankAccountService.makeTransfer(transferDto);
    }

    @PutMapping("/transfer/")
    public TransferDto updateTransfer(@RequestBody TransferDto transferDto) {
        return bankAccountService.updateTransferDetails(transferDto);
    }

    @DeleteMapping("/transfer/{id}")
    public void deleteTransfer(@PathVariable Integer id) {
        bankAccountService.cancelTransfer(id);
    }
}
