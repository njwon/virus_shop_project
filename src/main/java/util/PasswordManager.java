package util;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordManager {
    public static String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String rawPassword, String dbStoredHash) {
        return BCrypt.checkpw(rawPassword, dbStoredHash);
    }
}
