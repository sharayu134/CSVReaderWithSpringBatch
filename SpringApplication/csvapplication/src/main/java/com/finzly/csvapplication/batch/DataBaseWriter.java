package com.finzly.csvapplication.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finzly.csvapplication.dao.MarginedRateRepository;
import com.finzly.csvapplication.model.MarginedRate;

import java.util.List;

@Component
public class DataBaseWriter implements ItemWriter<MarginedRate> {

    @Autowired
    private MarginedRateRepository marginedRateRepository;

    @Override
    public void write(List<? extends MarginedRate> marginedRates) throws Exception {

        marginedRateRepository.saveAll(marginedRates);
    }
}
