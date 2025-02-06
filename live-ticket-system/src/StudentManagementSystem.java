package studentmanagement;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class StudentManagementSystem {
    private static final int CAPACITY = 100;//set Max capacity
    private static Student[] students = new Student[CAPACITY];
    private static int count = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = getValidIntegerInput(scanner);
            switch (choice) {
                case 1:
                    checkAvailableSeats();
                    break;
                case 2:
                    registerStudent(scanner);
                    break;
                case 3:
                    deleteStudent(scanner);
                    break;
                case 4:
                    findStudent(scanner);
                    break;
                case 5:
                    storeStudentDetails();
                    break;
                case 6:
                    loadStudentDetails();
                    break;
                case 7:
                    viewStudentList();
                    break;
                case 8:
                    openAdditionalControls(scanner);
                    break;
                case 9:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. Check available seats");
        System.out.println("2. Register student (with ID)");
        System.out.println("3. Delete student");
        System.out.println("4. Find student (with student ID)");
        System.out.println("5. Store student details into a file");
        System.out.println("6. Load student details from a file");
        System.out.println("7. View the list of students based on their names");
        System.out.println("8. Additional controls");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getValidIntegerInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // discard invalid input
        }
        return scanner.nextInt();
    }

    private static void checkAvailableSeats() {
        System.out.println("Available seats: " + (CAPACITY - count));//- available seat
    }

    private static void registerStudent(Scanner scanner) {
        if (count >= CAPACITY) {
            System.out.println("No available seats.");
            return;
        }

        System.out.print("Enter student ID (w followed by 7 digits): ");//get ID number
        String id = scanner.next();
        if (!isValidId(id)) {
            System.out.println("Invalid ID format. Must start with 'w' followed by 7 digits.");
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.next();

        students[count++] = new Student(id, name);
        System.out.println("Student registered successfully.");
    }

    private static boolean isValidId(String id) {
        return Pattern.matches("w\\d{7}", id);//check the ID number
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter student ID to delete: ");
        String id = scanner.next();

        for (int i = 0; i < count; i++) {
            if (students[i].getId().equals(id)) {
                students[i] = students[--count];
                students[count] = null;
                System.out.println("Student deleted successfully.");
                return;
            }
        }
        System.out.println("Student ID not found.");
    }

    private static void findStudent(Scanner scanner) {
        System.out.print("Enter student ID to find: ");
        String id = scanner.next();

        for (int i = 0; i < count; i++) {
            if (students[i].getId().equals(id)) {
                System.out.println("Student found: " + students[i]);
                return;
            }
        }
        System.out.println("Student ID not found.");
    }

    private static void storeStudentDetails() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            for (int i = 0; i < count; i++) {
                writer.print(students[i].getId() + "," + students[i].getName());
                for (int j = 0; j < 3; j++) {
                    Module module = students[i].getModule(j);
                    writer.print("," + (module != null ? module.getMark() : "NA"));
                }
                writer.println();
            }
            System.out.println("Student details stored successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while storing student details.");
            e.printStackTrace();
        }
    }

    private static void loadStudentDetails() {
        File file = new File("students.txt");//load old studnet details
        if (!file.exists()) {
            System.out.println("File not found. No student details to load.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String name = parts[1];
                Student student = new Student(id, name);
                for (int i = 0; i < 3; i++) {
                    if (!parts[i + 2].equals("NA")) {
                        student.setModuleMark(i, Integer.parseInt(parts[i + 2]));
                    }
                }
                students[count++] = student;
            }
            System.out.println("Student details loaded successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while loading student details.");
            e.printStackTrace();
        }
    }

    private static void viewStudentList() {
        Arrays.sort(students, 0, count, Comparator.comparing(Student::getName));
        for (int i = 0; i < count; i++) {
            System.out.println(students[i]);
        }
    }

    private static void openAdditionalControls(Scanner scanner) {
        while (true) {
            System.out.println("a. Add student name");
            System.out.println("b. Enter module marks 1, 2 and 3");
            System.out.println("c. Generate summary of the system");
            System.out.println("d. Generate complete report");
            System.out.println("e. Back to main menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.next();
            switch (choice) {
                case "a":
                    System.out.print("Enter student ID: ");
                    String id = scanner.next();
                    Student student = findStudentById(id);
                    if (student != null) {
                        System.out.print("Enter student name: ");
                        String name = scanner.next();
                        student = new Student(id, name); // update the student's name
                        System.out.println("Student name updated successfully.");
                    } else {
                        System.out.println("Student ID not found.");
                    }
                    break;
                case "b":
                    System.out.print("Enter student ID: ");
                    id = scanner.next();
                    student = findStudentById(id);
                    if (student != null) {
                        for (int i = 0; i < 3; i++) {
                            System.out.print("Enter mark for module " + (i + 1) + ": ");//getting module marks
                            int mark = getValidIntegerInput(scanner);
                            student.setModuleMark(i, mark);
                        }
                        System.out.println("Module marks updated successfully.");
                    } else {
                        System.out.println("Student ID not found.");
                    }
                    break;
                case "c":
                    generateSummary();
                    break;
                case "d":
                    generateCompleteReport();
                    break;
                case "e":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void generateSummary() {
        int totalRegistrations = count;
        int[] passedModules = new int[3];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < 3; j++) {
                if (students[i].getModule(j).getMark() > 40) {
                    passedModules[j]++;
                }
            }
        }
        System.out.println("Total student registrations: " + totalRegistrations);
        for (int i = 0; i < 3; i++) {
            System.out.println("Students scoring more than 40 in Module " + (i + 1) + ": " + passedModules[i]);
        }
    }

    private static void generateCompleteReport() {
        bubbleSortByAverage(students, count);
        for (int i = 0; i < count; i++) {
            Student student = students[i];
            double average = student.getAverage();
            System.out.printf("ID: %s, Name: %s, Module 1: %d, Module 2: %d, Module 3: %d, Total: %d, Average: %.2f, Grade: %s%n",
                    student.getId(), student.getName(), student.getModule(0).getMark(), student.getModule(1).getMark(),
                    student.getModule(2).getMark(), student.getModule(0).getMark() + student.getModule(1).getMark() + student.getModule(2).getMark(),
                    average, student.getGrade());
        }
    }

    private static void bubbleSortByAverage(Student[] array, int length) {
        boolean swapped;
        for (int i = 0; i < length - 1; i++) {
            swapped = false;
            for (int j = 0; j < length - 1 - i; j++) {
                if (array[j].getAverage() < array[j + 1].getAverage()) {
                    Student temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    private static Student findStudentById(String id) {
        for (int i = 0; i < count; i++) {
            if (students[i].getId().equals(id)) {
                return students[i];
            }
        }
        return null;
    }
}
