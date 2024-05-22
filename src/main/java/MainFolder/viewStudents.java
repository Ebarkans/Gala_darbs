package MainFolder;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.Comparator;

@Service
public class viewStudents {

    // public static void main(String[] args) {
    //     List<Students> users = viewAllUsers();
    //     searchUsersByEmail(users, null);
    //     sortUsersByName(users);
    //     sortUsersByRole(users);
    //     // Now you can do something with the sorted list of users
    // }

    public static List<Students> viewAllUsers() {
        List<Students> users = new ArrayList<>();
        try {
            File inputFile = new File("src/main/data/table.csv");
            try (Scanner scanner = new Scanner(inputFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(",");
                    if (fields.length > 6) {
                        Students user = new Students();
                        user.setName(fields[0]);
                        user.setSurname(fields[1]);
                        user.setEmail(fields[2]);
                        user.setPassword(fields[3]);
                        user.setDay(fields[4]);
                        user.setRole(Role.valueOf(fields[5]));
                        users.add(user);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return users;
    }

    public static void sortUsersByName(List<Students> users) {
        Collections.sort(users, new Comparator<Students>() {
            @Override
            public int compare(Students u1, Students u2) {
                return u1.getName().compareTo(u2.getName());
            }
        });
    }
    
    public static List<Students> searchUsersByEmail(List<Students> users, String email) {
        return users.stream()
            .filter(user -> email.equals(user.getEmail()))
            .collect(Collectors.toList());
    }
    
    public static void sortUsersByRole(List<Students> users) {
        Collections.sort(users, Comparator.comparing(student -> student.getRole().toString()));
    }
}