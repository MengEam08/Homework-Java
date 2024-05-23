package repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String id;
    private List<String> titles;
    private List<String> instructors;
    private List<String> requirements;
    private LocalDate startDate;

    public Course(String titles, String instructors, String requirements) {
        this.id = String.format("%04d", (int)(Math.random() * 9000) + 1000);
        this.titles = Arrays.stream(titles.split(",")).map(String::trim).collect(Collectors.toList());
        this.instructors = Arrays.stream(instructors.split(",")).map(String::trim).collect(Collectors.toList());
        this.requirements = Arrays.stream(requirements.split(",")).map(String::trim).collect(Collectors.toList());
        this.startDate = LocalDate.now();
    }
}