package it.synclab.smartparking.batch;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

import it.synclab.smartparking.repository.model.Pm10;
import it.synclab.smartparking.repository.model.Pm25;
import it.synclab.smartparking.repository.model.Temperature;
import it.synclab.smartparking.repository.model.Humidity;
import it.synclab.smartparking.repository.Pm10Repository;
import it.synclab.smartparking.repository.Pm25Repository;
import it.synclab.smartparking.repository.TempRepository;
import it.synclab.smartparking.repository.HumidRepository;

@Configuration
@EnableBatchProcessing
public class EnvironmentalSensorsBatch 
{	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//REPOSITORIES
	@Autowired
	private TempRepository tempRepository;
	
	@Autowired
	private HumidRepository humidRepository;
	
	@Autowired
	private Pm10Repository pm10Repository;
	
	@Autowired
	private Pm25Repository pm25Repository;
	
	private List<SensorsData> sensorsDatas;
	
	@Value("${sensor.environmental.url}")
	private String dataUrl;
	
	//// READERS
	//
	
	@Bean
	public FlatFileItemReader<SensorsData> sensorsDatasReader()
	{
		sensorsDatas = new ArrayList<SensorsData>();
		FlatFileItemReader<SensorsData> itemReader = new FlatFileItemReader<>();
		try {
			itemReader.setResource(new UrlResource(dataUrl));
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		itemReader.setName("txtReader");
		itemReader.setLineMapper(lineMapper());
		
		return itemReader;
	}
	
	private LineMapper<SensorsData> lineMapper()
	{
		DefaultLineMapper<SensorsData> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(";");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("date", "time", "pm25value", "pm10value", "tempValue", "umidValue");
		
		BeanWrapperFieldSetMapper<SensorsData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(SensorsData.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	@Bean
	public ItemReader<Pm25> pm25Reader()
	{
		List<Pm25> pm25Data = new ArrayList<Pm25>();
		ItemReader<Pm25> itemReader = new Pm25Reader(sensorsDatas, pm25Data);
		
		return itemReader;
	}
	
	@Bean
	public ItemReader<Pm10> pm10Reader()
	{
		List<Pm10> pm10Data = new ArrayList<Pm10>();
		
		ItemReader<Pm10> itemReader = new Pm10Reader(sensorsDatas, pm10Data);
		
		return itemReader;
	}
	
	@Bean
	public ItemReader<Temperature> tempReader()
	{
		List<Temperature> tempData = new ArrayList<Temperature>();
		
		ItemReader<Temperature> itemReader = new TempReader(sensorsDatas, tempData);
		
		return itemReader;
	}
	
	@Bean
	public ItemReader<Humidity> humidReader()
	{
		List<Humidity> humidData = new ArrayList<Humidity>();
		
		ItemReader<Humidity> itemReader = new HumidReader(sensorsDatas, humidData);
		
		return itemReader;
	}
	
	//////////	WRITERS
	//
	@Bean
	public ItemWriter<SensorsData> sensorsDatasListWriter()
	{
		ItemWriter<SensorsData> itemWriter = new SensorsDatasListWriter(sensorsDatas);
 		return itemWriter;
	}
	
	@Bean
	public RepositoryItemWriter<Pm25> pm25Writer()
	{
		pm25Repository.deleteAll();
		RepositoryItemWriter<Pm25> writer = new RepositoryItemWriter<>();
		writer.setRepository(pm25Repository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public RepositoryItemWriter<Pm10> pm10Writer()
	{
		pm10Repository.deleteAll();
		RepositoryItemWriter<Pm10> writer = new RepositoryItemWriter<>();
		writer.setRepository(pm10Repository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public RepositoryItemWriter<Temperature> tempWriter()
	{
		tempRepository.deleteAll();
		RepositoryItemWriter<Temperature> writer = new RepositoryItemWriter<>();
		writer.setRepository(tempRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public RepositoryItemWriter<Humidity> humidWriter()
	{
		humidRepository.deleteAll();
		RepositoryItemWriter<Humidity> writer = new RepositoryItemWriter<>();
		writer.setRepository(humidRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	///////	STEPS
	//
	@Bean
	public Step sensorsDataStep()
	{
		return stepBuilderFactory.get("sensorsData-step")
				.<SensorsData,SensorsData>chunk(10)
				.reader(sensorsDatasReader())
				.writer(sensorsDatasListWriter())
				.build();
	}
	
	@Bean
	public Step pm25Step()
	{
		return stepBuilderFactory.get("pm25-step")
				.<Pm25,Pm25>chunk(10)
				.reader(pm25Reader())
				.writer(pm25Writer())
				.build();
	}
	@Bean
	public Step pm10Step()
	{
		return stepBuilderFactory.get("pm10-step")
				.<Pm10,Pm10>chunk(10)
				.reader(pm10Reader())
				.writer(pm10Writer())
				.build();
	}
	@Bean
	public Step tempStep()
	{
		return stepBuilderFactory.get("temp-step")
				.<Temperature,Temperature>chunk(10)
				.reader(tempReader())
				.writer(tempWriter())
				.build();
	}
	@Bean
	public Step humidStep()
	{
		return stepBuilderFactory.get("humid-step")
				.<Humidity,Humidity>chunk(10)
				.reader(humidReader())
				.writer(humidWriter())
				.build();
	}
	
	//////	JOB RUNNER
	//
	
	@Bean(name="envSensorBatchJob")
	public Job envSensorBatchJob()
	{
		return jobBuilderFactory.get("importDatiAmbientali")
				.incrementer(new RunIdIncrementer())
				.flow(sensorsDataStep())
				.next(pm25Step())
				.next(pm10Step())
				.next(tempStep())
				.next(humidStep())
				.end()
				.build();
	}
	
}
