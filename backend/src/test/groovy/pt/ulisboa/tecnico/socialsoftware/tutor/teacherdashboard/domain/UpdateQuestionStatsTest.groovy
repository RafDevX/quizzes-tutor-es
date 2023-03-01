package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class UpdateQuestionStatsTest extends SpockTest {
    def teacher
    def teacherDashboard
    def question1
    def question2
    def question3

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(teacher)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)

        def topic = new Topic()
        topic.setName("TOPIC")
        topic.setCourse(externalCourse)
        topicRepository.save(topic)

        question1 = new Question()
        question1.setKey(1)
        question1.setContent("Question Content")
        question1.setTitle("Question Title")
        question1.setStatus(Question.Status.AVAILABLE)
        question1.setCourse(externalCourse)
        question1.addTopic(topic)
        questionRepository.save(question1)

        question2 = new Question()
        question2.setKey(2)
        question2.setContent("Question Content")
        question2.setTitle("Question Title")
        question2.setStatus(Question.Status.REMOVED)
        question2.setCourse(externalCourse)
        question2.addTopic(topic)
        questionRepository.save(question2)

        question3 = new Question()
        question3.setKey(3)
        question3.setContent("Question Content")
        question3.setTitle("Question Title")
        question3.setStatus(Question.Status.AVAILABLE)
        question3.setCourse(externalCourse)
        question3.addTopic(topic)
        questionRepository.save(question2)

        externalCourse.addQuestion(question1);
        externalCourse.addQuestion(question2);
        externalCourse.addQuestion(question3);
    }

    def "update question stats"() {
        given: "question stats of execution course"
        def questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats)

        when: "updating stats"
        questionStats.update()

        then: "it has correct stats values"
        questionStats.getNumAvailable() == 2

        and: "string representations is correct"
        questionStats.toString() == "QuestionStats{" +
                "id=" + questionStats.getId() +
                ", courseExecution=" + externalCourseExecution.toString() +
                ", numAvailable=" + "2" +
                "}";
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}
