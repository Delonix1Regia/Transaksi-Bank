import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Transaksi {
    String kode;
    String nama;
    String jenis;
    String tanggal;
    int jumlah;

    public Transaksi(String kode, String nama, String jenis, String tanggal, int jumlah) {
        this.kode = kode;
        this.nama = nama;
        this.jenis = jenis;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
    }
}

class BinaryTreeNode {
    Transaksi transaction;
    BinaryTreeNode left;
    BinaryTreeNode right;

    public BinaryTreeNode(Transaksi transaction) {
        this.transaction = transaction;
        this.left = null;
        this.right = null;
    }
}

class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class TransaksiBank {
    private static BinaryTreeNode root;
    private static Scanner scanner;
    private static User adminUser;
    private static User[] users;
    private static boolean isAdmin;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        root = null;
        isAdmin = false;
        
        System.out.println("╔════════ ❀•°❀°•❀ ════════╗");
        System.out.println("             Selamat datang!");
        System.out.println("╚════════ ❀•°❀°•❀ ════════╝");

        // Set username and password for admin
        adminUser = new User("admin", "adminpass");

        // Initialize user array
        users = new User[10];
        users[0] = new User("user1", "userpass1");
        users[1] = new User("user2", "userpass2");
        // Add more users as needed

        login();

        while (true) {
            if (isAdmin) {
                // Admin Menu
                System.out.println("-ˋˏ✄┈┈┈┈┈┈┈ Admin Menu ============");
                System.out.println("1. Tambah Transaksi");
                System.out.println("2. Tampilkan Transaksi");
                System.out.println("3. Cari Transaksi");
                System.out.println("4. Hapus Transaksi");
                System.out.println("5. Simpan Transaksi dalam File");
                System.out.println("6. Switch to User Mode");
                System.out.println("7. Logout"); // Tambahkan opsi logout
//                System.out.println("8. Exit");
            } else {
                // User Menu
                System.out.println("-ˋˏ✄┈┈┈┈┈┈┈ User Menu ============");
                System.out.println("1. Tambah Transaksi");
                System.out.println("2. Tampilkan Transaksi");
                System.out.println("3. Logout"); // Tambahkan opsi logout
//                System.out.println("4. Exit");
            }

            System.out.print("Masukkan Pilihan: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    insertBankTransaction();
                    break;
                case 2:
                    displayBankTransactions(root);
                    break;
                case 3:
                    if (isAdmin) {
                        searchBankTransaction();
                    } else {
                        isAdmin = false;
                        System.out.println("Log Out !");
                        login(); // Kembali ke proses login untuk user
                    }
                    break;
                case 4:
                    if (isAdmin) {
                        deleteBankTransaction();
                    } else {
                        System.out.println("Invalid choice for User Mode.");
                    }
                    break;
                case 5:
                    if (isAdmin) {
                        saveToFile();
                    } else {
                        System.out.println("Invalid choice for User Mode.");
                    }
                    break;
                case 6:
                    isAdmin = !isAdmin;
                    System.out.println(isAdmin ? "Switched to Admin Mode." : "Switched to User Mode.");
                    break;
                case 7: // Logout (Admin)
//                case 8: // Exit
//                    System.out.println("Exiting...");
//                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static boolean isAdminLogin(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(adminUser.username) && enteredPassword.equals(adminUser.password);
    }

    private static User userLogin(String enteredUsername, String enteredPassword) {
        for (User user : users) {
            if (user != null && enteredUsername.equals(user.username) && enteredPassword.equals(user.password)) {
                return user;
            }
        }
        return null;
    }

    private static void login() {
        System.out.print("Enter Username: ");
        String enteredUsername = scanner.next();
        System.out.print("Enter Password: ");
        String enteredPassword = scanner.next();

        if (isAdminLogin(enteredUsername, enteredPassword)) {
            isAdmin = true;
            System.out.println("Admin Login Successful!");
        } else {
            User currentUser = userLogin(enteredUsername, enteredPassword);
            if (currentUser != null) {
                System.out.println("User Login Successful!");
            } else {
                System.out.println("Invalid Username or Password. Exiting...");
                System.exit(0);
            }
        }
    }

    private static void insertBankTransaction() {
        System.out.print("Kode: ");
        String kode = scanner.next();
        System.out.print("Nama: ");
        String nama = scanner.next();
        System.out.print("Jenis Transaksi: ");
        String jenis = scanner.next();
        System.out.print("Tanggal Transaksi: ");
        scanner.nextLine();  // Membaca newline setelah jumlah untuk membersihkan buffer
        String tanggal = scanner.nextLine();
        System.out.print("Jumlah Transaksi: ");
        int jumlah = scanner.nextInt();


        Transaksi newTransaction = new Transaksi(kode, nama, jenis, tanggal, jumlah);
        root = insert(root, newTransaction);
        System.out.println("Bank Transaction added successfully!");
    }

    private static BinaryTreeNode insert(BinaryTreeNode root, Transaksi transaction) {
        if (root == null) {
            return new BinaryTreeNode(transaction);
        }

        if (transaction.kode.compareTo(root.transaction.kode) < 0) {
            root.left = insert(root.left, transaction);
        } else {
            root.right = insert(root.right, transaction);
        }

        return root;
    }

    private static void displayBankTransactions(BinaryTreeNode root) {
        System.out.println("============ All Transactions ============");
        displayInOrder(root);
    }

    private static void displayInOrder(BinaryTreeNode root) {
        if (root != null) {
            displayInOrder(root.left);
            System.out.println(root.transaction.kode + "\t" + root.transaction.nama + "\t" +
                    root.transaction.jenis + "\t" + root.transaction.tanggal + "\t" + root.transaction.jumlah);
            displayInOrder(root.right);
        }
    }

    private static void searchBankTransaction() {
        System.out.print("Enter Kode to search: ");
        String searchKode = scanner.next();
        BinaryTreeNode result = search(root, searchKode);
        if (result != null) {
            System.out.println("Transaction found:");
            System.out.println(result.transaction.kode + "\t" + result.transaction.nama + "\t" +
                    result.transaction.jenis + "\t" + result.transaction.tanggal + "\t" + result.transaction.jumlah);
        } else {
            System.out.println("Transaction not found.");
        }
    }

    private static BinaryTreeNode search(BinaryTreeNode root, String kode) {
        if (root == null || root.transaction.kode.equals(kode)) {
            return root;
        }

        if (kode.compareTo(root.transaction.kode) < 0) {
            return search(root.left, kode);
        } else {
            return search(root.right, kode);
        }
    }

    private static void deleteBankTransaction() {
        System.out.print("Enter Kode to delete: ");
        String deleteKode = scanner.next();
        root = delete(root, deleteKode);
        System.out.println("Transaction deleted successfully!");
    }

    private static BinaryTreeNode delete(BinaryTreeNode root, String kode) {
        if (root == null) {
            return root;
        }

        if (kode.compareTo(root.transaction.kode) < 0) {
            root.left = delete(root.left, kode);
        } else if (kode.compareTo(root.transaction.kode) > 0) {
            root.right = delete(root.right, kode);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            root.transaction.kode = minValue(root.right);
            root.right = delete(root.right, root.transaction.kode);
        }

        return root;
    }

    private static String minValue(BinaryTreeNode root) {
        String minValue = root.transaction.kode;
        while (root.left != null) {
            minValue = root.left.transaction.kode;
            root = root.left;
        }
        return minValue;
    }

    private static void saveToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("bank_transactions.txt"))) {
        writer.write("╔═══════════════════════════╗");
        writer.newLine();
        writer.write("                  Data Transaksi ");
        writer.newLine();
        writer.write("╚═══════════════════════════╝");
        writer.newLine();
//        writer.write("Kode:\tNama:\tJenis Transaksi:\tTanggal Transaksi:\tJumlah Transaksi:");
//        writer.newLine();
        System.out.println("");
        writer.write("══════════════════════════════");
        writer.newLine();
        saveToFile(root, writer);
        System.out.println("Bank Transactions saved to bank_transactions.txt");
    } catch (IOException e) {
        System.out.println("Error saving to file: " + e.getMessage());
    }
}

private static void saveToFile(BinaryTreeNode root, BufferedWriter writer) throws IOException {
    if (root != null) {
        saveToFile(root.left, writer);
        writer.write(root.transaction.kode + "\t" + root.transaction.nama + "\t" +
                root.transaction.jenis + "\t" + root.transaction.tanggal + "\t" + root.transaction.jumlah);
        writer.newLine();
        saveToFile(root.right, writer);
    }
}

}
