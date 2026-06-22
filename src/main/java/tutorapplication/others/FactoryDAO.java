package tutorapplication.others;

import tutorapplication.InMemory.BookingDAOInMemory;
import tutorapplication.InMemory.LessonDAOInMemory;
import tutorapplication.InMemory.UserDAOInMemory;
import tutorapplication.dao.*;

public class FactoryDAO {
    private static final String MYSQL = "mysql";
    private static final String MEMORY = "memory";
    private static final String JSON = "json";

    private FactoryDAO() {}

    public static UserDAO getUserDAO() {
        String DAOType = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(DAOType)) {
            return new UserDAOMYSQL();
        }
        else if (JSON.equalsIgnoreCase(DAOType)) {
            return new UserDAOJSON();
        }
        else if (MEMORY.equalsIgnoreCase(DAOType)) {
            return new UserDAOInMemory();
        }
        return null;
    }

    public static LessonDAO getLessonDAO() {
        String DAOType = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(DAOType)) {
            return new LessonDAOMYSQL();
        }
        else if (MEMORY.equalsIgnoreCase(DAOType)) {
            return new LessonDAOInMemory();
        }
        else if (JSON.equalsIgnoreCase(DAOType)) {
            return new LessonDAOInMemory();
        }
        return null;
    }

    public static BookingDAO getBookingDAO() {
        String DAOType = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(DAOType)) {
            return new BookingDAOMYSQL();
        }
        else if (MEMORY.equalsIgnoreCase(DAOType)) {
            return new BookingDAOInMemory();
        }
        else if (JSON.equalsIgnoreCase(DAOType)) {
            return new BookingDAOInMemory();
        }
        return null;
    }
}

