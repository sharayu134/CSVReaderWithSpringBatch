package com.finzly.csvapplication.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.finzly.csvapplication.model.MarginedRate;

@Transactional
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public FlatFileItemReader<MarginedRate> itemReader() {

        FlatFileItemReader<MarginedRate> flatFileItemReader = new FlatFileItemReader<>();

//      flatFileItemReader.setResource(new FileSystemResource("src/main/resources/ten_thousand_records.csv"));//6sec
//      flatFileItemReader.setResource(new FileSystemResource("src/main/resources/one_Lac_records.csv"));//22sec
//      flatFileItemReader.setResource(new FileSystemResource("src/main/resources/five_Lac_records.csv"));//1minsseven_Lac_records.csv
//      flatFileItemReader.setResource(new FileSystemResource("src/main/resources/seven_Lac_records.csv"));//1min50sec
   
      flatFileItemReader.setResource(new FileSystemResource("src/main/resources/ten_Lac_records.csv"));//2mins
//spring batch with no multithreading 10L records in 5mins 
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);//skipping header here
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setMaxItemCount(1000000);
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
        
//        Map< java.lang.Object, java.beans.PropertyEditor> ParseMap =new HashMap<>();
//        ParseMap.put(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MMM-yyyy"), false));
//        ParseMap.put(Double.class,new CustomNumberEditor( Double.class, new DecimalFormat("#####.000"),true));//Float.class, numberFormat, true
//
//        fieldSetMapper.setCustomEditors(ParseMap);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
    
    @Bean
    public Job multithreadedJob(JobBuilderFactory jobBuilderFactory, ItemWriter<MarginedRate> itemWriter) throws Exception {
        return jobBuilderFactory
                .get("Multithreaded JOB")
                .incrementer(new RunIdIncrementer())
                .flow(multithreadedManagerStep(null,itemWriter))
                .end()
                .build();
    }
    @Bean
    public Step multithreadedManagerStep(StepBuilderFactory stepBuilderFactory,ItemWriter<MarginedRate> itemWriter) throws Exception {
        return stepBuilderFactory
                .get("Multithreaded : Read -> Process -> Write ")
                .<MarginedRate,  MarginedRate>chunk(10000)
                .reader(itemReader())
//                .processor(multithreadedchProcessor())
                .writer(itemWriter)
                .taskExecutor(taskExecutor())
                .build();
    }
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(500);
//        executor.setMaxPoolSize(500);
//        executor.setQueueCapacity(500);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("MultiThreaded-");
        return executor;
    }


}


//https://www.youtube.com/watch?v=1XEX-u12i0A