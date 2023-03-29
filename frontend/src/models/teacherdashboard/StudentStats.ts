export default class StudentStats {
  id!: number;
  academicTerm!: string;
  numStudents!: number;
  numMore75CorrectQuestions!: number;
  numAtLeast3Quizzes!: number;

  constructor(jsonObj?: StudentStats) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.academicTerm = jsonObj.academicTerm;
      this.numStudents = jsonObj.numStudents;
      this.numMore75CorrectQuestions = jsonObj.numMore75CorrectQuestions;
      this.numAtLeast3Quizzes = jsonObj.numAtLeast3Quizzes;
    }
  }
}
