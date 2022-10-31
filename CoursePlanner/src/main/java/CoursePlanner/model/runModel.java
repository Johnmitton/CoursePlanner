package CoursePlanner.model;

public class runModel {
    public static void main(String[] args) {
        CoursePlanner cp = new CoursePlanner();

        cp.convertCSVFile("data/small_data.csv");
//        cp.convertCSV("data/course_data_2018.csv");
    }
}
