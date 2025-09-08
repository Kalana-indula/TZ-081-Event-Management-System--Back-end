package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.response.general.MultipleEntityResponse;
import com.eventwisp.app.dto.response.general.SingleEntityResponse;
import com.eventwisp.app.dto.transaction.CreateTransactionRequest;
import com.eventwisp.app.dto.transaction.TransactionDetails;
import com.eventwisp.app.entity.Organizer;
import com.eventwisp.app.entity.Transaction;
import com.eventwisp.app.repository.OrganizerRepository;
import com.eventwisp.app.repository.TransactionRepository;
import com.eventwisp.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private OrganizerRepository organizerRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  OrganizerRepository organizerRepository) {
        this.transactionRepository = transactionRepository;
        this.organizerRepository = organizerRepository;
    }


    @Override
    @Transactional
    public SingleEntityResponse<TransactionDetails> createTransaction(CreateTransactionRequest createTransactionRequest) {
        SingleEntityResponse<TransactionDetails> response = new SingleEntityResponse<>();

        // Validate organizer
        Optional<Organizer> optionalOrganizer = organizerRepository.findById(createTransactionRequest.getOrganizerId());
        if (optionalOrganizer.isEmpty()) {
            response.setMessage("Organizer not found with id: " + createTransactionRequest.getOrganizerId());
            response.setEntityData(null);
            return response;
        }

        Organizer organizer = optionalOrganizer.get();

        // Create transaction entity
        Transaction transaction = new Transaction();
        transaction.setAmount(createTransactionRequest.getAmount());
        transaction.setDate(LocalDate.now());
        transaction.setTime(LocalTime.now());
        transaction.setOrganizer(organizer);

        // Save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Update organizer financials
        organizer.setTotalWithdrawals(organizer.getTotalWithdrawals().add(savedTransaction.getAmount()));
        organizer.setCurrentBalance(organizer.getCurrentBalance().subtract(savedTransaction.getAmount()));
        organizerRepository.save(organizer);

        // Build DTO response
        TransactionDetails details = new TransactionDetails();
        details.setId(savedTransaction.getId());
        details.setAmount(savedTransaction.getAmount());
        details.setOrganizerId(organizer.getId());
        details.setOrganizerName(organizer.getFirstName() + " " + organizer.getLastName());
        details.setDate(savedTransaction.getDate());
        details.setTime(savedTransaction.getTime());

        response.setMessage("Transaction created successfully");
        response.setEntityData(details);

        return response;
    }

    @Override
    public MultipleEntityResponse<TransactionDetails> getAllTransactions() {
        MultipleEntityResponse<TransactionDetails> response = new MultipleEntityResponse<>();
        List<Transaction> transactions = transactionRepository.findAll();

        //check if there are any transactions
        if(transactions.isEmpty()) {
            response.setMessage("No transactions found");
            return response;
        }

        List<TransactionDetails> detailsList = new ArrayList<>();
        for (Transaction tx : transactions) {
            TransactionDetails details = new TransactionDetails();
            details.setId(tx.getId());
            details.setAmount(tx.getAmount());
            details.setOrganizerId(tx.getOrganizer().getId());
            details.setOrganizerName(tx.getOrganizer().getFirstName() + " " + tx.getOrganizer().getLastName());
            details.setDate(tx.getDate());
            details.setTime(tx.getTime());
            detailsList.add(details);
        }

        response.setMessage("Transactions retrieved successfully");
        response.setEntityList(detailsList);

        return response;
    }

    @Override
    public SingleEntityResponse<TransactionDetails> findTransactionById(Long transactionId) {

        SingleEntityResponse<TransactionDetails> response = new SingleEntityResponse<>();
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);

        if (optionalTransaction.isEmpty()) {
            response.setMessage("Transaction not found with id: " + transactionId);
            response.setEntityData(null);
            return response;
        }

        Transaction tx = optionalTransaction.get();
        TransactionDetails details = new TransactionDetails();
        details.setId(tx.getId());
        details.setAmount(tx.getAmount());
        details.setOrganizerId(tx.getOrganizer().getId());
        details.setOrganizerName(tx.getOrganizer().getFirstName() + " " + tx.getOrganizer().getLastName());
        details.setDate(tx.getDate());
        details.setTime(tx.getTime());

        response.setMessage("Transaction retrieved successfully");
        response.setEntityData(details);

        return response;
    }

    @Override
    public MultipleEntityResponse<TransactionDetails> findTransactionsByOrganizer(Long organizerId) {
        MultipleEntityResponse<TransactionDetails> response = new MultipleEntityResponse<>();
        List<Transaction> transactions = transactionRepository.findByOrganizerId(organizerId);

        if (transactions.isEmpty()) {
            response.setMessage("No transactions found for organizer with id: " + organizerId);
            response.setEntityList(new ArrayList<>());
            return response;
        }

        List<TransactionDetails> detailsList = new ArrayList<>();
        for (Transaction tx : transactions) {
            TransactionDetails details = new TransactionDetails();
            details.setId(tx.getId());
            details.setAmount(tx.getAmount());
            details.setOrganizerId(tx.getOrganizer().getId());
            details.setOrganizerName(tx.getOrganizer().getFirstName() + " " + tx.getOrganizer().getLastName());
            details.setDate(tx.getDate());
            details.setTime(tx.getTime());
            detailsList.add(details);
        }

        response.setMessage("Transactions for organizer retrieved successfully");
        response.setEntityList(detailsList);

        return response;
    }
}
