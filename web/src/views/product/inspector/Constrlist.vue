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
            <a-form-item label="当前流程节点">
              <a-select v-model="queryParam.wfNodeCode" placeholder="请选择" allowClear>
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
              <a-select v-model="queryParam.dataStateCode" placeholder="请选择" allowClear>
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
              <a-select v-model="queryParam.systemGradingCode" placeholder="请选择" allowClear>
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
        <a-row :style="{ marginBottom: '20px' }">
          <a-col :md="12" :sm="24" :offset="12">
            <div :style="{ textAlign: 'right' }">
              <a-button type="primary" @click="search">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetSearchForm">重置</a-button>
            </div>
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
        <a-dropdown v-if="record.bdFileList && record.bdFileList.length > 0" :trigger="['click']">
          <a class="ant-dropdown-link" @click="(e) => e.preventDefault()"> 查看文件 <a-icon type="down" /> </a>
          <a-menu slot="overlay">
            <a-menu-item v-for="(item, index) in record.bdFileList" :key="index">
              <span class="file-item-text" :class="item.filename?'file-item':''" @click="download(item)">
                {{ item.flagName + '：' + (item.filename || '') }}
              </span>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
        <span v-else>无</span>
      </span>
    </s-table>
  </a-card>
</template>

<script>
import { getInspectorList, ajaxGetDictItems } from '@/api/api'
import STable from '@/components/table/'
export default {
  components: { STable },
  name: 'inspectorConstrlist',
  data() {
    return {
      yearShowOne: false,
      queryParam: {
        name: '',
        systemGradingCode: '',
        year: null,
        wfCode: 'network_access',
        dataStateCode: '', //状态
        wfNodeCode: '', //当前流程节点
      },
      operateList: [],
      stateList: [],
      levelList: [],
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
          title: '系统类型',
          dataIndex: 'systemTypeName',
        },
        {
          title: '流程节点',
          dataIndex: 'wfNodeName',
        },
        {
          title: '过程文件',
          width: '300px',
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
    this.getDic('operateList', 'network_access_node')
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
      return getInspectorList(Object.assign(parameter, this.queryParam)).then((res) => {
        if (res && res.result) {
          return res.result
        }
      })
    },
    // 得到年份选择器的值
    panelChangeOne(value) {
      this.queryParam.year = value
      this.yearShowOne = false
    },
    onChange() {
      this.selectedRowKeys = row.selectedRowKeys
      this.selectedRows = row.selectedRows
    },
    //点击流程详情
    search() {
      this.$refs.tableDom.localPagination.current = 1
      this.$refs.tableDom.refresh()
    },
    resetSearchForm() {
      this.queryParam = {
        name: '',
        systemGradingCode: '',
        year: null,
        wfCode: 'network_access',
        dataStateCode: '', //状态
        wfNodeCode: '', //当前流程节点
      }
      this.search()
    },
    download(item) {
      if (!item.filename) {
        return
      }
      var a = document.createElement('a')
      a.download = item.filename
      a.href = `${window._CONFIG['domianURL']}/ists.bd/bdFile/download/${item.bdFileId}`
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
  },
}
</script>

<style lang="less" scoped>
.file-item {
  cursor: pointer;
  color: #40a9ff;
}
.file-item-text {
    display: inline-block;
    margin-right: 10px;
  }
</style>