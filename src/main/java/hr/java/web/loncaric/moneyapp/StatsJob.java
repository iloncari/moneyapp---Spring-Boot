package hr.java.web.loncaric.moneyapp;

import hr.java.web.loncaric.moneyapp.controller.ExpenseController;
import hr.java.web.loncaric.moneyapp.model.Expense;
import hr.java.web.loncaric.moneyapp.repository.ExpenseRepository;
import org.hibernate.loader.plan.build.internal.LoadGraphLoadPlanBuildingStrategy;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class StatsJob extends QuartzJobBean {
    @Autowired
    ExpenseRepository expenseRepository;


    private Logger log = LoggerFactory.getLogger(ExpenseController.class);
    public StatsJob() {
        super();
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("****************************************************");
        log.info("      " + "\t\t\t" + "SUM" + "\t\t\t" + "MIN" + "\t\t\t" + "MAX");
        for(Expense.Type type: Expense.Type.values()){
            Double suma = 0.0;
            Double min = 0.0;
            Double max = 0.0;

            Expense maxExpense = expenseRepository.findFirstByExpenseTypeOrderByPriceDesc(type);
            if(maxExpense!=null) max = maxExpense.getPrice();

            Expense minExpense = expenseRepository.findFirstByExpenseTypeOrderByPriceAsc(type);
            if(minExpense!=null) min = minExpense.getPrice();

            for(Expense expense: expenseRepository.findAllByExpenseType(type))
                suma += expense.getPrice();

            log.info(type + "\t\t\t" + String.format("%.2f", suma) + "\t\t" + String.format("%.2f", min) + "\t\t" + String.format("%.2f", max));

        }

    }
}
