package it.synclab.smartparking.batch;

import java.net.MalformedURLException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import it.synclab.smartparking.repository.model.ParkingSensors;
import it.synclab.smartparking.repository.ParkingSensorsRepository;
import it.synclab.smartparking.repository.SensorsRepository;

@Configuration
@EnableBatchProcessing
public class SmartParkingBatch 
{	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private SensorsRepository sensorsRepository;
	
	@Autowired
	private ParkingSensorsRepository parkingSensorsRepository;
	
	@Value("${sensor.parking.url}")
	private String sensorsUrl;
	
	@Bean
	public ItemReader<ParkingSensorsInputData> reader()
	{		
		Jaxb2Marshaller markerMashaller = new Jaxb2Marshaller();
		markerMashaller.setClassesToBeBound(ParkingSensorsInputData.class);
		
		try {
			return new StaxEventItemReaderBuilder<ParkingSensorsInputData>()
					.name("smartCitySimReader")
					.resource(new UrlResource(sensorsUrl))
					.addFragmentRootElements("marker")
					.unmarshaller(markerMashaller)
					.build();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Bean
	public ItemProcessor<ParkingSensorsInputData,ParkingSensors> processor()
	{		
		ItemProcessor<ParkingSensorsInputData,ParkingSensors> processor = new ParkingSensorsProcessor();
		((ParkingSensorsProcessor) processor).setRepository(sensorsRepository);		
		return processor;
	}
	
	@Bean
	public RepositoryItemWriter<ParkingSensors> writer()
	{		
		RepositoryItemWriter<ParkingSensors> writer = new RepositoryItemWriter<ParkingSensors>();
		writer.setRepository(parkingSensorsRepository);
		writer.setMethodName("save");
		return writer;
	}	
	
	@Bean ItemReader<ParkingSensors> clearer()
	{
		ItemReader<ParkingSensors> clearer = new ParkingSensorsClearer();
		((ParkingSensorsClearer) clearer).setRepository(parkingSensorsRepository);
		
		return clearer;
	}
	
	@Bean
	public Step clearingStep()
	{
		return stepBuilderFactory.get("clearParkingSensorsStep")
				.<ParkingSensors,ParkingSensors>chunk(10)
				.reader(clearer())
				.writer(new NoOpWriter())
				.build();
	}
	
	@Bean
	public Step readingStep()
	{
		return stepBuilderFactory.get("xmlFileToDBStep")
				.<ParkingSensorsInputData,ParkingSensors>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean(name="smartParkingJob")
	public Job smartParkingJob()
	{
		return jobBuilderFactory.get("importDataParkingArea")
				.flow(clearingStep())
				.next(readingStep())
				.end()
				.build();
	}	
}
