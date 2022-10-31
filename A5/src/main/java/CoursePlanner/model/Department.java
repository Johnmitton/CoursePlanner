package CoursePlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Department implements Comparable{
    private int deptId;
    private String name;
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();
    @JsonIgnore
    private List<String> courseNumbers = new ArrayList<>();

    public Department(String departmentName) {
        this.name = departmentName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Course getCourseFromID(int ID) {
        return courses.get(ID);
    }

    public Course addCourse(List<String> data) {
        String courseNumber = data.get(2);

        Course course = new Course(courseNumber);

        if(courses.contains(course)) {
            return courses.get(courses.indexOf(course));
        }

        courses.add(course);
        course.setCourseId(courses.indexOf(course));

        Collections.sort(courses);
        int i = 0;
        for(Course courseSort : courses) {
            courseSort.setCourseId(i);
            i++;
        }

        return course;
    }

    @Override
    public int compareTo(Object o) {
        Department compareTo = (Department) o;
        return this.getName().compareTo(compareTo.getName());
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Department) {
            Department department = (Department) obj;
            return this.getName().equals(department.getName());
        } else if(obj instanceof String) {
            return this.getName().equals((String) obj);
        }

        return false;
    }
}
