import java.sql.*;
import java.util.Scanner;

public class Banking_System {
    private static final String url = "jdbc:mysql://localhost:3306/java_p";
    private static final String username = "root";
    private static final String password = "@Krishna4545";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            return;
        }
        
        try (Connection connection = DriverManager.getConnection(url, username, password);      // The try block ensures that the resources declared within parentheses are automatically closed when the block exits, either normally or due to an exception.
             Scanner scanner = new Scanner(System.in)) {
            
            // Instantiate service classes
            Account_Holder user = new Account_Holder(connection, scanner);
            Account_Repository accounts = new Account_Repository(connection, scanner);
            Transaction_Manager accountManager = new Transaction_Manager(connection, scanner);
            Loan_Manager loanManager = new Loan_Manager(connection, scanner);
            Installment_Manager installmentManager = new Installment_Manager(connection, scanner);
            
            String email;
            long accountNumber;
            
            while (true) {
                System.out.println("\n*** WELCOME TO BANKING SYSTEM ***");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice1 = scanner.nextInt();
                
                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println("\nUser Logged In!");
                            
                            // Check if user has an account, if not create one.
                            if (!accounts.account_exist(email)) {
                                System.out.println("\nNo bank account found.");
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit to Main Menu");
                                int accChoice = scanner.nextInt();
                                if (accChoice == 1) {
                                    accountNumber = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + accountNumber);
                                } else {
                                    break;
                                }
                            }
                            // Retrieve account number after successful creation or if account exists
                            accountNumber = accounts.getAccount_number(email);
                            
                            int choice2 = 0;
                            while (choice2 != 7) {
                                System.out.println("\n--- Main Menu ---");
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Loan Management");
                                System.out.println("6. (Other Operations)");
                                System.out.println("7. Log Out");
                                System.out.print("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(accountNumber);
                                        break;
                                    case 2:
                                        accountManager.credit_money(accountNumber);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(accountNumber);
                                        break;
                                    case 4:
                                        accountManager.getBalance(accountNumber);
                                        break;
                                    case 5:
                                        // Loan Management Sub-Menu
                                        handleLoanManagement(loanManager, installmentManager, accountNumber, scanner);
                                        break;
                                    case 6:
                                        // Placeholder for any other operations
                                        System.out.println("Other operations coming soon.");
                                        break;
                                    case 7:
                                        System.out.println("Logging Out...");
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password!");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Sub-menu for Loan Management (modified to include installment options)
    private static void handleLoanManagement(Loan_Manager loanManager, Installment_Manager installmentManager, long accountNumber, Scanner scanner) {
        int loanChoice = 0;
        while (loanChoice != 7) {
            System.out.println("\n--- Loan Management Menu ---");
            System.out.println("1. Apply for a Loan");
            System.out.println("2. View Loan Details");
            System.out.println("3. Calculate EMI");
            System.out.println("4. Approve Loan (Admin Only)");
            System.out.println("5. Record Installment Payment");
            System.out.println("6. View Installment Summary");
            System.out.println("7. Return to Main Menu");
            System.out.print("Enter your choice: ");
            loanChoice = scanner.nextInt();
            
            switch (loanChoice) {
                case 1:
                    loanManager.applyForLoan(accountNumber);
                    break;
                case 2:
                    loanManager.viewLoanDetails(accountNumber);
                    break;
                case 3:
                    // EMI Calculation - prompting user for parameters
                    System.out.print("Enter Principal Amount: ");
                    double principal = scanner.nextDouble();
                    System.out.print("Enter Annual Interest Rate (in %): ");
                    double rate = scanner.nextDouble();
                    System.out.print("Enter Tenure (in months): ");
                    int tenure = scanner.nextInt();
                    double emi = Loan_Calculator.calculateEMI(principal, rate, tenure);
                    double total = Loan_Calculator.calculateTotalPayable(principal, rate, tenure);
                    System.out.printf("Calculated EMI: %.2f\n", emi);
                    System.out.printf("Total Payable Amount: %.2f\n", total);
                    break;
                case 4:
                    // Simulate loan approval (admin functionality)
                    System.out.print("Enter Loan ID to approve: ");
                    long loanId = scanner.nextLong();
                    loanManager.approveLoan(loanId);
                    break;
                case 5:
                    // Record Installment Payment
                    System.out.print("Enter Loan ID for installment payment: ");
                    long loanForInstallment = scanner.nextLong();
                    installmentManager.recordInstallmentPayment(loanForInstallment);
                    break;
                case 6:
                    // View Installment Summary
                    System.out.print("Enter Loan ID to view installment summary: ");
                    long loanToView = scanner.nextLong();
                    installmentManager.viewInstallmentSummary(loanToView);
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Enter Valid Choice!");
                    break;
            }
        }
    }
}
