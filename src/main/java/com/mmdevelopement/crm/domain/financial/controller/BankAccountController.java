package com.mmdevelopement.crm.domain.financial.controller;

import com.mmdevelopement.crm.domain.financial.entity.dto.BankAccountDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.TransferDto;
import com.mmdevelopement.crm.domain.financial.service.BankAccountService;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/financial/bank-account")
@RequiredArgsConstructor
public class BankAccountController {
    // apis to be implemented
    // 1. create account
    // 2. update account
    // 3. delete account
    // 4. get account by id -> detailed view maybe (with transfers tied to account)
    // 5. get all accounts -> id, name, balance, number

    // 6. transfer money between accounts
    // 7. update transfer
    // 8. delete transfer
    // 9. get transfer by id
    // 10. get all transfers

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
        if (bankAccountDto.id() == null) {
            throw new BadRequestException("Account id is required for update");
        }

        return bankAccountService.saveAccount(bankAccountDto);
    }

    @GetMapping("/transfers/")
    public List<TransferDto> getAllTransfers() {
        return bankAccountService.getAllTransfers();
    }

    @GetMapping("/transfers/{id}")
    public TransferDto getTransferById(@PathVariable Integer id) {
        return bankAccountService.getTransferById(id);
    }

    @PostMapping("/transfers/create")
    public TransferDto saveTransfer(@RequestBody TransferDto transferDto) {
        return bankAccountService.makeTransfer(transferDto);
    }
}
