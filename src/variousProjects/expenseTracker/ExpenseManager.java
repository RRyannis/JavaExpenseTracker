package variousProjects.expenseTracker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private List<Expense> expenses;

    public ExpenseManager(){
        expenses = new ArrayList<>();
    }
    public void addExpense(Expense expense){
        expenses.add(expense);
    }
    public void removeExpense(int index){
        if (index >= 0 && index < expenses.size()){
            expenses.remove(index);
        }
    }
    public void updateExpense(int index, Expense expense){
        if (index >= 0 && index < expenses.size()){
            expenses.set(index, expense);
        }
    }

    public List<Expense>  getExpenses(){
        return new ArrayList<>(expenses);
    }
    public double getTotalExpenses(){
        //Note: refactor into big decimal later
        double total = 0;
        for (Expense expense: expenses){
            total += expense.getAmount();
        }
        return total;
    }
    public void saveToFile(String filename){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))){
            oos.writeObject(expenses);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))){
            expenses = (List<Expense>) ois.readObject();
        } catch (FileNotFoundException e){
            //no existing file
            expenses = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            expenses = new ArrayList<>();
        }
    }
}
