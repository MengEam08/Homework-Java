package service;
import exception.Handling;
import model.OptionList;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.nocrala.tools.texttablefmt.*;
import repository.Course;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class ServiceImp implements OptionList {
    public static class CourseManager {
        private static final String COURSE_FILE = "course.csv";
        private static final Random RANDOM = new Random();
        public CourseManager() throws IOException {
            createCourseFileIfNotExists();
        }
        private void createCourseFileIfNotExists() throws IOException {
            File courseFile = new File(COURSE_FILE);
            if (!courseFile.exists()) {
                try (PrintWriter writer = new PrintWriter(courseFile)) {
                    writer.println("id,title,instructor,requirements,start_date");
                }
            }
        }
    }

    @Override
    public void addCourse() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter course title (separated by commas): ");
        String title = scanner.nextLine();

        System.out.print("Enter instructor names (separated by commas): ");
        String instructors = scanner.nextLine();

        System.out.print("Enter course requirements (separated by commas): ");
        String requirements = scanner.nextLine();

        String startDate = generateRandomDate(); // Now implemented

        try {
            Course course = new Course(title, instructors, requirements);
            FileWriter writer = new FileWriter(CourseManager.COURSE_FILE, true); // Append mode
            writer.append(course.getId() + "," + String.join(",", course.getTitles())
                    + "," + String.join(",", course.getInstructors()) + ","
                    + String.join(",", course.getRequirements()) + "," + course.getStartDate() + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    @Override
    public void listCourse() {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        table.addCell("ID");
        table.addCell("Course Title");
        table.addCell("Instructor Name");
        table.addCell("Course Requirements");
        table.addCell("Start Date");

        try (BufferedReader reader = new BufferedReader(new FileReader("course.csv"))) {
            String csvDelimiter = ",";
            String line;
            while ((line = reader.readLine())!= null) {
                String[] courseData = line.split(csvDelimiter);
                try {
                    int id = Integer.parseInt(courseData[0].trim());
                    table.addCell(String.valueOf(id));
                    table.addCell(courseData[1]);
                    table.addCell(courseData[2]);
                    table.addCell(courseData[3]);
                    if (courseData.length > 4 &&!courseData[4].isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate date = LocalDate.parse(courseData[4], formatter);
                        table.addCell(date.toString());
                    } else {
                        table.addCell("");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error parsing ID: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }

        System.out.println(table.render());
    }

    private String generateRandomDate() {
        int year = LocalDate.now().getYear();
        int month = CourseManager.RANDOM.nextInt(12) + 1;
        int day = CourseManager.RANDOM.nextInt(28) + 1;
        int hour = CourseManager.RANDOM.nextInt(24);
        int minute = CourseManager.RANDOM.nextInt(60);
        int second = CourseManager.RANDOM.nextInt(60);
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }


    @Override
    public void findCourseByTitle(String title) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("course.csv"), StandardCharsets.UTF_8);
            Optional<String> courseLineOptional = lines.stream()
                    .filter(line -> line.contains(title)) // Filter lines containing the title
                    .findFirst();
            if (courseLineOptional.isPresent()) {
                System.out.println("Found course:");
                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
                table.addCell("ID");
                table.addCell("Course Title");
                table.addCell("Instructor Name");
                table.addCell("Course Requirements");
                table.addCell("Start Date");
                String[] parts = courseLineOptional.get().split(",");
                table.addCell(parts[0]);
                table.addCell(parts[1]);
                table.addCell(parts[2]);
                table.addCell(parts[3]);
                table.addCell(parts[4]);
                System.out.println(table.render());
            } else {
                throw new NoSuchElementException("Course not found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    @Override
    public void findCourseById(String id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("course.csv"), StandardCharsets.UTF_8);
            Optional<String> courseLineOptional = lines.stream()
                    .filter(line -> line.startsWith(id)) // Filter lines starting with the given ID
                    .findFirst();
            if (courseLineOptional.isPresent()) {
                System.out.println("Found course:");
                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
                table.addCell("ID");
                table.addCell("Course Title");
                table.addCell("Instructor Name");
                table.addCell("Course Requirements");
                table.addCell("Start Date");
                String[] parts = courseLineOptional.get().split(",");
                table.addCell(parts[0]);
                table.addCell(parts[1]);
                table.addCell(parts[2]);
                table.addCell(parts[3]);
                table.addCell(parts[4]);
                System.out.println(table.render());
            } else {
                throw new Handling.CourseNotFoundException("Course not found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    @Override
    public void removeCourseById(String id) {
        try {
            Path tempFile = Files.createTempFile("temp", ".csv");
            BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8);
            Stream<String> stream = Files.lines(Paths.get(CourseManager.COURSE_FILE));

            stream.filter(line ->!line.startsWith(id)).forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();

            Files.deleteIfExists(Paths.get(CourseManager.COURSE_FILE));
            Files.move(tempFile, Paths.get(CourseManager.COURSE_FILE), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            System.err.println("Error removing course: " + e.getMessage());
        }
    }





    private void saveUpdatedCourses(List<String[]> updatedCourses) throws IOException {
        try (PrintWriter writer = new PrintWriter(CourseManager.COURSE_FILE)) {
            for (String[] course : updatedCourses) {
                writer.println(String.join(",", course));
            }
        }
    }
}
