package CoursePlanner.controllers;

import CoursePlanner.model.*;
import CoursePlanner.restapi.ApiOfferingDataWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class CoursePlannerController {

    private CoursePlanner coursePlanner = new CoursePlanner();

    private List<Watcher> watchers = new ArrayList<>();

    @GetMapping("/api/about")
    @ResponseStatus(HttpStatus.OK)
    public String getHelloMessage() {

        String about = "{\n" +
                "  \"appName\": \"CoursePlanner\",\n" +
                "  \"authorName\": \"John Mitton, 301415855\"\n" +
                "}";
        return about;
    }

    @GetMapping("/api/dump-model")
    @ResponseStatus(HttpStatus.OK)
    public void dumpModel() {
        coursePlanner.dumpModel();
    }

    @GetMapping("/api/departments")
    @ResponseStatus(HttpStatus.OK)
    public List<Department> getDepartments() {

        List<Department> departments = coursePlanner.getDepartments();

//        Collections.sort(departments);

        return departments;
    }

    @GetMapping("api/departments/{departmentID}/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<Course> getCourses(@PathVariable("departmentID") int departmentID) {

        if(departmentID < 0 || departmentID >= coursePlanner.getDepartments().size()) {
            throw new IllegalArgumentException();
        }

        Department department = coursePlanner.getDepartments().get(departmentID);

        List<Course> courses = department.getCourses();

//        Collections.sort(courses);

        return courses;
    }

    @GetMapping("/api/departments/{departmentID}/courses/{courseID}/offerings")
    @ResponseStatus(HttpStatus.OK)
    public List<Offering> getOfferings(@PathVariable("departmentID") int departmentID, @PathVariable("courseID") int courseID) {

        if(departmentID < 0 || departmentID >= coursePlanner.getDepartments().size()) {
            throw new IllegalArgumentException();
        }

        Department department = coursePlanner.getDepartments().get(departmentID);

        if(courseID < 0 || courseID >= department.getCourses().size()) {
            throw new IllegalArgumentException();
        }

        Course course = department.getCourses().get(courseID);
        List<Offering> offerings = course.getOfferings();

//        Collections.sort(offerings);

        return offerings;
    }

    @GetMapping("/api/departments/{departmentID}/courses/{courseID}/offerings/{courseOfferingID}")
    @ResponseStatus(HttpStatus.OK)
    public List<Section> getSections(@PathVariable("departmentID") int departmentID, @PathVariable("courseID") int courseID, @PathVariable("courseOfferingID") int courseOfferingID) {

        if(departmentID < 0 || departmentID >= coursePlanner.getDepartments().size()) {
            throw new IllegalArgumentException();
        }

        Department department = coursePlanner.getDepartments().get(departmentID);

        if(courseID < 0 || courseID >= department.getCourses().size()) {
            throw new IllegalArgumentException();
        }

        Course course = department.getCourses().get(courseID);

        if(courseOfferingID < 0 || courseOfferingID >= course.getOfferings().size()) {
            throw new IllegalArgumentException();
        }

        Offering offering = course.getOfferings().get(courseOfferingID);
        List<Section> sections = offering.getSections();
        return sections;
    }

    @PostMapping("/api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOffering(@RequestBody String courseInfoJson) {
        Gson gson = new Gson();
        ApiOfferingDataWrapper wrapper = gson.fromJson(courseInfoJson, ApiOfferingDataWrapper.class);

        List<String> CSV = new ArrayList<>();
        CSV.add(wrapper.semester);
        CSV.add(wrapper.subjectName);
        CSV.add(wrapper.catalogNumber);
        CSV.add(wrapper.location);
        CSV.add(String.valueOf(wrapper.enrollmentCap));
        CSV.add(String.valueOf(wrapper.enrollmentTotal));
        CSV.add(wrapper.instructor);
        CSV.add(wrapper.component);

        coursePlanner.convertCSVString(CSV);
    }


    @GetMapping("/api/watchers")
    @ResponseStatus(HttpStatus.OK)
    public List<Watcher> listWatchers() {
        return watchers;
    }

    @PostMapping("/api/watchers")
    @ResponseStatus(HttpStatus.CREATED)
    public void newWatcher(@RequestBody String watcherInfoJson) {
        JsonElement jsonElement = JsonParser.parseString(watcherInfoJson);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int deptID = jsonObject.get("deptId").getAsInt();
        int courseID = jsonObject.get("courseId").getAsInt();

        Department department = coursePlanner.getDepartmentFromID(deptID);
        Course course = department.getCourseFromID(courseID);

        watchers.add(new Watcher(course, department, watchers.size()));
    }

    @GetMapping("/api/watchers/{watcherID}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getWatcherEvents(@PathVariable("watcherID") int watcherID) {

        if(watcherID < 0 || watcherID >= watchers.size()) {
            throw new IllegalArgumentException();
        }

        return watchers.get(watcherID).getEvents();
    }

    @DeleteMapping("/api/watchers/{watcherID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWatcher(@PathVariable("watcherID") int watcherID) {
        watchers.remove(watcherID);

        int i = 0;
        for(Watcher watcher : watchers){
            watcher.setId(i);
            i++;
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Request ID Not Found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIDHandler() {
    }
}
