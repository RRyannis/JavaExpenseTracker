package variousProjects.expenseTracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
//tba profiles and sqlite
public class ExpenseTrackerGUI  extends JFrame{
    private JTextField txtAmount;
    private JTextField txtDescription;
    private JTextField txtDate;
    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private ExpenseManager expenseManager;

    public ExpenseTrackerGUI() {
        expenseManager = new ExpenseManager();
        expenseManager.loadFromFile("expenses.ser"); // Load saved expenses if exists

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
            expenseManager.saveToFile("expenses.ser"); // Save after add
        });
        btnEdit.addActionListener(e -> {
            editExpense();
            expenseManager.saveToFile("expenses.ser"); // Save after edit
        });
        btnDelete.addActionListener(e -> {
            deleteExpense();
            expenseManager.saveToFile("expenses.ser"); // Save after delete
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
            expenseManager.updateExpense(selectedRow, new Expense(amount, description, date));
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
        expenseManager.removeExpense(selectedRow);
        refreshTable();
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
