package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.QuestionStatsDto

class TeacherDashboardStatComparerTest extends SpockTest {
    def compareQuestionStats(QuestionStatsDto questionStatsDto, QuestionStats questionStats) {
        questionStatsDto.getId() == questionStats.getId()
        questionStatsDto.getNumAvailable() == questionStats.getNumAvailable()
        questionStatsDto.getAnsweredQuestionsUnique() == questionStats.getAnsweredQuestionsUnique()
        questionStatsDto.getAverageQuestionsAnswered() == questionStats.getAverageQuestionsAnswered()
        questionStatsDto.getAcademicTerm() == questionStats.getAcademicTerm()
        questionStatsDto.toString() == "QuestionStatsDto{" +
                "id=" + questionStats.getId() +
                ", numAvailable=" + questionStats.getNumAvailable() +
                ", answeredQuestionsUnique=" + questionStats.getAnsweredQuestionsUnique() +
                ", averageQuestionsAnswered=" + questionStats.getAverageQuestionsAnswered() +
                ", academicTerm='" + questionStats.getAcademicTerm() + '\'' +
                '}'
    }
}
