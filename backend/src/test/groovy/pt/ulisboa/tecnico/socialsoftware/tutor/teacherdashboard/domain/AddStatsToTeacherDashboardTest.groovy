package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class AddStatsToTeacherDashboardTest extends SpockTest {
    def teacher
    def teacherDashboard

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)
    }

    def "add StudentStats to TeacherDashboard"() {
        given:
        def previousNumberStudentStats = teacherDashboard.getStudentStats().size()
        def studentStats = new StudentStats()
        studentStatsRepository.save(studentStats)
        studentStats.setCourseExecution(externalCourseExecution)

        when:
        studentStats.setTeacherDashboard(teacherDashboard)
        def studentStats2 = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats2)

        then:
        teacherDashboard.getStudentStats().size() == previousNumberStudentStats + 2
        teacherDashboard.getStudentStats().contains(studentStats)
        teacherDashboard.getStudentStats().contains(studentStats2)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}