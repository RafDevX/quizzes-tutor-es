package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;

public class QuizStatsDto {
    private int id;
    private int numQuizzes;
    private int uniqueQuizzesSolved;
    private float averageQuizzesSolved;
    private String academicTerm;

    public QuizStatsDto(QuizStats quizStats) {
        this.id = quizStats.getId();
        this.numQuizzes = quizStats.getNumQuizzes();
        this.uniqueQuizzesSolved = quizStats.getUniqueQuizzesSolved();
        this.averageQuizzesSolved = quizStats.getAverageQuizzesSolved();
        this.academicTerm = quizStats.getAcademicTerm();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumQuizzes() {
        return numQuizzes;
    }

    public void setNumQuizzes(int numQuizzes) {
        this.numQuizzes = numQuizzes;
    }

    public int getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public void setUniqueQuizzesSolved(int uniqueQuizzesSolved) {
        this.uniqueQuizzesSolved = uniqueQuizzesSolved;
    }

    public float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    public void setAverageQuizzesSolved(float averageQuizzesSolved) {
        this.averageQuizzesSolved = averageQuizzesSolved;
    }

    public String getAcademicTerm() { return academicTerm; }

    public void setAcademicTerm(String academicTerm) { this.academicTerm = academicTerm; }

    @Override
    public String toString() {
        return "QuizStatsDto{" +
            "id=" + id +
            ", numQuizzes=" + numQuizzes +
            ", uniqueQuizzesSolved=" + uniqueQuizzesSolved +
            ", averageQuizzesSolved=" + averageQuizzesSolved +
            ", academicTerm='" + academicTerm + '\'' +
            '}';
    }
}
