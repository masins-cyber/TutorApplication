package tutorapplication.others;

import tutorapplication.inmemory.BookingDAOInMemory;
import tutorapplication.inmemory.LessonDAOInMemory;
import tutorapplication.inmemory.StudentDAOInMemory;
import tutorapplication.inmemory.TutorDAOInMemory;
import tutorapplication.dao.*;
import tutorapplication.json.StudentDAOJSON;
import tutorapplication.json.TutorDAOJSON;
import tutorapplication.mysql.BookingDAOMYSQL;
import tutorapplication.mysql.LessonDAOMYSQL;
import tutorapplication.mysql.StudentDAOMYSQL;
import tutorapplication.mysql.TutorDAOMYSQL;

public class FactoryDAO {
    private static final String MYSQL = "mysql";
    private static final String MEMORY = "memory";
    private static final String JSON = "json";

    private FactoryDAO() {}

    public static StudentDAO getStudentDAO() {
        String daotype = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daotype)) {
            return new StudentDAOMYSQL();
        }
        else if (JSON.equalsIgnoreCase(daotype)) {
            return new StudentDAOJSON();
        }
        else if (MEMORY.equalsIgnoreCase(daotype)) {
            return new StudentDAOInMemory();
        }
        return null;
    }

    public static TutorDAO getTutorDAO() {
        String daotype = Config.getPersistenceType();
        if (MYSQL.equalsIgnoreCase(daotype)) {
            return new TutorDAOMYSQL();
        }
        else if (JSON.equalsIgnoreCase(daotype)) {
            return new TutorDAOJSON();
        }
        else if (MEMORY.equalsIgnoreCase(daotype)) {
            return new TutorDAOInMemory();
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

