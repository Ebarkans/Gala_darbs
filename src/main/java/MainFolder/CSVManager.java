package MainFolder;

import java.io.*;
import java.util.Scanner;


public class CSVManager {
    private static final String FILE_PATH = "src/main/data/table.csv";

    public static boolean addStudentToCSV(String name, String surname, String email, String password, String day) throws IOException {
        Role role;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            // Check for empty values and handle appropriately
            name = name.isEmpty() ? "" : name;
            surname = surname.isEmpty() ? "" : surname;
            email = email.isEmpty() ? "" : email;
            password = password.isEmpty() ? "" : password;
            day = day.isEmpty() ? "" : day;

            if (email.equals("ebarkans3@edu.riga.lv")){
                role = Role.ADMIN;
            } else {
                role = Role.USER;
            }
    
            // Write the processed data to the CSV file
            writer.write(name + "," + surname + "," + email + "," + password + "," + day + ","+ role +"\n");
            return true;

        } catch (IOException e) {
            System.out.println("Error occurred while writing to the file." + e.getMessage());
            return false;
        }

    }
    // Pārbauda vai ievadītais ēpasts eksistē
    public static boolean emailExists(String email) {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                if (fields.length > 2 && fields[2].equals(email)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }

    // public static String Login(String email, String password){
    //     try {
    //         File inputFile = new File(FILE_PATH);
    //         try (Scanner scanner = new Scanner(inputFile)) {
    //             while (scanner.hasNextLine()) {
    //                 String line = scanner.nextLine();
    //                 String[] fields = line.split(",");
    //             }
    //         }
    //     }
                  
    //Get student by email and password
    public static Students getStudent(String email, String password){
        try {
            File inputFile = new File(FILE_PATH);
            try (Scanner scanner = new Scanner(inputFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(",");
                    // Assuming the email is in the third field and the password is in the fourth field
                    if (fields.length > 4 && fields[2].equals(email) && fields[3].equals(password)) {
                        // Create a new Students object with the data from the row
                        Students student = new Students();
                        student.setName(fields[0]);
                        student.setSurname(fields[1]);
                        student.setEmail(fields[2]);
                        student.setPassword(fields[3]);
                        student.setDay(fields[4]);
                        student.setRole(Role.valueOf(fields[5]));
    
                        // Return the Students object
                        return student;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return null;
    }

    public static void updateStudent(String email, String name, String surname, String newEmail, String password, String day, Role role) {
        try {
            File inputFile = new File(FILE_PATH);
            File tempFile = new File(FILE_PATH + ".tmp");
            try (Scanner scanner = new Scanner(inputFile);
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(",");
                    if (fields.length > 5 && fields[2].equals(email)) {
                        // Write the new email instead of the old email
                        writer.write(name + "," + surname + "," + newEmail + "," + password + "," + day + "," + role + "\n");
                    } else {
                        writer.write(line + "\n");
                    }
                }
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

    }

    public static void viewAllUsers() {
        try {
            File inputFile = new File(FILE_PATH);
            try (Scanner scanner = new Scanner(inputFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(",");
                    if (fields.length > 5) {
                        System.out.println("Name: " + fields[0] + ", Surname: " + fields[1] + ", Email: " + fields[2] + ", Password: " + fields[3] + ", Day: " + fields[4] + ", Role: " + fields[5]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static boolean deleteUser(String email) {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(FILE_PATH + ".tmp");
    
        boolean userDeleted = false;
    
        try (Scanner scanner = new Scanner(inputFile);
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");
    
                // Assuming the password is the second field in each line
                if (fields.length > 2 && !fields[2].equals(email)) {
                    writer.write(line + "\n");
                } else {
                    userDeleted = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return false;
        }
    
        if (userDeleted) {
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } else {
            tempFile.delete(); // Delete the temporary file if no user was deleted
        }
    
        return userDeleted;
    }
    
    public static void main(String[] args) {
        viewAllUsers();
    }

    
    public static void updateStudent(Students updatedStudent) {
        try {
            File inputFile = new File(FILE_PATH);
            File tempFile = new File("temp.csv");
    
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length > 4 && fields[2].equals(updatedStudent.getEmail()) && fields[3].equals(updatedStudent.getPassword())) {
                        // This is the line with the student to update, replace it with the updated student data
                        line = updatedStudent.getName() + "," + updatedStudent.getSurname() + "," + updatedStudent.getEmail() + "," + updatedStudent.getPassword() + "," + updatedStudent.getDay();
                    }
                    writer.write(line);
                    writer.newLine();
                }
            }
    
            // Delete the old file
            if (!inputFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }
    
            // Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inputFile))
                System.out.println("Could not rename file");
    
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


//     public Students getCurrentStudent(HttpServletRequest request) {
//     // Get the email of the current student from the session
//     String email = (String) request.getSession().getAttribute("email");

//     // If the email is null, then there's no current student
//     if (email == null) {
//         return null;
//     }

//     // Find the student in the CSV file
//     try {
//         File inputFile = new File(FILE_PATH);
//         try (Scanner scanner = new Scanner(inputFile)) {
//             while (scanner.hasNextLine()) {
//                 String line = scanner.nextLine();
//                 String[] fields = line.split(",");
//                 // Assuming the email is in the first field and the password is in the second field
//                 if (fields.length > 1 && fields[2].equals(email)) {
//                     Students student = new Students();
//                 }
//             }
//         }
//     } catch (FileNotFoundException e) {
//         System.out.println("Error reading the file: " + e.getMessage());
//     Students student = CSVManager.findStudentByEmail(email);

//     return student;
// }
// }
}