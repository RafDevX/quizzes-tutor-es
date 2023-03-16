package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

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

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
