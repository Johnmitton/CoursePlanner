package CoursePlanner.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVReader {

    private String path;
    Scanner scanner;

    public CSVReader(String path) {
        this.path = path;
        try {
            File file = new File(path);
            this.scanner = new Scanner(file);
            scanner.nextLine();
        } catch(FileNotFoundException e) {
            System.out.println("Error: file not found");
            e.printStackTrace();
        }
    }

    public List<String> nextLine() {
        String line = scanner.nextLine();

        line = line.replace("\"", "");

        return Arrays.asList(line.split(","));
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }
}