<template>
  <div class="page-header-index-wide">
    <a-card :loading="loading" :bodyStyle="{height:'400px',padding:'10px'}" :bordered="false" title="三同步整体概况">
      <a-row :style="{ marginBottom: '24px' }" :gutter="12">
        <a-col :span="8">
          <div style="border: 1px solid #e8e8e8">
            <chart-card :loading="loading" :bodyStyle="{padding: '20px 10px 12px' }" title="同步规划" :total="topObj.planCountPercent+'%'">
              <a-tooltip title="指标说明" slot="action">
                <a-icon type="info-circle-o" />
              </a-tooltip>
              <template slot="subtotal"> 
                <span class="sub-total">
                  /数量：{{topObj.planCount}}
                </span>
              </template>
              <template slot="footer">
                <mini-progress color="#1890FF" :target="topObj.planCountPercent" :percentage="topObj.planCountPercent" :height="8" />
              </template>
            </chart-card>
          </div>
        </a-col>
        <a-col :span="8">
          <div style="border: 1px solid #e8e8e8">
            <chart-card :loading="loading" :bodyStyle="{padding: '20px 10px 12px' }" title="同步建设" :total="topObj.buildCountPercent+'%'">
              <a-tooltip title="指标说明" slot="action">
                <a-icon type="info-circle-o" />
              </a-tooltip>
              <template slot="subtotal"> 
                <span class="sub-total">
                  /数量：{{topObj.buildCount}}
                </span>
              </template>
              <template slot="footer">
                <mini-progress color="#1890FF" :target="topObj.buildCountPercent" :percentage="topObj.buildCountPercent" :height="8" />
              </template>
            </chart-card>
          </div>
        </a-col>
        <a-col :span="8">
          <div style="border: 1px solid #e8e8e8">
            <chart-card :loading="loading" :bodyStyle="{padding: '20px 10px 12px' }" title="同步运行" :total="topObj.runtimeCountPercent+'%'">
              <a-tooltip title="指标说明" slot="action">
                <a-icon type="info-circle-o" />
              </a-tooltip>
              <template slot="subtotal"> 
                <span class="sub-total">
                  /数量：{{topObj.runtimeCount}}
                </span>
              </template>
              <template slot="footer">
                <mini-progress color="#1890FF" :target="topObj.runtimeCountPercent" :percentage="topObj.runtimeCountPercent" :height="8" />
              </template>
            </chart-card>
          </div>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="12">
          <div class="color-card one">
            <div class="icon">
              <a-icon class="icon-item" type="area-chart" />
            </div>
            <div class="count">
              {{total1}}<span class="unit">个</span>
            </div>
            <div class="desc">
              未纳入三同步安全管理
            </div>
          </div>
        </a-col>
        <a-col :span="12">
          <div class="color-card two">
            <div class="icon">
              <a-icon class="icon-item" type="dot-chart" />
            </div>
            <div class="count">
              {{total2}}<span class="unit">个</span>
            </div>
            <div class="desc">
              做了安全规划，未验收系统数
            </div>
          </div>
        </a-col>
      </a-row>
      <a-row :gutter="24" :style="{marginTop:'12px'}">
        <a-col :span="12">
          <div class="color-card three">
            <div class="icon">
              <a-icon class="icon-item" type="sliders" />
            </div>
            <div class="count">
              {{total3}}<span class="unit">个</span>
            </div>
            <div class="desc">
              未做安全规划、投入建设系统数
            </div>
          </div>
        </a-col>
        <a-col :span="12">
          <div class="color-card four">
            <div class="icon">
              <a-icon class="icon-item" type="fund" />
            </div>
            <div class="count">
              {{total4}}<span class="unit">个</span>
            </div>
            <div class="desc">
              全面纳入三同步，投入运行系统数
            </div>
          </div>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script>
import ChartCard from '@/components/ChartCard'
import MiniProgress from '@/components/chart/MiniProgress'
import {getRightTotal,tryxTotal,wysTotal,trysTotal,tryxxtTotal} from '@/api/api'
export default {
  name: 'IndexPie',
  components: {
    ChartCard,
    MiniProgress,
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
      topObj:{
        planCountPercent:0,
        planCount:0,
        buildCountPercent:0,
        buildCount:0,
        runtimeCountPercent:0,
        runtimeCount:0
      },
      total1:0,
      total2:0,
      total3:0,
      total4:0,
    }
  },
  mounted() {
    setTimeout(() => {
      this.init()
    }, 20)
  },
  methods: {
    init(){
      getRightTotal().then(res=>{
        if(res.success){
          this.topObj = res.result
          for(let i in this.topObj){
            if(i.indexOf('Percent')===-1){
              this.topObj[i+'Percent'] = Math.floor(this.topObj[i]/this.topObj.allCount * 100)
            }
          }
        }
      })
      tryxTotal().then(res=>{
        if(res.success){
          this.total1 = res.result
        }
      })
      wysTotal().then(res=>{
        if(res.success){
          this.total2 = res.result
        }
      })
      trysTotal().then(res=>{
        if(res.success){
          this.total3 = res.result
        }
      })
      tryxxtTotal().then(res=>{
        if(res.success){
          this.total4 = res.result
        }
      })
    }
  },
}
</script>

<style lang="less" scoped>
.page-header-index-wide {
}
.sub-total{
  font-size:14px;
  color:#000000;
}
.color-card{
  display: flex;
  align-items: center;
  height:80px;
  padding:10px 10px;
  color:#FFFFFF;
  .icon{
    margin:0px 10px;
    .icon-item{
      font-size: 32px;
    }
  }
  .count{
    flex:3;
    font-size: 32px;
    .unit{
      font-size: 16px;
    }
  }
  .desc{
    flex:2;
    font-size: 12px;
  }
}
.one{
  background: crimson;
}
.two{
  background: darkgoldenrod;
}
.three{
  background: darkturquoise;
}
.four{
  background: lightgreen;
}
</style>