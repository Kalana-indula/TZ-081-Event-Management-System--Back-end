package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.finance.CommissionUpdate;
import com.eventwisp.app.dto.finance.UpdateBalance;
import com.eventwisp.app.dto.response.general.SingleEntityResponse;
import com.eventwisp.app.entity.FinancialData;
import com.eventwisp.app.repository.FinancialDataRepository;
import com.eventwisp.app.service.FinancialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialDataServiceImpl implements FinancialDataService {

    private FinancialDataRepository financialDataRepository;

    @Autowired
    public FinancialDataServiceImpl(FinancialDataRepository financialDataRepository) {
        this.financialDataRepository = financialDataRepository;
    }

    //add initial data
    @Override
    public FinancialData addInitialData(FinancialData financialData) {

        return financialDataRepository.save(financialData);
    }

    @Override
    public SingleEntityResponse<Double> getCommission() {

        SingleEntityResponse<Double> response = new SingleEntityResponse<>();

        //fetch current data
        FinancialData data=financialDataRepository.findById(1).orElse(null);

        if(data==null){
            response.setMessage("No data found");
            return response;
        }


        response.setMessage("Commission: " + data.getCommission());
        response.setEntityData(data.getCommission());

        return response;
    }

    @Override
    public SingleEntityResponse<BigDecimal> getBalance() {

        SingleEntityResponse<BigDecimal> response = new SingleEntityResponse<>();

        //fetch financial data
        FinancialData data=financialDataRepository.findById(1).orElse(null);

        if(data==null){
            response.setMessage("No data found");
            return response;
        }

        response.setMessage("Current Balance: "+data.getPlatformBalance());
        response.setEntityData(data.getPlatformBalance());
        return response;
    }

    @Override
    public SingleEntityResponse<Double> updateCommission(CommissionUpdate commissionUpdate) {

        SingleEntityResponse<Double> response = new SingleEntityResponse<>();

        //fetch financial data
        FinancialData existingData=financialDataRepository.findById(1).orElse(null);

        if(existingData==null){
            response.setMessage("No data found");
            return response;
        }

        existingData.setCommission(commissionUpdate.getCommission());

        FinancialData updatedData=financialDataRepository.save(existingData);

        response.setMessage("New Commission: " + updatedData.getCommission());
        response.setEntityData(updatedData.getCommission());

        return response;
    }

    @Override
    public SingleEntityResponse<BigDecimal> updateBalance(UpdateBalance updateBalance) {

        SingleEntityResponse<BigDecimal> response = new SingleEntityResponse<>();

        //fetch existing data
        FinancialData existingData=financialDataRepository.findById(1).orElse(null);

        if(existingData==null){
            response.setMessage("No data found");
            return response;
        }

        existingData.setPlatformBalance(updateBalance.getBalance());

        FinancialData updatedData=financialDataRepository.save(existingData);

        response.setMessage("New Balance: " + updatedData.getPlatformBalance());
        response.setEntityData(updatedData.getPlatformBalance());

        return response;
    }


}
