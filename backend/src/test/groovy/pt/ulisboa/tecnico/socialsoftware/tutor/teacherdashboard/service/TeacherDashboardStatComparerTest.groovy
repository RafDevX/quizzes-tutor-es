package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.StudentStatsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.QuestionStatsDto

class TeacherDashboardStatComparerTest extends SpockTest {
    def compareStudentStats(StudentStatsDto studentStatsDto, StudentStats studentStats) {
        studentStatsDto.getId() == studentStats.getId()
        studentStatsDto.getNumStudents() == studentStats.getNumStudents()
        studentStatsDto.getNumMore75CorrectQuestions() == studentStats.getNumMore75CorrectQuestions()
        studentStatsDto.getNumAtLeast3Quizzes() == studentStats.getNumAtLeast3Quizzes()
        studentStatsDto.getAcademicTerm() == studentStats.getAcademicTerm()
        studentStatsDto.toString() == "QuestionStatsDto{" +
                "id=" + studentStats.getId() +
                ", numStudents=" + studentStats.getNumStudents() +
                ", numMore75CorrectQuestions=" + studentStats.getNumMore75CorrectQuestions() +
                ", numAtLeast3Quizzes=" + studentStats.getNumAtLeast3Quizzes() +
                ", academicTerm='" + studentStats.getAcademicTerm() + '\'' +
                '}'
    }
    
    def compareQuestionStats(QuestionStatsDto studentStatsDto, QuestionStats studentStats) {
        studentStatsDto.getId() == studentStats.getId()
        studentStatsDto.getNumAvailable() == studentStats.getNumAvailable()
        studentStatsDto.getAnsweredQuestionsUnique() == studentStats.getAnsweredQuestionsUnique()
        studentStatsDto.getAverageQuestionsAnswered() == studentStats.getAverageQuestionsAnswered()
        studentStatsDto.getAcademicTerm() == studentStats.getAcademicTerm()
        studentStatsDto.toString() == "QuestionStatsDto{" +
                "id=" + studentStats.getId() +
                ", numAvailable=" + studentStats.getNumAvailable() +
                ", answeredQuestionsUnique=" + studentStats.getAnsweredQuestionsUnique() +
                ", averageQuestionsAnswered=" + studentStats.getAverageQuestionsAnswered() +
                ", academicTerm='" + studentStats.getAcademicTerm() + '\'' +
                '}'
    }
}
