package hr.java.web.loncaric.moneyapp;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {
    @Bean
    public JobDetail testJobDetail() {
        return JobBuilder.newJob(StatsJob.class).withIdentity("statsJob").storeDurably().build();
    }

    @Bean
    public Trigger testJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
        return TriggerBuilder.newTrigger().forJob(testJobDetail()).withIdentity("statsTrigger").withSchedule(scheduleBuilder).build();
    }
}
