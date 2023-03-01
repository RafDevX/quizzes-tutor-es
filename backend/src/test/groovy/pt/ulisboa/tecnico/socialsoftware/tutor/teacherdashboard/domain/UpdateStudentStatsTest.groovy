package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher

@DataJpaTest
class UpdateStudentStatsTest extends SpockTest {
    def teacher
    def teacherDashboard
    def externalCourse2
    def externalCourseExecution2
    def student1
    def student2
    def student3

    def quiz1
    def quiz1Question1
    def quiz1Question1OptionOK
    def quiz1Question1OptionKO
    def quiz1Question2
    def quiz1Question2OptionOK
    def quiz1Question2OptionKO
    def quiz2
    def quiz2Question1
    def quiz2Question1OptionOK
    def quiz2Question1OptionKO
    def quiz2Question2
    def quiz2Question2OptionOK
    def quiz2Question2OptionKO

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        teacherDashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(teacherDashboard)

        student1 = new Student(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(student1)
        student2 = new Student(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(student2)
        student3 = new Student(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL, false, AuthUser.Type.TECNICO)
        userRepository.save(student3)

        externalCourse2 = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(externalCourse2)

        externalCourseExecution2 = new CourseExecution(externalCourse2, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(externalCourseExecution2)

        externalCourseExecution.addUser(student1)
        externalCourseExecution.addUser(student2)
        externalCourseExecution2.addUser(student2)
        externalCourseExecution2.addUser(student3)

        quiz1 = createQuiz()
        quiz1Question1 = createQuestion(quiz1)
        quiz1Question1OptionOK = createOptions(quiz1Question1, true)
        quiz1Question1OptionKO = createOptions(quiz1Question1, false)
        quiz1Question2 = createQuestion(quiz1)
        quiz1Question2OptionOK = createOptions(quiz1Question2, true)
        quiz1Question2OptionKO = createOptions(quiz1Question2, false)
        quiz2 = createQuiz()
        quiz2Question1 = createQuestion(quiz2)
        quiz2Question1OptionOK = createOptions(quiz2Question1, true)
        quiz2Question1OptionKO = createOptions(quiz2Question1, false)
        quiz2Question2 = createQuestion(quiz2)
        quiz2Question2OptionOK = createOptions(quiz2Question2, true)
        quiz2Question2OptionKO = createOptions(quiz2Question2, false)

    }

    def "update students statistic"() {
        given: "student stats of course"
        def studentStats = new StudentStats(externalCourseExecution, teacherDashboard)
        studentStatsRepository.save(studentStats)

        and: "student with 75% correct answers"
        def quiz1AnswerStudent1 = createQuizAnswer(quiz1, student1)
        createQuestionAnswer(quiz1AnswerStudent1, quiz1Question1, quiz1Question1OptionOK)
        createQuestionAnswer(quiz1AnswerStudent1, quiz1Question2, quiz1Question2OptionOK)
        def quiz2AnswerStudent1 = createQuizAnswer(quiz2, student1)
        createQuestionAnswer(quiz2AnswerStudent1, quiz1Question1, quiz1Question1OptionOK)
        createQuestionAnswer(quiz2AnswerStudent1, quiz2Question2, quiz2Question2OptionKO)

        and: "student with 50% correct answers"
        def quiz1AnswerStudent2 = createQuizAnswer(quiz1, student2)
        createQuestionAnswer(quiz1AnswerStudent2, quiz1Question1, quiz1Question1OptionOK)
        createQuestionAnswer(quiz1AnswerStudent2, quiz1Question2, quiz1Question2OptionKO)
        def quiz2AnswerStudent2 = createQuizAnswer(quiz2, student2)
        createQuestionAnswer(quiz2AnswerStudent2, quiz1Question1, quiz1Question1OptionOK)
        createQuestionAnswer(quiz2AnswerStudent2, quiz2Question2, quiz2Question2OptionKO)

        when: "updating statistic"
        studentStats.update()

        then: "it has correct stats values"
        studentStats.getNumStudents() == 2
        studentStats.getNumMore75CorrectQuestions() == 1


        and: "string representation is correct"
        studentStats.toString() == "StudentStats{" +
                "id=" +
                studentStats.getId() +
                ", courseExecution=" +
                externalCourseExecution.toString() +
                ", numStudents=2" +
                ", numMore75CorrectQuestions=1}"
    }

    def createQuiz() {
        Quiz quiz = new Quiz()
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quizRepository.save(quiz)

        return quiz
    }

    def createQuestion(quiz) {
        def newQuestion = new Question()
        newQuestion.setTitle("Question Title")
        newQuestion.setCourse(externalCourse)
        def questionDetails = new MultipleChoiceQuestion()
        newQuestion.setQuestionDetails(questionDetails)
        questionRepository.save(newQuestion)

        QuizQuestion quizQuestion = new QuizQuestion()
        quizQuestion.setQuestion(newQuestion)
        quizQuestion.setQuiz(quiz)
        quizQuestionRepository.save(quizQuestion)

        return quizQuestion
    }

    def createOptions(QuizQuestion quizQuestion, correct) {
        def option = new Option()
        option.setContent("Option Content")
        option.setCorrect(correct)
        option.setSequence(0)
        option.setQuestionDetails(quizQuestion.getQuestion().getQuestionDetails())
        optionRepository.save(option)

        return option
    }

    def createQuizAnswer(quiz, student) {
        def quizAnswer = new QuizAnswer()
        quizAnswer.setCompleted(true)
        quizAnswer.setStudent(student)
        quizAnswer.setQuiz(quiz)
        quizAnswerRepository.save(quizAnswer)

        return quizAnswer
    }

    def createQuestionAnswer(quizAnswer, question, option) {
        def questionAnswer = new QuestionAnswer()
        def answerDetails = new MultipleChoiceAnswer(questionAnswer, option)
        questionAnswer.setAnswerDetails(answerDetails)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswer.setQuizQuestion(question)
        questionAnswerRepository.save(questionAnswer)
        answerDetailsRepository.save(answerDetails)
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}
