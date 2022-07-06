<template>
  <div>
    <div class="table-operator" v-if="(canEditRiskPoint || canEditRiskResult) && queryForm.bdProjectId&&!isView">
      <a-upload 
      :action="actionUrl" 
      :headers="tokenHeader"
      :beforeUpload="beforeUpload"
      :showUploadList="false" 
      :data="{
        bdProjectId:queryForm.bdProjectId,
        wfInstanceId: Pid,
      }"
      @change="uploadDone">
        <a-button type="primary">批量导入</a-button>
      </a-upload>
      <a-button @click="addAsset">单个录入</a-button>
      <a-button type="text" @click="downLoadTemplate">下载导入模板<a-icon type="download" /></a-button>
    </div>
    <s-table
      ref="tableSysDom"
      :showPagination="true"
      size="default"
      :scroll="{ x: '100%' }"
      :columns="columns"
      :data="loadTableData"
      :showAlertInfo="true"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">{{(canEditRiskPoint||canEditRiskResult)&&!isView?'修改':'查看'}}</a>
        <a-divider v-if="(canEditRiskPoint||canEditRiskResult)&&!isView" type="vertical" />
        <a-popconfirm v-if="(canEditRiskPoint||canEditRiskResult)&&!isView" title="是否要删除此资产？" @confirm="deleteData(record)">
          <a>删除</a>
        </a-popconfirm>
      </span>
    </s-table>

    <a-modal v-model="visible" width="60%" title="资产录入">
      <a-form-model v-if="visible" ref="assetFormDom" :model="assetForm" :rules="rules">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="风险点" prop="riskPoints">
              <a-input :disabled="!canEditRiskPoint && !canEditRiskResult" v-model="assetForm.riskPoints"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="风险等级" prop="riskLevelCode">
              <a-select :disabled="!canEditRiskPoint && !canEditRiskResult" allowClear v-model="assetForm.riskLevelCode" placeholder="请选择">
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
            <a-form-model-item label="风险整改开始时间" prop="riskRectificationBeginDate">
              <a-date-picker
                allowClear
                :disabled="!canEditRiskPoint && !canEditRiskResult"
                mode="year"
                placeholder="请选择年份"
                format="YYYY"
                :disabledDate="riskStartYear"
                v-model="assetForm.riskRectificationBeginDateTZ"
                style="width: 100%"
                :open="yearShowStart"
                @openChange="openChangeStart"
                @panelChange="panelChangeStart"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="风险整改结束时间" prop="riskRectificationEndDate">
              <a-date-picker
                allowClear
                mode="year"
                :disabled="!canEditRiskPoint && !canEditRiskResult"
                placeholder="请选择年份"
                format="YYYY"
                :disabledDate="riskEndYear"
                v-model="assetForm.riskRectificationEndDateTZ"
                style="width: 100%"
                :open="yearShowEnd"
                @openChange="openChangeEnd"
                @panelChange="panelChangeEnd"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-form-model-item label="风险描述" prop="rectificationDescription">
          <a-textarea :disabled="!canEditRiskPoint && !canEditRiskResult" v-model="assetForm.rectificationDescription"></a-textarea>
        </a-form-model-item>
        <a-form-model-item label="整改建议" prop="rectificationProposal">
          <a-textarea :disabled="!canEditRiskPoint && !canEditRiskResult" v-model="assetForm.rectificationProposal"></a-textarea>
        </a-form-model-item>
        <a-form-model-item label="整改结果" :prop="canEditRiskResult?'rectificationResult':''">
          <a-textarea :disabled="!canEditRiskPoint && !canEditRiskResult" v-model="assetForm.rectificationResult" :readOnly="!canEditRiskResult"></a-textarea>
        </a-form-model-item>
      </a-form-model>
      <template slot="footer">
        <a-button @click="visible = false">取消</a-button>
        <a-button v-if="canEditRiskPoint||canEditRiskResult" @click="addAssetConfirm" :loading="recordloading" type="primary">确定</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import STable from '@/components/table/'
import moment from 'moment'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getRiskList, addRiskData, updateRiskData, deleteRiskData,ajaxGetDictItems } from '@/api/api'
export default {
  name: 'AssetInfo',
  components: { STable },
  mixins: [JeecgListMixin],
  props: {
    Pid: {
      type: String,
    },
    selectSysForm: {
      //主页面选择的系统实体对象
      tpye: Object,
    },
    baseInfo: {
      //页面处在哪个流程阶段的具体实体对象
      type: Object,
    },
    isView:{
      type:Boolean,
      default:false
    }
  },
  data() {
    return {
      disableMixinCreated:true,
      actionUrl:  `${window._CONFIG['domianURL']}/ists.bd/bdStrPointsWfInstance/importExcelJoin`,
      queryForm: {
        bdProjectId: '', //选择的项目id
        wfInstanceId: '', //流程id
      },
      levelList:[],
      yearShowEnd:false,
      yearShowStart:false,
      visible: false,
      recordloading: false,
      rules: {
        riskPoints:[{ required: true, message: '请填写风险点', trigger: 'blur' }],
        riskLevelCode: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        riskRectificationBeginDate: [{ required: true, message: '请选择风险整改开始时间', trigger: 'change' }],
        riskRectificationEndDate: [{ required: true, message: '请选择风险整改结束时间', trigger: 'change' }],
        rectificationDescription: [{ required: true, message: '请填写风险描述', trigger: 'blur' }],
        rectificationProposal: [{ required: true, message: '请填写整改建议', trigger: 'blur' }],
        rectificationResult: [{ required: true, message: '请填写整改结果', trigger: 'blur' }],
      },
      assetForm: {},
      typeList: [],
      defaultColumns: [
        {
          title: '风险点',
          dataIndex: 'riskPoints',
          width:'100px'
        },
        {
          title: '风险等级',
          dataIndex: 'riskLevelName',
          width:'100px'
        },
        {
          title: '风险描述',
          dataIndex: 'rectificationDescription',
          width:'300px'
        },
        {
          title: '整改内容',
          dataIndex: 'rectificationProposal',
          width:'300px'
        },
        {
          title: '整改开始时间',
          dataIndex: 'riskRectificationBeginDate',
          width:'180px'
        },
        {
          title: '整改完成时间',
          dataIndex: 'riskRectificationEndDate',
          width:'180px'
        },
        {
          title: '整改结果',
          dataIndex: 'rectificationResult',
          width:'180px'
        },
        {
          title: '操作',
          width: '150px',
          fixed: 'right',
          align:'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  computed: {
    columns() {
      let list = JSON.parse(JSON.stringify(this.defaultColumns))
      // if (!this.canEditRiskPoint && !this.canEditRiskResult) {
      //   list.splice(7, 1)
      // }
      return list
    },
    canEditRiskPoint() {
      return ['30', '40'].indexOf(this.baseInfo.wfNodeCode) !== -1
    },
    canEditRiskResult() {
      return ['40'].indexOf(this.baseInfo.wfNodeCode) !== -1
    },
  },
  mounted(){
     this.getDic('levelList', 'risk_level')
  },
  methods: {
     //获取字典接口
    getDic(listKey, key) {
      ajaxGetDictItems(key).then((res) => {
        this.$set(this, listKey, res.result)
      })
    },
    addAsset() {
      this.assetForm = {}
      this.visible = true
    },
    uploadDone({ file, fileList, event }) {
      if (file.response) {
        if (file.response.success&&!file.response.result) {
          this.$notification.success({
            message: '上传成功',
          })
        } else {
          this.$warning({
            title: file.response.message,
            content: (<div>
                <span>{file.response.result.msg}</span><br/>
                <span style="display:none">具体详情请 <a href={file.response.result.fileUrl} target="_blank" download={file.response.result.fileName}>点击下载</a> </span>
              </div>
            )
          })
        }
        this.search()
      }
    },
    riskStartYear(currentDate){
      if(!this.assetForm.riskRectificationEndDate){
        return false
      }
      let now = (function (UTCDateString) {
        if (!UTCDateString) {
          return '-'
        }
        var date2 = new Date(UTCDateString) //这步是关键
        var year = date2.getFullYear()
        var dateStr = year
        return dateStr
      })(currentDate)
      return now>this.assetForm.riskRectificationEndDate
    },
    riskEndYear(currentDate){
      if(!this.assetForm.riskRectificationBeginDate){
        return false
      }
      let now = (function (UTCDateString) {
        if (!UTCDateString) {
          return '-'
        }
        var date2 = new Date(UTCDateString) //这步是关键
        var year = date2.getFullYear()
        var dateStr = year
        return dateStr
      })(currentDate)
      return now<this.assetForm.riskRectificationBeginDate
    },
    openChangeStart(status) {
      if (status) {
        this.yearShowStart = true
        this.yearShowEnd = false
      }
    },
    panelChangeStart(value){
      this.assetForm.riskRectificationBeginDateTZ = value
      this.assetForm.riskRectificationBeginDate = (function (UTCDateString) {
        if (!UTCDateString) {
          return '-'
        }
        var date2 = new Date(UTCDateString) //这步是关键
        var year = date2.getFullYear()
        var dateStr = year
        return dateStr
      })(value)
      this.yearShowStart = false
    },
    openChangeEnd(status) {
      if (status) {
        this.yearShowEnd = true
        this.yearShowStart = false
      }
    },
    panelChangeEnd(value){
      this.assetForm.riskRectificationEndDateTZ = value
      this.assetForm.riskRectificationEndDate = (function (UTCDateString) {
        if (!UTCDateString) {
          return '-'
        }
        var date2 = new Date(UTCDateString) //这步是关键
        var year = date2.getFullYear()
        var dateStr = year
        return dateStr
      })(value)
      this.yearShowEnd = false
    },
    addAssetConfirm() {
      this.$refs.assetFormDom.validate((valid) => {
        if (valid) {
          if (this.assetForm.bdStrPointsId) {
            this.recordloading = true
            updateRiskData({
              bdStrPointsDto:this.assetForm,
              wfInstanceId:this.Pid
            })
              .then((res) => {
                this.recordloading = false
                if (res.success) {
                  this.visible = false
                  this.search()
                  this.$notification.success({
                    message: res.result,
                  })
                } else {
                  this.$notification.warning({
                    message: res.result,
                  })
                }
              })
              .catch((e) => {
                console.error(e)
                this.recordloading = false
              })
          } else {
            this.recordloading = true
            addRiskData({
              ...this.queryForm,
              bdStrPointsDto: this.assetForm,
            })
              .then((res) => {
                this.recordloading = false
                if (res.success) {
                  this.visible = false
                  this.search()
                  this.$notification.success({
                    message: res.message,
                  })
                } else {
                  this.$notification.warning({
                    message: res.message,
                  })
                }
              })
              .catch((e) => {
                console.error(e)
                this.recordloading = false
              })
          }
        }
      })
    },
    handleEdit(record) {
      this.assetForm = JSON.parse(JSON.stringify(record))
      this.assetForm.riskRectificationBeginDateTZ = moment(this.assetForm.riskRectificationBeginDate + '')
      this.assetForm.riskRectificationEndDateTZ = moment(this.assetForm.riskRectificationEndDate + '')
      this.visible = true
    },
    deleteData(record) {
      deleteRiskData({
        bdStrPointsId: record.bdStrPointsId,
      }).then((res) => {
        if (res.success) {
          this.search()
          this.$notification.success({
            message: res.result,
          })
        } else {
          this.$notification.warning({
            message: res.result,
          })
        }
      })
    },
    // 加载数据方法 必须为 Promise 对象
    loadTableData(parameter) {
      if (!this.queryForm.bdProjectId) {
        return false
      }
      return getRiskList(Object.assign(parameter, this.queryForm)).then((res) => {
        return res.result
      })
    },
    search() {
      if (this.$refs.tableSysDom) {
        this.$refs.tableSysDom.refresh(this.$refs.tableSysDom.localPagination, this.queryForm)
      }
    },
    beforeUpload(file, fileList){
      if(file.size===0){
        this.$notification.warning({
            message: '文件大小为0kb，请上传正确文件',
        })
        return false
      }
    },
    downLoadTemplate() {
      var a = document.createElement('a')
      a.download = this.canEditRiskPoint ? '风险点录入表单模板' : '风险整改结果录入表单模板'
      a.href = `/${a.download}.xlsx`
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
  },
  watch: {
    selectSysForm:{
      handler(newVal, oldVal) {
        if (newVal) {
          this.queryForm.wfInstanceId = this.Pid
          this.queryForm.bdProjectId = newVal.bdProjectId
          this.search()
        }
      },
      immediate: true,
      deep: true
    }
  },
}
</script>

<style>
</style>