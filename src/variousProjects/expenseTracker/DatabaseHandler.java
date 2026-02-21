package variousProjects.expenseTracker;
// TODO: Integrate with DatabaseHandler.deleteExpense()
import java.sql.* ;
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
    public static void getAllExpenses(){
        String sql = "SELECT* FROM expenses";
        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            int rowsFetched = pstmt.executeUpdate();
            if (rowsFetched > 0){
                System.out.println("Rows fetched.");
            } else {
                System.out.println("No rows fetched.");
            }
        } catch (SQLException e) {
            System.out.println("Fetching error.");
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
        initializeDatabase();
    }
}
