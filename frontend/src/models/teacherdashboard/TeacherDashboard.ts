import QuestionStats from './QuestionStats';

export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number;

  questionStats!: QuestionStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.questionStats = jsonObj.questionStats.map(
        (qs: QuestionStats) => new QuestionStats(qs)
      );
    }
  }
}
