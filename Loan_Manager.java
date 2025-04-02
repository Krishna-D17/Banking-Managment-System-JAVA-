import java.sql.*;
import java.util.Scanner;

public class Loan_Manager {
    private Connection connection;
    private Scanner scanner;

    public Loan_Manager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Method to apply for a loan
    public Loan applyForLoan(long accountNumber) {
        scanner.nextLine(); // Consume any pending newline

        System.out.print("Enter Loan Principal Amount: ");
        double principal = scanner.nextDouble();

        System.out.print("Enter Loan Tenure (in months): ");
        int tenure = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Loan Type (e.g., Student Loan, Property Loan, Auto Loan): ");
        String loanType = scanner.nextLine();

        System.out.print("Enter Collateral Details (if any, else type 'None'): ");
        String collateral = scanner.nextLine();

        // Retrieve default interest rate based on loan type.
        double interestRate = getInterestRateForLoanType(loanType);

        // Generate a new loan ID.
        long loanId = generateLoanId();

        Loan loan = new Loan(loanId, accountNumber, principal, interestRate, tenure, loanType, collateral, "Pending");

        // Insert the loan record into the database.
        String insertQuery = "INSERT INTO Loans (loanId, accountNumber, principalAmount, interestRate, loanTenure, loanType, collateral, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(insertQuery);
            ps.setLong(1, loanId);
            ps.setLong(2, accountNumber);
            ps.setDouble(3, principal);
            ps.setDouble(4, interestRate);
            ps.setInt(5, tenure);
            ps.setString(6, loanType);
            ps.setString(7, collateral);
            ps.setString(8, "Pending");
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Loan application submitted successfully with Loan ID: " + loanId);
            } else {
                System.out.println("Loan application failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loan;
    }

    // Simulated method to retrieve default interest rate based on loan type.
    private double getInterestRateForLoanType(String loanType) {
        // In a full implementation, you might query a LoanCategory table.
        if (loanType.equalsIgnoreCase("Student Loan")) {
            return 5.5;
        } else if (loanType.equalsIgnoreCase("Property Loan")) {
            return 7.0;
        } else if (loanType.equalsIgnoreCase("Auto Loan")) {
            return 6.5;
        }
        // Default rate if type is not recognized.
        return 8.0;
    }

    // Simulated method to generate a unique loan ID.
    private long generateLoanId() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT loanId FROM Loans ORDER BY loanId DESC LIMIT 1");
            if (rs.next()) {
                long lastLoanId = rs.getLong("loanId");
                return lastLoanId + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1000; // Starting ID if no loans exist
    }

    // Calculate total interest payable over the loan tenure.
    public double calculateTotalInterest(Loan loan) {
        // Simple interest calculation: Principal x (Rate/100) x (Time in years)
        double years = loan.getLoanTenure() / 12.0;
        return loan.getPrincipalAmount() * (loan.getInterestRate() / 100) * years;
    }

    // Approve a loan by updating its status.
    public void approveLoan(long loanId) {
        String updateQuery = "UPDATE Loans SET status = ? WHERE loanId = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(updateQuery);
            ps.setString(1, "Approved");
            ps.setLong(2, loanId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Loan with ID " + loanId + " approved.");
            } else {
                System.out.println("Loan approval failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View all loan details for a given account number.
    public void viewLoanDetails(long accountNumber) {
        String query = "SELECT * FROM Loans WHERE accountNumber = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Loan ID: " + rs.getLong("loanId"));
                System.out.println("Principal: " + rs.getDouble("principalAmount"));
                System.out.println("Interest Rate: " + rs.getDouble("interestRate") + "%");
                System.out.println("Tenure (months): " + rs.getInt("loanTenure"));
                System.out.println("Loan Type: " + rs.getString("loanType"));
                System.out.println("Collateral: " + rs.getString("collateral"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("--------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Optional: Validate the collateral details (this is a dummy implementation).
    // public boolean validateCollateral(Loan loan) {
    //     // For demonstration: if collateral is not "None", assume it is valid.
    //     return !loan.getCollateral().equalsIgnoreCase("None");
    // }
}
