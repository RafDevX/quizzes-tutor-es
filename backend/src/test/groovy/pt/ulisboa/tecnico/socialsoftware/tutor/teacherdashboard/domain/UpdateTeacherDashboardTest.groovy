package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {

    def externalCourseExecution2
    def teacher
    def teacherDashboard
    def dashboard
    def quizStats

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.EXTERNAL)
        userRepository.save(teacher)

        externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)

        User user = new Student(USER_1_NAME, false)
        user.addCourse(externalCourseExecution)
        userRepository.save(user)

        Quiz quiz = new Quiz()
        quiz.setKey(1)
        quiz.setType(Quiz.QuizType.GENERATED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)

    }

    def "update teacher dashboard stats"() {
        given: "student statistics in the teacher dashboard"
        def studentStats1 = Mock(StudentStats)
        studentStats1.getCourseExecution() >> externalCourseExecution
        teacherDashboard.addStudentStats(studentStats1)
        def studentStats2 = Mock(StudentStats)
        studentStats2.getCourseExecution() >> externalCourseExecution2
        teacherDashboard.addStudentStats(studentStats2)

        and: "quiz statistics in the teacher dashboard"
        def quizStats1 = Mock(QuizStats)
        quizStats1.getCourseExecution() >> externalCourseExecution
        teacherDashboard.addQuizStats(quizStats1)
        def quizStats2 = Mock(QuizStats)
        quizStats2.getCourseExecution() >> externalCourseExecution2
        teacherDashboard.addQuizStats(quizStats2)

        when: "updating dashboard"
        teacherDashboard.update()

        then: "update method is called (once) for every student stat"
        1 * studentStats1.update()
        1 * studentStats2.update()

        and: "update method is called (once) for every quiz stat"
        1 * quizStats1.update()
        1 * quizStats2.update()
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}
