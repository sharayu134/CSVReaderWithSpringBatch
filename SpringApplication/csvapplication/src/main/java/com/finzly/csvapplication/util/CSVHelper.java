package com.finzly.csvapplication.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.finzly.csvapplication.model.MarginedRate;


/**
 * @author USER This class is used to parse csv records to MarginedRates 
 *
 */
public class CSVHelper {
	

  public static String TYPE = "text/csv";
  static String[] HEADERs = { "DATE", "CURRENCY_NAME", "CURRENCY_CODE","TERMS","INTERNAL_SPOT_RATE","BUY_RATE","SELL_RATE","BOSS_RATE","CUTOFF_TIME" };

  public static boolean hasCSVFormat(MultipartFile file) {
    if (TYPE.equals(file.getContentType())
    		|| file.getContentType().equals("application/vnd.ms-excel")) {
      return true;
    }

    return false;
  }

  public static List<MarginedRate> csvToMarginedRate(InputStream is) {
	  
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(','));) {

      List<MarginedRate> marginedRateList = new ArrayList<>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
       String emptyDate="";
      for (CSVRecord csvRecord : csvRecords) {
    	  MarginedRate marginedRate;
    	  if(!emptyDate.equals( csvRecord.get("DATE"))) {
    	  
		try {
			marginedRate = new MarginedRate(
					new SimpleDateFormat("dd-MMM-yyyy").parse(csvRecord.get("DATE")),
					  csvRecord.get("CURRENCY_NAME"),
					  csvRecord.get("CURRENCY_CODE"),
					  csvRecord.get("TERMS"),
					  Double.parseDouble(csvRecord.get("INTERNAL_SPOT_RATE")),
					  Double.parseDouble(csvRecord.get("BUY_RATE")),
					  Double.parseDouble(csvRecord.get("SELL_RATE")),
					  Double.parseDouble(csvRecord.get("BOSS_RATE")),
//					  LocalTime.parse( csvRecord.get("CUTOFF_TIME"))
					  csvRecord.get("CUTOFF_TIME")
					  );
			  marginedRateList.add(marginedRate);
		} catch (NumberFormatException e) {
			Logger logger = LoggerFactory.getLogger(CSVHelper.class);
			logger.error("Error in NumberFormatException ",e);
		} catch (ParseException e) {
			Logger logger = LoggerFactory.getLogger(CSVHelper.class);
			logger.error("Error in ParseException ",e);
		}
      }
      }

      return marginedRateList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

}
