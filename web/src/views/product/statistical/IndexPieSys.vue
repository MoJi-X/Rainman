<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" title="" :bodyStyle="{ height: '420px', padding: '10px' }">
      <div v-if="!loading" id="echarts-sys" :style="{ width: '100%', height: '400px' }"></div>
      <div v-if="loading" class="loading-text"><span>数据加载中</span><a-icon type="loading" /></div>
    </a-card>
  </div>
</template>

<script>
import { getSysPie } from '@/api/api'
export default {
  name: 'IndexPieSys',
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
      pieData: [],
      title: '系统定级',
    }
  },
  mounted() {
    this.loading = true
    getSysPie().then((res) => {
      if (res.success) {
        this.pieData = []
        let total = 0
        for (let i = 0; i < res.result.length; i++) {
          let obj = {
            name: res.result[i].text,
            value: res.result[i].count,
          }
          this.pieData.push(obj)
          total += res.result[i].count
        }
        this.title += total + '个'
        this.loading = false
        this.$nextTick(() => {
          this.createPie()
        })
      }
      this.loading = false
    })
  },
  methods: {
    createPie() {
      let myChart = this.$echarts.init(document.getElementById('echarts-sys'))
      myChart.setOption({
        title: {
          text: this.title,
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: this.pieData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
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
</style>