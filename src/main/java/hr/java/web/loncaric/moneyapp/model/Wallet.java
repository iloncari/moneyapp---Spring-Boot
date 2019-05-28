package hr.java.web.loncaric.moneyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "WALLET")
public class Wallet {

    public enum Type{
        KUNSKI,
        EURO,
        DOLAR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createDate;

    @Column(name = "name")
    @NotEmpty(message = "Ime novčanika je obvezno")
    private String name;

    @Column(name = "type")
    @NotNull(message = "Vrsta novčanika je obvezna")
    @Enumerated(EnumType.STRING)
    private Type walletType;

        @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    @JsonProperty("expenses")
    private List<Expense> expenses;

    @ManyToOne
    @JoinColumn(name="USERID")
    @JsonIgnoreProperties({"wallets", "password"})
    private User user;



    public Wallet(String name, Type type, List<Expense> expenses) {
        this.name = name;
        this.walletType = type;
        this.expenses = expenses;
    }

    public Wallet() {}



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getWalletType() {
        return walletType;
    }

    public void setWalletType(Type walletType) {
        this.walletType = walletType;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", name='" + name + '\'' +
                ", walletType=" + walletType +
                ", expenses=" + expenses +
                ", user=" + user +
                '}';
    }
}
