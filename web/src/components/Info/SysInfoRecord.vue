<template>
  <div>
    <a-form-model ref="ruleForm" layout="vertical" :model="sysForm" :rules="rules" :wrapper-col="{ span: 20 }">
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-model-item label="系统名称" prop="name">
            <a-input-search
              v-if="canEdit"
              v-model="sysForm.name"
              enter-button="录入"
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

    <a-modal v-model="visible" width="80%" title="选择系统" @ok="addAsset" :dialogStyle="{ top: '20px' }">
      <a-form-model ref="sysFormDom" :model="sysFormCreate" :rules="createRules">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="系统名称" prop="name">
              <a-input v-model="sysFormCreate.name"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="系统定级" prop="systemGradingCode">
              <a-select allowClear v-model="sysFormCreate.systemGradingCode" placeholder="请选择">
                <a-select-option
                  v-for="(item, index) in levelList"
                  :key="index"
                  :value="item.value"
                  :label="item.title"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="年度" prop="year">
              <a-date-picker
                allowClear
                mode="year"
                placeholder="请选择年份"
                format="YYYY"
                v-model="sysFormCreate.yearTZ"
                style="width: 100%"
                :open="yearShowOne"
                @openChange="openChangeOne"
                @panelChange="panelChangeOne"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="项目立项时间" prop="projectInitiationTime">
              <a-date-picker
                allowClear
                style="width: 100%"
                v-model="sysFormCreate.projectInitiationTime"
                valueFormat="YYYY-MM-DD"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="系统类型" prop="systemTypeCode">
              <a-select allowClear v-model="sysFormCreate.systemTypeCode" placeholder="请选择">
                <a-select-option
                  v-for="(item, index) in sysTypeList"
                  :key="index"
                  :value="item.value"
                  :label="item.title"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="项目负责人" prop="projectLeaderCode">
              <a-input-search
                v-model="sysFormCreate.projectLeaderName"
                enter-button="选择"
                @search="onSearchPerson('projectLeader')"
                :readOnly="true"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="运维负责人" prop="omPrincipalCode">
              <a-input-search
                v-model="sysFormCreate.omPrincipalName"
                enter-button="选择"
                @search="onSearchPerson('omPrincipal')"
                :readOnly="true"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="运维部门" prop="omDepartmentCode">
              <a-input v-model="sysFormCreate.omDepartmentName" readOnly></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="开支类型" prop="expenditureTypeCode">
              <a-select allowClear v-model="sysFormCreate.expenditureTypeCode" placeholder="请选择">
                <a-select-option
                  v-for="(item, index) in payTypeList"
                  :key="index"
                  :value="item.value"
                  :label="item.title"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="服务开放范围" prop="serviceOpeningScopeCode">
              <a-select allowClear v-model="sysFormCreate.serviceOpeningScopeCode" placeholder="请选择">
                <a-select-option
                  v-for="(item, index) in serviceAreaList"
                  :key="index"
                  :value="item.value"
                  :label="item.title"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-model-item label="系统描述">
              <a-textarea v-model="sysFormCreate.description" :rows="3"></a-textarea>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>

    <a-modal v-model="personVisible" width="80%" title="人员选择" @ok="selectPerson" :dialogStyle="{ top: '20px' }">
      <ProcessDeal v-if="personVisible" ref="processDialogDom" :multi="false"></ProcessDeal>
    </a-modal>
  </div>
</template>

<script>
import STable from '@/components/table/'
import {
  ajaxGetDictItems,
  getSelectSysDetail,
  getSystemRecordList,
  sysAddSinger,
  specialUpdate,
} from '@/api/api'
import ProcessDeal from '@/components/Step/ProcessDeal'
import moment from 'moment'
export default {
  components: { STable, ProcessDeal },
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
      sysFormCreate: {},
      baseSysForm: {}, //保存系统录入返回的外部数据
      levelList: [], //字典值
      sysTypeList: [], //字典值
      payTypeList: [], //字典值
      serviceAreaList: [], //字典值
      yearShowOne: false, //用于年度板块的开关
      createRules: {
        name: [{ required: true, message: '请填写系统名称', trigger: 'blur' }],
        systemGradingCode: [{ required: true, message: '请选择系统定级', trigger: 'change' }],
        yearTZ: [{ required: true, message: '请选择年份', trigger: 'change' }],
        projectInitiationTime: [{ required: true, message: '请选择项目立项时间', trigger: 'change' }],
        systemTypeCode: [{ required: true, message: '请选择系统类型', trigger: 'change' }],
        projectLeaderCode: [{ required: true, message: '请选择项目负责人', trigger: 'change' }],
        omDepartmentCode: [{ required: true, message: '请选择运维部门', trigger: 'change' }],
        omPrincipalCode: [{ required: true, message: '请选择运维负责人', trigger: 'change' }],
        expenditureTypeCode: [{ required: true, message: '请选择开支类型', trigger: 'change' }],
        serviceOpeningScopeCode: [{ required: true, message: '请选择服务开放范围', trigger: 'change' }],
      },
      rules: {
        name: [{ required: true, message: '请选择系统', trigger: 'change' }],
      },
      currentPersonKey: '',
      visible: false,
      personVisible: false,
    }
  },
  mounted() {
    this.getDic('levelList', 'project_rank')
    this.getDic('sysTypeList', 'system_type')
    this.getDic('payTypeList', 'system_spending_type')
    this.getDic('serviceAreaList', 'system_service_coverage')
  },
  methods: {
    loadSys() {
      if (!this.baseInfo.wfInstanceId) {
        return
      }
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
    // 弹出日历和关闭日历的回调
    openChangeOne(status) {
      if (status) {
        this.yearShowOne = true
      }
    },
    panelChangeOne(value) {
      this.sysFormCreate.yearTZ = value
      this.sysFormCreate.year = (function (UTCDateString) {
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
      if (this.sysForm.wfInstanceId) {
        this.sysFormCreate = this.sysForm
      }
      this.visible = true
    },
    chooseItem(record) {
      getSelectSysDetail({
        bdProjectId: record.bdProjectId,
        wfInstanceId: this.baseInfo.wfInstanceId,
      }).then((res) => {
        if (res.success) {
          this.$set(this, 'sysForm', res.result)
          this.sysForm.wfInstanceId = this.baseInfo.wfInstanceId
          this.sysForm.yearTZ = moment(this.sysForm.year + '')
          this.visible = false
        }
      })
    },
    //新增资产
    addAsset() {
      this.$refs.sysFormDom.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.sysFormCreate.bdProjectId) {
            params = {
              ...this.sysFormCreate,
              bdProject: this.sysFormCreate,
            }
            specialUpdate(params).then((res) => {
              if (!res.success) {
                return
              }
              this.$notification.success({
                message: res.message,
              })
              this.visible = false
              this.loadSys()
            })
          } else {
            params = {
              ...this.sysFormCreate,
              bdProject: this.sysFormCreate,
              wfInstanceId: this.baseInfo.wfInstanceId,
            }
            sysAddSinger(params).then((res) => {
              if (!res.success) {
                return
              }
              this.$notification.success({
                message: res.message,
              })
              getSelectSysDetail({
                bdProjectId: res.result.bdProjectId,
                wfInstanceId: this.baseInfo.wfInstanceId,
              }).then(res=>{
                this.sysForm = {
                  ...res.result,
                  ...res.result.bdProject,
                }
                this.visible = false
              })
            })
          }
        }
      })
    },
    selectPerson() {
      let person = this.$refs.processDialogDom.selectUserRows[0]
      if (this.currentPersonKey === 'omPrincipal') {
        this.$set(this.sysFormCreate, 'omDepartmentName', person.orgCodeTxt)
        this.$set(this.sysFormCreate, 'omDepartmentCode', person.orgCode)
      }
      this.$set(this.sysFormCreate, this.currentPersonKey + 'Name', person.realname)
      this.$set(this.sysFormCreate, this.currentPersonKey + 'Code', person.username)
      this.personVisible = false
    },
    onSearchPerson(key) {
      this.currentPersonKey = key
      this.personVisible = true
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
  watch: {
    baseInfo: {
      handler(newVal, oldVal) {
        this.loadSys()
      },
      immediate: true,
      deep: true,
    },
  },
}
</script>

<style>
</style>