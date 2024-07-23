package com.mmdevelopement.crm.domain.financial.service;

import com.mmdevelopement.crm.domain.financial.entity.accounts.BankAccountEntity;
import com.mmdevelopement.crm.domain.financial.entity.accounts.TransferEntity;
import com.mmdevelopement.crm.domain.financial.entity.dto.BankAccountDto;
import com.mmdevelopement.crm.domain.financial.entity.dto.TransferDto;
import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import com.mmdevelopement.crm.domain.financial.repository.BankAccountRepository;
import com.mmdevelopement.crm.domain.financial.repository.TransferRepository;
import com.mmdevelopement.crm.infrastructure.exceptions.BadRequestException;
import com.mmdevelopement.crm.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final TransferRepository transferRepository;

    public List<BankAccountDto> getAllAccounts() {
        log.debug("Getting all bank accounts");

        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAll();

        return bankAccountEntities.stream()
                .map(BankAccountDto::fromEntity)
                .collect(Collectors.toList());
    }

    public BankAccountDto getAccountById(Integer id) {
        log.debug("Getting bank account with id {}", id);

        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account with id " + id + " not found"));

        return BankAccountDto.fromEntity(bankAccountEntity);

    }

    public BankAccountDto saveAccount(BankAccountDto bankAccountDto) {
        BankAccountEntity bankAccountEntity = bankAccountRepository.save(bankAccountDto.toEntity());

        log.info("Bank account with id {} created", bankAccountEntity.id());

        return BankAccountDto.fromEntity(bankAccountEntity);
    }

    public void deleteAccount(Integer id) {
        log.info("Deleting bank account with id {}", id);

        if (id == null) {
            throw new BadRequestException("Id cannot be null");
        }

        bankAccountRepository.deleteById(id);
    }

    public List<TransferDto> getAllTransfers() {
        log.debug("Getting all transfers");
        List<TransferEntity> transferEntities = transferRepository.findAll();

        return transferEntities.stream()
                .map(TransferDto::fromEntity)
                .peek(this::decorateWithAccountDetails)
                .collect(Collectors.toList());
    }

    private void decorateWithAccountDetails(TransferDto transferDto) {
        BankAccountEntity fromAccount = bankAccountRepository.findById(transferDto.getFromBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank account with id " + transferDto.getFromBankAccountId() + " not found"));

        BankAccountEntity toAccount = bankAccountRepository.findById(transferDto.getToBankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank account with id " + transferDto.getToBankAccountId() + " not found"));

        transferDto.setFromBankAccountName(fromAccount.name());
        transferDto.setToBankAccountName(toAccount.name());
    }

    public TransferDto getTransferById(Integer id) {
        log.debug("Getting transfer with id {}", id);

        TransferEntity transferEntity = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer with id " + id + " not found"));

        return TransferDto.fromEntity(transferEntity);
    }

    public void deleteTransfer(Integer id) {
        log.info("Deleting transfer with id {}", id);

        if (id == null) {
            throw new BadRequestException("Id cannot be null");
        }

        transferRepository.deleteById(id);
    }

    public TransferEntity saveTransfer(TransferDto transferDto) {
        TransferEntity transferEntity = transferDto.toEntity();

        return transferRepository.save(transferEntity);
    }

    @Transactional
    public void executeTransaction(Integer bankAccountId, Float amount, InvoiceDirection direction) {
        log.info("Executing transaction for account with id {} with amount {}", bankAccountId, amount);

        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account with id " + bankAccountId + " not found"));

        if (direction.equals(InvoiceDirection.IN)) {
            bankAccountEntity.sold(bankAccountEntity.sold() + amount);
        } else {
            if (bankAccountEntity.sold() < amount) {
                throw new BadRequestException("Not enough money in account with id " + bankAccountId);
            }

            bankAccountEntity.sold(bankAccountEntity.sold() - amount);
        }

        bankAccountRepository.save(bankAccountEntity);
    }

    @Transactional
    public TransferDto makeTransfer(TransferDto transferDto) {
        log.info("Making transfer from account {} to account {} with amount {}",
                transferDto.getFromBankAccountId(), transferDto.getToBankAccountId(), transferDto.getAmount());

        validateTransfer(transferDto);

        // check if accounts exist
        var fromAccount = getAccountById(transferDto.getFromBankAccountId()).toEntity();
        var toAccount = getAccountById(transferDto.getToBankAccountId()).toEntity();
        validateSufficientFunds(fromAccount, transferDto.getAmount());

        // update accounts
        fromAccount.sold(fromAccount.sold() - transferDto.getAmount());
        toAccount.sold(toAccount.sold() + transferDto.getAmount());

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);

        // create transfer
        var transferEntity = saveTransfer(transferDto);
        log.info("Transfer successful from account {} to account {} with amount {}",
                fromAccount.id(), toAccount.id(), transferDto.getAmount());

        return TransferDto.fromEntity(transferEntity)
                .setFromBankAccountName(fromAccount.name())
                .setToBankAccountName(toAccount.name());
    }

    public TransferDto updateTransferDetails(TransferDto transferDto) {
        if (transferDto.getId() == null) {
            throw new BadRequestException("Transfer id is required for update");
        }

        log.info("Updating transfer with id {}", transferDto.getId());
        TransferEntity transferEntity = transferRepository.findById(transferDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transfer with id " + transferDto.getId() + " not found"));

        validateNonEditableFields(transferEntity, transferDto);

        transferEntity.description(transferDto.getDescription());
        transferEntity.paymentMethod(transferDto.getPaymentMethod());
        transferEntity.reference(transferDto.getReference());

        transferRepository.save(transferEntity);
        log.info("Transfer with id {} updated", transferDto.getId());

        return TransferDto.fromEntity(transferEntity);
    }

    @Transactional
    public void cancelTransfer(Integer transferId) {
        log.info("Cancelling transfer with id {}", transferId);

        var transfer = getTransferById(transferId).toEntity();
        var fromAccount = getAccountById(transfer.fromBankAccountId()).toEntity();
        var toAccount = getAccountById(transfer.toBankAccountId()).toEntity();

        validateSufficientFunds(toAccount, transfer.amount());

        // update accounts
        log.info("Refunding amount {} from account {} to account {}", transfer.amount(), toAccount.id(), fromAccount.id());

        fromAccount.sold(fromAccount.sold() + transfer.amount());
        toAccount.sold(toAccount.sold() - transfer.amount());

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);

        log.info("Transfer with id {} cancelled", transferId);

        // delete transfer
        deleteTransfer(transferId);
    }

    private void validateTransfer(TransferDto transferDto) {
        if (transferDto.getFromBankAccountId() == null || transferDto.getToBankAccountId() == null) {
            throw new BadRequestException("From and to bank account ids cannot be null");
        }

        if (transferDto.getFromBankAccountId().equals(transferDto.getToBankAccountId())) {
            throw new BadRequestException("From and to bank account ids cannot be the same");
        }

        if (transferDto.getAmount() == null || transferDto.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }
    }

    private void validateSufficientFunds(BankAccountEntity fromAccount, Double amount) {
        if (fromAccount.id() == 1) {
            log.debug("Account with id {} is a personal account, no need to check funds", fromAccount.id());
            return;
        }

        if (fromAccount.sold() < amount) {
            throw new BadRequestException("Not enough funds in account with id " + fromAccount.id());
        }
    }

    public void rollbackTransaction(Integer integer, Float amount, String direction) {
        log.info("Rolling back transaction for account with id {} with amount {}", integer, amount);

        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(integer)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account with id " + integer + " not found"));

        if (direction.equals(InvoiceDirection.IN.toString())) {
            bankAccountEntity.sold(bankAccountEntity.sold() - amount);
        } else {
            bankAccountEntity.sold(bankAccountEntity.sold() + amount);
        }

        bankAccountRepository.save(bankAccountEntity);
    }

    private void validateNonEditableFields(TransferEntity transferEntity, TransferDto transferDto) {
        if (!transferEntity.fromBankAccountId().equals(transferDto.getFromBankAccountId()) ||
                !transferEntity.toBankAccountId().equals(transferDto.getToBankAccountId())) {
            throw new BadRequestException("From and to bank account ids cannot be changed during update");
        }

        if (!transferEntity.amount().equals(transferDto.getAmount())) {
            throw new BadRequestException("Amount cannot be changed during update");
        }

        if (transferEntity.transferDate().compareTo(transferDto.getDate()) != 0) {
            throw new BadRequestException("Transfer date cannot be changed during update");
        }
    }
}
