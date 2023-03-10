package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;

public class QuizStatsDto {

    private Integer id;

    private Integer numQuizzes;

    private Integer uniqueQuizzesSolved;

    private Float averageQuizzesSolved;

    public QuizStatsDto(QuizStats quizStats) {
        this.id = quizStats.getId();
        this.numQuizzes = quizStats.getNumQuizzes();
        this.uniqueQuizzesSolved = quizStats.getUniqueQuizzesSolved();
        this.averageQuizzesSolved = quizStats.getAverageQuizzesSolved();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumQuizzes() {
        return numQuizzes;
    }

    public void setNumQuizzes(Integer numQuizzes) {
        this.numQuizzes = numQuizzes;
    }

    public Integer getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public void setUniqueQuizzesSolved(Integer uniqueQuizzesSolved) {
        this.uniqueQuizzesSolved = uniqueQuizzesSolved;
    }

    public Float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    public void setAverageQuizzesSolved(Float averageQuizzesSolved) {
        this.averageQuizzesSolved = averageQuizzesSolved;
    }

    @Override
    public String toString() {
        return "QuizStatsDto{" +
            "id=" + id +
            ", numQuizzes=" + numQuizzes +
            ", uniqueQuizzesSolved=" + uniqueQuizzesSolved +
            ", averageQuizzesSolved=" + averageQuizzesSolved +
            '}';
    }
}
