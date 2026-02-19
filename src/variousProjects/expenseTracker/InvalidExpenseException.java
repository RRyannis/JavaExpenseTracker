package variousProjects.expenseTracker;

/**
 * Custom exception to handle cases where expense data
 * doesn't meet our business logic requirements.
 */
public class InvalidExpenseException extends Exception {

    public InvalidExpenseException(String message) {
        super(message);
    }

    public InvalidExpenseException(String message, Throwable cause) {
        super(message, cause);
    }
    public  void printMessage(String message){
        System.out.println("Error: " + message);
    }
}