package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
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
    def quiz1
    def quiz2
    def quiz3
    def quiz4
    def quiz5

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

        quiz1 = createQuiz(1)
        quiz2 = createQuiz(2)
        quiz3 = createQuiz(3)
        quiz4 = createQuiz(4)
        quiz5 = createQuiz(5)

        createQuizQuestion(quiz1, question1, 1)
        createQuizQuestion(quiz1, question2, 2)
        createQuizQuestion(quiz2, question1, 1)
        createQuizQuestion(quiz3, question1, 1)
        createQuizQuestion(quiz4, question2, 1)
        createQuizQuestion(quiz5, question3, 1)
    }

    def "update question stats with no students"() {
        given: "question stats of execution course"
        def questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats)

        when: "updating stats"
        questionStats.update()

        then: "it has correct stats values"
        questionStats.getNumAvailable() == 2
        questionStats.getAnsweredQuestionsUnique() == 0
        Float.compare(questionStats.getAverageQuestionsAnswered(), 0.0f) == 0

        and: "string representation is correct"
        questionStats.toString() == "QuestionStats{" +
                "id=" + questionStats.getId() +
                ", courseExecution=" + externalCourseExecution.toString() +
                ", numAvailable=" + "2" +
                ", answeredQuestionsUnique=" + "0" +
                ", averageQuestionsAnswered=" + questionStats.getAverageQuestionsAnswered() +
                // ^^ need to use getter instead of hardcoding as actual value isn't known (float approximations)
                "}"
    }

    def "update question stats with students"() {
        given: "question stats of execution course"
        def questionStats = new QuestionStats(externalCourseExecution, teacherDashboard)
        questionStatsRepository.save(questionStats)

        and: "students answering quizzes in said execution course"
        def student1 = createStudent(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL)
        def student2 = createStudent(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL)
        def student3 = createStudent(USER_4_NAME, USER_4_USERNAME, USER_4_EMAIL)
        createStudent(USER_5_NAME, USER_5_USERNAME, USER_5_EMAIL) // student4, does not answer any quiz

        createQuizAnswer(student1, quiz1, true)
        createQuizAnswer(student1, quiz4, false)
        createQuizAnswer(student2, quiz1, true)
        createQuizAnswer(student2, quiz5, false)
        createQuizAnswer(student3, quiz2, true)
        createQuizAnswer(student3, quiz3, true)

        when: "updating stats"
        questionStats.update()

        then: "it has correct stats values"
        questionStats.getNumAvailable() == 2
        questionStats.getAnsweredQuestionsUnique() == 2
        Float.compare(questionStats.getAverageQuestionsAnswered(), 5/4) == 0

        and: "string representation is correct"
        questionStats.toString() == "QuestionStats{" +
                "id=" + questionStats.getId() +
                ", courseExecution=" + externalCourseExecution.toString() +
                ", numAvailable=" + "2" +
                ", answeredQuestionsUnique=" + "2" +
                ", averageQuestionsAnswered=" + questionStats.getAverageQuestionsAnswered() +
                // ^^ need to use getter instead of hardcoding as actual value isn't known (float approximations)
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
        externalCourseExecution.addUser(student)
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

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
