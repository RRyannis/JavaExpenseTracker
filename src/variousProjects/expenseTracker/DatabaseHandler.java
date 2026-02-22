package variousProjects.expenseTracker;
// TODO: Integrate with DatabaseHandler.deleteExpense()
import java.math.BigDecimal;
import java.sql.* ;
import java.util.ArrayList;
import java.time.LocalDate;

public class DatabaseHandler {
    private static final String URL = "jdbc:sqlite:expenses.db";

    public static void initializeDatabase(){
        String sql = "CREATE TABLE IF NOT EXISTS expenses ("
                   + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " amount REAL NOT NULL,"
                   + " description TEXT NOT NULL,"
                   + " date TEXT NOT NULL"
                   + ");";

        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Database initialized and table is ready.");
        }
        catch(SQLException e) {
            System.out.println("❌ Database init error: " + e.getMessage());
        }
    }
    public static ArrayList<Expense> getAllExpenses(){
        String sql = "SELECT* FROM expenses";
        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            ArrayList<Expense> list = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                BigDecimal amount = BigDecimal.valueOf(rs.getDouble("amount"));
                String description = rs.getString("description");
                LocalDate date = Date.valueOf(rs.getString("date")).toLocalDate();
                Expense expense = new Expense(id,amount, description, date);
                list.add(expense);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("Fetching error.");
            return null;
        }
    }
    public static void addExpenseToDatabase(Expense expense) {
        String sql = "INSERT INTO expenses(amount, description, date) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //PreparedStatement to prevent "SQL Injection"
            pstmt.setBigDecimal(1, expense.getAmount());
            pstmt.setString(2, expense.getDescription());
            pstmt.setString(3, expense.getDate().toString()); // Stores as YYYY-MM-DD

            pstmt.executeUpdate();
            System.out.println("✅ Expense saved to database!");

        } catch (SQLException e) {
            System.out.println("❌ Insert error: " + e.getMessage());
        }
    }
    public static void deleteExpenseFromDatabase(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Expense deleted!");
            } else {
                System.out.println("No rows deleted.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Deletion error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //initializeDatabase();
        getAllExpenses();
    }
}
