import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Scanner;
import java.security.MessageDigest;

public class Login {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter login: ");
        String login = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/Login", "postgres", "mother1978");

            // Hash the password using MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }

            String hashedPassword = sb.toString();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM test1 WHERE Login_user = ? AND Password_user = ?");
            ps.setString(1, login);
            ps.setString(2, hashedPassword);

            ResultSet result = ps.executeQuery();
            while (!result.next()) {
                System.out.println(hashedPassword);
                System.out.println("Login or Password successful");
                new Captcha();
                break;
            }
            if (result.next()) {
                System.out.println("Login or Password failed");
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
