package testing;

import tutorapplication.others.Connect; // Importiamo la tua nuova classe Connect
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

class DBConnectionTest {

    private static final Logger logger = Logger.getLogger(DBConnectionTest.class.getName());

    @Test
    void testConnection() {
        try {
            Connect connectInstance = Connect.getInstance();
            assertNotNull(connectInstance, "Connect.getInstance() returned null. Check Singleton initialization.");

            Connection conn = connectInstance.getDBConnection();
            assertNotNull(conn, "The connection is null. Verify if 'connection.properties' file exists and credentials are correct.");
            assertFalse(conn.isClosed(), "The connection to MySQL should be open.");

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT 1")) {
                assertTrue(rs.next(), "The ResultSet should have at least one result.");

                logger.log(Level.INFO, "Database Test: OK (Connection successfully established via Connect Singleton)");
            }
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error during the database connection test: " + e.getMessage(), e);
            fail("The test failed due to an exception: " + e.getMessage());
        }
    }
}
