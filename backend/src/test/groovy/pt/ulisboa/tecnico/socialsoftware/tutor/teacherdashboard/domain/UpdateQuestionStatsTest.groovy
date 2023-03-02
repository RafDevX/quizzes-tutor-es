package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler

@DataJpaTest
class UpdateQuestionStatsTest extends SpockTest {
    def teacher
    def teacherDashboard
    def question1
    def question2
    def question3
    def student1
    def student2
    def student3
    def quiz1
    def quiz2
    def quizQuestion1
    def quizQuestion2
    def quizQuestion2Again
    def quizQuestion3
    def quizAnswer1
    def quizAnswer2
    def quizAnswer3
    def quizAnswer4
    def quizAnswer5
    def questionAnswer1
    def questionAnswer2
    def questionAnswer3
    def questionAnswer4
    def questionAnswer5
    def questionAnswer6
    def questionAnswer7
    def questionAnswer8

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
        questionRepository.save(question3)

        externalCourse.addQuestion(question1)
        externalCourse.addQuestion(question2)
        externalCourse.addQuestion(question3)

        student1 = new Student(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, false, AuthUser.Type.EXTERNAL)
        student3 = new Student(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL, false, AuthUser.Type.EXTERNAL)
        student2 = new Student(USER_4_NAME, USER_4_USERNAME, USER_4_EMAIL, false, AuthUser.Type.EXTERNAL)

        userRepository.save(student1)
        userRepository.save(student2)
        userRepository.save(student3)

        quiz1 = new Quiz()
        quiz1.setKey(1)
        quiz1.setType(Quiz.QuizType.GENERATED.toString())
        quiz1.setCourseExecution(externalCourseExecution)
        quiz1.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz1)

        quiz2 = new Quiz()
        quiz2.setKey(2)
        quiz2.setType(Quiz.QuizType.GENERATED.toString())
        quiz2.setCourseExecution(externalCourseExecution)
        quiz2.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz2)

        quizQuestion1 = new QuizQuestion(quiz1, question1, 1)
        quizQuestion2 = new QuizQuestion(quiz1, question2, 2)
        quizQuestion2Again = new QuizQuestion(quiz2, question2, 3)
        quizQuestion3 = new QuizQuestion(quiz2, question3, 4)
        quizQuestionRepository.save(quizQuestion1)
        quizQuestionRepository.save(quizQuestion2)
        quizQuestionRepository.save(quizQuestion2Again)
        quizQuestionRepository.save(quizQuestion3)

        quiz1.addQuizQuestion(quizQuestion1)
        quiz1.addQuizQuestion(quizQuestion2)
        quiz2.addQuizQuestion(quizQuestion2Again)
        quiz2.addQuizQuestion(quizQuestion3)

        quizAnswer1 = new QuizAnswer(student1, quiz1)
        quizAnswer1.setCompleted(true)
        quizAnswer2 = new QuizAnswer(student2, quiz1)
        quizAnswer2.setCompleted(true)
        quizAnswer3 = new QuizAnswer(student3, quiz1)
        quizAnswer3.setCompleted(true)
        quizAnswer4 = new QuizAnswer(student2, quiz2)
        quizAnswer4.setCompleted(false)
        quizAnswer5 = new QuizAnswer(student1, quiz2)
        quizAnswer5.setCompleted(false)

        quizAnswerRepository.save(quizAnswer1)
        quizAnswerRepository.save(quizAnswer2)
        quizAnswerRepository.save(quizAnswer3)
        quizAnswerRepository.save(quizAnswer4)
        quizAnswerRepository.save(quizAnswer5)

        questionAnswer1 = new QuestionAnswer(quizAnswer1, quizQuestion1, 1)
        questionAnswer2 = new QuestionAnswer(quizAnswer1, quizQuestion2, 2)
        questionAnswer3 = new QuestionAnswer(quizAnswer2, quizQuestion1, 1)
        questionAnswer4 = new QuestionAnswer(quizAnswer2, quizQuestion2, 2)
        questionAnswer5 = new QuestionAnswer(quizAnswer3, quizQuestion1, 1)
        questionAnswer6 = new QuestionAnswer(quizAnswer3, quizQuestion1, 2)
        questionAnswer7 = new QuestionAnswer(quizAnswer4, quizQuestion3, 1)
        questionAnswer8 = new QuestionAnswer(quizAnswer5, quizQuestion2Again, 1)

        questionAnswerRepository.save(questionAnswer1)
        questionAnswerRepository.save(questionAnswer2)
        questionAnswerRepository.save(questionAnswer3)
        questionAnswerRepository.save(questionAnswer4)
        questionAnswerRepository.save(questionAnswer5)
        questionAnswerRepository.save(questionAnswer6)
        questionAnswerRepository.save(questionAnswer7)
        questionAnswerRepository.save(questionAnswer8)
    }

    def "update question stats"() {
        given: "question stats of execution course"
        def questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats)

        when: "updating stats"
        questionStats.update()

        then: "it has correct stats values"
        questionStats.getNumAvailable() == 2
        questionStats.getAnsweredQuestionsUnique() == 2

        and: "string representations is correct"
        questionStats.toString() == "QuestionStats{" +
                "id=" + questionStats.getId() +
                ", courseExecution=" + externalCourseExecution.toString() +
                ", numAvailable=" + "2" +
                ", answeredQuestionsUnique=" + "2" +
                "}"
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
