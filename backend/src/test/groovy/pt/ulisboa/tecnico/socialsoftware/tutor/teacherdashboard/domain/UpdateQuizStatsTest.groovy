package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler

@DataJpaTest
class UpdateQuizStatsTest extends SpockTest {

    def teacher
    def dashboard
    def quizStats
    def Student student1
    def Student student2

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.EXTERNAL)
        userRepository.save(teacher)
        student1 = new Student(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, false, AuthUser.Type.EXTERNAL)
        userRepository.save(student1)
        student2 = new Student(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL, false, AuthUser.Type.EXTERNAL)
        userRepository.save(student2)

        Quiz quiz = new Quiz()
        quiz.setKey(1)
        quiz.setType(Quiz.QuizType.GENERATED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)

    }

    def createTeacherDashboard() {
        dashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(dashboard)
    }

    def createQuizStats() {
        quizStats = new QuizStats(externalCourseExecution, dashboard)
        quizStatsRepository.save(quizStats)
    }

    def "update quiz stats with no students"() {
        given: "a dashboard, a user and a quiz"
        createTeacherDashboard()

        and: "add quiz stats to the dashboard"
        createQuizStats()

        when: "updating statistic"
        quizStats.update()

        then: "the quiz stats has correct stats values"
        quizStats.getNumQuizzes() == 1
        quizStats.getAverageQuizzesSolved() == 0

        and: "the string representation is correct"
        quizStats.toString() == "QuizStats{" +
                "id=" + quizStats.getId() +
                ", courseExecution=" +
                quizStats.getCourseExecution() +
                ", numQuizzes=1" +
                ", averageQuizzesSolved=0.0" +
                "}"
    }

    def "update quiz stats with students"() {
        given: "students being inserted in the course"
        student1.addCourse(externalCourseExecution)
        student2.addCourse(externalCourseExecution)

        and: "a dashboard, a user and a quiz"
        createTeacherDashboard()
        def studentId = student1.getId()
        def quizId = quizRepository.findAll().get(0).getId()

        and: "add quiz stats to the dashboard"
        createQuizStats()

        and: "create a quiz answer"
        answerService.createQuizAnswer(studentId, quizId)

        when: "updating statistic"
        quizStats.update()

        then: "the quiz stats has correct stats values"
        quizStats.getNumQuizzes() == 1
        quizStats.getAverageQuizzesSolved() == 0.5

        and: "the string representation is correct"
        quizStats.toString() == "QuizStats{" +
                "id=" + quizStats.getId() +
                ", courseExecution=" +
                quizStats.getCourseExecution() +
                ", numQuizzes=1" +
                ", averageQuizzesSolved=0.5" +
                "}"
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
