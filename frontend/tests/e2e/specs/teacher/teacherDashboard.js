const IMAGE_COMPARISON_RETRY_OPTIONS = {
  limit: 3, // max number of retries
  delay: 500, // delay before next comparison, ms
};
const IMAGE_COMPARISON_THRESHOLD = 0.01;

describe('Teacher Dashboard', () => {
  // which course execution is initially associated with the demo teacher
  const INITIAL_ACADEMIC_TERM = '1st Semester';

  const ACADEMIC_TERMS = [
    { name: '2023/2024', endDate: '2023-12-31T00:00:00' },
    { name: '2022/2023', endDate: '2022-12-31T00:00:00' },
    { name: '2019/2020', endDate: '2019-12-31T00:00:00' },
  ];

  // stats for 2023, then for 2022, and so on...
  const STUDENT_STATS = [
    {
      numStudents: 320,
      numMore75CorrectQuestions: 310,
      numAtLeast3Quizzes: 315,
    },
    {
      numStudents: 280,
      numMore75CorrectQuestions: 210,
      numAtLeast3Quizzes: 250,
    },
    {
      numStudents: 250,
      numMore75CorrectQuestions: 150,
      numAtLeast3Quizzes: 180,
    },
  ];

  const QUIZ_STATS = [
    {
      numQuizzes: 400,
      uniqueQuizzesSolved: 350,
      averageQuizzesSolved: 200,
    },
    {
      numQuizzes: 300,
      uniqueQuizzesSolved: 200,
      averageQuizzesSolved: 100,
    },
    {
      numQuizzes: 200,
      uniqueQuizzesSolved: 150,
      averageQuizzesSolved: 80,
    },
  ];

  const QUESTION_STATS = [
    {
      numAvailable: 600,
      answeredQuestionsUnique: 400,
      averageQuestionsAnswered: 300,
    },
    {
      numAvailable: 500,
      answeredQuestionsUnique: 250,
      averageQuestionsAnswered: 150,
    },
    {
      numAvailable: 400,
      answeredQuestionsUnique: 300,
      averageQuestionsAnswered: 150,
    },
  ];

  before(() => {
    // delete existing course executions that can interfere with the tests
    for (const academicTerm of ACADEMIC_TERMS) {
      cy.deleteCourseExecutionOnDemoCourse(academicTerm.name);
    }

    // ensure demo teacher exists, to be associated with dashboards below
    cy.request('http://localhost:8080/auth/demo/teacher').then((response) => {
      Cypress.env('token', response.body.token);
    });

    // create course executions to be used in the tests
    for (const { name, endDate } of ACADEMIC_TERMS) {
      cy.createCourseExecutionOnDemoCourse(name, endDate);
    }

    // populate dashboards (i: 0 = 2023, 1 = 2022, 2 = 2019)
    ACADEMIC_TERMS.forEach(({ name: currentAcademicTermName }, i) => {
      // get stats associated with this this iteration
      // (e.g., for 2022 with i = 1, get stats for 2022 and 2019)
      // and join them with their associated academic term's name
      const getStatsForCurrentAcademicTerm = (stats) =>
        ACADEMIC_TERMS.map((term, j) => ({
          academicTerm: term.name,
          ...stats[j],
        })).slice(i);

      cy.createDemoTeacherDashboard(
        currentAcademicTermName,
        getStatsForCurrentAcademicTerm(STUDENT_STATS),
        getStatsForCurrentAcademicTerm(QUIZ_STATS),
        getStatsForCurrentAcademicTerm(QUESTION_STATS)
      );
    });
  });

  after(() => {
    // restore the original teacher <-> course execution association
    cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm(
      INITIAL_ACADEMIC_TERM
    );
    // delete all previously created dashboards and course executions
    cy.deleteTeacherDashboards();
    for (const academicTerm of ACADEMIC_TERMS) {
      cy.deleteCourseExecutionOnDemoCourse(academicTerm.name);
    }
  });

  describe('Execution course with two past executions (2023)', () => {
    beforeEach(() => {
      // for simplification, have demo teacher only have one course execution
      // at a time -- here, set it to 2023
      cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm(
        ACADEMIC_TERMS[0].name
      );

      cy.demoTeacherLogin();
    });

    it('should display the correct statistics for the current year', () => {
      cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
        'getDashboard'
      );

      // open the teacher dashboard
      cy.get('[data-cy="dashboardMenuButton"]').click();
      cy.wait('@getDashboard');

      // check all tiles
      for (const [attribute, expectedValue] of [
        STUDENT_STATS,
        QUIZ_STATS,
        QUESTION_STATS,
      ].flatMap((stats) => Object.entries(stats[0]))) {
        cy.get(`[data-cy="tile-${attribute}"]`).should((tile) => {
          // `should` currently does not natively support custom error
          // messages, so we have to use `expect` instead;
          // see https://github.com/cypress-io/cypress/issues/6474

          expect(tile, `Tile ${attribute} has incorrect value`).to.have.text(
            expectedValue.toString()
          );
        });
      }
    });

    it('should display the correct charts', () => {
      cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
        'getDashboard'
      );

      // open the teacher dashboard
      cy.get('[data-cy="dashboardMenuButton"]').click();
      cy.wait('@getDashboard');

      // check all charts
      for (const stat of ['studentStats', 'quizStats', 'questionStats']) {
        cy.get(`[data-cy="chart-${stat}"]`).scrollIntoView({
          offset: { top: -50 },
        });
        cy.get(`[data-cy="chart-${stat}"]`)
          .should('be.visible')
          .invoke('attr', 'style', 'background: white;')
          .compareSnapshot(`${stat}2023`, IMAGE_COMPARISON_THRESHOLD, {
            ...IMAGE_COMPARISON_RETRY_OPTIONS,
            error: `Chart ${stat} does not sufficiently match the reference image`,
          });
      }
    });
  });
});
