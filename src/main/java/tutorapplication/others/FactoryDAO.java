package tutorapplication.others;

import tutorapplication.inmemory.BookingDAOInMemory;
import tutorapplication.inmemory.LessonDAOInMemory;
import tutorapplication.inmemory.UserDAOInMemory;
import tutorapplication.dao.*;

public class FactoryDAO {
    private static final String MYSQL = "mysql";
    private static final String MEMORY = "memory";
    private static final String JSON = "json";

    private FactoryDAO() {}

    public static UserDAO getUserDAO() {
        String daotype = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daotype)) {
            return new UserDAOMYSQL();
        }
        else if (JSON.equalsIgnoreCase(daotype)) {
            return new UserDAOJSON();
        }
        else if (MEMORY.equalsIgnoreCase(daotype)) {
            return new UserDAOInMemory();
        }
        return null;
    }

    public static LessonDAO getLessonDAO() {
        String daotype = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daotype)) {
            return new LessonDAOMYSQL();
        }
        else if (MEMORY.equalsIgnoreCase(daotype)) {
            return new LessonDAOInMemory();
        }
        else if (JSON.equalsIgnoreCase(daotype)) {
            return new LessonDAOInMemory();
        }
        return null;
    }

    public static BookingDAO getBookingDAO() {
        String daotype = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daotype)) {
            return new BookingDAOMYSQL();
        }
        else if (MEMORY.equalsIgnoreCase(daotype)) {
            return new BookingDAOInMemory();
        }
        else if (JSON.equalsIgnoreCase(daotype)) {
            return new BookingDAOInMemory();
        }
        return null;
    }
}

