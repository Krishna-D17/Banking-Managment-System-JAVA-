import java.sql.*;
import java.util.Scanner;

public class Installment_Manager {
    private Connection connection;
    private Scanner scanner;

    public Installment_Manager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Record an installment payment for a given loan
    public void recordInstallmentPayment(long loanId) {
        scanner.nextLine(); // Consume any newline
        System.out.print("Enter Installment Amount: ");
        double installmentAmount = scanner.nextDouble();
        scanner.nextLine();

        // Insert installment payment record into the LoanInstallments table
        String insertQuery = "INSERT INTO LoanInstallments (loanId, installmentAmount) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(insertQuery);
            ps.setLong(1, loanId);
            ps.setDouble(2, installmentAmount);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Installment payment of Rs." + installmentAmount + " recorded for Loan ID: " + loanId);
            } else {
                System.out.println("Failed to record installment payment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View the installment summary for a given loan
    public void viewInstallmentSummary(long loanId) {
        // Query the total amount repaid for the loan
        String query = "SELECT SUM(installmentAmount) AS totalRepaid FROM LoanInstallments WHERE loanId = ?";
        double totalRepaid = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRepaid = rs.getDouble("totalRepaid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retrieve loan details (principal, interest rate, and tenure) to calculate total payable amount
        String loanQuery = "SELECT principalAmount, interestRate, loanTenure FROM Loans WHERE loanId = ?";
        double principal = 0;
        double interestRate = 0;
        int tenure = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(loanQuery);
            ps.setLong(1, loanId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                principal = rs.getDouble("principalAmount");
                interestRate = rs.getDouble("interestRate");
                tenure = rs.getInt("loanTenure");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Calculate total payable amount using the Loan_Calculator utility class
        double totalPayable = Loan_Calculator.calculateTotalPayable(principal, interestRate, tenure);

        System.out.println("\n--- Installment Summary ---");
        System.out.println("Loan ID: " + loanId);
        System.out.printf("Total Amount to be Paid: Rs. %.2f\n" , totalPayable);
        System.out.printf("Total Amount Repaid: Rs. %.2f\n", totalRepaid);
        System.out.printf("Remaining Amount: Rs. %.2f\n", (totalPayable - totalRepaid));
    }
}