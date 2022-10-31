package CoursePlanner.model;

public interface CourseObserver {
    void courseChanged(int total, int cap, String type, String term, int year);
}
