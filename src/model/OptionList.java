package model;

import java.util.List;

public interface OptionList {
    void addCourse();
    void listCourse();
    void findCourseByTitle(String title);
    void findCourseById(String id);
    void removeCourseById(String id);
}
