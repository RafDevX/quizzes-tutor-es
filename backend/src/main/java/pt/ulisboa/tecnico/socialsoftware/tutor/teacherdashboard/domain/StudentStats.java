package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;

import javax.persistence.*;
import java.util.stream.Collectors;

@Entity
public class StudentStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "num_students")
    private Integer numStudents;

    @Column(name = "num_more_75_correct_questions")
    private Integer numMore75CorrectQuestions;

    @OneToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    public StudentStats() {
    }

    public StudentStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
    }

    public void remove() {
        teacherDashboard.removeStudentStats(this);
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
        this.teacherDashboard.addStudentStats(this);
    }

    public Integer getNumStudents() {
        return numStudents;
    }

    public Integer getNumMore75CorrectQuestions() {
        return numMore75CorrectQuestions;
    }

    public void update() {
        this.updateNumStudents();
        this.updateNumMore75CorrectQuestions();
    }

    private void updateNumStudents() {
        this.numStudents = this.courseExecution.getStudents().size();
    }

    private void updateNumMore75CorrectQuestions() {
        this.numMore75CorrectQuestions = (int) this.courseExecution.getQuizzes()
                .stream()
                .flatMap(quiz -> quiz.getQuizAnswers().stream())
                .collect(Collectors.groupingBy(
                        QuizAnswer::getStudent
                ))
                .values()
                .stream()
                .map(answers -> {
                    final long totalQuestions = answers.stream()
                            .map(quizAnswer -> (long) quizAnswer.getQuestionAnswers().size())
                            .reduce(0L, Long::sum);
                    final long correctQuestions = answers.stream()
                            .map(QuizAnswer::getNumberOfCorrectAnswers)
                            .reduce(0L, Long::sum);

                    return correctQuestions * 100 / totalQuestions;
                })
                .filter(correctRate -> correctRate >= 75)
                .count();
    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "StudentStats{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", numStudents=" + numStudents +
                ", numMore75CorrectQuestions=" + numMore75CorrectQuestions +
                '}';
    }
}
