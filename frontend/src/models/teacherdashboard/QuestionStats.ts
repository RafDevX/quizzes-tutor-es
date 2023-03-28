export default class QuestionStats {
  id!: number;
  academicTerm!: string;
  numAvailable!: number;
  answeredQuestionsUnique!: number;
  averageQuestionsAnswered!: number;

  constructor(jsonObj?: QuestionStats) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.academicTerm = jsonObj.academicTerm;
      this.numAvailable = jsonObj.numAvailable;
      this.answeredQuestionsUnique = jsonObj.answeredQuestionsUnique;
      this.averageQuestionsAnswered = jsonObj.averageQuestionsAnswered;
    }
  }
}
