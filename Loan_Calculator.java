public class Loan_Calculator {
    // Calculate EMI using the formula:
    // EMI = [P x R x (1+R)^N] / [(1+R)^N - 1]
    // P: Principal, R: monthly interest rate, N: number of months
    public static double calculateEMI(double principal, double annualInterestRate, int tenureMonths) {
        double monthlyRate = annualInterestRate / 1200; // Convert annual rate (%) to monthly fraction
        double numerator = principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths);
        double denominator = Math.pow(1 + monthlyRate, tenureMonths) - 1;
        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0;
    }

    // Calculate total payable amount (EMI * tenure)
    public static double calculateTotalPayable(double principal, double annualInterestRate, int tenureMonths) {
        double emi = calculateEMI(principal, annualInterestRate, tenureMonths);
        return emi * tenureMonths;
    }
}
