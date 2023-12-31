package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class QuizStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "num_quizzes")
    private Integer numQuizzes;

    @Column(name = "unique_quizzes_solved")
    private Integer uniqueQuizzesSolved;

    @Column(name = "average_quizzes_solved")
    private Float averageQuizzesSolved;

    @OneToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    public QuizStats() {
    }

    public QuizStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
        update();
    }

    public void remove() {
        teacherDashboard.removeQuizStats(this);
        teacherDashboard = null;
    }

    public Integer getId() {
        return id;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public String getAcademicTerm() {
        return this.courseExecution.getAcademicTerm();
    }

    public TeacherDashboard getTeacherDashboard() {
        return teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
        this.teacherDashboard = teacherDashboard;
        this.teacherDashboard.addQuizStats(this);
    }

    public Integer getNumQuizzes() {
        return numQuizzes;
    }

    public Integer getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public Float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    public void update() {
        this.updateNumQuizzes();
        this.updateUniqueQuizzesSolved();
        this.updateAverageQuizzesSolved();
    }

    private void updateNumQuizzes() {
        final LocalDateTime now = LocalDateTime.now();
        this.numQuizzes = (int) courseExecution
            .getQuizzes()
            .stream()
            .filter(quiz -> quiz.getAvailableDate().isBefore(now))
            .count();
    }

    private void updateUniqueQuizzesSolved() {
        this.uniqueQuizzesSolved = (int) this.courseExecution.getQuizzes()
                .stream()
                .map(Quiz::getQuizAnswers)
                .map(answers -> answers.stream().filter(QuizAnswer::isCompleted).count())
                .filter(count -> count > 0)
                .count();
    }

    private void updateAverageQuizzesSolved() {
        final Float numStudents = (float) courseExecution.getStudents().size();
        final Float numQuizAnswers = (float) courseExecution.getQuizzes()
                .stream()
                .map(Quiz::getQuizAnswers)
                .flatMap(quizAnswers -> quizAnswers.stream())
                .count();

        this.averageQuizzesSolved = numStudents == 0 ? 0 : numQuizAnswers / numStudents;
    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "QuizStats{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", numQuizzes=" + numQuizzes +
                ", uniqueQuizzesSolved=" + uniqueQuizzesSolved +
                ", averageQuizzesSolved=" + averageQuizzesSolved +
                '}';
    }
}
