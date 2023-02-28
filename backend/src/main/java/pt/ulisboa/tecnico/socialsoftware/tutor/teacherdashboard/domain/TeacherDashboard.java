package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Entity
public class TeacherDashboard implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherDashboard", orphanRemoval = true)
    private final List<StudentStats> studentStats = new ArrayList<>();

    public TeacherDashboard() {
    }

    public TeacherDashboard(CourseExecution courseExecution, Teacher teacher) {
        setCourseExecution(courseExecution);
        setTeacher(teacher);
    }

    public void remove() {
        teacher.getDashboards().remove(this);
        teacher = null;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        this.teacher.addDashboard(this);
    }

    public List<StudentStats> getStudentStats() {
        return Collections.unmodifiableList(studentStats);
    }

    public void addStudentStats(StudentStats studentStats) {
        if (this.studentStats.stream()
                .anyMatch(studentStats1 -> Objects.equals(studentStats1.getCourseExecution().getId(), studentStats.getCourseExecution().getId()))) {
            throw new TutorException(ErrorMessage.STUDENT_STATS_ALREADY_EXISTS, studentStats.getCourseExecution().getId());
        }
        if (!Objects.equals(studentStats.getCourseExecution().getCourse().getId(), this.courseExecution.getCourse().getId())) {
            throw new TutorException(ErrorMessage.STUDENT_STATS_INCORRECT_COURSE,
                    studentStats.getCourseExecution().getCourse().getId(),
                    this.courseExecution.getCourse().getId());
        }

        this.studentStats.add(studentStats);
    }

    public void removeStudentStats(StudentStats studentStats) {
        if (!this.studentStats.contains(studentStats)) {
            throw new TutorException(ErrorMessage.STUDENT_STATS_NOT_FOUND, studentStats.getCourseExecution().getId());
        }

        this.studentStats.remove(studentStats);
    }

    public void update() {
        this.studentStats.forEach(StudentStats::update);
    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "Dashboard{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", teacher=" + teacher +
                '}';
    }

}
