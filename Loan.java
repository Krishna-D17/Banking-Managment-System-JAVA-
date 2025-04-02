public class Loan {
    private long loanId;
    private long accountNumber;
    private double principalAmount;
    private double interestRate;
    private int loanTenure; // in months
    private String loanType;
    private String collateral;
    private String status; // e.g., "Pending", "Approved", "Rejected", "Closed"

    public Loan(long loanId, long accountNumber, double principalAmount, double interestRate, int loanTenure, String loanType, String collateral, String status) {
        this.loanId = loanId;
        this.accountNumber = accountNumber;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.loanTenure = loanTenure;
        this.loanType = loanType;
        this.collateral = collateral;
        this.status = status;
    }

    // Getters and Setters
    public long getLoanId() {
        return loanId;
    }
    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }
    public long getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }
    public double getPrincipalAmount() {
        return principalAmount;
    }
    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }
    public double getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    public int getLoanTenure() {
        return loanTenure;
    }
    public void setLoanTenure(int loanTenure) {
        this.loanTenure = loanTenure;
    }
    public String getLoanType() {
        return loanType;
    }
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
    public String getCollateral() {
        return collateral;
    }
    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // Display loan details
    public void displayLoanDetails() {
        System.out.println("Loan ID: " + loanId);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Principal Amount: " + principalAmount);
        System.out.println("Interest Rate: " + interestRate + "%");
        System.out.println("Loan Tenure (months): " + loanTenure);
        System.out.println("Loan Type: " + loanType);
        System.out.println("Collateral: " + collateral);
        System.out.println("Status: " + status);
    }
}
