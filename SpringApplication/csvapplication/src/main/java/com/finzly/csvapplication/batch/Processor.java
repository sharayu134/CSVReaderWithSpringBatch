package com.finzly.csvapplication.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.finzly.csvapplication.model.MarginedRate;

//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;

/**
 *  This class is processor component for step .
 * @return  marginedRate object
 * @author Sharayu Yadav.
 */
@Component
public class Processor implements ItemProcessor<MarginedRate, MarginedRate> {

    @Override
    public MarginedRate process(MarginedRate marginedRate) throws Exception {
//    to process
        return marginedRate;
    }
}
