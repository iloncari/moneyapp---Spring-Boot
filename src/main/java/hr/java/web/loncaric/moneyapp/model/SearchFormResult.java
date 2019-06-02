package hr.java.web.loncaric.moneyapp.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SearchFormResult {
    @NotEmpty(message = "{validation.expense.name_empty}")
    String name;
    @NotNull(message = "{validaton.expense.price_min_error}")
    private Double priceMin;
    @NotNull(message = "{validaton.expense.price_max_error}")
    private Double priceMax;


    @NotNull(message = "{validation.expense.type_empty}")
    private Expense.Type type;

    public Expense.Type getType() {
        return type;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    public void setType(Expense.Type type) {
        this.type = type;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchFormResult(String name) {
        this.name = name;
    }

    public SearchFormResult() {
    }
}
