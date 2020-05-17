package ca.cmpt213.as5courseplanner.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ModelDumpParser class is responsible for parsing a CSV file and writing its data to the terminal
 */

public class ModelDumpParser implements Iterable<Department>{
    private List<Department> departmentList = new ArrayList<>();
    private File file;

    private static final int SEMESTER_CODE_INDEX = 0;
    private static final int DEPARTMENT_INDEX = 1;
    private static final int COURSE_NUMBER_INDEX = 2;
    private static final int LOCATION_INDEX = 3;
    private static final int ENROLLMENT_CAP_INDEX = 4;
    private static final int ENROLLMENT_TOTAL_INDEX = 5;
    private static final int INSTRUCTOR_INDEX = 6;

    private AtomicLong courseId = new AtomicLong();
    private AtomicLong courseOfferingId = new AtomicLong();

    @Override
    public Iterator<Department> iterator() {
        return departmentList.iterator();
    }

    public ModelDumpParser() throws FileNotFoundException{
        this.parseFile();
    }

    public ModelDumpParser(File file) throws FileNotFoundException{
        this.file = file;
        this.parseFile();
    }

    public Department getDepartment(long deptId) {
        for (Department eachDept : departmentList) {
            if (eachDept.getDeptId() == deptId) {
                return eachDept;
            }
        }
        return null;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public long getCourseId() {
        return courseId.get();
    }

    public long incrementAndGetCourseId() {
        return courseId.incrementAndGet();
    }

    public long getCourseOfferingId() {
        return courseOfferingId.get();
    }

    public long incrementAndGetOfferingId() {
        return courseOfferingId.incrementAndGet();
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    public void parseFile() throws FileNotFoundException {

        try (Scanner fileReader = new Scanner(file)) {
            // skip headers
            fileReader.nextLine();

            // reads in the fields of the CSV File
            while (fileReader.hasNextLine()) {
                String[] fields = fileReader.nextLine().split(",");
                final int COMPONENT_INDEX = fields.length - 1;

                List<String> courseOfferingFields = new ArrayList<>();
                courseOfferingFields.add(fields[LOCATION_INDEX]);
                courseOfferingFields.addAll(Arrays.asList(fields).subList(INSTRUCTOR_INDEX, COMPONENT_INDEX));
                courseOfferingFields.add(fields[SEMESTER_CODE_INDEX]);

                List<String> componentFields = new ArrayList<>();
                componentFields.add(fields[ENROLLMENT_CAP_INDEX]);
                componentFields.add(fields[ENROLLMENT_TOTAL_INDEX]);
                componentFields.add(fields[fields.length - 1]);

                Department department = new Department(fields[DEPARTMENT_INDEX]);
                Course course = new Course(fields[COURSE_NUMBER_INDEX], courseId.incrementAndGet());
                Offering offering = new Offering(courseOfferingFields, courseOfferingId.incrementAndGet());
                Section section = new Section(componentFields);

                addToDepartmentList(department, course, offering, section);
            }

            fileReader.close();
            sortEverything();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            exitProgram();
        }
    }


    public void sortEverything() {
        Collections.sort(departmentList);

        for (Department eachDept : departmentList) {
            Collections.sort(eachDept.getCourseList());

            for (Course eachCourse : eachDept) {
                Collections.sort(eachCourse.getOfferingList());

                for (Offering eachOffering : eachCourse) {
                    Collections.sort(eachOffering.getSectionList());
                }
            }
        }
    }

    public void addToDepartmentList(Department newDepartment, Course newCourse,
                                    Offering newOffering, Section newSection) {
        //Check for duplicate departments; if there is, let Course check for duplicates
        for(Department department: departmentList) {
            if(newDepartment.getName().equals(department.getName())) {
                department.addToCourseList(newCourse, newOffering, newSection);
                return;
            }
        }
        //If not duplicate, add to departments list
        newDepartment.setDeptId(departmentList.size());
        departmentList.add(newDepartment);
    }

    // print all the courses in the list to the terminal
    public String modelDump() {
        StringBuilder printCourses = new StringBuilder();

        for (Department eachDept : departmentList) {
            for (Course eachCourse : eachDept) {
                printCourses.append(eachDept.getName() + " " + eachCourse.getCatalogNumber() + "\n");

                for (Offering eachOffering : eachCourse) {
                    printCourses.append("\t" + eachOffering.getSemesterCode() + " in "
                            + eachOffering.getLocation() + " by "
                            + eachOffering.getInstructors() + "\n");

                    for (Section eachSection : eachOffering) {
                        printCourses.append("\t\t" + "Type=" + eachSection.getType() + ", Enrollment="
                                + eachSection.getEnrollmentTotal() + "/"
                                + eachSection.getEnrollmentCap() + "\n");
                    }
                }
            }
        }

        return printCourses.toString();
    }

    // only ModelDumpParser class should be able to call this method
    private void exitProgram() {
        final int FAILURE = -1;
        System.out.println("File was not found!");
        System.out.println("Program will now exit.");
        System.exit(FAILURE);
    }
}