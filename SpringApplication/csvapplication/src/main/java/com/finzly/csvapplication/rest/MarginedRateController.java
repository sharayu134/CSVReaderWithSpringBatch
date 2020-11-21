package com.finzly.csvapplication.rest;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.finzly.csvapplication.service.MarginedRateService;

/**
 * @author USER
 * This is MarginedRateController
 */

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class MarginedRateController{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private MarginedRateService marginedRateService;
	
	@PostMapping("/marginedrates")
	public void add(@RequestParam("file") MultipartFile file) {
		logger.debug("inside post");
		
		try {
			marginedRateService.save(file);
		} catch (NumberFormatException | ParseException e) {
			logger.error("Error parsing file",e);
		}
	}
}
