<template>
  <div>
    <a-form-model ref="ruleForm" layout="vertical" :model="sysForm" :rules="rules" :wrapper-col="{ span: 20 }">
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-model-item label="系统名称" prop="name">
            <a-input-search
              v-if="canEdit"
              v-model="sysForm.name"
              enter-button="选择"
              @search="onSearch"
              :readOnly="true"
            />
            <a-input v-else :readOnly="true" v-model="sysForm.name"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="系统定级">
            <a-input :readOnly="true" v-model="sysForm.systemGradingName"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="系统类型">
            <a-input :readOnly="true" v-model="sysForm.systemTypeName"></a-input>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-model-item label="年度">
            <a-input :readOnly="true" v-model="sysForm.year"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="项目立项时间">
            <a-input :readOnly="true" v-model="sysForm.createdDate"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="运维部门">
            <a-input :readOnly="true" v-model="sysForm.omDepartmentName"></a-input>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-model-item label="项目负责人">
            <a-input :readOnly="true" v-model="sysForm.projectLeaderName"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="运维负责人">
            <a-input :readOnly="true" v-model="sysForm.omPrincipalName"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="开支类型">
            <a-input :readOnly="true" v-model="sysForm.expenditureTypeName"></a-input>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-model-item label="服务开放范围">
            <a-input :readOnly="true" v-model="sysForm.serviceOpeningScopeName"></a-input>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row :gutter="24">
        <a-col :span="24">
          <a-form-model-item label="系统描述">
            <a-textarea :readOnly="true" v-model="sysForm.description" :auto-size="{ minRows: 3 }"></a-textarea>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>

    <a-modal v-model="visible" width="80%" title="选择系统" :dialogStyle="{ top: '20px' }">
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="6" :sm="24">
              <a-form-item label="系统名称">
                <a-input v-model="bdProject.name" placeholder="请输入" allowClear />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
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
            <a-col :md="6" :sm="24">
              <div>
                <a-button type="primary" @click="searchList">查询</a-button>
                <a-button style="margin-left: 8px" @click="resetList">重置</a-button>
              </div>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <s-table ref="tableDom" size="default" :columns="columns" :data="loadData" :showAlertInfo="true">
        <span slot="action" slot-scope="text, record">
          <a
            href="javascript:void(0)"
            @click="
              () => {
                chooseItem(record,true)
              }
            "
            >点击选择</a
          >
        </span>
      </s-table>
      <template slot="footer">
        <a-button
          @click="
            () => {
              visible = false
            }
          "
          >关闭</a-button
        >
      </template>
    </a-modal>
  </div>
</template>

<script>
import STable from '@/components/table/'
import { getSelectSysList, ajaxGetDictItems, getSelectSysDetail, getSystemRecordList, saveSysData } from '@/api/api'
export default {
  components: { STable },
  props: {
    canEdit: {
      //是否处于编辑状态
      type: Boolean,
      default: true,
    },
    baseInfo: {
      type: Object,
    },
  },
  data() {
    return {
      sysForm: {},
      bdProject: {
        name: '',
        systemGradingCode: '',
        year: null,
      },
      wfInstance: {
        wfCode: '',
        stateCode: '', //状态
        wfNodeCode: '', //当前流程节点
      },
      levelList: [],
      columns: [
        {
          title: '操作',
          scopedSlots: { customRender: 'action' },
        },
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
          title: '系统定级',
          dataIndex: 'systemGradingName',
        },
        {
          title: '开支类型',
          dataIndex: 'expenditureTypeName',
        },
        {
          title: '项目负责人',
          dataIndex: 'projectLeaderName',
        },
        {
          title: '项目部门',
          dataIndex: 'projectDepartmentName',
        },
      ],
      yearShowOne: false, //用于年度板块的开关
      rules: {
        name: [{ required: true, message: '请选择系统', trigger: 'change' }],
      },
      visible: false,
    }
  },
  mounted() {
    this.getDic('levelList', 'project_rank')
  },
  watch: {
    baseInfo: {
      handler(newVal, oldVal) {
        this.wfInstance.wfCode = newVal.wfCode
        this.searchList()
        this.loadSys()
      },
      immediate: true,
      deep: true,
    },
  },
  methods: {
    loadSys() {
      getSystemRecordList({ wfInstanceId: this.baseInfo.wfInstanceId }).then((res) => {
        if (res && res.result && res.result.length > 0) {
          this.chooseItem(res.result[0])
        }
      })
    },
    //获取字典接口
    getDic(listKey, key) {
      ajaxGetDictItems(key).then((res) => {
        this.$set(this, listKey, res.result)
      })
    },
    searchList() {
      if (!this.$refs.tableDom) {
        return
      }
      this.$refs.tableDom.localPagination.current = 1
      this.$refs.tableDom.refresh()
    },
    resetList() {
      this.bdProject = {
        name: '',
        systemGradingCode: '',
        yearTZ: '',
        year: '',
      }
      this.wfInstance = {
        wfCode: this.baseInfo.wfCode,
        stateCode: '', //状态
        wfNodeCode: '', //当前流程节点
      }
      this.search()
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
    onSearch() {
      this.visible = true
    },
    chooseItem(record, isLoad = false) {
      getSelectSysDetail({
        bdProjectId: record.bdProjectId,
        wfInstanceId: this.baseInfo.wfInstanceId,
      }).then((res) => {
        if (res.success) {
          this.$set(this, 'sysForm', res.result)
          this.$emit('selectSys', res.result)
          this.visible = false
        }
      })
      if (isLoad) {
        saveSysData({
          bdProjectId: record.bdProjectId,
          wfInstanceId: this.baseInfo.wfInstanceId,
        }).then((res) => {
          if (!res.success) {
            this.$notification.warning({
                message: res.message,
            })
          }
        })
      }
    },
    loadData(parameter) {
      if (!this.wfInstance.wfCode) {
        return []
      }
      return getSelectSysList(
        Object.assign(parameter, {
          ...this.bdProject,
          ...this.wfInstance,
        })
      ).then((res) => {
        return res.result
      })
    },
    async checkValid() {
      let pass = true
      await this.$refs.ruleForm.validate((valid) => {
        if (!valid) {
          pass = false
        }
      })
      return pass
    },
  },
}
</script>

<style>
</style>