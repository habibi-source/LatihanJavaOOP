import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
class Admin extends User {
    private String spesifikAtributAdmin;

    public Admin(Scanner masukkan) {

        System.out.println("masukkan username = ");
        setUsername(masukkan.nextLine());
        System.out.println("Masukkabn password = ");
        setPassword(masukkan.nextLine());

        System.out.println("Create data admin berhasil");
    }

    public void saveToDatabase(Connection connection) throws SQLException {
        String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, this.getPassword());
            pstmt.setString(2, this.getPassword());
            pstmt.executeUpdate();

            // Other methods specific to Admin (if needed)
        }

    }

    public String getSpesifikAtributAdmin() {
        return spesifikAtributAdmin;
    }

    public void setSpesifikAtributAdmin(String adminSpesifikAtribut) {
        this.spesifikAtributAdmin = adminSpesifikAtribut;

    }

    public void displayRole() {
        System.out.println("Saya adalah Admin");
    }

}

class User {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    void pendaftaran(Scanner masukkan) {

        System.out.println("Masukkan Username = ");
        setUsername(masukkan.nextLine());
        System.out.println("Masukkab Password = ");
        setPassword(masukkan.nextLine());
        System.out.println("username = " + getUsername() + ", password = " + getPassword());
        System.out.println("Data User berhasil di create");
    }

    public void saveToDatabase(Connection connection) throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, this.getUsername());
            pstmt.setString(2, this.getPassword());
            pstmt.executeUpdate();
        }
    }

    public void displayRole() {
        System.out.println("Saya adalah User");
    }

    public boolean login(Connection connection, String username, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/pendaftaran_sekolah";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Ganti dengan password MySQL Anda
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int pilihan;
   do {
        System.out.println("Menu:");
        System.out.println("1. Membuat akun baru");
        System.out.println("2. Login");
        System.out.println("0. Keluar");
        System.out.print("Masukkan pilihan: ");
        pilihan = input.nextInt();
        input.nextLine(); // Membersihkan newline dari buffer

        switch (pilihan) {
            case 1:
                // Membuat akun baru
                User user = new User();
                user.pendaftaran(input);
                break;
            case 2:
                // Login
                System.out.print("Masukkan username: ");
                String loginUsername = input.nextLine();
                System.out.print("Masukkan password: ");
                String loginPassword = input.nextLine();

                try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    User loginUser = new User();
                    if (loginUser.login(connection, loginUsername, loginPassword)) {
                        System.out.println("Login berhasil");
                        // Lakukan sesuatu setelah login berhasil
                    } else {
                        System.out.println("Login gagal, username atau password salah");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                System.out.println("Terima kasih. Sampai jumpa!");
                break;
            default:
                System.out.println("Pilihan tidak valid. Silakan masukkan pilihan yang benar.");
        }
    } while (pilihan != 0);
}

}
