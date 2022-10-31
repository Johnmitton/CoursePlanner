package CoursePlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Offering implements Comparable{
    private int courseOfferingId;
    private int semesterCode;
    private String location;
    @JsonIgnore
    private List<String> professors;
    public String instructors;
    private String term;
    @JsonIgnore
    private int termIndex;
    private int year;
    @JsonIgnore
    private List<Section> sections = new ArrayList<>();
    @JsonIgnore
    private List<String> sectionTypes = new ArrayList<>();

    public Offering(int semesterCode, String location, List<String> professors) {
        this.semesterCode = semesterCode;
        this.location = location;
        this.professors = professors;
        setInstructors();
        setYear();
        setTerm();
    }

    public String getTerm() {
        return term;
    }

    public int getYear() {
        return year;
    }

    public int getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(int courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public String getLocation() {
        return location;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<String> getProfessors() {
        return professors;
    }

    public Section addSection(List<String> data) {
        String sectionType = data.get(data.size() - 1);
        int enrollmentTotal = Integer.parseInt(data.get(5));
        int enrollmentCap = Integer.parseInt(data.get(4));

        if(sectionTypes.contains(sectionType)) {
            int sectionIndex = sectionTypes.indexOf(sectionType);
            Section section = sections.get(sectionIndex);
            section.addCap(enrollmentCap);
            section.addTotal(enrollmentTotal);
            return section;
        }

        Section section = new Section(sectionType, enrollmentTotal, enrollmentCap);
        sections.add(section);
        sectionTypes.add(sectionType);

        return section;
    }

    private void setInstructors() {
        instructors = "";
        for(String prof : professors) {
            if(professors.indexOf(prof) == 0) {
                instructors = instructors + prof;
                continue;
            }
            instructors = instructors + ", " + prof;
        }
    }

    private void setTerm() {
        switch(termIndex) {
            case(1):
                term = "Spring";
                break;
            case(4):
                term = "Summer";
                break;
            case(7):
                term = "Fall";
                break;
        }
    }

    private void setYear() {
        int x = semesterCode / 1000;
        int y = (semesterCode - 1000 * x) / 100;
        int z = (semesterCode - 1000 * x - 100 * y) / 10;

        termIndex = semesterCode - (x*1000) - (y*100) - (z*10);

        this.year = 1900 + (100 * x) + (10 * y) + z;

    }

    @Override
    public int compareTo(Object o) {
        Offering compared = (Offering) o;
        int comparedCode = compared.getSemesterCode();
        int thisCode = this.getSemesterCode();

        if(thisCode < comparedCode) {
            return -1;
        } else if(thisCode == comparedCode) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        String currentOfferingName = this.getSemesterCode() + this.getLocation();
        if(obj instanceof Offering) {
            Offering offering = (Offering) obj;
            String newOfferingName = offering.getSemesterCode() + offering.getLocation();

            return currentOfferingName.equals(newOfferingName);
        } else if(obj instanceof String) {
            return currentOfferingName.equals((String) obj);
        }
        return false;
    }
}
