<template>
  <a-card :bordered="false" :bodyStyle="{ padding: '10px 20px' }">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-item label="业务名称">
              <a-input v-model="queryParam.name" placeholder="请输入" allowClear />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-item label="业务类型">
              <a-select v-model="queryParam.type" placeholder="请选择" allowClear>
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
            <a-form-item label="业务类别">
              <a-select v-model="queryParam.category" placeholder="请选择" allowClear>
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

    <a-table
      :columns="columns"
      row-key="id"
      :data-source="newsList"
      :pagination="{
        current:parseInt(queryParam.current),
        pageSize:parseInt(queryParam.pageSize),
        total:parseInt(queryParam.total),
        showTotal: (total, range) => {
          return ' 共' + total + '条'
        },
      }"
      @change="changePage"
      :loading="loading"
    >
      <template slot="action" slot-scope="text, record">
        <a-popconfirm title="是否要跳转到对应页面？" @confirm="handleEdit(record)">
          <a>查看</a>
        </a-popconfirm>
      </template>
      <template slot="category" slot-scope="text, record">
        {{ stateList[record.category - 1].title }}
      </template>
      <template slot="type" slot-scope="text, record">
        {{ operateList[record.type - 1].title }}
      </template>
    </a-table>
  </a-card>
</template>

<script>
import { doubleNewListData, doubleTotalData, doubleNewCommonUrl } from '@/api/api'
export default {
  name: 'inspectorNewlist',
  data() {
    return {
      yearShowOne: false,
      queryParam: {
        name: '',
        year: '',
        category: '',
        current: 1,
        pageSize: 10,
        total: 0,
        type: '',
      },
      commonUrl: '', //公共的跳转链接
      newsList: [],
      loading: false,
      operateList: [
        //1：存量业务、2：新业务
        {
          title: '存量业务',
          value: 1,
        },
        {
          title: '新业务',
          value: 2,
        },
      ],
      stateList: [
        //1：一类业务、2：二类业务、3：三类业务
        {
          title: '一类业务',
          value: 1,
        },
        {
          title: '二类业务',
          value: 2,
        },
        {
          title: '三类业务',
          value: 3,
        },
      ],
      columns: [
        {
          title: '业务名称',
          dataIndex: 'name',
        },
        {
          title: '年度',
          dataIndex: 'year',
        },
        {
          title: '业务类型',
          scopedSlots: { customRender: 'type' },
        },
        {
          title: '业务类别',
          scopedSlots: { customRender: 'category' },
        },
        {
          title: '业务责任人',
          dataIndex: 'responsible_person',
        },
        {
          title: '所属部门',
          dataIndex: 'departname',
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
    this.loadTableData()
    doubleNewCommonUrl().then((res) => {
      this.commonUrl = res
    })
  },
  methods: {
    // 弹出日历和关闭日历的回调
    openChangeOne(status) {
      if (status) {
        this.yearShowOne = true
      }
    },
    // 加载数据方法 必须为 Promise 对象
    loadTableData() {
      this.loading = true
      doubleNewListData(this.queryParam)
        .then((res) => {
          this.loading = false
          if (res) {
            this.newsList = res
          }
        })
        .catch((err) => {
          this.loading = false
        })
      doubleTotalData(this.queryParam).then((res) => {
        this.queryParam.total = parseInt(res) || 0
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
    handleEdit(record) {
      window.open(this.commonUrl + '&id=' + record.id)
    },
    changePage(pagination, filters, sorter) {
      this.queryParam.current = parseInt(pagination.current)
      this.queryParam.pageSize = parseInt(pagination.pageSize)
      this.loadTableData()
    },
    //点击流程详情
    search() {
      this.loadTableData()
    },
    resetSearchForm() {
      this.queryParam = {
        name: '',
        year: '',
        category: '',
        current: 1,
        pageSize: 10,
        type: '',
      }
      this.loadTableData()
    },
  },
}
</script>

<style lang="less" scoped>
.file-item {
  display: inline-block;
  margin: 0px 10px;
}
</style>