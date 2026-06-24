package tutorapplication.dao;

import tutorapplication.exception.LessonAlreadyInsertedException;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;

import java.util.List;

public interface LessonDAO {

    boolean saveLesson(Lesson lesson) throws LessonAlreadyInsertedException;

    List<Lesson> findLessonsByFilters(String subject, String day,  String timeSlot, Double maxPrice) throws LessonsNotFoundException;

    Lesson findLessonById(int id);

    void updateLessonStatus(Lesson lesson, boolean available);
}

