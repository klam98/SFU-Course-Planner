package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Course class represents each unique course and its entire list of offerings
 */

public class Course implements Iterable<CourseOffering> {
    private long courseId;
    private String catalogNumber;
    private List<CourseOffering> courseOfferingList = new ArrayList<>();

    @Override
    public Iterator<CourseOffering> iterator() {
        return courseOfferingList.iterator();
    }

    public Course(long courseId, String catalogNumber) {
        this.courseId = courseId;
        this.catalogNumber = catalogNumber.trim();
    }

    @JsonIgnore
    public CourseOffering getCourseOffering(long courseOfferingId) {
        for (CourseOffering eachOffering : courseOfferingList) {
            if (eachOffering.getCourseOfferingId() == courseOfferingId) {
                return eachOffering;
            }
        }
        return null;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @JsonIgnore
    public List<CourseOffering> getCourseOfferingList() {
        return courseOfferingList;
    }

    public void setCourseOfferingList(List<CourseOffering> courseOfferingList) {
        this.courseOfferingList = courseOfferingList;
    }

    @JsonIgnore
    public void sortCourseOfferingList() {
        Collections.sort(courseOfferingList, (offering1, offering2) ->
                offering1.getSemesterCode() - offering2.getSemesterCode());
    }

    public void addCourseOfferingToList(CourseOffering newCourseOffering, Section newSection) {
        // if duplicate course offering; group them together via its section
        for (CourseOffering eachOffering : courseOfferingList) {
            if (eachOffering.getSemesterCode() == newCourseOffering.getSemesterCode()
                    && eachOffering.getLocation().equals(newCourseOffering.getLocation())) {
                if (!eachOffering.getInstructorList().equals(newCourseOffering.getInstructorList())) {
                    eachOffering.addInstructorToList(newCourseOffering.getInstructorList());
                    return;
                }
                else {
                    eachOffering.addSectionToList((newSection));
                    return;
                }
            }
        }

        // otherwise unique course offering; add it to courseOfferingList
        courseOfferingList.add(newCourseOffering);
        sortCourseOfferingList();
    }

    public int getNumEnrolledBySemester(int semesterCode) {
        int enrollmentTotal = 0;

        for (CourseOffering eachOffering : courseOfferingList) {
            if (eachOffering.getSemesterCode() == semesterCode) {
                enrollmentTotal += eachOffering.getNumEnrolledToLecture();
            }
        }

        return enrollmentTotal;
    }
}
