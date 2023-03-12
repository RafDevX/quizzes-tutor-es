package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

import java.time.LocalDateTime

@DataJpaTest
class UpdateQuizStatsTest extends SpockTest {

    def teacher
    def dashboard
    def quizStats
    def Student student1
    def Student student2

    def setup() {
        createExternalCourseAndExecution()

        createQuiz(1, LocalDateTime.now())
        createQuiz(2, LocalDateTime.MAX)

        teacher = new Teacher(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.EXTERNAL)
        userRepository.save(teacher)
        student1 = new Student(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, false, AuthUser.Type.EXTERNAL)
        userRepository.save(student1)
        student2 = new Student(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL, false, AuthUser.Type.EXTERNAL)
        userRepository.save(student2)
    }

    def createTeacherDashboard() {
        dashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(dashboard)
    }

    def createQuizStats() {
        quizStats = new QuizStats(externalCourseExecution, dashboard)
        quizStatsRepository.save(quizStats)
    }

    def createQuiz(int key, LocalDateTime dateTime) {
        Quiz quiz = new Quiz()
        quiz.setKey(key)
        quiz.setType(Quiz.QuizType.GENERATED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quiz.setAvailableDate(dateTime)
        quizRepository.save(quiz)
    }

    def createQuizAnswer(Quiz quiz, Student student, boolean completed) {
        def quizAnswer = new QuizAnswer(student, quiz)
        quizAnswer.setCompleted(completed)
        quizAnswerRepository.save(quizAnswer)

        return quizAnswer
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
        quizStats.getUniqueQuizzesSolved() == 0
        quizStats.getAverageQuizzesSolved() == 0

        and: "the string representation is correct"
        quizStats.toString() == "QuizStats{" +
                "id=" + quizStats.getId() +
                ", courseExecution=" +
                quizStats.getCourseExecution() +
                ", numQuizzes=1" +
                ", uniqueQuizzesSolved=0" +
                ", averageQuizzesSolved=0.0" +
                "}"
    }

    def "update quiz stats with students"() {
        given: "students being inserted in the course"
        student1.addCourse(externalCourseExecution)
        student2.addCourse(externalCourseExecution)

        and: "a dashboard"
        createTeacherDashboard()

        and: "add quiz stats to the dashboard"
        createQuizStats()

        and: "create a quiz answer"
        createQuizAnswer(quizRepository.findAll().get(0), student1, true)

        when: "updating statistic"
        quizStats.update()

        then: "the quiz stats has correct stats values"
        quizStats.getNumQuizzes() == 1
        quizStats.getUniqueQuizzesSolved() == 1
        quizStats.getAverageQuizzesSolved() == 0.5

        and: "the string representation is correct"
        quizStats.toString() == "QuizStats{" +
                "id=" + quizStats.getId() +
                ", courseExecution=" +
                quizStats.getCourseExecution() +
                ", numQuizzes=1" +
                ", uniqueQuizzesSolved=1" +
                ", averageQuizzesSolved=" +
                quizStats.getAverageQuizzesSolved() +
                "}"
    }

    def "update quiz stats with different course executions" ()
    {
        given: "a second course execution"
        def externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_1_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        and: "add students to the both course executions"
        student1.addCourse(externalCourseExecution)
        student2.addCourse(externalCourseExecution)
        student1.addCourse(externalCourseExecution2)
        student2.addCourse(externalCourseExecution2)

        and: "create a quiz for the second course execution"
        def quiz2 = new Quiz()
        quiz2.setKey(3)
        quiz2.setType(Quiz.QuizType.GENERATED.toString())
        quiz2.setCourseExecution(externalCourseExecution2)
        quiz2.setAvailableDate( LocalDateTime.now())
        quizRepository.save(quiz2)

        and: "create a dashboard for the first course execution"
        createTeacherDashboard()

        and: "create a dashboard for the second course execution"
        def dashboard2 = new TeacherDashboard(externalCourseExecution2, teacher)
        teacherDashboardRepository.save(dashboard2)

        and: "create quiz stats for the first course execution"
        createQuizStats()

        and: "create quiz stats for the second course execution"
        def quizStats2 = new QuizStats(externalCourseExecution2, dashboard)
        quizStatsRepository.save(quizStats2)

        and: "create a quiz answer for the first course execution"
        createQuizAnswer(quizRepository.findAll().get(0), student1, true)

        when: ("updating statistic")
        quizStats.update()
        quizStats2.update()

        then: "the quiz stats of the first execution has correct stats values"
        quizStats.getNumQuizzes() == 1
        quizStats.getUniqueQuizzesSolved() == 1
        quizStats.getAverageQuizzesSolved() == 0.5

        and: "the quiz stats of the second execution has correct stats values"
        quizStats2.getNumQuizzes() == 1
        quizStats2.getUniqueQuizzesSolved() == 0
        quizStats2.getAverageQuizzesSolved() == 0
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
