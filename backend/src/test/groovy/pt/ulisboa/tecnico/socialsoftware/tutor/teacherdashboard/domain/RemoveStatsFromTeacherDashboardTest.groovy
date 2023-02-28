package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException


@DataJpaTest
class RemoveStatsFromTeacherDashboardTest extends SpockTest {
    def teacher
    def teacherDashboard

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
        teacher.addCourse(externalCourseExecution)
        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
    }

    def "remove student stats from teacher dashboard"() {
        given: "student stats in teacher dashboard"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats)

        and:
        def previousNumberStudentStats = teacherDashboard.getStudentStats().size()

        when: "removing student stats from dashboard"
        studentStats.remove()

        then: "it gets removed from dashboard"
        teacherDashboard.getStudentStats().size() == previousNumberStudentStats - 1
        !teacherDashboard.getStudentStats().contains(studentStats)
    }

    def "remove student stats from teacher dashboard it isn't part of"() {
        given: "student stats in teacher dashboard"
        def studentStats = new StudentStats()
        studentStats.setCourseExecution(externalCourseExecution)
        studentStatsRepository.save(studentStats)

        and:
        def previousNumberStudentStats = teacherDashboard.getStudentStats().size()

        when: "removing student stats from dashboard"
        teacherDashboard.removeStudentStats(studentStats)

        then: "it gets removed from dashboard"
        teacherDashboard.getStudentStats().size() == previousNumberStudentStats
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.STUDENT_STATS_NOT_FOUND
    }
    
    def createQuizStats() {
        def quizStats = new QuizStats(externalCourseExecution, teacherDashboard)
        quizStatsRepository.save(quizStats)
        return quizStats
    }

    def "remove a QuizStats"() {
        given: "a QuizStats"
        def quizStats = createQuizStats()

        when: "a QuizStats is removed"
        quizStats.remove()

        then: "the QuizStats is removed"
        teacherDashboard.getQuizStats().size() == 0
    }

    def "cannot remove quizStats that does not belong to dashboard"() {
        given: "Add a QuizStats and remove it from the dashboard"
        def quizStats = createQuizStats()
        teacherDashboard.removeQuizStats(quizStats)

        when: "the QuizStats is removed for the second time"
        teacherDashboard.removeQuizStats(quizStats)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.QUIZ_STATS_NOT_FOUND
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}
