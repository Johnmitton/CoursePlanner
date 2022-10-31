package CoursePlanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import CoursePlanner.restapi.ApiDepartmentWrapper;

public class CoursePlanner {

    private List<Department> departments = new ArrayList<>();
    private List<String> departmentNames = new ArrayList<>();

    private List<ApiDepartmentWrapper> departmentWrappers= new ArrayList();

    public CoursePlanner() {
        convertCSVFile("data/course_data_2018.csv");
    }

    public void convertCSVFile(String path) {
        CSVReader converter = new CSVReader(path);
        while(converter.hasNextLine()){
            List<String> line = converter.nextLine();

            convertCSVString(line);

//            for(String item : line) {
//                System.out.print(item + ", ");
//            }
//            System.out.println();
        }
    }

    public void convertCSVString(List<String> line) {
        Department department = convertDepartment(line);

        Course course = convertCourse(line, department);

        Offering offering = convertCourseOffering(line, course);

        Section section = convertSection(line, offering);

        int total = Integer.parseInt(line.get(5));
        int cap =  Integer.parseInt(line.get(4));
        String type = section.getType();
        String term = offering.getTerm();
        int year = offering.getYear();

        course.notifyObservers(total, cap, type, term, year);
    }

    public void dumpModel() {

//        try {
//            FileWriter myWriter = new FileWriter("output.txt");


            for (Department department : departments) {
                System.out.println(department.getName() + ", Department ID: " + department.getDeptId());
                for (Course course : department.getCourses()) {
                    System.out.println("    " + course.getCatalogNumber() + ", CourseID: " + course.getCourseId());

//                    myWriter.write("    " + course.getCatalogueNumber() + "\n");

                    for (Offering offering : course.getOfferings()) {
                        System.out.print("        OfferingID: " + offering.getCourseOfferingId() + ", " + offering.getSemesterCode() + " in " + offering.getLocation() + " by ");

//                        myWriter.write("        " + offering.getSemester() + " in " + offering.getLocation() + " by ");

                        for (String prof : offering.getProfessors()) {
                            if (offering.getProfessors().indexOf(prof) == offering.getProfessors().size() - 1) {
                                System.out.println(prof);

//                                myWriter.write(prof + "\n");

                                continue;
                            }
                            System.out.print(prof + ", ");

//                            myWriter.write(prof + ", ");

                        }
                        for (Section section : offering.getSections()) {
                            System.out.println("            TYPE=" + section.getType() +
                                    ", Enrollment=" + section.getEnrollmentTotal() + "/" + section.getEnrollmentCap());

//                            myWriter.write("            TYPE=" + section.getSectionType() +
//                                    ", Enrollment=" + section.getEnrollmentTotal() + "/" + section.getEnrollmentCap() + "\n");

                        }
                    }
                }
            }
//            myWriter.close();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
    }

    public Department convertDepartment(List<String> data) {
        String departmentName = data.get(1);

        Department department = new Department(departmentName);

        if(departments.contains(department)) {
            return departments.get(departments.indexOf(department));
        }

        departments.add(department);
        department.setDeptId(departments.indexOf(department));

        Collections.sort(departments);
        int i = 0;
        for(Department departmentSort : departments) {
            departmentSort.setDeptId(i);
            i++;
        }

        return department;
    }

    public Course convertCourse(List<String> data, Department department) {
        return department.addCourse(data);
    }

    public Offering convertCourseOffering(List<String> data, Course course) {
        return course.addOffering(data);
    }

    public Section convertSection(List<String> data, Offering offering) {
        return offering.addSection(data);
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public Department getDepartmentFromID(int ID) {
        return departments.get(ID);
    }

}