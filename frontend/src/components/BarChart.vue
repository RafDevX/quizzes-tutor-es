<template>
  <Bar :chartData="chartData" :chartOptions="chartOptions" />
</template>

<script lang="ts">
import 'chart.js/auto';
import type { ChartData, ChartOptions } from 'chart.js';
import { Bar } from 'vue-chartjs/legacy';
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component({ components: { Bar } })
export default class BarChart extends Vue {
  @Prop({ required: true }) readonly chartData!: ChartData;
  @Prop({ required: true }) readonly chartOptions!: ChartOptions;
}
</script>
