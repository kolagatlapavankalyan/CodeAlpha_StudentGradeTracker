package codealpha.studentgradetracker;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// --------------------- Student Class ---------------------
class Student {
    private String name;
    private ArrayList<Integer> grades;

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) return 0;
        int sum = 0;
        for (int g : grades) sum += g;
        return (double) sum / grades.size();
    }

    public int getHighest() {
        int high = Integer.MIN_VALUE;
        for (int g : grades) if (g > high) high = g;
        return high;
    }

    public int getLowest() {
        int low = Integer.MAX_VALUE;
        for (int g : grades) if (g < low) low = g;
        return low;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void displayReport() {
        System.out.println("Student Name: " + name);
        System.out.println("Grades: " + grades);
        System.out.printf("Average: %.2f | Highest: %d | Lowest: %d%n",
                getAverage(), getHighest(), getLowest());
        System.out.println("--------------------------------------");
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder(name);
        for (int g : grades) sb.append(",").append(g);
        return sb.toString();
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        Student s = new Student(parts[0]);
        for (int i = 1; i < parts.length; i++) s.addGrade(Integer.parseInt(parts[i]));
        return s;
    }
}

// --------------------- Main Class ---------------------
public class StudentGradeTracker {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = loadStudentsFromFile();
        int choice;

        System.out.println("=======================================");
        System.out.println("        🎓 STUDENT GRADE TRACKER");
        System.out.println("=======================================");

        do {
            System.out.println("\n1. Add Student");
            System.out.println("2. Add Grades");
            System.out.println("3. View Report");
            System.out.println("4. Save & Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String name = sc.nextLine();
                    students.add(new Student(name));
                    System.out.println("✅ Student added successfully!");
                    break;

                case 2:
                    if (students.isEmpty()) {
                        System.out.println("⚠ No students available. Add one first.");
                        break;
                    }
                    System.out.println("Enter student number:");
                    for (int i = 0; i < students.size(); i++)
                        System.out.println((i + 1) + ". " + students.get(i).getName());

                    int num = sc.nextInt();
                    if (num < 1 || num > students.size()) {
                        System.out.println("⚠ Invalid choice!");
                        break;
                    }
                    Student s = students.get(num - 1);
                    System.out.print("Enter number of subjects: ");
                    int n = sc.nextInt();
                    for (int i = 0; i < n; i++) {
                        System.out.print("Enter grade " + (i + 1) + ": ");
                        int grade = sc.nextInt();
                        s.addGrade(grade);
                    }
                    System.out.println("✅ Grades added successfully!");
                    break;

                case 3:
                    if (students.isEmpty()) {
                        System.out.println("⚠ No student records found.");
                    } else {
                        System.out.println("\n======= STUDENT REPORT =======");
                        for (Student stu : students) stu.displayReport();
                    }
                    break;

                case 4:
                    saveStudentsToFile(students);
                    System.out.println("💾 Data saved successfully! Exiting...");
                    break;

                default:
                    System.out.println("⚠ Invalid choice! Try again.");
            }
        } while (choice != 4);

        sc.close();
    }

    // Save data to a file
    private static void saveStudentsToFile(ArrayList<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.write(s.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }

    // Load data from a file
    private static ArrayList<Student> loadStudentsFromFile() {
        ArrayList<Student> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                list.add(Student.fromFileString(line));
            System.out.println("📂 Loaded " + list.size() + " student(s) from file.");
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
        return list;
    }
}