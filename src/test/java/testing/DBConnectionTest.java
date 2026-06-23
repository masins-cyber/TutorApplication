package testing;

import tutorapplication.others.Connect;
import org.junit.jupiter.api.Test;
import tutorapplication.others.Print;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

class DBConnectionTest {

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
                Print.println("Database Test: OK (Connection successfully established via Connect Singleton)");
            }
        }
        catch (Exception e) {
            Print.errorPrint("Error during the database connection test: " + e.getMessage());
            fail("The test failed due to an exception");
        }
    }
}
