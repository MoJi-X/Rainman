<template>
  <div class="file-upload">
    <a-row :gutter="24" :style="{ borderBottom: '1px solid rgba(0,0,0,0.1)' }">
      <a-col :span="6">
        <div class="title">立项材料</div>
      </a-col>
      <a-col :span="18">
        <div class="title">操作上传</div>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :span="24">
        <a-tabs :tabBarStyle="{width:'25%',height:'300px'}" :default-active-key="0" tab-position="left">
          <a-tab-pane v-for="(item, index) in tabList" :key="index" :tab="item.flagName">
            <div style="padding:10px 0px;">
              <a-upload-dragger
                :action="actionUrl"
                :headers="tokenHeader"
                :disabled="isView||item.wfNodeCode!==baseInfo.wfNodeCode"
                :beforeUpload="beforeUpload"
                :data="{
                  flagCode: item.flagCode,
                  bdFileId: item.bdFileId || '',
                  tableId: item.bdProjectId,
                  tableName: 'bd_project',
                  wfInstanceId: Pid,
                }"
                :showUploadList="false"
                @change="uploadFileDone"
              >
                <div v-if="isView||item.wfNodeCode!==baseInfo.wfNodeCode">
                  <p class="ant-upload-drag-icon">
                    <img src="@/assets/downLoad.png" style="width:64px;height:64px">
                  </p>
                  <p class="ant-upload-text">下方文件可点击下载</p>
                </div>
                <div @click="getCurrenUpload(item, index)" v-else>
                  <p class="ant-upload-drag-icon">
                    <a-icon type="inbox" />
                  </p>
                  <p class="ant-upload-text">点击或将文件拖拽到这里上传</p>
                  <p class="ant-upload-hint">支持扩展名：.rar .zip .doc .docx .pdf .jpg...</p>
                </div>
              </a-upload-dragger>
              <div class="file-item" v-if="item.bdFileId">
                <a-icon type="file-text" />
                <div class="file-item-name" @click="download(item)">{{ item.filename }}</div>
              </div>
            </div>
          </a-tab-pane>
        </a-tabs>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
export default {
  mixins: [JeecgListMixin],
  props: {
    Pid: {
      type: String,
    },
    isView: {
      //是否处于预览状态
      type: Boolean,
      default: false,
    },
    tabList: {
      type: Array,
      default: () => {
        return []
      },
    },
    baseInfo:{
      type:Object
    }
  },
  data() {
    return {
      disableMixinCreated:true,
      actionUrl: `${window._CONFIG['domianURL']}/ists.bd/bdFile/upload`,
      fileList: [],
      currentUploadRow: {}, //当前上传文件的行  里面需要带一个index属性说明是哪个文件类型
    }
  },
  methods: {
    download(item) {
      var a = document.createElement('a')
      a.download = item.filename
      a.href = `${window._CONFIG['domianURL']}/ists.bd/bdFile/download/${item.bdFileId}`
      document.body.appendChild(a)
      a.click()
      document.body.removeChild(a)
    },
    getCurrenUpload(item, index) {
      this.currentUploadRow = item
      this.currentUploadRow.index = index
    },
    changeIcon(index, closed) {
      this.fileList[index].closed = closed
      this.$forceUpdate()
    },
    deleteFile(index) {
      this.fileList.splice(index, 1)
      this.$forceUpdate()
    },
    beforeUpload(file, fileList){
      if(file.size===0){
        this.$notification.warning({
            message: '文件大小为0kb，请上传正确文件',
        })
        return false
      }
    },
    uploadFileDone({ file, fileList, event }) {
      if (file.response) {
        if (file.response.success) {
          this.$notification.success({
            message: '上传成功',
          })
          this.$set(this.tabList, this.currentUploadRow.index, {
            ...this.tabList[this.currentUploadRow.index],
            ...file.response.result,
          })
        } else {
          this.$notification.warning({
            message: file.response.message,
          })
        }
      }
    },
  },
}
</script>

<style lang="less" scoped>
.file-upload {
  margin: -24px 0px;
  .title {
    padding: 12px 0px;
    text-align: center;
  }
  .file-list {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 12px 0;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    .file-list-name {
      flex: 1;
    }
    .file-list-dl {
      flex: 1;
    }
    .file-list-icon {
      flex: 1;
    }
  }
  .file-item{
    padding:10px 0px;
    display: flex;
    align-items: center;
    .file-item-name{
      margin-left: 10px;
      cursor: pointer;
    }
    .file-item-name:hover{
      color:#40a9ff;
    }
  }
}
</style>