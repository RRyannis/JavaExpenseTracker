package variousProjects.expenseTracker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal amount;
    private String description;
    private LocalDate date;

    public Expense( BigDecimal amount, String description){
        this.amount = amount;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
