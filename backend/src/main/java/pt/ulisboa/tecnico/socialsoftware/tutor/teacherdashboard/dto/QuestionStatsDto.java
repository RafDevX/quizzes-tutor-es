package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats;

public class QuestionStatsDto {
    private int id;
    private int numAvailable;
    private int answeredQuestionsUnique;
    private float averageQuestionsAnswered;
    private String academicTerm;

    public QuestionStatsDto(QuestionStats questionStats) {
        this.id = questionStats.getId();
        this.numAvailable = questionStats.getNumAvailable();
        this.answeredQuestionsUnique = questionStats.getAnsweredQuestionsUnique();
        this.averageQuestionsAnswered = questionStats.getAverageQuestionsAnswered();
        this.academicTerm = questionStats.getAcademicTerm();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public int getAnsweredQuestionsUnique() {
        return answeredQuestionsUnique;
    }

    public void setAnsweredQuestionsUnique(int answeredQuestionsUnique) {
        this.answeredQuestionsUnique = answeredQuestionsUnique;
    }

    public float getAverageQuestionsAnswered() {
        return averageQuestionsAnswered;
    }

    public void setAverageQuestionsAnswered(float averageQuestionsAnswered) {
        this.averageQuestionsAnswered = averageQuestionsAnswered;
    }

    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    @Override
    public String toString() {
        return "QuestionStatsDto{" +
                "id=" + id +
                ", numAvailable=" + numAvailable +
                ", answeredQuestionsUnique=" + answeredQuestionsUnique +
                ", averageQuestionsAnswered=" + averageQuestionsAnswered +
                ", academicTerm='" + academicTerm + '\'' +
                '}';
    }
}
