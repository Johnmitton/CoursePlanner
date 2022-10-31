package CoursePlanner.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Watcher {
    public long id;
    public Department department;
    public Course course;
    public List<String> events = new ArrayList<>();

    public Watcher(Course course, Department department, int ID) {
        this.course = course;
        this.department = department;
        this.id = ID;
        registerAsObserver();
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getEvents() {
        return events;
    }

    private void registerAsObserver() {
        course.addObserver(new CourseObserver() {
            @Override
            public void courseChanged(int total, int cap, String type, String term, int year) {
                Date date = new Date();
                events.add(date + ": Added section " + type + " with enrollment (" + total + " / " + cap + ") to offering " + term + " " + year);
            }
        });
    }
}
