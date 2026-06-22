package tutorapplication.InMemory;

import tutorapplication.dao.LessonDAO;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonDAOInMemory implements LessonDAO {

    private static final List<Lesson> lessonsTable = new ArrayList<>();
    private static int idCounter = 1;

    @Override
    public boolean saveLesson(Lesson lesson) {
        if (lesson == null) {
            return false;
        }
        for (int i = 0; i < lessonsTable.size(); i++) {
            Lesson current = lessonsTable.get(i);
            if (current.isAvailable() && current.getTutorEmail().equalsIgnoreCase(lesson.getTutorEmail()) && current.getDate().equalsIgnoreCase(lesson.getDate()) && current.getTime().equalsIgnoreCase(lesson.getTime())) {
                return false;
            }
        }
        Lesson lessonToSave = new Lesson(idCounter++, lesson.getSubject(), lesson.getDate(), lesson.getTime(), lesson.getPrice(), lesson.getTutorEmail(), true);
        lessonsTable.add(lessonToSave);
        return true;
    }

    @Override
    public List<Lesson> findLessonsByFilters(String subject, String date, String timeSlot, Double maxPrice) throws LessonsNotFoundException {
        List<Lesson> lessons = new ArrayList<>();

        double finalPrice = java.util.Objects.requireNonNullElse(maxPrice, 999.99);

        for (int i = 0; i < lessonsTable.size(); i++) {
            Lesson l = lessonsTable.get(i);

            if (!l.isAvailable()) {
                continue;
            }
            if (subject != null && !subject.isEmpty() && !l.getSubject().equalsIgnoreCase(subject)) {
                continue;
            }
            if (date != null && !date.isEmpty() && !l.getDate().equalsIgnoreCase(date)) {
                continue;
            }
            if (timeSlot != null && !timeSlot.isEmpty() && !l.getTime().equalsIgnoreCase(timeSlot)) {
                continue;
            }
            if (l.getPrice() > finalPrice) {
                continue;
            }
            lessons.add(l);
        }
        if (lessons.isEmpty()) {
            throw new LessonsNotFoundException();
        }
        return lessons;
    }

    @Override
    public Lesson findLessonById(int lessonId) {
        for (int i = 0; i < lessonsTable.size(); i++) {
            Lesson l = lessonsTable.get(i);
            if (l.getId() == lessonId) {
                return l;
            }
        }
        return null;
    }

    @Override
    public void updateLessonStatus(Lesson lesson, boolean available) {
        if (lesson == null) return;
        for (int i = 0; i < lessonsTable.size(); i++) {
            Lesson l = lessonsTable.get(i);
            if (l.getTutorEmail().equalsIgnoreCase(lesson.getTutorEmail()) && l.getDate().equalsIgnoreCase(lesson.getDate()) && l.getTime().equalsIgnoreCase(lesson.getTime())) {
                l.setAvailable(available);
                break;
            }
        }
        lesson.setAvailable(available);
    }
}

