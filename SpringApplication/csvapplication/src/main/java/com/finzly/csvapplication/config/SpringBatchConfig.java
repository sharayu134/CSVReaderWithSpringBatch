package com.finzly.csvapplication.config;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.convert.CustomConversions;

import com.finzly.csvapplication.model.MarginedRate;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<MarginedRate> itemReader,
                   ItemProcessor<MarginedRate, MarginedRate> itemProcessor,
                   ItemWriter<MarginedRate> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<MarginedRate, MarginedRate>chunk(10000)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();


        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader<MarginedRate> itemReader() {

        FlatFileItemReader<MarginedRate> flatFileItemReader = new FlatFileItemReader<>();

//      flatFileItemReader.setResource(new FileSystemResource("src/main/resources/ten_thousand_records.csv"));
         flatFileItemReader.setResource(new FileSystemResource("src/main/resources/one_Lac_records.csv"));
//           flatFileItemReader.setResource(new FileSystemResource("src/main/resources/three_lac_twenty_thousands_records.csv"));

         
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);//skipping header here
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setMaxItemCount(100000);
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<MarginedRate> lineMapper() {

        DefaultLineMapper<MarginedRate> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] { "DATE", "CURRENCY_NAME", "CURRENCY_CODE","TERMS","INTERNAL_SPOT_RATE","BUY_RATE","SELL_RATE","BOSS_RATE","CUTOFF_TIME" });

        BeanWrapperFieldSetMapper<MarginedRate> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(MarginedRate.class);
        
        Map< java.lang.Object, java.beans.PropertyEditor> ParseMap =new HashMap<>();
  
          
        ParseMap.put(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MMM-yyyy"), false));
        ParseMap.put(Double.class,new CustomNumberEditor( Double.class, new DecimalFormat("#####.000"),true));//Float.class, numberFormat, true

        fieldSetMapper.setCustomEditors(ParseMap);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

}
//  class CS implements ConversionService {
//
//    @Override
//    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
//        return sourceType == String.class && targetType == double.class;
//    }
//
//    @Override
//    public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
//        return sourceType.equals(TypeDescriptor.valueOf(String.class)) &&
//               targetType.equals(TypeDescriptor.valueOf(double.class)) ;
//    }
//
//    @Override
//    public <T> T convert(Object source, Class<T> targetType) {
//        return (T)Double.valueOf(source.toString().replace(',', '.'));
//    }
//
//    @Override
//    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
//        return Double.valueOf(source.toString().replace(',', '.'));
//    }
//}

//https://www.youtube.com/watch?v=1XEX-u12i0A