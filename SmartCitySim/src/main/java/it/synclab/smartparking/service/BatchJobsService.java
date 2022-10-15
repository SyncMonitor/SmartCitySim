package it.synclab.smartparking.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class BatchJobsService 
{
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("envSensorBatchJob")
	private Job envSensorBatchJob;
	
	@Autowired
	@Qualifier("smartParkingJob")
	private Job smartParkingJob;
	
	@Scheduled(cron="${polling.timer}")
	public void importEnvironmentalDataToDBJob()
	{
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("startAt", System.currentTimeMillis()).toJobParameters();
		
		try {			
			JobExecution smartParkingjobExecution = jobLauncher.run(envSensorBatchJob, jobParameters);
			System.out.println("Job's Status:::"+smartParkingjobExecution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron="${polling.timer}")
	public void importParkingDataToDBJob()
	{
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("startAt", System.currentTimeMillis()).toJobParameters();
		
		try {			
			JobExecution smartParkingjobExecution = jobLauncher.run(smartParkingJob, jobParameters);
			System.out.println("Job's Status:::"+smartParkingjobExecution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
