package tutorapplication.others;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    private PasswordHasher() {
        throw new IllegalStateException("Utility class");
    }

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        }
        catch (Exception _) {
            return false;
        }
    }
}
