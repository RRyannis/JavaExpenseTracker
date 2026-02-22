package variousProjects.expenseTracker;

import javax.swing.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.swing.JOptionPane.*;

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    public Expense(){}

    public Expense( BigDecimal amount, String description, LocalDate date){
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Expense(int id, BigDecimal amount, String description, LocalDate date ){
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("Invalid entry");
            //showMessageDialog(this, "Amount must be positive!");
            return;
        }
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
