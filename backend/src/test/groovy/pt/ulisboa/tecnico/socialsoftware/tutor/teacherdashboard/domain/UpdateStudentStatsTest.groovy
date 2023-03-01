package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class UpdateStudentStatsTest extends SpockTest {
    def teacher
    def teacherDashboard
    def externalCourse2
    def externalCourseExecution2
    def student1
    def student2
    def student3

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)

        student1 = new Student(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(student1)
        student2 = new Student(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(student2)
        student3 = new Student(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(student3)

        externalCourse2 = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse2)

        externalCourseExecution2 = new CourseExecution(externalCourse2, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        externalCourseExecution.addUser(student1)
        externalCourseExecution.addUser(student2)
        externalCourseExecution2.addUser(student2)
        externalCourseExecution2.addUser(student3)
    }

    def "update students statistic"() {
        given: "student stats of course"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats)

        when: "updating statistic"
        studentStats.update()

        then: "it has correct stats values"
        studentStats.getNumStudents() == 2

        and: "string representation is correct"
        studentStats.toString() == "StudentStats{" +
                "id=" +
                studentStats.getId() +
                ", courseExecution=" +
                externalCourseExecution.toString() +
                ", numStudents=2}"
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}
