package hr.java.web.loncaric.moneyapp.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SearchFormResult {
    @NotEmpty(message = "Naziv troška je obavezan")
    String name;
    @NotNull(message = "Minimalni iznos troška je obavezan")
    private Double priceMin;
    @NotNull(message = "Maksimalni iznos troška je obavezan")
    private Double priceMax;


    @NotNull(message = "Morate odabrati kategoriju troška")
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
