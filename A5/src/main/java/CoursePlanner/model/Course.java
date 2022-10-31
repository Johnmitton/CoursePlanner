package CoursePlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Course implements Comparable{
    private int courseId;
    private String catalogNumber;
    @JsonIgnore
    private List<Offering> offerings = new ArrayList<>();
    @JsonIgnore
    private List<String> offeringsNames = new ArrayList<>();

    private List<CourseObserver> observers = new ArrayList<>();

    public Course(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public Offering addOffering(List<String> data) {
        String offeringLocation = data.get(3);
        String offeringSemester = data.get(0);

        List<String> professors = new ArrayList<>();

        for(int i = 6; i < data.size() - 1; i++) {
            String prof = data.get(i);
            prof = prof.trim();
            professors.add(prof);
        }

        Offering offering = new Offering(Integer.parseInt(offeringSemester), offeringLocation, professors);

        if(offerings.contains(offering)) {
            return offerings.get(offerings.indexOf(offering));
        }

        offerings.add(offering);

        offering.setCourseOfferingId(offerings.indexOf(offering));

        Collections.sort(offerings);
        int i = 0;
        for(Offering offeringSort : offerings) {
            offeringSort.setCourseOfferingId(i);
            i++;
        }

        return offering;
    }

    public void addObserver(CourseObserver observer) { observers.add(observer); }

    public void notifyObservers(int total, int cap, String type, String term, int year) {
        for(CourseObserver observer : observers) {
            observer.courseChanged(total, cap, type, term, year);
        }
    }

    @Override
    public int compareTo(Object o) {
        Course compared = (Course) o;
        int courseIdCompared = Integer.parseInt(compared.getCatalogNumber().replaceAll("[^\\d.]", ""));
        int courseIdThis = Integer.parseInt(this.getCatalogNumber().replaceAll("[^\\d.]", ""));

        if(courseIdThis < courseIdCompared) {
            return -1;
        } else if(courseIdThis == courseIdCompared) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Course) {
            Course course = (Course) obj;
            return this.getCatalogNumber().equals(course.getCatalogNumber());
        } else if(obj instanceof String) {
            return this.getCatalogNumber().equals((String) obj);
        }
        return false;
    }
}
