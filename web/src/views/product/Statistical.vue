<template>
  <div class="page-header-index-wide">
    <a-row :gutter="12">
      <a-col :lg="19" :md="24" :style="{marginBottom:'12px'}">
        <a-row :gutter="12" :style="{marginBottom:'12px'}">
          <a-col :span="11">
            <IndexBar></IndexBar>
          </a-col>
          <a-col :span="13">
            <IndexPie></IndexPie>
          </a-col>
        </a-row>
        <a-row :gutter="12">
          <a-col :span="11">
            <IndexPieSys></IndexPieSys>
          </a-col>
          <a-col :span="13">
            <IndexLineAndBar></IndexLineAndBar>
          </a-col>
        </a-row>
      </a-col>
      <a-col :lg="5" :md="24" style="position:relative;height:888px;overflow-y:auto">
        <a-row :gutter="12">
          <a-col
            :lg="24"
            :md="6"
            v-for="(item, index) in dataList"
            :key="index"
            :style="{ marginBottom: '12px', cursor: 'pointer' }"
            @click="goList(item)"
          >
            <chart-card :title="item.name" :total="item.value + ''" :style="{height:'120px'}" :hideKey="['content', 'footer']">
              <a-tooltip title="指标说明" slot="action">
                <a-icon type="info-circle-o" />
              </a-tooltip>
              <template slot="icon">
                <img style="width: 50px; height: 50px; margin: 0 20px 10px 10px" :src="item.imgUrl" />
              </template>
            </chart-card>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import IndexBar from './statistical/IndexBar.vue'
import IndexPie from './statistical/IndexPie.vue'
import IndexPieSys from './statistical/IndexPieSys.vue'
import IndexLineAndBar from './statistical/IndexLineAndBar.vue'
import ChartCard from '@/components/ChartCard'
import { getModuleTotal,doubleTotalData } from '@/api/api'
export default {
  name: 'Statistical',
  components: {
    IndexBar,
    IndexPie,
    IndexPieSys,
    IndexLineAndBar,
    ChartCard,
  },
  data() {
    return {
      queryParam: {
        name: '',
        year: '',
        category: '',
        type: '',
      },
      dataList: [
        {
          name: '定级备案',
          value: '0',
          imgUrl: require('@/assets/系统定级.png'),
          wfCode: 'project_rank',
          path: 'syslist',
        },
        {
          name: '规划设计',
          value: '0',
          imgUrl: require('@/assets/规划设计.png'),
          wfCode: 'project_check',
          path: 'reviewlist',
        },
        {
          name: '安全入网',
          value: '0',
          imgUrl: require('@/assets/安全入网.png'),
          wfCode: 'network_access',
          path: 'constrlist',
        },
        {
          name: '4A管控',
          value: '0',
          imgUrl: require('@/assets/4A管控.png'),
          wfCode: 'network_access',
          bdFileFlagCode: '4A_template',
          path: 'constrlist',
        },
        {
          name: '安全验收',
          value: '0',
          imgUrl: require('@/assets/安全验收.png'),
          wfCode: 'accept',
          path: 'acceptlist',
        },
        {
          name: '双新评估',
          value: '0',
          imgUrl: require('@/assets/双新评估.png'),
          wfCode: 'project_rank',
          path: 'newlist',
        },
        {
          name: '安全运维',
          value: '0',
          imgUrl: require('@/assets/安全运维.png'),
          wfCode: 'operation',
          path: 'safelist',
        },
        {
          name: '风险评估',
          value: '0',
          imgUrl: require('@/assets/风险评估.png'),
          wfCode: 'risk_assessment',
          path: 'risklist',
        },
        {
          name: '变更报备',
          value: '0',
          imgUrl: require('@/assets/变更报备.png'),
          wfCode: 'alter_report',
          path: 'changelist',
        },
        {
          name: '应急处置',
          value: '0',
          imgUrl: require('@/assets/应急处置.png'),
          wfCode: 'disposal',
          path: 'disposallist',
        },
        {
          name: '安全退网',
          value: '0',
          imgUrl: require('@/assets/安全退网.png'),
          wfCode: 'network_exit',
          path: 'logoutlist',
        },
      ],
    }
  },
  mounted() {
    this.getTotalData()
  },
  methods: {
    getTotalData() {
      this.dataList.forEach((item) => {
        let params = {
          wfCode: item.wfCode,
          bdFileFlagCode: item.bdFileFlagCode,
        }
        if(item.name==='双新评估'){
          return
        }
        getModuleTotal(params).then((res) => {
          item.value = res.result
        })
      })
      doubleTotalData(this.queryParam).then((res) => {
        this.$set( this.dataList[5],'value',parseInt(res) || 0)
      })
    },
    goList(item) {
      this.$router.push({
        path: `/inspector/${item.path}`,
      })
    },
  },
}
</script>

<style lang="less" scoped>
</style>