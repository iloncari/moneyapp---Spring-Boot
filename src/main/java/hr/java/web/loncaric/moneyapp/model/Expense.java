package hr.java.web.loncaric.moneyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "EXPENSE")
public class Expense {

    public enum Type {
        HRANA,
        PIĆE,
        NAJAM,
        REŽIJE,
        DUĆAN
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Naziv troška je obavezan")
    @Size(min = 3, max = 50, message = "Naziv troška mora imati između 3 i 50 znakova")
    private String name;

    @Column(name = "price")
    @NotNull(message = "Iznos troška je obavezan")
    private Double price;

    @Column(name = "type")
    @NotNull(message = "Morate odabrati kategoriju troška")
    @Enumerated(EnumType.STRING)
    private Type expenseType;

    @Column(name = "created_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "walletid")
    @JsonIgnoreProperties({"expenses", "user"})
    private Wallet wallet;

    public Expense() {
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Type getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(Type expenseType) {
        this.expenseType = expenseType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
