<template>
  <div>
    <a-card :bordered="false" title="三同步纳入系统情况" :bodyStyle="{height:'400px',padding:'10px'}">
      <a-row>
        <a-col :span="8">
          <head-info title="纳入三同步安全管理总数" :content="loginfo.manageTotal"></head-info>
        </a-col>
        <a-col :span="8">
          <head-info title="今年纳入总数" :content="loginfo.current"></head-info>
        </a-col>
        <a-col :span="8">
          <head-info title="当月纳入总数" :content="loginfo.history"></head-info>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="22">
          <div v-if="!loading" id="echarts-bar1" :style="{ width: '100%', minHeight: '300px' }"></div>
          <div v-if="loading" class="loading-text"><span>数据加载中</span><a-icon type="loading" /></div>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script>
import HeadInfo from '@/components/tools/HeadInfo.vue'
import { getSafeManageTotal, getYearTotal, getMonthTotal, getMonthRangeTotal } from '@/api/api'
export default {
  name: 'IndexBar',
  components: {
    HeadInfo,
  },
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
      loginfo: {
        manageTotal: '',
        current: '',
        history: '',
      },
      barValue: [],
      barTime: [],
    }
  },
  mounted() {
    this.loading = true
    this.getTotal()
  },
  methods: {
    getTotal() {
      getSafeManageTotal().then((res) => {
        if (res.success) {
          this.loginfo.manageTotal = res.result + ''
        }
      })
      getYearTotal().then((res) => {
        if (res.success) {
          this.loginfo.current = res.result + ''
        }
      })
      getMonthTotal().then((res) => {
        if (res.success) {
          this.loginfo.history = res.result + ''
        }
      })
      getMonthRangeTotal().then((res) => {
        if (res.success) {
          //this.barData = this.parentData
          for (let i = 0; i < res.result.length; i++) {
            this.barTime.push(res.result[i].name)
            this.barValue.push(res.result[i].value)
          }
          this.loading = false
          this.$nextTick(()=>{
            this.getBar()
          })
        }
        this.loading = false
      })
    },
    getBar() {
      let myChart = this.$echarts.init(document.getElementById('echarts-bar1'))
      myChart.setOption({
        tooltip: {
          trigger: 'item',
        },
        xAxis: {
          type: 'category',
          data: this.barTime,
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: this.barValue,
            type: 'bar',
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
    height: 300px;
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