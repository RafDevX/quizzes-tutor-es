<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <!--
        use a template that does not translate to a real HTML element in
        the final page structure, so that all items are treated equally in
        the flexbox layout, which would not be the case with an additional
        level of hierarchy -- helps if there isn't enough space to display
        all of a collection's items in a single row
      -->
      <template v-for="{ collection, attributes } in labels">
        <div
          v-for="{ attribute, label } in attributes"
          :key="attribute"
          class="items"
        >
          <div class="icon-wrapper">
            <animated-number
              :number="
                // @ts-ignore -- TS in Vue templates is not that good at inference;
                // here, it doesn't know `attribute` refers to `collection` in particular,
                // not to the union of all possible collections
                teacherDashboard[collection][0][attribute]
              "
            />
          </div>
          <div class="project-name">
            <p>{{ label }}</p>
          </div>
        </div>
      </template>
    </div>
    <template v-if="showComparisonCharts">
      <h2>Comparison with previous course executions</h2>
      <div class="stats-container">
        <bar-chart
          v-for="chart in charts"
          :key="chart.collection"
          :chartData="chart"
          :chartOptions="chartOptions"
          class="bar-chart"
        />
      </div>
    </template>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import AnimatedNumber from '@/components/AnimatedNumber.vue';
import TeacherDashboard from '@/models/teacherdashboard/TeacherDashboard';
import BarChart from '@/components/BarChart.vue';
import { ChartData } from 'chart.js';

// { a: number, b: string[] } => number | string[]
type ValueOf<T> = T[keyof T];

// SomeType[] => SomeType
type ArrayElement<A> = A extends readonly (infer T)[] ? T : never;

// number | string[] | string | A[] => string[] | A[]
type ArrayOnly<A> = A extends (infer _)[] ? A : never;

// { a: number, b: string[], c: A[] } => string[] | A[]
type ArrayValues<T> = ArrayOnly<ValueOf<T>>;

// Utility type that allows restricting `attribute` to be a key
// of (only) the collection `collection`
type CollectionLabel<T extends keyof TeacherDashboard> = {
  collection: T;
  attributes: {
    attribute: Exclude<
      keyof ArrayElement<TeacherDashboard[T]>,
      'id' | 'academicTerm'
    >;
    label: string;
  }[];
};

// 'a' | 'b' | 'c' => CollectionLabel<'a'> | CollectionLabel<'b'> | CollectionLabel<'c'>
// avoids CollectionLabel<'a' | 'b' | 'c'> which would happen if done manually,
// and would lead to invalid typing
type DistributeCollectionLabel<U> = U extends keyof TeacherDashboard
  ? CollectionLabel<U>
  : never;

@Component({
  components: { AnimatedNumber, BarChart },
})
export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;

  teacherDashboard: TeacherDashboard | null = null;

  // ideally these'd be objects, e.g.
  // { studentStats: { numStudents: 'Students: Total', ... }, ... }
  // but that would not guarantee ordering, so we use 'uglier' arrays instead
  labels: DistributeCollectionLabel<
    Exclude<keyof TeacherDashboard, 'id' | 'numberOfStudents'>
  >[] = [
    {
      collection: 'studentStats',
      attributes: [
        { attribute: 'numStudents', label: 'Students: Total' },
        {
          attribute: 'numMore75CorrectQuestions',
          label: 'Students: Solved >= 75% of Questions',
        },
        {
          attribute: 'numAtLeast3Quizzes',
          label: 'Students: Solved >= 3 Quizzes',
        },
      ],
    },
    {
      collection: 'quizStats',
      attributes: [
        { attribute: 'numQuizzes', label: 'Quizzes: Total Available' },
        {
          attribute: 'uniqueQuizzesSolved',
          label: 'Quizzes: Solved (Unique)',
        },
        {
          attribute: 'averageQuizzesSolved',
          label: 'Quizzes: Solved (Unique, Average Per Student)',
        },
      ],
    },
    {
      collection: 'questionStats',
      attributes: [
        {
          attribute: 'numAvailable',
          label: 'Questions: Total Available',
        },
        {
          attribute: 'answeredQuestionsUnique',
          label: 'Questions: Solved (Unique)',
        },
        {
          attribute: 'averageQuestionsAnswered',
          label: 'Questions: Solved (Unique, Average Per Student)',
        },
      ],
    },
  ];

  // these options are used for all charts
  chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    legend: {
      display: true,
      position: 'top',
      labels: {
        display: true,
        fontColor: '#000',
        fontSize: 14,
      },
    },
  };

  // colors to be used for bar chart's bars
  barChartColors = [
    '#C0392B', // red
    '#2980B9', // blue
    '#1ABC9C', // green
  ];

  // array of charts to be displayed, with their respective data and
  // an additional property to identify the collection they refer to.
  // null if not yet populated
  charts: (ChartData & { collection: string })[] | null = null;

  populateChartData() {
    if (!this.teacherDashboard) {
      this.charts = null;
      return;
    }

    this.charts = this.labels.map(({ collection, attributes }) => {
      const collectionValues = this.teacherDashboard![
        collection
      ] as ArrayValues<TeacherDashboard>;

      return {
        collection,
        labels: collectionValues.map((stat) => stat.academicTerm).reverse(),
        // ^ reverse to order from oldest to newest
        datasets: attributes.map(({ attribute, label }, index) => ({
          label,
          data: collectionValues
            .map((stat) => stat[attribute as keyof typeof stat] as number)
            .reverse(), // reverse to order from oldest to newest
          backgroundColor: Array(collectionValues.length).fill(
            this.barChartColors[index % this.barChartColors.length]
          ),
          // bar colors in a repeating pattern of the colors listed above
        })),
      };
    });
  }

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();
      this.populateChartData();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }

  // only show comparison charts if there are multiple course executions
  // (more specifically, if at least one collection has multiple entries)
  get showComparisonCharts() {
    return (
      this.teacherDashboard != null &&
      this.charts != null &&
      this.labels.some(
        ({ collection }) =>
          Array.isArray(this.teacherDashboard![collection]) &&
          this.teacherDashboard![collection].length > 1
      )
    );
  }
}
</script>

<style lang="scss" scoped>
.stats-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: stretch;
  align-content: center;
  height: 100%;

  .items {
    background-color: rgba(255, 255, 255, 0.75);
    color: #1976d2;
    border-radius: 5px;
    flex-basis: 25%;
    margin: 20px;
    cursor: pointer;
    transition: all 0.6s;
  }

  .bar-chart {
    background-color: rgba(255, 255, 255, 0.9);
    height: 400px;
    width: 800px;
    margin: 20px;
  }
}

.icon-wrapper,
.project-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-wrapper {
  font-size: 100px;
  transform: translateY(0px);
  transition: all 0.6s;
}

.icon-wrapper {
  align-self: end;
}

.project-name {
  align-self: start;
}

.project-name p {
  font-size: 24px;
  font-weight: bold;
  letter-spacing: 2px;
  transform: translateY(0px);
  transition: all 0.5s;
}

.items:hover {
  border: 3px solid black;

  & .project-name p {
    transform: translateY(-10px);
  }

  & .icon-wrapper i {
    transform: translateY(5px);
  }
}
</style>
