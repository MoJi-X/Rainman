<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" title="" :bodyStyle="{ height: '420px', padding: '10px' }">
      <div v-if="!loading" id="eaecharts-sys" :style="{ width: '100%', height: '400px' }"></div>
      <div v-if="loading" class="loading-text"><span>数据加载中</span><a-icon type="loading" /></div>
    </a-card>
  </div>
</template>

<script>
import { getExtendData } from '@/api/api'
export default {
  name: 'IndexLineAndBar',
  props: {
    parentData: {
      //数据全部来自父级，不可修改
      type: Array,
      default: () => {
        return []
      },
    },
  },
  data() {
    return {
      loading: false,
      title: '三同步部门开展情况',
      developmentList: [], //部门情况
      barData1: [],
      barData2: [],
      barData3: [],
      lineData: [],
      lineTop: 0,
    }
  },
  mounted() {
    this.loading = true
    getExtendData().then((res) => {
      if (res.success) {
        let barNum = 0
        let lineNum = 0
        for (let i = 0; i < res.result.length; i++) {
          this.developmentList.push(res.result[i].orgName)
          this.barData1.push(res.result[i].planCount || 0)
          this.barData2.push(res.result[i].buildCount || 0)
          this.barData3.push(res.result[i].runtimeCount || 0)
          let maxBar = Math.max(res.result[i].planCount, res.result[i].buildCount, res.result[i].runtimeCount)
          if (barNum < maxBar) barNum = maxBar

          let lData = res.result[i].planCount + res.result[i].buildCount + res.result[i].runtimeCount
          this.lineData.push(lData)
          if (lineNum < lData) lineNum = lData
        }
        this.lineTop = (lineNum % 10===0?parseInt(lineNum / 10):(parseInt(lineNum / 10) + 1)) * 10
        this.loading = false
        this.$nextTick(() => {
          this.createChart()
        })
      }
      this.loading = false
    })
  },
  methods: {
    createChart() {
      let myChart = this.$echarts.init(document.getElementById('eaecharts-sys'))
      myChart.setOption({
        title: {
          text: this.title,
          left: 'center',
        },
        legend: {
          data: ['同步规划', '同步建设', '同步运行', '总数'],
          bottom: '0%',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999',
            },
          },
        },
        xAxis: [
          {
            type: 'category',
            data: this.developmentList,
            axisPointer: {
              type: 'shadow',
            },
            axisLabel: {
              fontSize: 12,
              interval: 0,
              rotate: 15,
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '数量',
            min: 0,
            max: this.lineTop,
            interval: 10,
            axisLabel: {
              formatter: '{value}',
            },
          },
          // {
          //   type: 'value',
          //   name: '数量',
          //   min: 0,
          //   max: this.lineTop,
          //   interval: 10,
          //   axisLabel: {
          //     formatter: '{value}',
          //   },
          // },
        ],
        series: [
          {
            name: '同步规划',
            type: 'bar',
            color: '#70dfdf',
            label: {
              show: true,
              position: 'top',
              formatter: function (params) {
                if (params.value > 0) {
                  return params.value
                } else {
                  return ''
                }
              },
            },
            data: this.barData1,
          },
          {
            name: '同步建设',
            type: 'bar',
            label: {
              show: true,
              position: 'top',
              formatter: function (params) {
                if (params.value > 0) {
                  return params.value
                } else {
                  return ''
                }
              },
            },
            color: '#5bc2e7',
            data: this.barData2,
          },
          {
            name: '同步运行',
            type: 'bar',
            label: {
              show: true,
              position: 'top',
              formatter: function (params) {
                if (params.value > 0) {
                  return params.value
                } else {
                  return ''
                }
              },
            },
            color: '#3390FF',
            data: this.barData3,
          },
          {
            name: '总数',
            type: 'line',
            color: '#FF458C',
            // yAxisIndex: 1,
            label: {
              show: true,
              position: 'top',
              formatter: function (params) {
                if (params.value > 0) {
                  return params.value
                } else {
                  return ''
                }
              },
            },
            data: this.lineData,
          },
        ],
      })
      window.onresize = () => {
        myChart.resize()
      }
    },
  },
}
</script>

<style lang="less" scoped>
.page-header-index-wide {
  position: relative;
  .loading-text {
    font-size: 24px;
    height: 400px;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    span {
      display: line-block;
      margin-right: 10px;
    }
  }
}
</style>