package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ModelDumpParser class is responsible for parsing a CSV file and writing its data to /docs/output_dump.txt
 */

public class ModelDumpParser implements Iterable<Department> {
    private List<Department> departmentList = new ArrayList<>();
    private File file;
    private AtomicLong courseId = new AtomicLong();
    private AtomicLong courseOfferingId = new AtomicLong();

    private static final int SEMESTER_CODE_INDEX = 0;
    private static final int SUBJECT_INDEX = 1;
    private static final int CATALOG_NUMBER_INDEX = 2;
    private static final int LOCATION_INDEX = 3;
    private static final int ENROLLMENT_CAP_INDEX = 4;
    private static final int ENROLLMENT_TOTAL_INDEX = 5;
    private static final int INSTRUCTOR_INDEX = 6;
//    private static final int COMPONENT_CODE_INDEX = 7;

    @Override
    public Iterator<Department> iterator() {
        return departmentList.iterator();
    }

    public ModelDumpParser(File file) throws FileNotFoundException {
        parseCSVFile(file);
    }

    @JsonIgnore
    public Department getDepartment(long deptId) {
        for (Department eachDept : departmentList) {
            if (eachDept.getDepartmentId() == deptId) {
                return eachDept;
            }
        }
        return null;
    }

    @JsonIgnore
    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public AtomicLong getCourseId() {
        return courseId;
    }

    public void setCourseId(AtomicLong courseId) {
        this.courseId = courseId;
    }

    public long incrementAndGetCourseId() {
        return courseId.incrementAndGet();
    }

    public AtomicLong getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(AtomicLong courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public long incrementAndGetCourseOfferingId() {
        return courseOfferingId.incrementAndGet();
    }

    public void sortDepartmentList() {
        Collections.sort(departmentList, (dept1, dept2) -> dept1.getName().compareTo(dept2.getName()));
    }

    public void addToDepartmentList(Department newDepartment, Course newCourse,
                                    CourseOffering newCourseOffering, Section newSection) {
        // if duplicate department; group them together via its course
        for (Department eachDept : departmentList) {
            if (eachDept.getName().equals(newDepartment.getName())) {
                eachDept.addToCourseList(newCourse, newCourseOffering, newSection);
                return;
            }
        }

        // otherwise unique department; add it to departmentList
        departmentList.add(newDepartment);
        sortDepartmentList();
    }

    public void parseCSVFile(File file) throws FileNotFoundException {
        Scanner fileReader = null;

        try {
            fileReader = new Scanner(file); // can be changed here to try small_data.csv

            // skip headers
            fileReader.nextLine();

            // read in fields of the CSV file
            while (fileReader.hasNextLine()) {
                // data structure to hold each string field that is split by the comma in the CSV file
                String[] fields = fileReader.nextLine().split(",");
                final int COMPONENT_CODE_INDEX = fields.length - 1; // should be 7

                // combining fields to feed them back into each class' constructors
                List<String> courseOfferingFields = new ArrayList<>();
                List<String> sectionFields = new ArrayList<>();

                // each CourseOffering object is identified by these fields
                courseOfferingFields.add(fields[LOCATION_INDEX]);

                // catch i number of instructors; handles both cases of a single/many instructor(s)
                for (int i = INSTRUCTOR_INDEX; i < COMPONENT_CODE_INDEX; i++) {
                    courseOfferingFields.add(fields[i]);
                }
                courseOfferingFields.add(fields[SEMESTER_CODE_INDEX]);

                // each Section object is identified by these fields
                sectionFields.add(fields[ENROLLMENT_CAP_INDEX]);
                sectionFields.add(fields[ENROLLMENT_TOTAL_INDEX]);
                sectionFields.add(fields[COMPONENT_CODE_INDEX]);

                Department department = new Department(fields[SUBJECT_INDEX]);
                Course course = new Course(courseId.incrementAndGet(), fields[CATALOG_NUMBER_INDEX]);
                CourseOffering courseOffering = new CourseOffering(courseOfferingId.incrementAndGet(),
                                                                   courseOfferingFields);
                Section section = new Section(sectionFields);

                // sorting done recursively through each class
                addToDepartmentList(department, course, courseOffering, section);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            exitProgram("File was not found!");
        }
        finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    public String modelDump() {
        StringBuilder printCourses = new StringBuilder();

        // iterating through each list
        for (Department eachDept : departmentList) {

            for (Course eachCourse : eachDept) {
                printCourses.append(eachDept.getName()).append(" ")
                        .append(eachCourse.getCatalogNumber()).append("\n");

                for (CourseOffering eachOffering : eachCourse) {
                    printCourses.append("\t").append(eachOffering.getSemesterCode()).append(" in ")
                            .append(eachOffering.getLocation()).append(" by ")
                            .append(eachOffering.getInstructors()).append("\n");

                    for (Section eachSection : eachOffering) {
                        printCourses.append("\t\t" + "Type=").append(eachSection.getType())
                                .append(", Enrollment=")
                                .append(eachSection.getEnrollmentTotal()).append("/")
                                .append(eachSection.getEnrollmentCapacity()).append("\n");
                    }
                }
            }
        }

        return printCourses.toString();
    }

    // only ModelDumpParser class should be able to call this method
    private void exitProgram(String errorMessage) {
        final int FAILURE = -1;
        System.out.println(errorMessage);
        System.out.println("Program will now exit.");
        System.exit(FAILURE);
    }
}
