package ca.cmpt213.as5courseplanner.controllers;

import ca.cmpt213.as5courseplanner.model.*;
import ca.cmpt213.as5courseplanner.exceptions.*;
import ca.cmpt213.as5courseplanner.placeholderObjects.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * CoursePlannerController class sets up the endpoints for the REST API
 */

@RestController
public class CoursePlannerController {
    private About aboutMessage = new About();
    private ModelDumpParser courseParser;

    private List<Watcher> watcherList = new ArrayList<>();

    File mainCSVFile = new File("data/course_data_2018.csv");
    File smallCSVFile = new File("data/small_data.csv");

    public CoursePlannerController() throws FileNotFoundException {
        courseParser = new ModelDumpParser(mainCSVFile);
    }

    private final static int SPRING_SEMESTER_CODE = 1;
    private final static int SUMMER_SEMESTER_CODE = 4;
    private final static int FALL_SEMESTER_CODE = 7;

    private boolean isValidSemesterCode(int code) {
        // semester codes are the last digit of course codes
        int semesterCode = code % 10;

        return semesterCode == SPRING_SEMESTER_CODE
                || semesterCode == SUMMER_SEMESTER_CODE
                || semesterCode == FALL_SEMESTER_CODE;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/about")
    public About getAboutMessage() {
        return aboutMessage;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/dump-model")
    public void getDumpModel() throws FileNotFoundException {
        System.out.println(courseParser.modelDump());
    }

    // List of all departments.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments")
    public List<Department> getDepartments() {
        return courseParser.getDepartmentList();
    }

    // Lists all courses for department with deptId.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments/{id}/courses")
    public List<Course> getCourses(@PathVariable("id") int deptID) throws DepartmentNotFoundException {

        if (courseParser.getDepartment(deptID) == null) {
            throw new DepartmentNotFoundException("Department with ID " + deptID + " was not found!");
        }

        return courseParser.getDepartment(deptID).getCourseList();
    }

    // Lists the offerings of the course with courseId inside department with deptId
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments/{id}/courses/{cid}/offerings")
    public List<Offering> getOfferings(@PathVariable("id") int deptID,
                                       @PathVariable("cid") int courseID)
                                       throws DepartmentNotFoundException,
                                              CourseNotFoundException
    {
        if (courseParser.getDepartment(deptID) == null) {
            throw new DepartmentNotFoundException("Department with ID " + deptID + " was not found!");
        }
        else if (courseParser.getDepartment(deptID).getCourse(courseID) == null) {
            throw new CourseNotFoundException("Course with ID " + courseID + " was not found!");
        }

        return courseParser.getDepartment(deptID)
                           .getCourse(courseID)
                           .getOfferingList();
    }

    /* Return the list of sections for the offering with courseOfferingId, in the course with
       courseId, in the department with deptId. */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments/{id}/courses/{cid}/offerings/{oid}")
    public List<Section> getOfferings(@PathVariable("id") int deptID,
                                      @PathVariable("cid") int courseID,
                                      @PathVariable("oid") int offeringID)
                                      throws DepartmentNotFoundException,
                                             CourseNotFoundException,
                                             OfferingNotFoundException
    {
        if (courseParser.getDepartment(deptID) == null) {
            throw new DepartmentNotFoundException("Department with ID " + deptID + " was not found!");
        }
        else if (courseParser.getDepartment(deptID).getCourse(courseID) == null) {
            throw new CourseNotFoundException("Course with ID " + courseID + " was not found!");
        }
        else if (courseParser.getDepartment(deptID).getCourse(courseID).getOffering(offeringID) == null) {
            throw new OfferingNotFoundException("Offering with ID " + offeringID + " was not found!");
        }

        return courseParser.getDepartment(deptID)
                           .getCourse(courseID)
                           .getOffering(offeringID)
                           .getSectionList();
    }

    /* Returns a list of data points showing how many spaces in courses were filled by students during
       each semester for the selected department. */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/stats/students-per-semester")
    public List<GraphData> getEnrollmentList(@RequestParam int deptId) throws DepartmentNotFoundException{
        List<GraphData> graphData = new ArrayList<>();

        if (courseParser.getDepartment(deptId) == null) {
            throw new DepartmentNotFoundException("Department with ID " + deptId + " was not found!");
        }

        int firstSemester = courseParser.getDepartment(deptId).getFirstSemesterCode();
        int lastSemester = courseParser.getDepartment(deptId).getLastSemesterCode();

        for (int i = firstSemester; i < lastSemester; i++) {
            if (isValidSemesterCode(i)) {
                graphData.add(new GraphData(i, courseParser.getDepartment(deptId)));
            }
        }

        return graphData;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/addoffering")
    public void addOffering(@RequestBody OfferingsPlaceholder placeholder) {
        boolean foundDepartment = false;
        Department placeholderDepartment = new Department();

        for (Department eachDepartment : courseParser) {
            if (eachDepartment.getName().equals(placeholder.subjectName)) {
                foundDepartment = true;
                placeholderDepartment = eachDepartment;
                break;
            }
        }

        utilityHelpOfferingMethod(placeholder, placeholderDepartment);

        if (!foundDepartment) {
            Department newDepartment = new Department(placeholder.subjectName);
            utilityHelpOfferingMethod(placeholder, newDepartment);
            courseParser.getDepartmentList().add(newDepartment);
        }
    }

    private void utilityHelpOfferingMethod(OfferingsPlaceholder placeholder, Department department) {
        Course newCourse = new Course(placeholder.catalogNumber, courseParser.incrementAndGetCourseId());

        //Create an list of strings for fields to avoid creating a new constructor
        List<String> courseComponentFields = new ArrayList<>();
        courseComponentFields.add("" + placeholder.enrollmentCap);
        courseComponentFields.add("" + placeholder.enrollmentTotal);
        courseComponentFields.add(placeholder.component);

        Section courseSection = new Section(courseComponentFields);

        //Create an list of strings for fields to avoid creating a new constructor
        List<String> offeringFields = new ArrayList<>();
        offeringFields.add(placeholder.location);
        offeringFields.add(placeholder.instructor);
        offeringFields.add("" + placeholder.semester);

        Offering newOffering = new Offering(offeringFields, courseParser.incrementAndGetOfferingId());

        newOffering.addSectionToList(courseSection);

        // notify observers
        for (Course eachCourse : department.getCourseList()) {
            if (eachCourse.getCatalogNumber().equals(placeholder.catalogNumber)) {
                eachCourse.notifyObservers(newOffering, courseSection);
            }
        }

        courseParser.addToDepartmentList(department, newCourse, newOffering, courseSection);
        newCourse.setCourseId(department.getCourseList().size());

        courseParser.sortEverything();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/watchers")
    public List<Watcher> getAllWatchers() {
        return watcherList;
    }
}