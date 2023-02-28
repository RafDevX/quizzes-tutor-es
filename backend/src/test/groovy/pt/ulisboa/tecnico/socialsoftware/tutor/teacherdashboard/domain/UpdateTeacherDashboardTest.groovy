package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {
    def teacher
    def teacherDashboard
    def externalCourseExecution2

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
    }

    def "update teacher dashboard stats"() {
        given: "student statistics in the teacher dashboard"
        def studentStats1 = Mock(StudentStats)
        studentStats1.getCourseExecution() >> externalCourseExecution
        teacherDashboard.addStudentStats(studentStats1)
        def studentStats2 = Mock(StudentStats)
        studentStats2.getCourseExecution() >> externalCourseExecution2
        teacherDashboard.addStudentStats(studentStats2)

        when: "updating dashboard"
        teacherDashboard.update()

        then: "update method is called (once) for every student stats"
        1 * studentStats1.update()
        1 * studentStats2.update()
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}
