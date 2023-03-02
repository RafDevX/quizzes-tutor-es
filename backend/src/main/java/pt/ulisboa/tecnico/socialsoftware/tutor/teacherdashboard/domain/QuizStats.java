package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;

import javax.persistence.*;

@Entity
public class QuizStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "num_quizzes")
    private Integer numQuizzes;

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

    public Float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    public void update() {
        this.updateNumQuizzes();
        this.updateAverageQuizzesSolved();
    }

    private void updateNumQuizzes() {
        this.numQuizzes = courseExecution.getNumberOfQuizzes();
    }

    private void updateAverageQuizzesSolved() {
        this.averageQuizzesSolved = (float) this.courseExecution.getStudents()
            .stream()
            .mapToInt(student -> student.getQuizAnswers().size())
            .average()
            .orElse(0.0f);
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
                ", averageQuizzesSolved=" + averageQuizzesSolved +
                '}';
    }
}
