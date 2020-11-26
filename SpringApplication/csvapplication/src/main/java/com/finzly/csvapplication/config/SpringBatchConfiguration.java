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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.finzly.csvapplication.model.MarginedRate;
/** 
 * This class is used to set configuration for spring batch.It defines Job, Step, Task executor and Reader component of the step.
 * 
 * @author Sharayu Yadav.
 */

@EnableConfigServer
@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {
	
	   public static final String JOB_NAME = "Multithreaded JOB";
	   
	   @Bean
	    public Job multithreadedJob(JobBuilderFactory jobBuilderFactory, ItemWriter<MarginedRate> itemWriter) throws Exception {
	        return jobBuilderFactory
	                .get(JOB_NAME)
	                .incrementer(new RunIdIncrementer())
	                .flow(multithreadedManagerStep(null,itemWriter))
	                .end()
	                .build();
	    }
	   
	    @Value("${chunkSize}") 
		public int chunkSize;
	    
	    public static final String Step_NAME = "Multithreaded : Read -> Process -> Write";
	    
	    @Bean
	    public Step multithreadedManagerStep(StepBuilderFactory stepBuilderFactory,ItemWriter<MarginedRate> itemWriter) throws Exception {
	        return stepBuilderFactory
	                .get(Step_NAME)
	                .<MarginedRate,  MarginedRate>chunk(chunkSize)
	                .reader(itemReader())
//	                .processor(multithreadedchProcessor())
	                .writer(itemWriter)
	                .taskExecutor(taskExecutor())
	                .build();
	    }
	    
	    @Value("${corePoolSize}") 
		public int corePoolSize;
		@Value("${maximumPoolSize}") 
		public   int maximumPoolSize;
		@Value("${queueCapacity}") 
		public  int queueCapacity;
	    
	    @Bean
	    public TaskExecutor taskExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(corePoolSize);
	        executor.setMaxPoolSize(maximumPoolSize);
	        executor.setQueueCapacity(queueCapacity);
	        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	        executor.setThreadNamePrefix("MultiThreaded-");
	        return executor;
	    }
	
	@Value("${csvResourceFileName}") 
	public String csvResourceFileName;
	
	@Value("${maximumItemCount}") 
	public   int maximumItemCount;
	@Value("${numberOfLinesToSkip}") 
	public  int numberOfLinesToSkip;
	  
	public static final String CSV_NAME = "CSV-Reader";
    @Bean
    public FlatFileItemReader<MarginedRate> itemReader() {

    	
        FlatFileItemReader<MarginedRate> flatFileItemReader = new FlatFileItemReader<>();
     
        flatFileItemReader.setResource(new FileSystemResource(csvResourceFileName));

        flatFileItemReader.setName(CSV_NAME);
        flatFileItemReader.setLinesToSkip(numberOfLinesToSkip);
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setMaxItemCount(maximumItemCount);
        
        return flatFileItemReader;
    }

    @Value("${delimiter}") 
	public  String delimiter;
    @Value("${fieldNames}") 
	public  String[] fieldNames;
    
    @Bean
    public LineMapper<MarginedRate> lineMapper() {

        DefaultLineMapper<MarginedRate> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(delimiter);
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(fieldNames);
        
        BeanWrapperFieldSetMapper<MarginedRate> fieldSetMapper = new BeanWrapperFieldSetMapper<>();        
        fieldSetMapper.setTargetType(MarginedRate.class);
        
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }
  
}

//lineTokenizer.setNames(new String[] { "DATE", "CURRENCY_NAME", "CURRENCY_CODE","TERMS","INTERNAL_SPOT_RATE","BUY_RATE","SELL_RATE","BOSS_RATE","CUTOFF_TIME" });

//Map< java.lang.Object, java.beans.PropertyEditor> ParseMap =new HashMap<>();
//ParseMap.put(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MMM-yyyy"), false));
//ParseMap.put(Double.class,new CustomNumberEditor( Double.class, new DecimalFormat("#####.000"),true));//Float.class, numberFormat, true
//
//fieldSetMapper.setCustomEditors(ParseMap);
//https://www.youtube.com/watch?v=1XEX-u12i0A