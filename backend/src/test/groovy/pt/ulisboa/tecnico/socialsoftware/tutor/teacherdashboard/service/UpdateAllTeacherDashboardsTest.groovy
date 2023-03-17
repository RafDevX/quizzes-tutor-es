package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuestionStatsRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuizStatsRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.StudentStatsRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.services.TeacherDashboardService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll

import java.time.LocalDateTime

@DataJpaTest
class UpdateAllTeacherDashboardsTest extends SpockTest {
    def course

    def setup() {
        course = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(course)
    }

    def "update all teacher dashboards with no pre-existing dashboards creates dashboards"() {
        given: "two course executions"
        def courseExecution1 = createCourseExecution(COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, LOCAL_DATE_YESTERDAY)
        def courseExecution2 = createCourseExecution(COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, LOCAL_DATE_TODAY)

        and: "three teachers"
        def teacher1 = createTeacher(1, USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL)
        def teacher2 = createTeacher(2, USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL)
        def teacher3 = createTeacher(3, USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL)

        and: "teachers are added to course executions"
        addTeacherToCourseExecution(teacher1, courseExecution1)
        addTeacherToCourseExecution(teacher2, courseExecution2)
        addTeacherToCourseExecution(teacher3, courseExecution1)
        addTeacherToCourseExecution(teacher3, courseExecution2)

        when: "all teacher dashboards are updated"
        teacherDashboardService.updateAllTeacherDashboards()

        then: "missing teacher dashboards are created"
        teacherDashboardRepository.count() == 4L

        and: "teacher dashboards are created with correct information"
        def dashboards = teacherDashboardRepository.findAll()
        def dashboard11 = dashboards.find { it.courseExecution == courseExecution1 && it.teacher == teacher1 }
        def dashboard13 = dashboards.find { it.courseExecution == courseExecution1 && it.teacher == teacher3 }
        def dashboard22 = dashboards.find { it.courseExecution == courseExecution2 && it.teacher == teacher2 }
        def dashboard23 = dashboards.find { it.courseExecution == courseExecution2 && it.teacher == teacher3 }

        dashboard11 != null
        dashboard13 != null
        dashboard22 != null
        dashboard23 != null

        and: "stats are created"
        def expected = 1 + 2 + 1 + 2
        studentStatsRepository.count() == expected
        quizStatsRepository.count() == expected
        questionStatsRepository.count() == expected
    }

    @Unroll
    def "update all teacher dashboards calls update methods"() {
        given: "three mock teachers"
        def mockTeacher1 = Mock(Teacher)
        mockTeacher1.getId() >> 1
        def mockTeacher2 = Mock(Teacher)
        mockTeacher2.getId() >> 2
        def mockTeacher3 = Mock(Teacher)
        mockTeacher3.getId() >> 3

        and: "a mock course"
        def mockCourse = Mock(Course)
        mockCourse.getId() >> 1
        mockCourse.getQuestions() >> []

        and: "two mock course executions"
        def mockCourseExecution1 = Mock(CourseExecution)
        mockCourseExecution1.getId() >> 1
        mockCourseExecution1.getTeachers() >> [mockTeacher1, mockTeacher3]
        mockCourseExecution1.getCourse() >> mockCourse
        mockCourseExecution1.getEndDate() >> LOCAL_DATE_YESTERDAY
        mockCourseExecution1.getStudents() >> []
        mockCourseExecution1.getQuizzes() >> []
        def mockCourseExecution2 = Mock(CourseExecution)
        mockCourseExecution2.getId() >> 2
        mockCourseExecution2.getTeachers() >> [mockTeacher2, mockTeacher3]
        mockCourseExecution2.getCourse() >> mockCourse
        mockCourseExecution2.getEndDate() >> LOCAL_DATE_TODAY
        mockCourseExecution2.getStudents() >> []
        mockCourseExecution2.getQuizzes() >> []

        and: "mock course returns mock course executions"
        mockCourse.getCourseExecutions() >> [mockCourseExecution1, mockCourseExecution2]

        and: "four mock teacher dashboards"
        def mockTeacherDashboard1 = Mock(TeacherDashboard)
        mockTeacherDashboard1.getCourseExecution() >> mockCourseExecution1
        mockTeacherDashboard1.getTeacher() >> mockTeacher1
        def mockTeacherDashboard2 = Mock(TeacherDashboard)
        mockTeacherDashboard2.getCourseExecution() >> mockCourseExecution2
        mockTeacherDashboard2.getTeacher() >> mockTeacher2
        def mockTeacherDashboard3 = Mock(TeacherDashboard)
        mockTeacherDashboard3.getCourseExecution() >> mockCourseExecution1
        mockTeacherDashboard3.getTeacher() >> mockTeacher3
        def mockTeacherDashboard4 = Mock(TeacherDashboard)
        mockTeacherDashboard4.getCourseExecution() >> mockCourseExecution2
        mockTeacherDashboard4.getTeacher() >> mockTeacher3

        and: "mock teachers return mock teacher dashboards"
        mockTeacher1.getDashboards() >> buildSet([mockTeacherDashboard1], [dashboard1AlreadyExists])
        mockTeacher2.getDashboards() >> buildSet([mockTeacherDashboard2], [dashboard2AlreadyExists])
        mockTeacher3.getDashboards() >> buildSet(
                [mockTeacherDashboard3, mockTeacherDashboard4],
                [dashboard3AlreadyExists, dashboard4AlreadyExists]
        )

        and: "a mock course execution repository"
        def courseExecutionRepository = Mock(CourseExecutionRepository)
        courseExecutionRepository.findAll() >> [mockCourseExecution1, mockCourseExecution2]

        and: "a mock teacher dashboard repository"
        def teacherDashboardRepository = Mock(TeacherDashboardRepository)
        teacherDashboardRepository.findAll() >> buildSet(
                [mockTeacherDashboard1, mockTeacherDashboard2, mockTeacherDashboard3, mockTeacherDashboard4],
                [dashboard1AlreadyExists, dashboard2AlreadyExists, dashboard3AlreadyExists, dashboard4AlreadyExists]
        )

        and: "mock stats repositories"
        def studentStatsRepository = Mock(StudentStatsRepository)
        def quizStatsRepository = Mock(QuizStatsRepository)
        def questionStatsRepository = Mock(QuestionStatsRepository)

        and: "a teacher dashboard service that uses the mock repositories"
        def service = new TeacherDashboardService(
                courseExecutionRepository: courseExecutionRepository,
                teacherDashboardRepository: teacherDashboardRepository,
                studentStatsRepository: studentStatsRepository,
                quizStatsRepository: quizStatsRepository,
                questionStatsRepository: questionStatsRepository
        )

        when: "all teacher dashboards are updated"
        service.updateAllTeacherDashboards()

        then: "update method is called exactly once on all teacher dashboards"
        1 * mockTeacherDashboard1.update()
        1 * mockTeacherDashboard2.update()
        1 * mockTeacherDashboard3.update()
        1 * mockTeacherDashboard4.update()

        and: "save method is called correctly on teacher dashboard repository"
        (dashboard1AlreadyExists ? 1 : 2) * teacherDashboardRepository.save({
            it.getCourseExecution().getId() == 1 && it.getTeacher().getId() == 1
        }) >> mockTeacherDashboard1
        (dashboard2AlreadyExists ? 1 : 2) * teacherDashboardRepository.save({
            it.getCourseExecution().getId() == 2 && it.getTeacher().getId() == 2
        }) >> mockTeacherDashboard2
        (dashboard3AlreadyExists ? 1 : 2) * teacherDashboardRepository.save({
            it.getCourseExecution().getId() == 1 && it.getTeacher().getId() == 3
        }) >> mockTeacherDashboard3
        (dashboard4AlreadyExists ? 1 : 2) * teacherDashboardRepository.save({
            it.getCourseExecution().getId() == 2 && it.getTeacher().getId() == 3
        }) >> mockTeacherDashboard4
        0 * teacherDashboardRepository.save(*_) >> { throw new Exception("Unexpected call to save") }

        and: "save method is called the correct number of times on stats repositories"
        expectedStatsCalls * studentStatsRepository.save(_)
        expectedStatsCalls * quizStatsRepository.save(_)
        expectedStatsCalls * questionStatsRepository.save(_)

        where:
        dashboard1AlreadyExists | dashboard2AlreadyExists | dashboard3AlreadyExists | dashboard4AlreadyExists || expectedStatsCalls
        false                   | false                   | false                   | false                   || 1 + 2 + 1 + 2
        true                    | false                   | false                   | false                   || 0 + 2 + 1 + 2
        true                    | true                    | false                   | false                   || 0 + 0 + 1 + 2
        true                    | true                    | true                    | false                   || 0 + 0 + 0 + 2
        true                    | true                    | true                    | true                    || 0 + 0 + 0 + 0
    }

    def createCourseExecution(String acronym, String academicTerm, LocalDateTime endDate) {
        def courseExecution = new CourseExecution(course, acronym, academicTerm, Course.Type.TECNICO, endDate)
        courseExecutionRepository.save(courseExecution)

        return courseExecution
    }

    def createTeacher(int key, String name, String username, String email) {
        def teacher = new Teacher(name, username, email, false, AuthUser.Type.TECNICO)
        teacher.setKey(key)
        userRepository.save(teacher)

        return teacher
    }

    def addTeacherToCourseExecution(Teacher teacher, CourseExecution courseExecution) {
        courseExecution.addUser(teacher)
        courseExecutionRepository.save(courseExecution)
    }

    static <T> Set<T> buildSet(List<T> objs, List<Boolean> includeConds) {
        Set<T> set = new HashSet<>()
        objs.eachWithIndex { obj, i ->
            if (includeConds[i]) {
                set.add(obj)
            }
        }
        return set
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
