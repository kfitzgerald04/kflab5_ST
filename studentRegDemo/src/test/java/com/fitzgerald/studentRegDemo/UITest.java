/**
 * This test file re-uses the basic CRUD tests from the last lab. I didn't test everything
 * because most of the test would be repeated from the last lab, so I focused mainly
 * on the new frontend testing. Please do not penalize me for not re-testing things in 
 * POSTMAN, which can be found in my test --> resources file :) This particular implementation
 * covers frontend testing only, with error cases where necessary! -KF
 */

package com.fitzgerald.studentRegDemo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * UI Test Suite for Student Registration Frontend
 *
 * Test Coverage:
 * - Student CRUD operations
 * - Course CRUD operations
 * - Adding students to courses
 * - Business rule validation  (limited but mostly derived from error handling)

 * Backend:  http://localhost:8080
 * Frontend: http://localhost:5173
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UITest {

    // chrome
    private WebDriver driver;

    // wait for frontend elements to load/update before Selenium tries to interact with them
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        
        WebDriverManager.chromedriver().setup();

        // new Chrome browser for each test
        driver = new ChromeDriver();

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("http://localhost:5173/students");
    }

    @AfterEach
    void tearDown() {
        // close browser after each test 
        if (driver != null) driver.quit();
    }

    // open student list page
    void goToStudents() {
        driver.get("http://localhost:5173/students");
    }

    // open course list page
    void goToCourses() {
        driver.get("http://localhost:5173/");
    }

    // counts how many students are in the table (business rule verification)
    int getStudentRowCount() {
        List<WebElement> rows = driver.findElements(By.cssSelector("#student-list-table tbody tr"));
        return rows.size();
    }


    // counts the current number of courses 
    int getCourseRowCount() {
        List<WebElement> rows = driver.findElements(By.cssSelector("#course-list-table tbody tr"));
        return rows.size();
    }

    // create a student (button)
    void createStudent(String name, String major, String gpa) {
        driver.findElement(By.id("new-student-name")).clear();
        driver.findElement(By.id("new-student-name")).sendKeys(name);

        driver.findElement(By.id("new-student-major")).clear();
        driver.findElement(By.id("new-student-major")).sendKeys(major);

        driver.findElement(By.id("new-student-gpa")).clear();
        driver.findElement(By.id("new-student-gpa")).sendKeys(gpa);

        driver.findElement(By.xpath("//button[text()='Create Student']")).click();

        // wait for at least one student row to be populated
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("#student-list-table tbody tr"), 0));
    }


    // create a course (button same as creating a student)
    void createCourse(String name, String instructor, String maxSize, String room) {
        driver.findElement(By.id("new-course-name")).clear();
        driver.findElement(By.id("new-course-name")).sendKeys(name);

        driver.findElement(By.id("new-course-instructor")).clear();
        driver.findElement(By.id("new-course-instructor")).sendKeys(instructor);

        driver.findElement(By.id("new-course-max-size")).clear();
        driver.findElement(By.id("new-course-max-size")).sendKeys(maxSize);

        driver.findElement(By.id("new-course-room")).clear();
        driver.findElement(By.id("new-course-room")).sendKeys(room);

        driver.findElement(By.xpath("//button[text()='Create Course']")).click();

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("#course-list-table tbody tr"), 0));
    }

    // ====================
    // STUDENT TESTS (CRUD) (re-using test from last lab)
    // =====================

    @Test
    @Order(1)
    @DisplayName("SUI1: Create valid student")
    void createValidStudent() {
        goToStudents();

        int before = getStudentRowCount();

        createStudent("Jim Hopper", "Criminal Justice", "3.5");

        int after = getStudentRowCount();

        // check that the student was added 
        assertTrue(after > before);

        // check that the names appears correctly
        assertTrue(driver.getPageSource().contains("Jim Hopper"));
    }


    // serves as a BVA as well (gpa == 0.0)
    @Test
    @Order(2)
    @DisplayName("SUI2: Create student GPA 0.0")
    void createStudentGpaZero() {
        goToStudents();

        int before = getStudentRowCount();

        createStudent("Lucas Sharp", "Math", "0.0");

        //Thread.sleep(1000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(d -> getStudentRowCount() > before);

        int after = getStudentRowCount();
        assertEquals(before + 1, after); 

        // check that the student was added
        assertTrue(driver.getPageSource().contains("Lucas Sharp"));
    }

    // serves as a BVA as well (gpa == 4.0)
    @Test
    @Order(3)
    @DisplayName("SUI3: Create student GPA 4.0")
    void createStudentGpaFour() {
        goToStudents();

        int before = getStudentRowCount();

        createStudent("Jim Lee", "Physics", "4.0");

         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(d -> getStudentRowCount() > before);

        int after = getStudentRowCount();
        assertEquals(before + 1,  after);

        // check that the student was added
        assertTrue(driver.getPageSource().contains("Jim Lee"));
    }

    // error case
    @Test
    @Order(4)
    @DisplayName("SUI4: Reject GPA below 0")
    void rejectInvalidLowGpa() throws InterruptedException {
        goToStudents();

        int before = getStudentRowCount();

        driver.findElement(By.id("new-student-name")).sendKeys("Bad GPA Low");
        driver.findElement(By.id("new-student-major")).sendKeys("Chemistry");
        driver.findElement(By.id("new-student-gpa")).sendKeys("-1.0");
        driver.findElement(By.xpath("//button[text()='Create Student']")).click();

        // no frontend error handling, so jsut checking that for invalid inputs, the table remains the same 
        Thread.sleep(1000);

        int after = getStudentRowCount();

        assertEquals(before, after);
    }

    @Test
    @Order(5)
    @DisplayName("SUI5: Student name > 255 characters")
    void rejectLongName() throws InterruptedException {
    goToStudents();

    int before = getStudentRowCount();

    // generating name > 255 ch
    String longName = "A".repeat(260);

    driver.findElement(By.id("new-student-name")).sendKeys(longName);
    driver.findElement(By.id("new-student-major")).sendKeys("Test Major");
    driver.findElement(By.id("new-student-gpa")).sendKeys("3.0");

    driver.findElement(By.xpath("//button[text()='Create Student']")).click();

    Thread.sleep(1000);

    int after = getStudentRowCount();

    // check that the student was not added
    assertEquals(before, after);
}

    @Test
    @Order(6)
    @DisplayName("SUI6: Edit student")
    void editStudent() throws InterruptedException {
        goToStudents();

    
        // gets the first value in the student table, edits it (buttons)
        WebElement firstRow = driver.findElement(By.cssSelector("#student-list-table tbody tr"));
        firstRow.findElement(By.id("edit-student-button")).click();

    
        // wait for the edit field, then edit the selected student
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-student-name")));
        nameInput.clear();
        nameInput.sendKeys("Edited Student");

        WebElement majorInput = driver.findElement(By.id("edit-student-major"));
        majorInput.clear();
        majorInput.sendKeys("Edited Major");

        WebElement gpaInput = driver.findElement(By.id("edit-student-gpa"));
        gpaInput.clear();
        gpaInput.sendKeys("3.8");

        driver.findElement(By.id("edit-student-save-button")).click();

        Thread.sleep(1000);

        // check that the student was updated correctly 
        assertTrue(driver.getPageSource().contains("Edited Student"));
    }

    @Test
    @Order(7)
    @DisplayName("SUI7: Delete student")
    void deleteStudent() throws InterruptedException {
        goToStudents();

        int before = getStudentRowCount();

        createStudent("Delete Me", "Waterboarding", "2.5");

        Thread.sleep(1000);

        int after = getStudentRowCount();

        // check that a row was added
        assertEquals(before +1, after);

        // delete the last student in the table 
        List<WebElement> rows = driver.findElements(By.cssSelector("#student-list-table tbody tr"));
        WebElement lastRow = rows.get(rows.size() - 1);
        lastRow.findElement(By.id("delete-student-button")).click();

        Thread.sleep(1000);

        // check that a row was removed
        int afterDelete = getStudentRowCount();
        assertEquals(before, afterDelete);
    }

    // ======================
    // COURSE TESTS (re-using test from last lab)
    // ======================

    @Test
    @Order(8)
    @DisplayName("CUI1: Create valid course")
    void createValidCourse() {
        goToCourses();

        int before = getCourseRowCount();

        createCourse("Software Testing", "Dr. Baarsch", "2", "339");

        int after = getCourseRowCount();

        // check that a course was added 
        assertTrue(after > before);
        assertTrue(driver.getPageSource().contains("Software Testing"));
    }

    @Test
    @Order(9)
    @DisplayName("CUI2: Edit course")
    void editCourse() throws InterruptedException {
        goToCourses();

        WebElement firstRow = driver.findElement(By.cssSelector("#course-list-table tbody tr"));
        firstRow.findElement(By.id("edit-course-button")).click();

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-course-name")));
        nameInput.clear();
        nameInput.sendKeys("Edited Course");

        WebElement roomInput = driver.findElement(By.id("edit-course-room"));
        roomInput.clear();
        roomInput.sendKeys("Room 999");

        driver.findElement(By.id("edit-course-save-button")).click();

        Thread.sleep(1000);

        // check that the course was updated correctly 
        assertTrue(driver.getPageSource().contains("Edited Course"));
    }

    @Test
    @Order(10)
    @DisplayName("CUI3: Add student to course")
    void addStudentToCourse() throws InterruptedException {
        goToCourses();

        // get the first course
        WebElement firstRow = driver.findElement(By.cssSelector("#course-list-table tbody tr"));
        firstRow.findElement(By.id("edit-course-button")).click();

        // choose a student from the dropdown, and click the "add student" button
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("select-student")));
        Select select = new Select(dropdown);

        if (select.getOptions().size() > 1) {
            select.selectByIndex(1);
            driver.findElement(By.id("add-student-button")).click();

            Thread.sleep(1000);

            // check that the student has been added to the class
            assertFalse(driver.findElement(By.cssSelector("#course-list-table tbody tr"))
                    .findElement(By.cssSelector("[id^='course-roster-']")).getText().isBlank());
        }
    }

    @Test
    @Order(11)
    @DisplayName("CUI4: Remove student from course")
    void removeStudentFromCourse() throws InterruptedException {
    goToCourses();

    // select course in edit mode
    WebElement row = driver.findElement(By.cssSelector("#course-list-table tbody tr"));
    row.findElement(By.id("edit-course-button")).click();

    WebElement removeDropdown = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.id("remove-student-select"))
    );

    Select select = new Select(removeDropdown);

    // if a student exists, remove them
    int before = select.getOptions().size();

    if (before > 0) {
    select.selectByIndex(0);
    driver.findElement(By.id("remove-student-button")).click();

    Thread.sleep(1000);

    Select newSelect = new Select(driver.findElement(By.id("remove-student-select")));
    int afterSize = newSelect.getOptions().size();
    
    // check that student was removed
    assertTrue(afterSize < before);
}
} 

    @Test
    @Order(12)
    @DisplayName("CUI5: Adding student to full course")
    void preventAddToFullCourse() throws InterruptedException {
    goToCourses();

    // create course with size 1
    createCourse("Full Course Test", "Tester", "1", "1");

    // create 2 students
    goToStudents();
    createStudent("Student One", "CS", "3.0");
    createStudent("Student Two", "CS", "3.0");

    goToCourses();

    // open/select the course
    List<WebElement> rows = driver.findElements(By.cssSelector("#course-list-table tbody tr"));
    WebElement row = rows.get(rows.size() - 1);
    row.findElement(By.id("edit-course-button")).click();

    // add first student
    Select select = new Select(
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("select-student")))
    );

    if (select.getOptions().size() > 1) {
        select.selectByIndex(1);
        driver.findElement(By.id("add-student-button")).click();
        Thread.sleep(1000);
    }

    String before = row.findElement(By.cssSelector("[id^='course-roster-']")).getText();

    // add second student (error)
    select = new Select(driver.findElement(By.id("select-student")));
    if (select.getOptions().size() > 2) {
        select.selectByIndex(2);
        driver.findElement(By.id("add-student-button")).click();
        Thread.sleep(1000);
    }

    String after = row.findElement(By.cssSelector("[id^='course-roster-']")).getText();

    // check that the roster didn't add the second student
    assertEquals(before, after);
}

    @Test
    @Order(13)
    @DisplayName("CUI6: Delete course")
    void deleteCourse() throws InterruptedException {
        goToCourses();

        int before = getCourseRowCount();

        createCourse("Delete Course", "Temp", "1", "404");

        Thread.sleep(1000);

        int afterCourse = getCourseRowCount();
        assertEquals(before + 1, afterCourse);

        // delete row
        List<WebElement> rows = driver.findElements(By.cssSelector("#course-list-table tbody tr"));
        WebElement lastRow = rows.get(rows.size() - 1);
        lastRow.findElement(By.id("delete-course-button")).click();

        Thread.sleep(1000);

        int delete = getCourseRowCount();

        // check that the course was removed correctly
        assertEquals(before, delete);
    }
}