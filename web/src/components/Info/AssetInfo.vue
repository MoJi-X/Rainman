<template>
  <div>
    <div class="table-operator" v-if="canEdit && queryForm.bdProjectId">
      <a-upload
        :action="actionUrl"
        :headers="tokenHeader"
        :beforeUpload="beforeUpload"
        :showUploadList="false"
        :data="{
          bdProjectId: queryForm.bdProjectId,
          wfInstanceId: Pid,
        }"
        @change="uploadDone"
      >
        <a-button type="primary">批量导入</a-button>
      </a-upload>
      <a-button @click="addAsset">单个录入</a-button>
      <a-button type="text" @click="downLoadTemplate">下载导入模板<a-icon type="download" /></a-button>
    </div>
    <s-table
      ref="tableSysDom"
      :showPagination="true"
      size="default"
      :columns="columns"
      :data="loadTableData"
      :showAlertInfo="true"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">{{canEdit?'修改':'查看'}}</a>
        <a-divider v-if="canEdit" type="vertical" />
        <a-popconfirm v-if="canEdit" title="是否要删除此资产？" @confirm="deleteData(record)">
          <a>删除</a>
        </a-popconfirm>
      </span>
    </s-table>

    <a-modal v-model="visible" width="60%" title="资产录入">
      <a-form-model v-if="visible" ref="assetFormDom" :model="assetForm" :rules="rules">
        <a-form-model-item label="设备类型" prop="equipmentTypeName">
          <a-input :disabled="!canEdit" v-model="assetForm.equipmentTypeName" :maxLength="32"></a-input>
        </a-form-model-item>
        <a-form-model-item label="设备IP" prop="equipmentIp">
          <a-textarea :disabled="!canEdit" v-model="assetForm.equipmentIp" :maxLength="32"></a-textarea>
        </a-form-model-item>
        <a-form-model-item label="设备端口" prop="equipmentPort">
          <a-textarea :disabled="!canEdit" v-model="assetForm.equipmentPort" :maxLength="32"></a-textarea>
        </a-form-model-item>
        <a-form-model-item label="设备机房" prop="deployComputerRoom">
          <a-textarea :disabled="!canEdit" v-model="assetForm.deployComputerRoom"></a-textarea>
        </a-form-model-item>
      </a-form-model>
      <template slot="footer">
        <a-button @click="visible = false">取消</a-button>
        <a-button v-if="canEdit" @click="addAssetConfirm" :loading="recordloading" type="primary">确定</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import STable from '@/components/table/'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAssetsList, addAssetNew, updateAsset, deleteAsset } from '@/api/api'
export default {
  name: 'AssetInfo',
  mixins: [JeecgListMixin],
  components: { STable },
  props: {
    Pid: {
      type: String,
    },
    selectSysForm: {
      //主页面选择的系统实体对象
      tpye: Object,
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
      actionUrl: `${window._CONFIG['domianURL']}/ists.bd/bdAssetsWfInstance/importExcelJoin`,
      queryForm: {
        bdAssets: {},
        bdProjectId: '', //选择的项目id
        wfInstanceId: '', //流程id
      },
      visible: false,
      recordloading: false,
      rules: {
        equipmentTypeName: [{ required: true, message: '请选择设备类型', trigger: 'change' }],
        equipmentIp: [{ required: true, message: '请填写设备IP', trigger: 'blur' }],
        equipmentPort: [{ required: true, message: '请填写设备端口', trigger: 'blur' }],
        deployComputerRoom: [{ required: true, message: '请填写设备机房', trigger: 'blur' }],
      },
      assetForm: {},
      typeList: [],
      defaultColumns: [
        {
          title: '设备类型',
          dataIndex: 'equipmentTypeName',
          width:'200px'
        },
        {
          title: '设备IP',
          dataIndex: 'equipmentIp',
          width:'200px'
        },
        {
          title: '设备端口',
          dataIndex: 'equipmentPort',
          width:'200px'
        },
        {
          title: '部署机房',
          dataIndex: 'deployComputerRoom',
        },
        {
          title: '运维负责人',
          dataIndex: 'omPrincipalName',
          width:'200px'
        },
        {
          title: '操作',
          dataIndex: 'no',
          width:'200px',
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  computed: {
    columns() {
      let list = JSON.parse(JSON.stringify(this.defaultColumns))
      // if (!this.canEdit) {
      //   list.splice(5, 1)
      // }
      return list
    },
  },
  methods: {
    addAsset() {
      this.assetForm = {}
      this.visible = true
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
                  </a>{' '}
                </span>
              </div>
            ),
          })
        }
        this.search()
      }
    },
    download(url, name) {
      //下载文件
      var a = document.createElement('a')
      a.download = name
      a.href = url
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
    addAssetConfirm() {
      this.$refs.assetFormDom.validate((valid) => {
        if (valid) {
          if (this.assetForm.bdAssetsId) {
            this.recordloading = true
            updateAsset(this.assetForm)
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
            addAssetNew({
              ...this.queryForm,
              bdAssets: this.assetForm,
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
          }
        }
      })
    },
    handleEdit(record) {
      this.assetForm = record
      this.visible = true
    },
    deleteData(record) {
      deleteAsset({
        bdAssetsId: record.bdAssetsId,
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
      return getAssetsList(Object.assign(parameter, this.queryForm)).then((res) => {
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
      a.download = '资产录入表单模板'
      a.href = '/资产录入表单模板.xlsx'
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
  },
  watch: {
    selectSysForm: {
      handler(newVal, oldVal) {
        if (newVal) {
          this.queryForm.bdAssets = newVal
          this.queryForm.wfInstanceId = this.Pid
          this.queryForm.bdProjectId = newVal.bdProjectId
          this.search()
        }
      },
      immediate: true,
      deep: true,
    },
  },
}
</script>

<style>
</style>