package tutorapplication.others;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    private static final String CONFIG_FILE = "src/main/resources/resources/config.properties";
    private static final Properties properties = new Properties();
    private static String persistenceType;

    private Config() {
        throw new IllegalStateException("Utility class");
    }

    public static void loadFromFile() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            persistenceType = properties.getProperty("persistence.type", "mysql");
        }
        catch (IOException e) {
            logger.log(Level.SEVERE,"Error reading " + CONFIG_FILE + ", using default: mysql");
            persistenceType = "mysql";
        }
    }

    public static String getPersistenceType() {
        if (persistenceType == null) {
            loadFromFile();
        }
        return persistenceType;
    }

    public static void setPersistenceType(String type) {
        persistenceType = type;
    }
}
