import java.io.*;
import java.util.*;

class Student {
    String lastName;
    String firstName;
    int grade;
    int classroom;
    int bus;
    String teacherLastName;
    String teacherFirstName;

    public Student(String lastName, String firstName, int grade, int classroom, int bus, String teacherLastName, String teacherFirstName) {
        this.lastName = lastName.toUpperCase();
        this.firstName = firstName.toUpperCase();
        this.grade = grade;
        this.classroom = classroom;
        this.bus = bus;
        this.teacherLastName = teacherLastName.toUpperCase();
        this.teacherFirstName = teacherFirstName.toUpperCase();
    }

    public String getBusInfo() {
        return firstName + " " + lastName + " (Bus Route: " + bus + ")";
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (Classroom: " + classroom + ", Teacher: " + teacherFirstName + " " + teacherLastName + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return lastName.equalsIgnoreCase(student.lastName) &&
                firstName.equalsIgnoreCase(student.firstName) &&
                grade == student.grade &&
                classroom == student.classroom &&
                bus == student.bus &&
                teacherLastName.equalsIgnoreCase(student.teacherLastName) &&
                teacherFirstName.equalsIgnoreCase(student.teacherFirstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName.toLowerCase(), firstName.toLowerCase(), grade, classroom, bus, teacherLastName.toLowerCase(), teacherFirstName.toLowerCase());
    }
}

public class SchoolSearch {

    public static List<Student> readStudentsFromFile(String filename) throws IOException {
        List<Student> students = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length != 7) {
                System.out.println("Incorrect data format in line: " + line);
                continue;
            }
            String lastName = data[0].trim();
            String firstName = data[1].trim();
            int grade = Integer.parseInt(data[2].trim());
            int classroom = Integer.parseInt(data[3].trim());
            int bus = Integer.parseInt(data[4].trim());
            String teacherLastName = data[5].trim();
            String teacherFirstName = data[6].trim();
            students.add(new Student(lastName, firstName, grade, classroom, bus, teacherLastName, teacherFirstName));
        }
        br.close();
        return students;
    }

    public static void writeStudentToFile(String filename, Student student, boolean append) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename, append));
        bw.write(student.lastName + "," + student.firstName + "," + student.grade + "," +
                student.classroom + "," + student.bus + "," + student.teacherLastName + "," + student.teacherFirstName);
        bw.newLine();
        bw.close();
    }

    public static void findByLastName(List<Student> students, String lastName) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.lastName.equalsIgnoreCase(lastName) && uniqueStudents.add(s)) {
                System.out.println(s.firstName + " " + s.lastName + " (Classroom: " + s.classroom + ", Teacher: " + s.teacherFirstName + " " + s.teacherLastName + ")");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Student with last name " + lastName + " not found.");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findByLastNameAndBus(List<Student> students, String lastName, int bus) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.lastName.equalsIgnoreCase(lastName) && s.bus == bus && uniqueStudents.add(s)) {
                System.out.println(s.getBusInfo());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students with last name " + lastName + " found on bus route " + bus + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findByLastNameInteractive(List<Student> students, String lastName, Scanner scanner) {
        System.out.print("Would you like to find by bus route? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            System.out.print("Enter bus route number: ");
            try {
                int busRoute = Integer.parseInt(scanner.nextLine().trim());
                findByLastNameAndBus(students, lastName, busRoute);
            } catch (NumberFormatException e) {
                System.out.println("Invalid bus route number format.");
            }
        } else if (response.equals("no")) {
            findByLastName(students, lastName);
        } else {
            System.out.println("Invalid response. Please enter 'yes' or 'no'.");
        }
    }

    public static void findByTeacherLastName(List<Student> students, String teacherLastName) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.teacherLastName.equalsIgnoreCase(teacherLastName) && uniqueStudents.add(s)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students found for teacher with last name " + teacherLastName + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findByClassroom(List<Student> students, int classroom) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.classroom == classroom && uniqueStudents.add(s)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students found in classroom " + classroom + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void findByBus(List<Student> students, int busRoute) {
        long startTime = System.nanoTime();
        Set<Student> uniqueStudents = new HashSet<>();
        boolean found = false;
        for (Student s : students) {
            if (s.bus == busRoute && uniqueStudents.add(s)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students found using bus route " + busRoute + ".");
        }
        long endTime = System.nanoTime();
        printSearchTime(startTime, endTime);
    }

    public static void printSearchTime(long startTime, long endTime) {
        long duration = endTime - startTime;
        double seconds = duration / 1_000_000_000.0;
        System.out.printf("\nSearch time: %.6f seconds%n", seconds);
    }

    public static void main(String[] args) {
        try {
            List<Student> students = readStudentsFromFile("students.txt");

            Scanner scanner = new Scanner(System.in);
            String command;

            do {
                System.out.println("\nMenu:");
                System.out.println("S (Student) <lastname> - Пошук класу і вчителя за прізвищем учня з можливістю фільтрації по автобусу");
                System.out.println("T (Teacher) <lastname> - Пошук учнів за ім'ям вчителя");
                System.out.println("C (Classroom) <number> -  Пошук учнів за номером класу");
                System.out.println("B (Bus) <number> - Пошук учнів за номером автобусного маршруту");
                System.out.println("Q - Quit - Вихід");
                System.out.print("Enter a command: ");

                command = scanner.nextLine().trim().toUpperCase();
                String[] parts = command.split(" ");
                System.out.println();
                switch (parts[0]) {
                    case "S":
                        if (parts.length == 2) {
                            findByLastNameInteractive(students, parts[1], scanner);
                        } else {
                            System.out.println("Invalid command format for student search.");
                        }
                        break;
                    case "T":
                        if (parts.length == 2) {
                            findByTeacherLastName(students, parts[1]);
                        } else {
                            System.out.println("Invalid command format for teacher search.");
                        }
                        break;
                    case "C":
                        if (parts.length == 2) {
                            try {
                                int classroom = Integer.parseInt(parts[1]);
                                findByClassroom(students, classroom);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid classroom number format.");
                            }
                        } else {
                            System.out.println("Invalid command format for classroom search.");
                        }
                        break;
                    case "B":
                        if (parts.length == 2) {
                            try {
                                int bus = Integer.parseInt(parts[1]);
                                findByBus(students, bus);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid bus route number format.");
                            }
                        } else {
                            System.out.println("Invalid command format for bus search.");
                        }
                        break;
                    case "Q":
                        System.out.println("Exit.");
                        break;
                    default:
                        System.out.println("Unknown command. Please try again.");
                }

            } while (!command.equals("Q"));

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}