package CoursePlanner.model;

public class Section {
    private String type;
    private int enrollmentTotal;
    private int enrollmentCap;

    public Section(String type, int enrollmentTotal, int enrollmentCap) {
        this.type = type;
        this.enrollmentTotal = enrollmentTotal;
        this.enrollmentCap = enrollmentCap;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public void addTotal(int x) {
        enrollmentTotal += x;
    }

    public void addCap(int x) {
        enrollmentCap += x;
    }

    public String getType() {
        return type;
    }
}
