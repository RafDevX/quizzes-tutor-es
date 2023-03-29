import QuizStats from './QuizStats';
import QuestionStats from './QuestionStats';

export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number;

  quizStats!: QuizStats[];
  questionStats!: QuestionStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.quizStats = jsonObj.quizStats.map(
        (stats: QuizStats) => new QuizStats(stats)
      );
      this.questionStats = jsonObj.questionStats.map(
        (stats: QuestionStats) => new QuestionStats(stats)
      );
    }
  }
}
