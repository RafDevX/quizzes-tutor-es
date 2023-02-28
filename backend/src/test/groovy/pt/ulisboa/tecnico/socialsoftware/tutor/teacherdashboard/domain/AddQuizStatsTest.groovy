package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest


@DataJpaTest
class AddQuizStatsTest extends SpockTest {
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

    def "add duplicate QuizStats to dashboard"() {
        given: "a QuizStats is created"
        def quizStats = new QuizStats(externalCourseExecution, teacherDashboard)
        quizStatsRepository.save(quizStats)

        when: "try to add the same QuizStats to the dashboard"
        teacherDashboard.addQuizStats(quizStats)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.QUIZ_STATS_ALREADY_EXISTS
    }

    def "add QuizStats with different course to dashboard"() {
        given: "a new Course is created"
        def externalCourse2 = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse2)

        and: "a new CourseExecution is created"
        def externalCourseExecution2 = new CourseExecution(externalCourse2, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        when: "multiple QuizStats are added to the dashboard"
        def quizStats1 = new QuizStats(externalCourseExecution, teacherDashboard)
        quizStatsRepository.save(quizStats1)
        def quizStats2 = new QuizStats(externalCourseExecution2, teacherDashboard)
        quizStatsRepository.save(quizStats2)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.QUIZ_STATS_INCORRECT_COURSE
    }


    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}