<template>
  <div>
    <div class="table-operator" v-if="canEdit">
      <a-upload
        :action="actionUrl"
        :headers="tokenHeader"
        :beforeUpload="beforeUpload"
        :showUploadList="false"
        :data="{
          wfInstanceId: Pid,
        }"
        @change="uploadDone"
      >
        <a-button type="primary">批量导入</a-button>
      </a-upload>
      <a-button @click="addSys">单个录入</a-button>
      <a-button type="text" @click="downLoadTemplate">下载导入模板<a-icon type="download" /></a-button>
    </div>
    <a-table
      ref="tableSysDom"
      size="default"
      :columns="columns"
      :pagination="false"
      :data-source="sysList"
      :showAlertInfo="true"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">{{ canEdit ? '编辑' : '查看' }}</a>
        <a-divider v-if="canEdit" type="vertical" />
        <a-popconfirm v-if="canEdit" title="是否要删除此系统？" @confirm="removeSys(record)">
          <a>删除</a>
        </a-popconfirm>
      </span>
      <span slot="file1" slot-scope="text, record">
        <div>
          <a style="margin-right: 10px" @click="download(record, 0)">{{ record.bdProject.bdFileList[0].filename }}</a>
          <a-upload
            :action="actionFileUrl"
            :headers="tokenHeader"
            :beforeUpload="beforeUpload"
            :data="{
              flagCode: record.bdProject.bdFileList[0].flagCode,
              bdFileId: record.bdProject.bdFileList[0].bdFileId || '',
              tableId: record.bdProjectId,
              tableName: 'bd_project',
              wfInstanceId: record.wfInstanceId,
            }"
            :showUploadList="false"
            @change="uploadFileDone"
          >
            <a-icon type="upload" v-if="record.bdProject.bdFileList[0].bdFileId&&canEdit" @click="currentUpload(record, 0)" />
            <a href="javascript:void(0)" v-if="!record.bdProject.bdFileList[0].bdFileId" @click="currentUpload(record, 0)">上传文件</a>
          </a-upload>
        </div>
      </span>
      <span slot="file2" slot-scope="text, record">
        <div>
          <a style="margin-right: 10px" @click="download(record, 1)">{{ record.bdProject.bdFileList[1].filename }}</a>
          <a-upload
            :action="actionFileUrl"
            :beforeUpload="beforeUpload"
            :headers="tokenHeader"
            :data="{
              flagCode: record.bdProject.bdFileList[1].flagCode,
              bdFileId: record.bdProject.bdFileList[1].bdFileId || '',
              tableId: record.bdProjectId,
              tableName: 'bd_project',
              wfInstanceId: record.wfInstanceId,
            }"
            :showUploadList="false"
            @change="uploadFileDone"
          >
            <a-icon type="upload" v-if="record.bdProject.bdFileList[1].bdFileId&&canEdit" @click="currentUpload(record, 1)" />
            <a href="javascript:void(0)" v-if="!record.bdProject.bdFileList[1].bdFileId" @click="currentUpload(record, 1)">上传文件</a>
          </a-upload>
        </div>
      </span>
      <span slot="file3" slot-scope="text, record">
        <div>
          <a style="margin-right: 10px" @click="download(record, 2)">{{ record.bdProject.bdFileList[2].filename }}</a>
          <a-upload
            :action="actionFileUrl"
            :headers="tokenHeader"
            :beforeUpload="beforeUpload"
            :data="{
              flagCode: record.bdProject.bdFileList[2].flagCode,
              bdFileId: record.bdProject.bdFileList[2].bdFileId || '',
              tableId: record.bdProjectId,
              tableName: 'bd_project',
              wfInstanceId: record.wfInstanceId,
            }"
            :showUploadList="false"
            @change="uploadFileDone"
          >
            <a-icon type="upload" v-if="record.bdProject.bdFileList[2].bdFileId&&canEdit" @click="currentUpload(record, 2)" />
            <a href="javascript:void(0)" v-if="!record.bdProject.bdFileList[2].bdFileId" @click="currentUpload(record, 2)">上传文件</a>
          </a-upload>
        </div>
      </span>
    </a-table>

    <a-modal v-model="visible" width="60%" title="资产录入" :dialogStyle="{ top: '20px' }">
      <a-form-model ref="sysFormDom" :model="sysForm" :rules="rules">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="系统名称" prop="name">
              <a-input v-model="sysForm.name" :disabled="!canEdit"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="系统定级" prop="systemGradingCode">
              <a-select allowClear :disabled="!canEdit" v-model="sysForm.systemGradingCode" placeholder="请选择">
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
                :disabled="!canEdit"
                placeholder="请选择年份"
                format="YYYY"
                v-model="sysForm.yearTZ"
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
                :disabled="!canEdit"
                style="width: 100%"
                v-model="sysForm.projectInitiationTime"
                valueFormat="YYYY-MM-DD"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="系统类型" prop="systemTypeCode">
              <a-select allowClear :disabled="!canEdit" v-model="sysForm.systemTypeCode" placeholder="请选择">
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
                v-model="sysForm.projectLeaderName"
                enter-button="选择"
                @search="onSearch('projectLeader')"
                :readOnly="true"
                :disabled="!canEdit"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="运维负责人" prop="omPrincipalCode">
              <a-input-search
                v-model="sysForm.omPrincipalName"
                enter-button="选择"
                @search="onSearch('omPrincipal')"
                :disabled="!canEdit"
                :readOnly="true"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="运维部门" prop="omDepartmentCode">
              <a-input v-model="sysForm.omDepartmentName" readOnly :disabled="!canEdit"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-model-item label="开支类型" prop="expenditureTypeCode">
              <a-select allowClear :disabled="!canEdit" v-model="sysForm.expenditureTypeCode" placeholder="请选择">
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
              <a-select allowClear :disabled="!canEdit" v-model="sysForm.serviceOpeningScopeCode" placeholder="请选择">
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
              <a-textarea :disabled="!canEdit" v-model="sysForm.description" :rows="3"></a-textarea>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
      <template slot="footer">
        <a-button @click="visible = false">取消</a-button>
        <a-button v-if="canEdit" @click="addAsset" :loading="recordloading" type="primary">确定</a-button>
      </template>
    </a-modal>

    <a-modal v-model="personVisible" :dialogStyle="{ top: '20px' }" width="80%" title="人员选择" @ok="selectPerson">
      <ProcessDeal v-if="personVisible" ref="processDialogDom" :multi="false"></ProcessDeal>
    </a-modal>
  </div>
</template>

<script>
import {
  getSystemRecordList,
  ajaxGetDictItems,
  sysAddSinger,
  sysUpdateSinger,
  sysDetailSinger,
  sysDeleteSinger,
  sysDeleteSingerBp
} from '@/api/api'
import moment from 'moment'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import ProcessDeal from '@/components/Step/ProcessDeal'
export default {
  components: { ProcessDeal },
  name: 'SysManageList',
  mixins: [JeecgListMixin],
  props: {
    Pid: {
      type: String,
    },
    canEdit: {
      //页面是否为预览状态
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      disableMixinCreated: true,
      visible: false,
      personVisible: false,
      actionUrl: `${window._CONFIG['domianURL']}/ists.bd/bdProjectWfInstance/importExcelJoinWfInstanceId`, //导入系统基础信息数据
      actionFileUrl: `${window._CONFIG['domianURL']}/ists.bd/bdFile/upload`, //上传项目方案文件
      currentUploadRow: {}, //当前上传文件的行  里面需要带一个index属性说明是哪个文件类型
      levelList: [], //字典值
      sysTypeList: [], //字典值
      payTypeList: [], //字典值
      serviceAreaList: [], //字典值
      recordloading: false,
      yearShowOne: false,
      sysForm: {},
      baseSysForm: {}, //保存系统录入返回的外部数据
      sysList: [],
      currentPersonKey: '', //当前弹窗中选中的人赋值的key
      rules: {
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
      defaultColumns: [
        {
          title: '系统名称',
          dataIndex: 'name',
        },
        {
          title: '项目负责人',
          dataIndex: 'projectLeaderName',
        },
        // {
        //   title: '系统类型',
        //   dataIndex: 'systemTypeName',
        // },
        {
          title: '年度',
          dataIndex: 'year',
        },
        // {
        //   title: '自主定级',
        //   dataIndex: 'systemGradingName',
        // },
        {
          title: '操作',
          dataIndex: 'no',
          scopedSlots: { customRender: 'action' },
        },
        {
          title: '项目方案文件上传',
          children: [
            {
              title: '安全需求分析报告',
              dataIndex: 'file1',
              key: 'file1',
              width: 200,
              scopedSlots: { customRender: 'file1' },
            },
            {
              title: '安全总体方案',
              dataIndex: 'file2',
              key: 'file2',
              width: 200,
              scopedSlots: { customRender: 'file2' },
            },
            {
              title: '安全建设方案',
              dataIndex: 'file3',
              key: 'file3',
              width: 200,
              scopedSlots: { customRender: 'file3' },
            },
          ],
        },
      ],
    }
  },
  computed: {
    columns() {
      let list = JSON.parse(JSON.stringify(this.defaultColumns))
      // if (!this.canEdit) {
      //   list.splice(3, 1)
      // }
      return list
    },
  },
  mounted() {
    this.getDic('levelList', 'project_rank')
    this.getDic('sysTypeList', 'system_type')
    this.getDic('payTypeList', 'system_spending_type')
    this.getDic('serviceAreaList', 'system_service_coverage')
    this.loadTableData()
  },
  methods: {
    onSearch(key) {
      this.currentPersonKey = key
      this.personVisible = true
    },
    //获取字典接口
    getDic(listKey, key) {
      ajaxGetDictItems(key).then((res) => {
        this.$set(this, listKey, res.result)
      })
    },
    //新增资产
    addAsset() {
      this.$refs.sysFormDom.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.sysForm.bdProjectId) {
            params = {
              ...this.baseSysForm,
              bdProject: this.sysForm,
            }
            this.recordloading = true
            sysUpdateSinger(params)
              .then((res) => {
                this.recordloading = false
                if (!res.success) {
                  return
                }
                this.$notification.success({
                  message: res.message,
                })
                this.visible = false
                this.loadTableData()
              })
              .catch((e) => {
                console.error(e)
                this.recordloading = false
              })
          } else {
            params = {
              ...this.sysForm,
              bdProject: this.sysForm,
              wfInstanceId: this.Pid,
            }
            this.recordloading = true
            sysAddSinger(params)
              .then((res) => {
                this.recordloading = false
                if (!res.success) {
                  this.$notification.warning({
                    message: res.message,
                  })
                  return
                }
                this.$notification.success({
                  message: res.message,
                })
                this.visible = false
                this.loadTableData()
              })
              .catch((e) => {
                console.error(e)
                this.recordloading = false
              })
          }
        }
      })
    },
    removeSys(record) {
      sysDeleteSinger({ bdProjectWfInstanceId: record.bdProjectWfInstanceId }).then((res) => {
        if (!res.success) {
          return
        }
        sysDeleteSingerBp({ bdProjectWfInstanceId: record.bdProjectWfInstanceId }).then((res) => {
          if (!res.success) {
            return
          }
          this.$notification.success({
            message: res.result,
          })
          this.loadTableData()
        })
      })
    },
    selectPerson() {
      let person = this.$refs.processDialogDom.selectUserRows[0]
      if (this.currentPersonKey === 'omPrincipal') {
        this.$set(this.sysForm, 'omDepartmentName', person.orgCodeTxt)
        this.$set(this.sysForm, 'omDepartmentCode', person.orgCode)
      }
      this.$set(this.sysForm, this.currentPersonKey + 'Name', person.realname)
      this.$set(this.sysForm, this.currentPersonKey + 'Code', person.username)
      this.personVisible = false
    },
    // 弹出日历和关闭日历的回调
    openChangeOne(status) {
      if (status) {
        this.yearShowOne = true
      }
    },
    panelChangeOne(value) {
      this.sysForm.yearTZ = value
      this.sysForm.year = (function (UTCDateString) {
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
    addSys() {
      this.$set(this, 'sysForm', {})
      this.$set(this, 'baseSysForm', {})
      this.visible = true
    },
    currentUpload(record, index) {
      this.currentUploadRow = record
      this.currentUploadRow.index = index
    },
    uploadDone({ file, fileList, event }) {
      if (file.response) {
        if (file.response.success && !file.response.result) {
          this.$notification.success({
            message: '上传成功',
          })
        } else {
          this.$warning({
            title: file.response.message,
            content: (
              <div>
                <span>{file.response.result.msg}</span>
                <br />
                <span style="display:none">
                  具体详情请{' '}
                  <a href={file.response.result.fileUrl} target="_blank" download={file.response.result.fileName}>
                    点击下载
                  </a>
                </span>
              </div>
            ),
          })
        }
        this.loadTableData()
      }
    },
    uploadFileDone(file, fileList, event) {
      if (file.file.response && file.file.response.success) {
        this.$notification.success({
          message: '上传成功',
        })
        this.currentUploadRow.bdFileList[this.currentUploadRow.index] = file.file.response.result
        let index = this.sysList.findIndex((item) => {
          return item.bdProjectId == this.currentUploadRow.bdProjectId
        })
        this.$set(this.sysList, index, this.currentUploadRow)
      }
    },
    download(record, index) {
      var a = document.createElement('a')
      a.download = record.bdFileList[index].filename
      a.href = `${window._CONFIG['domianURL']}/ists.bd/bdFile/download/${record.bdFileList[index].bdFileId}`
      console.log(a.href)
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
    handleEdit(record) {
      sysDetailSinger({ bdProjectWfInstanceId: record.bdProjectWfInstanceId }).then((res) => {
        let data = res.result
        this.$set(this, 'baseSysForm', data)
        this.$set(this, 'sysForm', { ...data.bdProject, yearTZ: moment(data.bdProject.year + '') })
        this.visible = true
      })
    },
    // 加载数据方法 必须为 Promise 对象
    loadTableData() {
      this.$set(this, 'sysList', [])
      getSystemRecordList({ wfInstanceId: this.Pid }).then((res) => {
        if (res && res.result) {
          for (let i = 0; i < res.result.length; i++) {
            res.result[i] = { ...res.result[i].bdProject, ...res.result[i] }
          }
        }
        this.sysList = res.result
      })
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
      a.download = '系统定级导入表单模板'
      a.href = '/系统定级导入表单模板.xlsx'
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
  },
}
</script>

<style>
</style>