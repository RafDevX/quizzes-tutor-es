package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll

import java.time.LocalDateTime

@DataJpaTest
class CreateTeacherDashboardTest extends SpockTest {
    def teacher
    def course

    def setup() {
        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        course = new Course(COURSE_2_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course)
    }

    def createCourseExecution(String acronym, String academicTerm, LocalDateTime localDate) {
        externalCourseExecution = new CourseExecution(course, acronym, academicTerm, Course.Type.EXTERNAL, localDate)
        courseExecutionRepository.save(externalCourseExecution)

        return externalCourseExecution
    }

    def "create a dashboard with a single course execution"() {
        given: "a teacher in a course execution"
        def courseExecution = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, LOCAL_DATE_TODAY)
        teacher.addCourse(courseExecution)

        when: "a user creates a dashboard"
        teacherDashboardService.getTeacherDashboard(courseExecution.getId(), teacher.getId())

        then: "a dashboard is created"
        teacherDashboardRepository.count() == 1L
        def result = teacherDashboardRepository.findAll().get(0)
        TeacherDashboard dashboard = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == courseExecution.getId()
        result.getTeacher().getId() == teacher.getId()

        and: "there is exactly ONE quiz stats within the dashboard"
        def quizStats = result.getQuizStats()
        quizStats.size() == 1
        quizStats.get(0).getCourseExecution().getId() == courseExecution.getId()
        quizStatsRepository.getReferenceById(quizStats.get(0).getId()) != null

        and: "there is exactly ONE question stats within the dashboard"
        def questionStats = result.getQuestionStats()
        questionStats.size() == 1
        questionStats.get(0).getCourseExecution().getId() == courseExecution.getId()
        questionStatsRepository.getReferenceById(questionStats.get(0).getId()) != null

        and: "the teacher has a reference for the dashboard"
        teacher.getDashboards().size() == 1
        teacher.getDashboards().contains(result)
    }

    def "create a dashboard with two course executions"() {
        given: "a teacher in two course executions"
        def courseExecution1 = createCourseExecution(COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, LOCAL_DATE_TODAY)
        teacher.addCourse(courseExecution1)
        def courseExecution2 = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, LOCAL_DATE_YESTERDAY)
        teacher.addCourse(courseExecution2)

        when: "a user creates a dashboard"
        teacherDashboardService.getTeacherDashboard(courseExecution1.getId(), teacher.getId())

        then: "a dashboard is created"
        teacherDashboardRepository.count() == 1L
        TeacherDashboard dashboard = teacherDashboardRepository.findAll().get(0)
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == courseExecution1.getId()
        result.getTeacher().getId() == teacher.getId()

        and: "there are exactly TWO quiz stats within the dashboard"
        def quizStats = result.getQuizStats()
        quizStats.size() == 2
        quizStats.get(0).getCourseExecution().getId() == courseExecution1.getId()
        quizStatsRepository.getReferenceById(quizStats.get(0).getId()) != null
        quizStats.get(1).getCourseExecution().getId() == courseExecution2.getId()
        quizStatsRepository.getReferenceById(quizStats.get(1).getId()) != null

        and: "there are exactly TWO question stats within the dashboard"
        def questionStats = result.getQuestionStats()
        questionStats.size() == 2
        questionStats.get(0).getCourseExecution().getId() == courseExecution1.getId()
        questionStatsRepository.getReferenceById(questionStats.get(0).getId()) != null
        questionStats.get(1).getCourseExecution().getId() == courseExecution2.getId()
        questionStatsRepository.getReferenceById(questionStats.get(1).getId()) != null
    }

    def "create a dashboard with four course executions"() {
        given: "a teacher in four course executions"
        def courseExecution1 = createCourseExecution(COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, LOCAL_DATE_TODAY)
        teacher.addCourse(courseExecution1)
        def courseExecution2 = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, LOCAL_DATE_YESTERDAY)
        teacher.addCourse(courseExecution2)
        def courseExecution3 = createCourseExecution(COURSE_3_ACRONYM, COURSE_3_ACADEMIC_TERM, LOCAL_DATE_LATER)
        teacher.addCourse(courseExecution3)
        def courseExecution4 = createCourseExecution(COURSE_4_ACRONYM, COURSE_4_ACADEMIC_TERM, LOCAL_DATE_BEFORE)
        teacher.addCourse(courseExecution4)

        when: "a user creates a dashboard"
        teacherDashboardService.getTeacherDashboard(courseExecution1.getId(), teacher.getId())

        then: "a dashboard is created"
        teacherDashboardRepository.count() == 1L
        TeacherDashboard dashboard = teacherDashboardRepository.findAll().get(0)
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == courseExecution1.getId()
        result.getTeacher().getId() == teacher.getId()

        then: "there are exactly THREE quiz stats within the dashboard"
        def quizStats = dashboard.getQuizStats()
        quizStats.size() == 3
        quizStats.get(0).getCourseExecution().getId() == courseExecution1.getId()
        quizStatsRepository.getReferenceById(quizStats.get(0).getId()) != null
        quizStats.get(1).getCourseExecution().getId() == courseExecution2.getId()
        quizStatsRepository.getReferenceById(quizStats.get(1).getId()) != null
        quizStats.get(2).getCourseExecution().getId() == courseExecution4.getId()
        quizStatsRepository.getReferenceById(quizStats.get(2).getId()) != null

        and: "there are exactly THREE question stats within the dashboard"
        def questionStats = dashboard.getQuestionStats()
        questionStats.size() == 3
        questionStats.get(0).getCourseExecution().getId() == courseExecution1.getId()
        questionStatsRepository.getReferenceById(questionStats.get(0).getId()) != null
        questionStats.get(1).getCourseExecution().getId() == courseExecution2.getId()
        questionStatsRepository.getReferenceById(questionStats.get(1).getId()) != null
        questionStats.get(2).getCourseExecution().getId() == courseExecution4.getId()
        questionStatsRepository.getReferenceById(questionStats.get(2).getId()) != null
    }

    def "cannot create multiple dashboards for a teacher on a course execution"() {
        given: "a teacher in a course execution"
        def courseExecution = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, LOCAL_DATE_TODAY)
        teacher.addCourse(courseExecution)

        and: "an empty dashboard for the teacher"
        teacherDashboardService.createTeacherDashboard(courseExecution.getId(), teacher.getId())

        when: "a second dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 1L

        and: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_ALREADY_HAS_DASHBOARD
    }

    def "cannot create a dashboard for a user that does not belong to the course execution"() {
        given: "a course execution"
        def courseExecution = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, LOCAL_DATE_TODAY)

        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecution.getId(), teacher.getId())

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_NO_COURSE_EXECUTION
    }

    def "cannot create a dashboard with a course execution with null end date"() {
        given: "a course execution with null end date"
        def courseExecution = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, null)
        teacher.addCourse(courseExecution)

        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecution.getId(), teacher.getId())

        then: "dashboard does not have stats for the course execution"
        def dashboard = teacherDashboardRepository.findAll().get(0)
        dashboard.getStudentStats().size() == 0
        dashboard.getQuizStats().size() == 0
        dashboard.getQuestionStats().size() == 0
    }

    @Unroll
    def "cannot create a dashboard with courseExecutionId=#courseExecutionId"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecutionId, teacher.getId())

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_FOUND

        where:
        courseExecutionId << [0, 100]
    }

    @Unroll
    def "cannot create a dashboard with teacherId=#teacherId"() {
        given: "a course execution"
        def courseExecution = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, LOCAL_DATE_TODAY)

        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecution.getId(), teacherId)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.USER_NOT_FOUND

        where:
        teacherId << [0, 100]
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
