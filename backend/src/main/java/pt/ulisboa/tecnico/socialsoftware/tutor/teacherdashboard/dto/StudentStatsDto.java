package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats;

public class StudentStatsDto {
    private int id;
    private String academicTerm;
    private int numStudents;
    private int numMore75CorrectQuestions;
    private int numAtLeast3Quizzes;

    public StudentStatsDto(StudentStats studentStats) {
        this.id = studentStats.getId();
        this.academicTerm = studentStats.getCourseExecution().getAcademicTerm();
        this.numStudents = studentStats.getNumStudents();
        this.numMore75CorrectQuestions = studentStats.getNumMore75CorrectQuestions();
        this.numAtLeast3Quizzes = studentStats.getNumAtLeast3Quizzes();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public int getNumMore75CorrectQuestions() {
        return numMore75CorrectQuestions;
    }

    public void setNumMore75CorrectQuestions(int numMore75CorrectQuestions) {
        this.numMore75CorrectQuestions = numMore75CorrectQuestions;
    }

    public int getNumAtLeast3Quizzes() {
        return numAtLeast3Quizzes;
    }

    public void setNumAtLeast3Quizzes(int numAtLeast3Quizzes) {
        this.numAtLeast3Quizzes = numAtLeast3Quizzes;
    }

    @Override
    public String toString() {
        return "StudentStatsDto{" +
                "id=" + id +
                ", academicTerm='" + academicTerm +
                ", numStudents=" + numStudents +
                ", numMore75CorrectQuestions=" + numMore75CorrectQuestions +
                ", numAtLeast3Quizzes=" + numAtLeast3Quizzes +
                '}';
    }
}
