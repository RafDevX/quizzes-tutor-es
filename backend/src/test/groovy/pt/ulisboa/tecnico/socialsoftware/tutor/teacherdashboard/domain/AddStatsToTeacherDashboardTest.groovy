package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

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
        teacherDashboard.toString() == "TeacherDashboard{" +
                "id=" +
                teacherDashboard.getId() +
                ", courseExecution=" + externalCourseExecution +
                ", teacher=" + teacher +
                ", studentStats=[" +
                studentStats + ", " + studentStats2 +
                "], quizStats=[]" +
                '}';

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

    def "add quiz stats to teacher dashboard"() {
        when: "quiz stats is created"
        def quizStats = new QuizStats(externalCourseExecution, teacherDashboard)
        quizStatsRepository.save(quizStats)

        then: "an empty quiz stats is created"
        quizStatsRepository.count() == 1L
        def result = quizStatsRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == externalCourseExecution.getId()
        result.getTeacherDashboard().getId() == teacherDashboard.getId()
        result.getTeacherDashboard().getTeacher().getId() == teacher.getId()
        result.getCourseExecution().getCourse().getId() == externalCourseExecution.getCourse().getId()

        and: "the string representation is correct"
        teacherDashboard.toString() == "TeacherDashboard{" +
                "id=" + teacherDashboard.getId() +
                ", courseExecution=" + externalCourseExecution +
                ", teacher=" + teacher +
                ", studentStats=[]" +
                ", quizStats=[" + quizStats +
                ']}'
    }

    def "add duplicate quiz stats to teacher dashboard"() {
        given: "a quiz stats is created"
        def quizStats = new QuizStats(externalCourseExecution, teacherDashboard)
        quizStatsRepository.save(quizStats)

        when: "try to add the same quiz stats to the dashboard"
        teacherDashboard.addQuizStats(quizStats)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.QUIZ_STATS_ALREADY_EXISTS
    }

    def "add quiz stats with different course to teacher dashboard"() {
        given: "a new course is created"
        def externalCourse2 = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse2)

        and: "a new course execution is created"
        def externalCourseExecution2 = new CourseExecution(externalCourse2, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        when: "multiple quiz stats are added to the teacher dashboard"
        def quizStats1 = new QuizStats(externalCourseExecution, teacherDashboard)
        quizStatsRepository.save(quizStats1)
        def quizStats2 = new QuizStats(externalCourseExecution2, teacherDashboard)
        quizStatsRepository.save(quizStats2)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.errorMessage == ErrorMessage.QUIZ_STATS_INCORRECT_COURSE
    }

    def "add question stats to teacher dashboard"() {
        given:
        def previousNumberQuestionStats = teacherDashboard.getQuestionStats().size()

        and: "additional course execution"
        def externalCourseExecution2 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        when: "multiple question stats are added to teacher dashboard"
        def questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats)
        def questionStats2 = new QuestionStats(externalCourseExecution2, teacherDashboard)
        questionStatsRepository.save(questionStats2)

        then: "it gets added successfully"
        teacherDashboard.getQuestionStats().size() == previousNumberQuestionStats + 2
        teacherDashboard.getQuestionStats().contains(questionStats)

        and: "question stats' teacher dashboards are correct"
        questionStats.getTeacherDashboard() == teacherDashboard
        questionStats2.getTeacherDashboard() == teacherDashboard

        and: "question stats' teacher dashboards string representations are correct"
        teacherDashboard.toString() == "TeacherDashboard{" +
            "id=" + teacherDashboard.getId() +
            ", courseExecution=" + externalCourseExecution +
            ", teacher=" + teacher +
            ", questionStats=[" +
            questionStats + ", " + questionStats2 +
            "]}";
    }

    def "add duplicate question stats to teacher dashboard"() {
        given: "teacher dashboard with one question stats"
        def previousNumberQuestionStats = teacherDashboard.getQuestionStats().size()
        def questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats)

        when: "duplicate question stats is added to teacher dashboard"
        def questionStats2 = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats2)

        then:
        def error = thrown(TutorException)
        teacherDashboard.getQuestionStats().size() == previousNumberQuestionStats + 1
        error.getErrorMessage() == ErrorMessage.QUESTION_STATS_ALREADY_EXISTS
    }

    def "add question stats with different course to teacher dashboard"() {
        given:
        def previousNumberQuestionStats = teacherDashboard.getQuestionStats().size()

        and: "additional course execution"
        def externalCourse2 = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse2)
        def externalCourseExecution2 = new CourseExecution(externalCourse2, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        when: "multiple question stats are added to teacher dashboard"
        def questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats)
        def questionStats2 = new QuestionStats(externalCourseExecution2, teacherDashboard)
        questionStatsRepository.save(questionStats2)

        then:
        teacherDashboard.getQuestionStats().size() == previousNumberQuestionStats + 1
        def error = thrown(TutorException)
        error.getErrorMessage() == ErrorMessage.QUESTION_STATS_INCORRECT_COURSE
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}
