package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Department class represents all the courses a department teaches
 */

public class Department implements Iterable<Course> {
    private long deptId;
    private String name;
    private List<Course> courseList = new ArrayList<>();
    private List<Integer> semesterCodesList = new ArrayList<>();

    @Override
    public Iterator<Course> iterator() {
        return courseList.iterator();
    }

    public Department(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Course getCourse(long courseId) {
        for (Course eachCourse : courseList) {
            if (eachCourse.getCourseId() == courseId) {
                return eachCourse;
            }
        }
        return null;
    }

    public long getDepartmentId() {
        return deptId;
    }

    public void setDepartmentId(long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    @JsonIgnore
    public void sortCourseList() {
        Collections.sort(courseList, (course1, course2) ->
                course1.getCatalogNumber().compareTo(course2.getCatalogNumber()));
    }

    public void addToCourseList(Course newCourse, CourseOffering newOffering, Section newSection) {
        // if duplicate course; group them together via its course offering
        for (Course eachCourse : courseList) {
            if (eachCourse.getCatalogNumber().equals(newCourse.getCatalogNumber())) {
                eachCourse.addCourseOfferingToList(newOffering, newSection);
                return;
            }
        }

        // populate semesterCodesList each time a new courseOffering is added
        semesterCodesList.add(newOffering.getSemesterCode());

        // otherwise unique course offering; add it to courseOfferingList
        courseList.add(newCourse);
        sortCourseList();
    }

    public List<GraphData> getDepartmentStats() {
        List<GraphData> dataPoints = new ArrayList<>();

        for (Integer eachSemesterCode : semesterCodesList) {
            int enrollmentTotal = 0;

            for (Course eachCourse : courseList) {
                enrollmentTotal += eachCourse.getNumEnrolledBySemester(eachSemesterCode);
            }

            GraphData newPoint = new GraphData(enrollmentTotal, eachSemesterCode);
            dataPoints.add(newPoint);
        }

        Collections.sort(dataPoints, (point1, point2) -> point1.getSemesterCode() - point2.getSemesterCode());

        return dataPoints;
    }
}
