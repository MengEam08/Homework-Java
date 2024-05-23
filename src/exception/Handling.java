package exception;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Handling {
    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }
    }

    public static class NoCoursesAvailableException extends RuntimeException {
        public NoCoursesAvailableException(String message) {
            super(message);
        }
    }

    public static class InvalidChoiceException extends RuntimeException {
        public InvalidChoiceException(String message) {
            super(message);
        }
    }

}
