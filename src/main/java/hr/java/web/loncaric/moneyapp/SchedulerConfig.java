package hr.java.web.loncaric.moneyapp;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class SchedulerConfig {
    @Bean
    public JobDetail testJobDetail() {
        return JobBuilder.newJob(StatsJob.class).withIdentity("statsJob").storeDurably().build();
    }

    @Bean
    public Trigger testJobTrigger() {
      //  SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
       // return TriggerBuilder.newTrigger().forJob(testJobDetail()).withIdentity("statsTrigger").withSchedule(scheduleBuilder).build();

        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(testJobDetail());
        factoryBean.setStartDelay(0L);
        factoryBean.setStartTime(new Date());
        factoryBean.setName("statsTrigger");
        factoryBean.setCronExpression("1 1 1 ? * 4#1 *");
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return factoryBean.getObject();
    }

//job svakog prvog cet u mjesecu

}
