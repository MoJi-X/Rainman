<template>
  <a-card :bordered="false" :bodyStyle="{ padding: '10px 20px' }">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-item label="系统名称">
              <a-input v-model="queryParam.name" placeholder="请输入" allowClear />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-item label="年度">
              <a-date-picker
                mode="year"
                placeholder="请选择年份"
                format="YYYY"
                v-model="queryParam.yearTZ"
                style="width: 100%"
                :open="yearShowOne"
                @openChange="openChangeOne"
                @panelChange="panelChangeOne"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="resetSearchForm">重置</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <s-table
      ref="tableDom"
      size="default"
      :columns="columns"
      :data="loadTableData"
      :showAlertInfo="true"
      @onSelect="onChange"
      bordered
    >
      <span slot="action" slot-scope="text, record">
        <a
          v-if="
            record.networkAccessWfInstanceId ||
            record.acceptWfInstanceId ||
            record.projectCheckWfInstanceId ||
            record.projectRankWfInstanceId
          "
          @click="
            () => {
              stepDetail(record)
            }
          "
          >流程详情</a
        >
      </span>
      <span slot="action" slot-scope="text, record">
        <a
          v-if="
            record.networkAccessWfInstanceId ||
            record.acceptWfInstanceId ||
            record.projectCheckWfInstanceId ||
            record.projectRankWfInstanceId
          "
          @click="
            () => {
              stepDetail(record)
            }
          "
          >流程详情</a
        >
      </span>
      <template v-for="(item,index) in key" :slot="item.code" slot-scope="text,record">
          <span style="color:#FAAD14" v-if="record[item.code+'StateName']==='进行中'">{{record[item.code+'StateName']}}</span>
          <span style="color:#389e0d" v-else-if="record[item.code+'StateName']==='已完结'">{{record[item.code+'StateName']}}</span>
          <span style="color:#ff4d4f" v-else>{{record[item.code+'StateName']}}</span>
      </template>
    </s-table>
  </a-card>
</template>

<script>
import STable from '@/components/table/'
import { productList, ajaxGetDictItems } from '@/api/api'
export default {
  components: { STable},
  name: 'Productlist',
  data() {
    return {
      queryParam: {},
      stateList: [],
      yearShowOne: false,
      columns: [
        {
          title: '项目系统名称',
          dataIndex: 'name',
        },
        {
          title: '年度',
          dataIndex: 'year',
        },
        {
          title:'同步规划',
          children:[
            {
              title: '系统定级',
              dataIndex: 'projectRankStateName',
              scopedSlots: { customRender: 'projectRank' },
            },
            {
              title: '立项评审',
              dataIndex: 'projectCheckStateName',
              scopedSlots: { customRender: 'projectCheck' },
            },
            {
              title: '特需流程',
              dataIndex: 'ineedCheckStateName',
              scopedSlots: { customRender: 'ineedCheck' },
            }
          ]
        },
        {
          title:'同步建设',
          children:[
            {
              title: '建设入网',
              dataIndex: 'networkAccessStateName',
              scopedSlots: { customRender: 'networkAccess' },
            },
            {
              title: '安全验收',
              dataIndex: 'acceptStateName',
              scopedSlots: { customRender: 'accept' },
            }
          ]
        },
        {
          title:'同步运行',
          children:[
            {
              title: '变更报备',
              dataIndex: 'alterReportStateName',
              scopedSlots: { customRender: 'alterReport' },
            },
            {
              title: '安全运维',
              dataIndex: 'operationStateName',
              scopedSlots: { customRender: 'operation' },
            },
            {
              title: '风险评估',
              dataIndex: 'riskAssessmentStateName',
              scopedSlots: { customRender: 'riskAssessment' },
            },
            {
              title: '处置备查',
              dataIndex: 'disposalStateName',
              scopedSlots: { customRender: 'disposal' },
            },
            {
              title: '安全退网',
              dataIndex: 'networkExitStateName',
              scopedSlots: { customRender: 'networkExit' },
            }
          ]
        },
        {
          title: '操作',
          width: '150px',
          scopedSlots: { customRender: 'action' },
        },
      ],
      key:[
        {
          code: 'projectRank',
          wfCode:'project_rank',
          name: '系统定级',
          filename:'sysDetail',
          isLeaf:true
        },
        {
          code: 'projectCheck',
          wfCode:'project_check',
          name: '立项评审',
          filename:'reviewDetail',
          isLeaf:true
        },
        {
          code: 'ineedCheck',
          wfCode:'ineed_check',
          name: '特需流程',
          filename:'specialDetail',
          isLeaf:true
        },
        {
          code: 'networkAccess',
          wfCode:'network_access',
          name: '入网建设',
          filename:'constrDetail',
          isLeaf:true
        },
        {
          code: 'accept',
          wfCode:'accept',
          name: '安全验收',
          filename:'safeDetail',
          isLeaf:true
        },
        {
          code: 'alterReport',
          wfCode:'alter_report',
          name: '变更报备',
          filename:'changeDetail'
        },
        {
          code: 'operation',
          wfCode:'operation',
          name: '安全运维',
          filename:'safeRunDetail'
        },
        {
          code: 'riskAssessment',
          wfCode:'risk_assessment',
          name: '风险评估',
          filename:'riskDetail'
        },
        {
          code: 'disposal',
          wfCode:'disposal',
          name: '处置备查',
          filename:'disposalDetail'
        },
        {
          code: 'networkExit',
          wfCode:'network_exit',
          name: '安全退网',
          filename:'logoutDetail'
        },
      ],
      selectedRowKeys: [], //勾选的数据
      selectedRows: [],
    }
  },
  mounted() {
    this.getDic('stateList', 'process_status')
  },
  methods: {
    //获取字典接口
    getDic(listKey, key) {
      ajaxGetDictItems(key).then((res) => {
        this.$set(this, listKey, res.result)
      })
    },
    // 弹出日历和关闭日历的回调
    openChangeOne(status) {
      if (status) {
        this.yearShowOne = true
      }
    },
    // 加载数据方法 必须为 Promise 对象
    loadTableData(parameter) {
      return productList(Object.assign(parameter, this.queryParam)).then((res) => {
        return res.result
      })
    },
    // 得到年份选择器的值
    panelChangeOne(value) {
      this.queryParam.yearTZ = value
      this.queryParam.year = (function (UTCDateString) {
        if (!UTCDateString) {
          return '-'
        }
        var date2 = new Date(UTCDateString) //这步是关键
        var year = date2.getFullYear()
        var dateStr = year
        return dateStr
      })(value)
      this.yearShowOne = false
    },
    onChange() {
      this.selectedRowKeys = row.selectedRowKeys
      this.selectedRows = row.selectedRows
    },
    stepDetail(record) {
      //StateName  WfInstanceId
      let key = this.key
      let processList = []
      for (let i = 0; i < key.length; i++) {
        if (record[key[i].code + 'WfInstanceId']) {
          let obj = {}
          obj['WfInstanceId'] = record[key[i].code + 'WfInstanceId']
          obj['StateName'] = record[key[i].code + 'StateName']
          obj['ProcessName'] = key[i].name
          obj['filename'] = key[i].filename
          obj['isLeaf'] = Boolean(key[i].isLeaf)
          obj['bdProjectId'] = record.bdProjectId
          obj['wfCode'] = key[i].wfCode
          processList.push(obj)
        }
      }
      this.$ls.set('productDetailList',processList)
      this.$router.push({
        path: '/product/productDetail',
      })
    },
    search() {
      this.$refs.tableDom.localPagination.current = 1
      this.$refs.tableDom.refresh()
    },
    resetSearchForm() {
      this.queryParam = {}
      this.search()
    },
  },
}
</script>

<style lang="less" scoped>
</style>