package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@DataJpaTest
class AddStatsToTeacherDashboardTest extends SpockTest {
    def teacher
    def teacherDashboard

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(teacher)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
    }

    def "add student stats to teacher dashboard"() {
        given:
        def previousNumberStudentStats = teacherDashboard.getStudentStats().size()

        and: "additional course execution"
        def externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        when: "multiple student stats are added to teacher dashboard"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats)
        def studentStats2 = new StudentStats(externalCourseExecution2, teacherDashboard)
        studentStatsRepository.save(studentStats2)

        then: "it gets added successfully"
        teacherDashboard.getStudentStats().size() == previousNumberStudentStats + 2
        teacherDashboard.getStudentStats().contains(studentStats)

        and: "student stats' teacher dashboards are correct"
        studentStats.getTeacherDashboard() == teacherDashboard
        studentStats2.getTeacherDashboard() == teacherDashboard

        and: "students stats' teacher dashboards string representation are correct"
        teacherDashboard.toString() == "Dashboard{" +
                "id=" +
                teacherDashboard.getId() +
                ", courseExecution=" + externalCourseExecution +
                ", teacher=" + teacher +
                ", studentStats=[" +
                studentStats + ", " + studentStats2 +
                ']}';

    }

    def "add duplicate student stats to teacher dashboard"() {
        given: "teacher dashboard with one student stats"
        def previousNumberStudentStats = teacherDashboard.getStudentStats().size()
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats)

        when: "duplicate student stats is added to teacher dashboard"
        def studentStats2 = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats2)

        then:
        def error = thrown(TutorException)
        teacherDashboard.getStudentStats().size() == previousNumberStudentStats + 1
        error.getErrorMessage() == ErrorMessage.STUDENT_STATS_ALREADY_EXISTS
    }

    def "add student stats with different course to teacher dashboard"() {
        given:
        def previousNumberStudentStats = teacherDashboard.getStudentStats().size()

        and: "additional course execution"
        def externalCourse2 = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse2)
        def externalCourseExecution2 = new CourseExecution(externalCourse2, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        when: "multiple student stats are added to teacher dashboard"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats)
        def studentStats2 = new StudentStats(externalCourseExecution2, teacherDashboard)
        studentStatsRepository.save(studentStats2)

        then:
        teacherDashboard.getStudentStats().size() == previousNumberStudentStats + 1
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.STUDENT_STATS_INCORRECT_COURSE
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}
