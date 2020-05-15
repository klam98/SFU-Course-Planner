package ca.cmpt213.as5courseplanner.model;

import ca.cmpt213.as5courseplanner.exceptions.CourseOfferingNotFoundException;
import ca.cmpt213.as5courseplanner.exceptions.DepartmentNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Watcher implements Observer {
    private long watcherId;
    private Department department;

    // Course object if observable
    private Course course;
    private List<String> events = new ArrayList<>();

    @Override
    public void addUpdate(CourseOffering newOffering, Section newSection) {
        // necessary components that observers inquire info about from courses
        Date date = new Date();
        String currentDate = date.toString();
        String type = newSection.getType();
        String enrollmentTotal = String.valueOf(newSection.getEnrollmentTotal());
        String enrollmentCapacity = String.valueOf(newSection.getEnrollmentCapacity());
        String term = newOffering.getTerm();
        String year = String.valueOf(newOffering.getYear());

        //Build a complete message and add it to our list of events
        String eventMessage = currentDate + ": " + "Added section " + type + " with Enrollment"
                + " (" + enrollmentTotal + "/" + enrollmentCapacity + ") "
                + "to offering " + term + " " + year + ".";

        // add eventMessage to a queue of updates for observers
        events.add(eventMessage);
    }

    // default constructor for empty watcher
    public Watcher() { }

    public Watcher(long watcherId, long deptId, long courseId, ModelDumpParser parser)
                   throws DepartmentNotFoundException, CourseOfferingNotFoundException
    {
        this.watcherId = watcherId;

        // instantiate a department for the watcher if a deptId matches a department found in the list
        for (Department eachDept : parser.getDepartmentList()) {
            if (eachDept.getDepartmentId() == deptId) {
                this.department = eachDept;
            }
        }

        if (this.department == null) {
            throw new DepartmentNotFoundException();
        }

        // instantiate a course for the watcher if a courseId matches a course found in the list
        for (Course eachCourse : department.getCourseList()) {
            if (eachCourse.getCourseId() == courseId) {
                this.course = eachCourse;
            }
        }

        if (this.course == null) {
            throw new CourseOfferingNotFoundException();
        }
    }

    public long getWatcherId() {
        return watcherId;
    }

    public Department getDepartment() {
        return department;
    }

    public Course getCourse() {
        return course;
    }

    public List<String> getEvents() {
        return events;
    }
}
