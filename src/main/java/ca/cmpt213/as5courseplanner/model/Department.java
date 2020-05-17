package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Department class represents all the courses a department teaches
 */

public class Department implements Comparable<Department>, Iterable<Course>{
    private long deptId;
    private String name;
    private List<Course> courseList = new ArrayList<>();

    @Override
    public int compareTo(Department other) {
        return name.compareTo(other.name);
    }

    @Override
    public Iterator<Course> iterator() {
        return courseList.iterator();
    }

    public Department() { }
    public Department(String name) {
        this.name = name.trim();
    }

    @JsonIgnore
    public List<Course> getCourseList() {
        return courseList;
    }

    public Course getCourse(long courseId) {
        for (Course eachCourse: courseList) {
            if (eachCourse.getCourseId() == courseId) {
                return eachCourse;
            }
        }
        return null;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addToCourseList(Course newCourse, Offering newOffering, Section newSection) {
        // check for duplicate course; if there is, let Offering check for duplicates
        for (Course eachCourse: courseList) {
            if (eachCourse.getCatalogNumber().equals(newCourse.getCatalogNumber())) {
                eachCourse.addToOfferingList(newOffering, newSection);
                return;
            }
        }
        // if not duplicate, add to course list
        newCourse.addToOfferingList(newOffering, newSection);
        courseList.add(newCourse);
    }

    public int getFirstSemesterCode() {
        int firstSemester = 0;
        for (Course eachCourse: courseList) {
            int semesterCode = eachCourse.getOfferingList().get(0).getSemesterCode();

            if (semesterCode < firstSemester || firstSemester == 0) {
                firstSemester = semesterCode;
            }
        }

        return firstSemester;
    }

    public int getLastSemesterCode() {
        int lastSemester = 0;
        for (Course eachCourse: courseList) {
            int semesterCode = eachCourse.getOfferingList().get(eachCourse.getOfferingList().size() - 1)
                                         .getSemesterCode();

            if (semesterCode > lastSemester) {
                lastSemester = semesterCode;
            }
        }

        return lastSemester;
    }
}