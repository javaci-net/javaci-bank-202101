package net.javaci.bank202101.api.job;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QuartzInitializer {

	
	@Autowired
	private Scheduler scheduler;
	
	@PostConstruct
	private void init() {
		log.info("QuartzInitializer started");
		
		JobDetail job = JobBuilder.newJob().ofType(ExchangeRateJob.class)
				.withIdentity("myJob", "group1").build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("myTrigger", "group1")
				.startNow()
				//.withSchedule(CronScheduleBuilder.cronSchedule("0 0 7 ? * MON-FRI")) // Production da bu sekilde olacak
				.withSchedule( SimpleScheduleBuilder
						.simpleSchedule()
						.withIntervalInSeconds(500)
						.withRepeatCount(0))
				.build();

		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			log.error("Exception occured while scheduling exchange rate job", e);
		}

	}
}
