<template>
  <a-card :bordered="false" :bodyStyle="{ padding: '10px 20px' }">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-item label="系统名称">
              <a-input v-model="bdProject.name" placeholder="请输入" allowClear />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-item label="当前流程节点">
              <a-select v-model="wfInstance.wfNodeCode" placeholder="请选择" allowClear>
                <a-select-option
                  v-for="(item, index) in operateList"
                  :key="index"
                  :label="item.title"
                  :value="item.value"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-item label="状态">
              <a-select v-model="wfInstance.stateCode" placeholder="请选择" allowClear>
                <a-select-option
                  v-for="(item, index) in stateList"
                  :key="index"
                  :label="item.title"
                  :value="item.value"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-item label="系统定级">
              <a-select v-model="bdProject.systemGradingCode" placeholder="请选择" allowClear>
                <a-select-option
                  v-for="(item, index) in levelList"
                  :key="index"
                  :label="item.title"
                  :value="item.value"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="6" :sm="24" v-if="toggleSearchStatus">
            <a-form-item label="年度">
              <a-date-picker
                mode="year"
                placeholder="请选择年份"
                format="YYYY"
                v-model="bdProject.yearTZ"
                style="width: 100%"
                :open="yearShowOne"
                @openChange="openChangeOne"
                @panelChange="panelChangeOne"
              />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24" :offset="toggleSearchStatus ? 6 : 12">
            <a-form-item :style="{ textAlign: 'right' }">
              <a-button
                type="primary"
                icon="plus"
                @click="
                  () => {
                    enterDetail()
                  }
                "
                >发起系统定级评审</a-button
              >
              <a-button style="margin-left: 8px" type="primary" @click="search">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetSearchForm">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '高级搜索' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </a-form-item>
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
    >
      <span slot="action" slot-scope="text, record">
        <a v-if="record.stateCode === 'returned'" @click="handleEdit(record, 'edit')">修改</a>
        <a v-if="record.stateCode === 'not_started'" @click="handleEdit(record, 'update')">发起流程</a>
        <a v-if="record.stateCode === 'finished'" @click="handleEdit(record, 'view')">流程详情</a>
        <a v-if="record.stateCode === 'in_progress'" @click="handleEdit(record, 'check')">流程处理</a>
        <a-divider v-if="record.stateCode === 'not_started'" type="vertical" />
        <a-popconfirm v-if="record.stateCode === 'not_started'" title="是否要删除此流程？" @confirm="deleteData(record)">
          <a>删除</a>
        </a-popconfirm>
      </span>
    </s-table>
  </a-card>
</template>

<script>
import STable from '@/components/table/'
import { createDraft, getSystemList, ajaxGetDictItems,deleteWfData } from '@/api/api'
export default {
  components: { STable },
  name: 'Syslist',
  data() {
    return {
      yearShowOne: false,
      toggleSearchStatus: false,
      bdProject: {
        name: '',
        systemGradingCode: '',
        year: null,
      },
      wfInstance: {
        wfCode: 'project_rank',
        stateCode: '', //状态
        wfNodeCode: '', //当前流程节点
      },
      operateList: [],
      stateList: [],
      levelList: [],
      columns: [
        {
          title: '流程名称',
          dataIndex: 'name',
        },
        {
          title: '当前流程节点',
          dataIndex: 'wfNodeName',
        },
        {
          title: '状态',
          dataIndex: 'stateName',
        },
        {
          title: '操作',
          width: '150px',
          scopedSlots: { customRender: 'action' },
        },
      ],
      selectedRowKeys: [], //勾选的数据
      selectedRows: [],
    }
  },
  mounted() {
    this.getDic('stateList', 'process_status')
    this.getDic('levelList', 'project_rank')
    this.getDic('operateList', 'project_rank_node')
  },
  methods: {
    deleteData(record){
      deleteWfData({
        wfInstanceId:record.wfInstanceId
      }).then(res=>{
        if(res.success){
          this.$notification.success({
            message:'删除成功',
          })
          this.search()
        }
      })
    },
    handleToggleSearch() {
      this.toggleSearchStatus = !this.toggleSearchStatus
    },
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
    panelChangeOne(value) {
      this.bdProject.yearTZ = value
      this.bdProject.year = (function (UTCDateString) {
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
    // 加载数据方法 必须为 Promise 对象
    loadTableData(parameter) {
      return getSystemList(
        Object.assign(parameter, {
          bdProject: this.bdProject,
          wfInstance: this.wfInstance,
        })
      ).then((res) => {
        if (res && res.result) {
          for (let i = 0; i < res.result.records.length; i++) {
            res.result.records[i] = { ...res.result.records[i], ...res.result.records[i].wfInstance }
          }
        }
        return res.result
      })
    },
    onChange(row) {
      this.selectedRowKeys = row.selectedRowKeys
      this.selectedRows = row.selectedRows
    },
    //点击流程详情
    handleEdit(record, type) {
      this.$ls.set('sysDetailId', record.wfInstanceId)
      this.$router.push({
        path: '/planning/sysDetail',
      })
    },
    search() {
      debugger
      this.$refs.tableDom.localPagination.current = 1
      this.$refs.tableDom.refresh()
    },
    resetSearchForm() {
      this.bdProject = {
        name: '',
        systemGradingCode: '',
        year: null,
      }
      this.wfInstance = {
        wfCode: 'project_rank',
        stateCode: '', //状态
        wfNodeCode: '', //当前流程节点
      }
      this.search()
    },
    //发起系统定级评审
    async enterDetail() {
      let res = await createDraft({
        wfCode: 'project_rank',
      })
      if (!res.success) {
        this.$notification.error({
          message: res.message,
        })
        return
      }
      let Pid = ''
      if (res.success) {
        Pid = res.result.wfInstanceId
      } else {
        this.$notification.warning({
          message: res.message,
        })
        return
      }
      this.$ls.set('sysDetailId', Pid)
      this.$router.push({
        path: '/planning/sysDetail',
      })
    },
  },
}
</script>

<style lang="less" scoped>
</style>