package variousProjects.expenseTracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpenseTrackerGUI  extends JFrame{
    private JTextField txtAmount;
    private JTextField txtDescription;
    private JTextField txtDate;
    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private ExpenseManager expenseManager;

    public ExpenseTrackerGUI() {
        expenseManager = new ExpenseManager();
        expenseManager.loadAllExpenses(); // Load saved expenses if exists

        setTitle("Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField(10);
        inputPanel.add(txtAmount);

        inputPanel.add(new JLabel("Description:"));
        txtDescription = new JTextField(15);
        inputPanel.add(txtDescription);

        inputPanel.add(new JLabel("Date:"));
        txtDate = new JTextField(15);
        inputPanel.add(txtDate);

        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnSummary = new JButton("Summary");

        inputPanel.add(btnAdd);
        inputPanel.add(btnEdit);
        inputPanel.add(btnDelete);
        inputPanel.add(btnSummary);

        tableModel = new DefaultTableModel(new Object[]{"Amount", "Description", "Date"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        expenseTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(expenseTable);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            addExpense();
            expenseManager.loadAllExpenses();
            refreshTable();
        });
        btnEdit.addActionListener(e -> {
            editExpense();
            expenseManager.loadAllExpenses();
            refreshTable();
        });
        btnDelete.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this expense?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            deleteExpense();
            expenseManager.loadAllExpenses();
            refreshTable();
        });
        btnSummary.addActionListener(e -> showSummary());

        refreshTable();
        setVisible(true);
    }

    private void addExpense() {
        String amountStr = txtAmount.getText().trim();
        String description = txtDescription.getText().trim();
        String dateStr = txtDate.getText().trim();


        if (amountStr.isEmpty() || description.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide amount, description and date.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            LocalDate date = LocalDate.parse(dateStr);
            expenseManager.addExpense(new Expense(amount, description, date));
            refreshTable();
            txtAmount.setText("");
            txtDescription.setText("");
            txtDate.setText(LocalDate.now().toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void editExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit.", "Edit Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Expense oldExpense = expenseManager.getExpenses().get(selectedRow);
        int id = oldExpense.getId();
        String amountStr = txtAmount.getText().trim();
        String description = txtDescription.getText().trim();
        String dateStr = txtDate.getText().trim();


        if (amountStr.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide amount and description.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            LocalDate date = LocalDate.parse(dateStr);
            Expense updatedExpense = new Expense(amount, description, date);
            updatedExpense.setId(id);
            DatabaseHandler.editExpenseInDatabase(updatedExpense);
            expenseManager.updateExpense(selectedRow,updatedExpense);
            refreshTable();
            txtAmount.setText("");
            txtDescription.setText("");
            txtDate.setText(LocalDate.now().toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException ex){
            JOptionPane.showMessageDialog(this, "Invalid date. Please use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Expense expenseToDelete = expenseManager.getExpenses().get(selectedRow);
        DatabaseHandler.deleteExpenseFromDatabase(expenseToDelete.getId());
        expenseManager.removeExpense(selectedRow);
        refreshTable();
        JOptionPane.showMessageDialog(this, "✅ Expense deleted from database!");
        System.out.println("ID to delete: " + expenseToDelete.getId());
//        int selectedRow = expenseTable.getSelectedRow();
//        if (selectedRow != -1) {
//            // Get the ID directly from the TABLE model instead of the ArrayList
//            // This assumes ID is a hidden column or column 0.
//            // If you don't have ID in the table, let's stick to the list check:
//
//            Expense expenseToDelete = expenseManager.getExpenses().get(selectedRow);
//
//            System.out.println("--- DELETION ATTEMPT ---");
//            System.out.println("Table Row Index: " + selectedRow);
//            System.out.println("Object ID from List: " + expenseToDelete.getId());
//            System.out.println("Object Desc from List: " + expenseToDelete.getDescription());
//
//            DatabaseHandler.deleteExpenseFromDatabase(expenseToDelete.getId());
//
//            // Check the database again right after
//            expenseManager.removeExpense(selectedRow);
//            refreshTable();
        //not deleting from db, to fix later
    }

    private void showSummary() {
        BigDecimal totalExpenses = expenseManager.getTotalExpenses();
        JOptionPane.showMessageDialog(this, "Total Expenses: " + totalExpenses, "Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Expense> expenses = expenseManager.getExpenses();
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{expense.getAmount(), expense.getDescription(), expense.getDate()});
        }
    }

    public static void main(String[] args) {
        try {
            // Set the modern Dark theme
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }

}
