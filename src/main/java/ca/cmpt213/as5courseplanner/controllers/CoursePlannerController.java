package ca.cmpt213.as5courseplanner.controllers;

import ca.cmpt213.as5courseplanner.model.*;
import ca.cmpt213.as5courseplanner.exceptions.*;
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
    private About aboutMessage = new About("CMPT 213 REST API Course Planner", "Kenrick Lam");
    private ModelDumpParser courseParser;

    File mainCSVFile = new File("data/course_data_2018.csv");
    File smallCSVFile = new File("data/small_data.csv");

    public CoursePlannerController() throws FileNotFoundException {
        courseParser = new ModelDumpParser(mainCSVFile);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/about")
    public About getAboutMessage() {
        return aboutMessage;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/dump-model")
    public void getDumpModel() throws FileNotFoundException {
        // print to console
        System.out.println(courseParser.modelDump());
    }

    // List of all departments.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments")
    public List<Department> getAllDepartments() {
        return courseParser.getDepartmentList();
    }

    // Lists all courses for department with deptId.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments/{id}/courses")
    public List<Course> getAllCoursesOneDepartment(@PathVariable("id") long deptId) throws DepartmentNotFoundException {
        if (courseParser.getDepartment(deptId) == null) {
            throw new DepartmentNotFoundException("Department with ID: " + deptId + " was not found!");
        }

        return courseParser.getDepartment(deptId)
                           .getCourseList();
    }

    // Lists the offerings of the course with courseId inside department with deptId
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments/{id}/courses/{cid}/offerings")
    public List<CourseOffering> getAllCourseOfferings(@PathVariable("id") long deptID,
                                                   @PathVariable("cid") long courseID)
                                                   throws DepartmentNotFoundException,
                                                          CourseNotFoundException {
        if (courseParser.getDepartment(deptID) == null) {
            throw new DepartmentNotFoundException("Department with ID " + deptID + " was not found!");
        }
        else if (courseParser.getDepartment(deptID).getCourse(courseID) == null) {
            throw new CourseNotFoundException("Course with ID " + courseID + " was not found!");
        }

        return courseParser.getDepartment(deptID)
                           .getCourse(courseID)
                           .getCourseOfferingList();
    }

    /* Return the list of sections for the offering with courseOfferingId, in the course with
       courseId, in the department with deptId. */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/departments/{id}/courses/{cid}/offerings/{oid}")
    public List<Section> getAllSectionsOneCourse(@PathVariable("id") long deptID,
                                                       @PathVariable("cid") long courseID,
                                                       @PathVariable("oid") long courseOfferingID)
                                                       throws DepartmentNotFoundException,
                                                              CourseNotFoundException,
                                                              CourseOfferingNotFoundException {
        if (courseParser.getDepartment(deptID) == null) {
            throw new DepartmentNotFoundException("Department with ID " + deptID + " was not found!");
        }
        else if (courseParser.getDepartment(deptID).getCourse(courseID) == null) {
            throw new CourseNotFoundException("Course with ID " + courseID + " was not found!");
        }
        else if (courseParser.getDepartment(deptID).getCourse(courseID).getCourseOffering(courseOfferingID) == null) {
            throw new CourseOfferingNotFoundException("Offering with ID " + courseOfferingID + " was not found!");
        }
        return courseParser.getDepartment(deptID)
                           .getCourse(courseID)
                           .getCourseOffering(courseOfferingID)
                           .getSectionList();
    }

    /* Returns a list of data points showing how many spaces in courses were filled by students during
       each semester for the selected department. */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/stats/students-per-semester")
    public List<GraphData> getGraphData(@RequestParam long deptID) throws DepartmentNotFoundException {
        List<GraphData> graphData = new ArrayList<>();

        if (courseParser.getDepartment(deptID) == null) {
            throw new DepartmentNotFoundException("Department with ID " + deptID + " was not found!");
        }

        return courseParser.getDepartment(deptID)
                           .getDepartmentStats();
    }
}
