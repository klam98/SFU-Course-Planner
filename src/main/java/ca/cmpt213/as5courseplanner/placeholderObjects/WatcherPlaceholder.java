package ca.cmpt213.as5courseplanner.placeholderObjects;

/**
 * A place holder object to store the fields for when we call @RequestParam
 */

public class WatcherPlaceholder {
    public long deptId;
    public long courseId;

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}