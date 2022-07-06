<template>
  <div class="safe-detail">
    <div v-if="!hasSubmit">
      <a-card :border="false" :style="{ marginTop: '-12px' }">
        <Info ref="InfoDom" :baseInfo="baseInfo"></Info>
      </a-card>
      <a-card title="流程进度" :style="{ marginTop: '12px' }">
        <Step :processType="5" :Pid="Pid"></Step>
      </a-card>
      <div id="targetTop"></div>
      <a-card title="项目系统信息" :border="false" :style="{ marginTop: '12px' }">
        <SysInfo
          ref="SysInfoDom"
          :baseInfo="baseInfo"
          :canEdit="(operate === 'edit' || operate === 'update') && !isView && !otherView"
          @selectSys="selectSys"
        ></SysInfo>
      </a-card>
      <a-card title="资产信息" :border="false" :style="{ marginTop: '12px' }">
        <AssetInfo
          :canEdit="(operate === 'edit' || operate === 'update') && !isView && !otherView"
          :Pid="Pid"
          :selectSysForm="selectSysForm"
          @saveData="saveData"
        ></AssetInfo>
      </a-card>
      <a-card title="系统过程文件" :border="false" :style="{ marginTop: '12px' }">
        <FileUpload
          ref="FileUploadDom"
          :isView="isView || otherView"
          :Pid="Pid"
          :baseInfo="baseInfo"
          :tabList="fileTabList"
          @saveData="saveData"
        ></FileUpload>
      </a-card>
      <a-card title="录入安全风险测试点" v-if="showRiskPoint" :border="false" :style="{ marginTop: '12px' }">
        <RiskInfo
          ref="RiskInfoDom"
          :Pid="Pid"
          :selectSysForm="selectSysForm"
          :baseInfo="baseInfo"
          :isView="isView"
        ></RiskInfo>
      </a-card>
      <a-card title="流转意见" v-if="operate !== 'update'" :style="{ marginTop: '12px' }">
        <Opinion ref="OpinionDom" :Pid="Pid"></Opinion>
      </a-card>
      <div id="targetTop2"></div>
      <a-card title="审批意见" v-if="operate === 'check' && !isView && !otherView" :style="{ marginTop: '12px' }">
        <Approval ref="ApprovalDom" @changeResult="(result) => (showProcess = !result)"></Approval>
      </a-card>
      <a-card
        title="流程处理"
        :style="{ marginTop: '12px' }"
        v-if="operate !== 'view' && !isView && !otherView && (baseInfo.wfNodeCode != '80' || showProcess)"
      >
        <div class="choose-btn">
          <a-button type="primary" @click="()=>{personVisible=true}">选择流程处理人</a-button>
        </div>
        <div class="person-tags">
          <a-tag v-for="(item,index) in processPerson" :key="index" style="font-size:16px;padding:5px;">
            {{item.realname}}
          </a-tag>
        </div>
      </a-card>
      <a-card :style="{ marginTop: '12px' }">
        <div class="footer">
          <div class="footer-draft footer-item">
            <!-- {{ draftTitle ? draftTitle + parseInt(saveTime / 60) + '分钟前' : '' }} -->
          </div>
          <div class="footer-btn footer-item">
            <a-button class="btn-item" @click="goBack">取消</a-button>
            <a-button 
            class="btn-item button-color-green"
            type="primary"
            v-if="baseInfo.dataStateCode === 'temp'" 
            @click="getSave"
            :loading="saveLoadding"
            >暂存</a-button>
            <a-button
              class="btn-item"
              v-if="operate !== 'view' && !isView && !otherView"
              @click="processSubmit"
              type="primary"
              :loading="loadding"
              >流程处理</a-button
            >
          </div>
        </div>
      </a-card>
    </div>
    <a-card v-else>
      <ProcessResult
        :processType="5"
        @goDetail="getDetailInfo"
        :Pid="Pid"
        parentPath="/construction/safelist"
      ></ProcessResult>
    </a-card>

    <a-modal v-model="personVisible" :dialogStyle="{ top: '20px' }" width="80%" title="人员选择">
      <ProcessDeal ref="ProcessDom" :multi="false" @ok="selectPerson"></ProcessDeal>
      <template slot="footer">
        <a-button @click="()=>{personVisible=false}">关闭</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import Step from '@/components/Step/Step'
import Opinion from '@/components/Step/Opinion'
import Approval from '@/components/Step/Approval'
import AssetInfo from '@/components/Info/AssetInfo'
import Info from '@/components/Info/Info'
import FileUpload from '@/components/Info/FileUpload'
import SysInfo from '@/components/Info/SysInfo'
import ProcessDeal from '@/components/Step/ProcessDeal'
import ProcessResult from '../modules/ProcessResult.vue'
import RiskInfo from '@/components/Info/RiskInfo'
import { getSysDetailInfo, getNextStep,getSaveData } from '@/api/api'
export default {
  props: {
    isView: {
      type: Boolean,
      default: false,
    },
  },
  components: {
    Info,
    SysInfo,
    ProcessDeal,
    ProcessResult,
    Step,
    Opinion,
    Approval,
    FileUpload,
    AssetInfo,
    RiskInfo,
  },
  data() {
    return {
      operate: '', //edit修改（被驳回状态）,update发起流程，view流程详情，check流程审批
      Pid: '', //生成的流程id
      saveTime: 0, //草稿保存的时间
      canEdit: true, //页面是否为可编辑状态
      otherView: '', //另外的参数来控制页面为查看状态
      processPerson: [],
      fileTabList: [],
      selectSysForm: {}, //选中的系统实体对象
      bdProjectId: '',
      draftTimer: null,
      loadding: false, //点击流程处理的请求loading
      saveLoadding:false,
      hasSubmit: false, //是否提交数据
      baseInfo: {}, //系统定级基础数据
      showProcess: false,
      personVisible:false
    }
  },
  computed: {
    //当定时服务启动时，显示保存标题
    draftTitle() {
      if (this.draftTimer) {
        return '已保存草稿'
      } else {
        return ''
      }
    },
    showRiskPoint() {
      return ['30', '40', '50', '60', '70', '80'].indexOf(this.baseInfo.wfNodeCode) !== -1
    },
  },
  created() {
    let params = this.$ls.get('safeDetailId')
    this.Pid = params.split(',')[0]
    this.otherView = params.split(',')[1]
    if (this.Pid) {
      this.getDetailInfo()
    }
  },
  methods: {
    async getDetailInfo() {
      //获取详情信息
      let params = {
        wfInstanceId: this.Pid,
      }
      this.showProcess = false //重置流程处理人开关
      getSysDetailInfo(params).then((res) => {
        if (res.result) {
          this.baseInfo = res.result
          let type = ''
          if (this.baseInfo.stateCode === 'returned') {
            type = 'edit'
          } else if (this.baseInfo.stateCode === 'not_started') {
            type = 'update'
          } else if (this.baseInfo.stateCode === 'in_progress') {
            type = 'check'
          }
          if (this.baseInfo.stateCode === 'finished' || !this.baseInfo.stateCode || !this.baseInfo.taskId) {
            type = 'view'
          }
          this.operate = type
          this.hasSubmit = false
        }
      })
    },
    selectPerson(rows) {
      this.$set(this, 'processPerson', rows)
    },
    selectSys(sysForm) {
      this.selectSysForm = sysForm
      //let whiteList = ['equipment_basic_info', 'account_checklist', 'service_port_checklist', 'topology_map']
      let whiteList = ['security_analysis_report', 'overall_security_solution', 'security_building_solution']
      let fileTabList = []
      this.bdProjectId = sysForm.bdProjectId //选择系统后把项目id给到全局
      sysForm.bdFileList.forEach((item) => {
        if (
          whiteList.findIndex((witem) => {
            return witem === item.flagCode
          }) !== -1
        ) {
          fileTabList.push({
            ...item,
            bdProjectId: sysForm.bdProjectId,
            readOnly: true,
          })
        } else {
          fileTabList.push({ ...item, bdProjectId: sysForm.bdProjectId })
        }
      })
      this.$set(this, 'fileTabList', fileTabList)
    },
    getSave() {
      this.saveLoadding = true
      getSaveData({
        wfInstanceId: this.baseInfo.wfInstanceId,
      }).then((res) => {
        this.saveLoadding = false
        if (res.success) {
          this.$notification.success({
            message: '保存成功',
          })
        } else {
          this.$notification.warning({
            message: '保存失败',
          })
        }
      })
    },
    async processSubmit() {
      if (!(await this.$refs.SysInfoDom.checkValid())) {
        document.getElementById('targetTop').scrollIntoView({
          behavior: 'smooth',
          block: 'start',
          inline: 'nearest',
        })
        return
      }
      if (this.operate === 'check' && !(await this.$refs.ApprovalDom.checkValid())) {
        document.getElementById('targetTop2').scrollIntoView({
          behavior: 'smooth',
          block: 'start',
          inline: 'nearest',
        })
        return
      }
      //提交数据成功后变更hasSubmit
      if ((this.baseInfo.wfNodeCode != '80' || this.showProcess) && this.processPerson.length === 0) {
        this.$notification.warning({
          message: '请选择流程处理人',
        })
        return
      }
      let ids = []
      this.processPerson.forEach((item) => {
        ids.push(item.username)
      })
      let params = {
        approve: this.$refs.ApprovalDom ? this.$refs.ApprovalDom.checkForm.opinion : '',
        message: this.$refs.ApprovalDom ? this.$refs.ApprovalDom.checkForm.message : '',
        bdProjectId: this.bdProjectId,
        taskId: this.baseInfo.taskId,
        wfNextNodeCandidateUsers: ids.join(','),
      }
      this.loadding = true
      getNextStep(params).then((res) => {
        this.loadding = false
        if (res.success) {
          this.$notification.success({
            message: res.message || '提交成功',
          })
          this.hasSubmit = true
        } else {
          this.$notification.warning({
            message: res.message || '提交失败',
          })
        }
      })
    },
    saveData() {
      this.saveTime = 0
      clearInterval(this.draftTimer)
      this.draftTimer = setInterval(() => {}, 60000)
    },
    goBack() {
      let path = this.isView ? '/product/list' : '/construction/safelist'
      this.$router.push({
        path: path,
      })
    },
  },
}
</script>

<style lang="less" scoped>
.safe-detail {
  .footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    .footer-btn {
      .btn-item {
        margin-right: 10px;
      }
    }
  }
  .person-tags{
    margin-top:20px;
  }
}
</style>