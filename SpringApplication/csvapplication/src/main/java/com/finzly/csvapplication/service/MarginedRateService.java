package com.finzly.csvapplication.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.finzly.csvapplication.dao.MarginedRateRepository;
import com.finzly.csvapplication.model.MarginedRate;
import com.finzly.csvapplication.util.CSVHelper;

/**
 * @author USER This is Margined Rate Service which saves parsed csv records to database
 */

@Service
public class MarginedRateService {
	
	 @Autowired
	 MarginedRateRepository marginedRateRepository;
	 
	  public void save(MultipartFile file) throws NumberFormatException, ParseException {
	    try {
	      List<MarginedRate> marginedRate = CSVHelper.csvToMarginedRate(file.getInputStream());
	      marginedRateRepository.saveAll(marginedRate);
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store csv data: " + e.getMessage());
	    }
	  }
	
}
