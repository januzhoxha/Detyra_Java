package Detyra_Bonus;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

class Transaction {
    public static final String DEPOZITIM = "Depozitim";
    public static final String TERHEQJE = "Terheqje";

    private final int reference;
    private final String type;
    private final double amount;

    public Transaction(int reference, String type, double amount) {
        this.reference = reference;
        this.type = type;
        this.amount = amount;
    }

    public int getReference() {
        return reference;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        String shenja = type.equals(DEPOZITIM) ? "+" : "-";
        return String.format(Locale.US, "Ref: %d | %-9s | %s%.2f EUR",
                reference, type, shenja, amount);
    }
}

class BankAccount {
    private static final Random RANDOM = new Random();

    private String ownerName = "";
    private String accountNumber = "";
    private double balance = 0;

    private Transaction[] transactions = new Transaction[10];
    private int transactionCount = 0;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public Transaction getTransaction(int index) {
        if (index < 0 || index >= transactionCount) {
            return null;
        }
        return transactions[index];
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        addTransaction(Transaction.DEPOZITIM, amount);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        addTransaction(Transaction.TERHEQJE, amount);
        return true;
    }

    private void addTransaction(String type, double amount) {
        if (transactionCount == transactions.length) {
            Transaction[] iRi = new Transaction[transactions.length * 2];
            for (int i = 0; i < transactions.length; i++) {
                iRi[i] = transactions[i];
            }
            transactions = iRi;
        }
        int reference = 100000 + RANDOM.nextInt(900000); // gjithmone 6 shifra
        transactions[transactionCount] = new Transaction(reference, type, amount);
        transactionCount++;
    }
}

public class Main {

    private static void printMenu() {
        System.out.println();
        System.out.println("======= BANK APP =======");
        System.out.println();
        System.out.println("1. Perditeso te dhenat e llogarise");
        System.out.println("2. Depozito para");
        System.out.println("3. Terhiq para");
        System.out.println("4. Shfaq detajet e llogarise");
        System.out.println("5. Shfaq historikun e transaksioneve");
        System.out.println("0. Dil nga aplikacioni");
        System.out.println();
        System.out.print("Zgjedh opsionin: ");
    }

    private static double readAmount(Scanner scanner, String mesazhi) {
        System.out.print(mesazhi);
        String line = scanner.nextLine().trim().replace(',', '.');
        try {
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void updateAccount(Scanner scanner, BankAccount account) {
        System.out.print("Emri dhe mbiemri i pronarit: ");
        String emri = scanner.nextLine().trim();
        System.out.print("Numri i llogarise: ");
        String numri = scanner.nextLine().trim();

        if (emri.isEmpty() || numri.isEmpty()) {
            System.out.println("Gabim: emri dhe numri i llogarise nuk mund te jene bosh.");
            return;
        }
        account.setOwnerName(emri);
        account.setAccountNumber(numri);
        System.out.println("Te dhenat u perditesuan me sukses.");
    }

    private static void showDetails(BankAccount account) {
        String emri = account.getOwnerName().isEmpty() ? "(pa vendosur)" : account.getOwnerName();
        String numri = account.getAccountNumber().isEmpty() ? "(pa vendosur)" : account.getAccountNumber();

        System.out.println();
        System.out.println("======= ACCOUNT DETAILS =======");
        System.out.println("Pronari: " + emri);
        System.out.println("Numri i llogarise: " + numri);
        System.out.printf(Locale.US, "Bilanci aktual: %.2f EUR%n", account.getBalance());
        System.out.println("Total transaksione te suksesshme: " + account.getTransactionCount());
    }

    private static void showHistory(BankAccount account) {
        System.out.println();
        System.out.println("======= TRANSACTION HISTORY =======");
        System.out.println();
        if (account.getTransactionCount() == 0) {
            System.out.println("Nuk ka transaksione.");
        }
        for (int i = 0; i < account.getTransactionCount(); i++) {
            System.out.println(account.getTransaction(i));
        }
        System.out.println();
        System.out.println("-----------------------------------");
        System.out.printf(Locale.US, "Bilanci aktual: %.2f EUR%n", account.getBalance());
        System.out.println("Total transaksione: " + account.getTransactionCount());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankAccount account = new BankAccount();
        boolean running = true;

        while (running) {
            printMenu();
            String zgjedhja = scanner.nextLine().trim();

            switch (zgjedhja) {
                case "1":
                    updateAccount(scanner, account);
                    break;
                case "2": {
                    double shuma = readAmount(scanner, "Shuma per depozitim: ");
                    if (account.deposit(shuma)) {
                        System.out.println("Depozitimi u krye me sukses.");
                    } else {
                        System.out.println("Gabim: shuma duhet te jete numer me i madh se 0.");
                    }
                    break;
                }
                case "3": {
                    double shuma = readAmount(scanner, "Shuma per terheqje: ");
                    if (account.withdraw(shuma)) {
                        System.out.println("Terheqja u krye me sukses.");
                    } else {
                        System.out.println("Gabim: shuma nuk eshte e vlefshme ose tejkalon bilancin.");
                    }
                    break;
                }
                case "4":
                    showDetails(account);
                    break;
                case "5":
                    showHistory(account);
                    break;
                case "0":
                    System.out.println("Faleminderit, mirupafshim!");
                    running = false;
                    break;
                default:
                    System.out.println("Opsion i pavlefshem. Provo perseri.");
            }
        }
        scanner.close();
    }
}