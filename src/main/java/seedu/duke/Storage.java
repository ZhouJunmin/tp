package seedu.duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Storage {

    private final static String FOLDER_PATH = "./data/";
    private final static String USER_TIMETABLE_FILE_PATH = "./data/myTimetable.csv";

    public static void writeToFile(Timetable timetable) {
        try {
            FileWriter fw = new FileWriter(USER_TIMETABLE_FILE_PATH);
            fw.write(timetable.toString());
            fw.close();
        } catch (IOException e) {
            System.out.println("Failed writing timetable to file");
        }
    }

    public static Timetable loadTimetable(String timetableName) {
        Timetable newTimetable = new Timetable();
        String filePathName = FOLDER_PATH + timetableName + ".csv";
        Path filePath = Paths.get(filePathName);

        if (!Files.exists(filePath)) {
            System.out.println("File at " + filePathName + " is not found. Trying to create one.");
            createFile(filePathName);
        }

        File f = new File(filePathName);
        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("Loading file failed.");
            return newTimetable;
        }
        s.useDelimiter(System.lineSeparator());

        int lineNumber = 1;
        while (s.hasNext()) {
            String line = s.next();
            try {
                Course course = parseCourse(line);
                newTimetable.addCourse(course);
            } catch (Exception e) {
                System.out.println("Data corrupted at line " + lineNumber + "of file at " + filePathName);
            }
            lineNumber ++;
        }
        s.close();

        return newTimetable;
    }

    private static void createFile(String filePathName) {
        Path folderPath = Paths.get(FOLDER_PATH);
        Path filePath = Paths.get(filePathName);

        if (Files.exists(folderPath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException ex) {
                System.out.println("File creation failed.");
                return;
            }
        } else {
            try {
                Files.createDirectory(folderPath);
                Files.createFile(filePath);
            } catch (IOException ex) {
                System.out.println("File creation failed.");
                return;
            }
        }

        System.out.println("File creation successful.");
    }

    public static Course parseCourse(String sentence) throws Exception {
        String[] words = sentence.split(",");
        String courseCode, courseName;
        int modularCredits, year, term;

        try {
            courseCode = words[0];
            courseName = words[1];
            modularCredits = Integer.parseInt(words[2]);
            year = Integer.parseInt(words[3]);
            term = Integer.parseInt(words[4]);
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            throw new Exception();
        }

        return new Course(courseCode, courseName, modularCredits, year, term);
    }

}