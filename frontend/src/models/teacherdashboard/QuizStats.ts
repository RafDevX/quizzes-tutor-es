export default class QuizStats {
  id!: number;
  academicTerm!: string;
  numQuizzes!: number;
  uniqueQuizzesSolved!: number;
  averageQuizzesSolved!: number;

  constructor(jsonObj?: QuizStats) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.academicTerm = jsonObj.academicTerm;
      this.numQuizzes = jsonObj.numQuizzes;
      this.uniqueQuizzesSolved = jsonObj.uniqueQuizzesSolved;
      this.averageQuizzesSolved = jsonObj.averageQuizzesSolved;
    }
  }
}
