package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherDashboardDto {
    private Integer id;
    private Integer numberOfStudents;

    private List<QuizStatsDto> quizStats;

    private List<QuestionStatsDto> questionsStats;

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();
        // For the number of students, we consider only active students
        this.numberOfStudents = teacherDashboard.getCourseExecution().getNumberOfActiveStudents();
        this.quizStats = teacherDashboard.getQuizStats().stream().map(QuizStatsDto::new).collect(Collectors.toList());
        this.questionsStats = teacherDashboard.getQuestionStats().stream().map(QuestionStatsDto::new)
                .collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public List<QuizStatsDto> getQuizStats() {
        return quizStats;
    }

    public void setQuizStats(List<QuizStatsDto> quizStats) {
        this.quizStats = quizStats;
    }

    public List<QuestionStatsDto> getQuestionsStats() {
        return questionsStats;
    }

    public void setQuestionsStats(List<QuestionStatsDto> questionsStats) {
        this.questionsStats = questionsStats;
    }

    @Override
    public String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                ", numberOfStudents=" + this.getNumberOfStudents() +
                ", quizStats=" + this.getQuizStats() +
                ", questionsStats=" + this.getQuestionsStats() +
                "}";
    }
}
