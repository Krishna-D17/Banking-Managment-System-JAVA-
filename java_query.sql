CREATE DATABASE if not exists java_p;

USE java_p;

CREATE TABLE if not exists user (
    id INT AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (email)
)AUTO_INCREMENT = 112315113;

CREATE TABLE if not exists accounts (
    account_number BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) NOT NULL,
    security_pin CHAR(4) NOT NULL,
    PRIMARY KEY (account_number),
    FOREIGN KEY (email) REFERENCES user(email)
);

CREATE TABLE if not exists Loans (
    loanId BIGINT PRIMARY KEY,
    accountNumber BIGINT NOT NULL,
    principalAmount DECIMAL(15,2) NOT NULL,
    interestRate DECIMAL(5,2) NOT NULL,
    loanTenure INT NOT NULL, -- tenure in months
    loanType VARCHAR(50) NOT NULL,
    collateral VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (accountNumber) REFERENCES accounts(account_number)
);

CREATE TABLE if not exists LoanInstallments (
    installmentId BIGINT PRIMARY KEY AUTO_INCREMENT,
    loanId BIGINT NOT NULL,
    installmentAmount DECIMAL(15,2) NOT NULL,
    installmentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (loanId) REFERENCES Loans(loanId)
);


SELECT user();
SELECT system_user();

SELECT @@port;

SELECT @@hostname;


select * from user;
select * from accounts;
select * from Loans;
select* from LoanInstallments;
describe user;
describe accounts;
describe Loans;
describe LoanInstallments;

