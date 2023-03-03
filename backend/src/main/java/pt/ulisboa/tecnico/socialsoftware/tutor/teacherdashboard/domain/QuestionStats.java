package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.*;
import java.util.stream.Collectors;


@Entity
public class QuestionStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "num_available")
    private Integer numAvailable;

    @Column(name = "answered_questions_unique")
    private Integer answeredQuestionsUnique;

    @Column(name = "average_questions_answered")
    private Float averageQuestionsAnswered;

    @OneToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;


    public QuestionStats() {
    }

    public QuestionStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
        update();
    }

    public void remove() {
        this.teacherDashboard.removeQuestionStats(this);
        this.teacherDashboard = null;
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
        this.teacherDashboard.addQuestionStats(this);
    }

    public Integer getNumAvailable() {
        return numAvailable;
    }

    public Integer getAnsweredQuestionsUnique() {
        return answeredQuestionsUnique;
    }

    public Float getAverageQuestionsAnswered() {
        return averageQuestionsAnswered;
    }

    public void update() {
        this.updateNumAvailable();
        this.updateAnsweredQuestionsUnique();
        this.updateAverageQuestionsAnswered();
    }

    public void updateNumAvailable() {
        this.numAvailable = (int) this.courseExecution.getCourse()
                .getQuestions()
                .stream()
                .filter(question -> question.getStatus() == Question.Status.AVAILABLE)
                .count();
    }

    public void updateAnsweredQuestionsUnique() {
        this.answeredQuestionsUnique = (int) this.courseExecution.getQuizzes()
                .stream()
                .flatMap(quiz -> quiz.getQuizAnswers().stream())
                .filter(QuizAnswer::isCompleted)
                .flatMap(quizAnswer -> quizAnswer.getQuestionAnswers().stream())
                .map(QuestionAnswer::getQuestion)
                .distinct()
                .count();
    }

    public void updateAverageQuestionsAnswered() {
        final int totalStudents = this.courseExecution.getStudents().size();
        final long totalAnsweredQuestions = this.courseExecution.getQuizzes()
                .stream()
                .flatMap(quiz -> quiz.getQuizAnswers().stream())
                .filter(QuizAnswer::isCompleted)
                .collect(Collectors.groupingBy(
                        QuizAnswer::getStudent
                ))
                .values()
                .stream()
                .map(answers -> answers.stream()
                        .flatMap(quizAnswer -> quizAnswer.getQuestionAnswers().stream())
                        .map(QuestionAnswer::getQuestion)
                        .distinct()
                        .count()
                )
                .reduce(0L, Long::sum);

        this.averageQuestionsAnswered = totalStudents == 0 ? 0.0f : (float) totalAnsweredQuestions / totalStudents;
    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "QuestionStats{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", numAvailable=" + numAvailable +
                ", answeredQuestionsUnique=" + answeredQuestionsUnique +
                ", averageQuestionsAnswered=" + averageQuestionsAnswered +
                '}';
    }
}
