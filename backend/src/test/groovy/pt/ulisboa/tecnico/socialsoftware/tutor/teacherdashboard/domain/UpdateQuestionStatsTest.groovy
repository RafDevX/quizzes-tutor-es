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

        def question1 = createQuestion(1, topic, Question.Status.AVAILABLE)
        def question2 = createQuestion(2, topic, Question.Status.REMOVED)
        def question3 = createQuestion(3, topic, Question.Status.AVAILABLE)

        def student1 = createStudent(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL)
        def student2 = createStudent(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL)
        def student3 = createStudent(USER_4_NAME, USER_4_USERNAME, USER_4_EMAIL)

        def quiz1 = createQuiz(1)
        def quiz2 = createQuiz(2)

        def quizQuestion1 = createQuizQuestion(quiz1, question1, 1)
        def quizQuestion2 = createQuizQuestion(quiz1, question2, 2)
        def quizQuestion2Again = createQuizQuestion(quiz2, question2, 3)
        def quizQuestion3 = createQuizQuestion(quiz2, question3, 4)

        def quizAnswer1 = createQuizAnswer(student1, quiz1, true)
        def quizAnswer2 = createQuizAnswer(student2, quiz1, true)
        def quizAnswer3 = createQuizAnswer(student3, quiz1, true)
        def quizAnswer4 = createQuizAnswer(student2, quiz2, false)
        def quizAnswer5 = createQuizAnswer(student1, quiz2, false)

        createQuestionAnswer(quizAnswer1, quizQuestion1, 1)
        createQuestionAnswer(quizAnswer1, quizQuestion2, 2)
        createQuestionAnswer(quizAnswer2, quizQuestion1, 1)
        createQuestionAnswer(quizAnswer2, quizQuestion2, 2)
        createQuestionAnswer(quizAnswer3, quizQuestion1, 1)
        createQuestionAnswer(quizAnswer3, quizQuestion1, 2)
        createQuestionAnswer(quizAnswer4, quizQuestion3, 1)
        createQuestionAnswer(quizAnswer5, quizQuestion2Again, 1)
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

    def createQuestion(int key, Topic topic, Question.Status status) {
        def question = new Question()
        question.setKey(key)
        question.setContent("Question Content")
        question.setTitle("Question Title")
        question.setStatus(status)
        question.setCourse(externalCourse)
        question.addTopic(topic)
        questionRepository.save(question)
        externalCourse.addQuestion(question)
        return question
    }

    def createStudent(String name, String username, String email) {
        def student = new Student(name, username, email, false, AuthUser.Type.TECNICO)
        userRepository.save(student)
        return student
    }

    def createQuiz(int key) {
        def quiz = new Quiz()
        quiz.setKey(key)
        quiz.setType(Quiz.QuizType.GENERATED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)
        return quiz
    }

    def createQuizQuestion(Quiz quiz, Question question, int sequence) {
        def quizQuestion = new QuizQuestion(quiz, question, sequence)
        quizQuestionRepository.save(quizQuestion)
        return quizQuestion
    }

    def createQuizAnswer(Student student, Quiz quiz, boolean completed) {
        def quizAnswer = new QuizAnswer(student, quiz)
        quizAnswer.setCompleted(completed)
        quizAnswerRepository.save(quizAnswer)
        return quizAnswer
    }

    def createQuestionAnswer(QuizAnswer quizAnswer, QuizQuestion quizQuestion, int sequence) {
        def questionAnswer = new QuestionAnswer(quizAnswer, quizQuestion, sequence)
        questionAnswerRepository.save(questionAnswer)
        return questionAnswer
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
