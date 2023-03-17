package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.StudentStatsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.QuizStatsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.QuestionStatsDto

class TeacherDashboardStatComparerTest extends SpockTest {
    def compareStudentStats(StudentStatsDto studentStatsDto, id, numStudents, numMore75CorrectQuestions, numAtLeast3Quizzes, academicTerm) {
        assert studentStatsDto.getId() == id
        assert studentStatsDto.getNumStudents() == numStudents
        assert studentStatsDto.getNumMore75CorrectQuestions() == numMore75CorrectQuestions
        assert studentStatsDto.getNumAtLeast3Quizzes() == numAtLeast3Quizzes
        assert studentStatsDto.getAcademicTerm() == academicTerm
        assert studentStatsDto.toString() == "StudentStatsDto{" +
                "id=" + id +
                ", academicTerm='" + academicTerm + '\'' +
                ", numStudents=" + numStudents +
                ", numMore75CorrectQuestions=" + numMore75CorrectQuestions +
                ", numAtLeast3Quizzes=" + numAtLeast3Quizzes +
                '}'
    }

    def compareQuizStats(QuizStatsDto quizStatsDto, id, numQuizzes, uniqueQuizzesSolved, averageQuizzesSolved, academicTerm) {
        assert quizStatsDto.getId() == id
        assert quizStatsDto.getNumQuizzes() == numQuizzes
        assert quizStatsDto.getUniqueQuizzesSolved() == uniqueQuizzesSolved
        assert quizStatsDto.getAverageQuizzesSolved() == averageQuizzesSolved
        assert quizStatsDto.getAcademicTerm() == academicTerm
        assert quizStatsDto.toString() == "QuizStatsDto{" +
                "id=" + id +
                ", numQuizzes=" + numQuizzes +
                ", uniqueQuizzesSolved=" + uniqueQuizzesSolved +
                ", averageQuizzesSolved=" + averageQuizzesSolved +
                ", academicTerm='" + academicTerm + '\'' +
                '}'
    }
    
    def compareQuestionStats(QuestionStatsDto studentStatsDto, id, numAvailable, answeredQuestionsUnique, averageQuestionsAnswered, academicTerm) {
        assert studentStatsDto.getId() == id
        assert studentStatsDto.getNumAvailable() == numAvailable
        assert studentStatsDto.getAnsweredQuestionsUnique() == answeredQuestionsUnique
        assert studentStatsDto.getAverageQuestionsAnswered() == averageQuestionsAnswered
        assert studentStatsDto.getAcademicTerm() == academicTerm
        assert studentStatsDto.toString() == "QuestionStatsDto{" +
                "id=" + id +
                ", numAvailable=" + numAvailable +
                ", answeredQuestionsUnique=" + answeredQuestionsUnique +
                ", averageQuestionsAnswered=" + averageQuestionsAnswered +
                ", academicTerm='" + academicTerm + '\'' +
                '}'
    }
}
