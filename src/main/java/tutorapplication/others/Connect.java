package tutorapplication.others;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connect {
    private static final Logger logger = Logger.getLogger(Connect.class.getName());

    private String jdbc;
    private String user;
    private String password;

    private static Connect instance = null;
    private Connection conn;
    private static final String PATH = "src/main/resources/resources/connection.properties";

    private Connect() {}

    public static synchronized Connect getInstance() {
        if (instance == null) {
            instance = new Connect();
        }
        return instance;
    }

    public synchronized Connection getDBConnection() {
        try {
            if (this.conn == null || this.conn.isClosed()) {
                getInfo();

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                }
                catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE,"MySQL JDBC Driver not found in classpath: ", e);
                }
                this.conn = DriverManager.getConnection(jdbc, user, password);
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE,"Error in Connect.java retrieving connection: %s", e);
        }
        return this.conn;
    }

    private void getInfo() {
        try (FileInputStream fileInputStream = new FileInputStream(PATH)) {
            Properties prop = new Properties();
            prop.load(fileInputStream);

            this.jdbc = prop.getProperty("JDBC_URL");
            this.user = prop.getProperty("USER");
            this.password = prop.getProperty("db.pkn_val");

        } catch (IOException e) {
            logger.log(Level.SEVERE,"Failed to load connection configuration file: ", e);
        }
    }
}
