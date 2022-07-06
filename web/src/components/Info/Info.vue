<template>
  <div class="info">
    <div class="info-avatar">
      <a-avatar :size="36" icon="user" :src="dataBase.avatar" />
    </div>
    <div class="info-content">
      <a-descriptions :title="'标题：' + (dataBase.name||'')">
        <a-descriptions-item label="流水单号">{{ dataBase.bdFlowSheetCode }}</a-descriptions-item>
        <a-descriptions-item label="发起人">{{ dataBase.initiatorName }}</a-descriptions-item>
        <a-descriptions-item label="发起部门">{{ dataBase.initiatorDepartmentName }}</a-descriptions-item>
        <a-descriptions-item label="当前处理人">{{ dataBase.realname }}</a-descriptions-item>
        <a-descriptions-item label="当前处理部门">{{ dataBase.orgName }}</a-descriptions-item>
        <a-descriptions-item label="申请时间">{{ dataBase.createdDate }}</a-descriptions-item>
        <a-descriptions-item label="当前处理时间">{{ dataBase.lastModifiedDate }}</a-descriptions-item>
        <a-descriptions-item label="当前状态">
          <span style="font-weight:bold">{{ dataBase.stateName }}</span>
        </a-descriptions-item>
        <a-descriptions-item label="当前流程节点">
          <span style="font-weight:bold">{{ dataBase.wfNodeName }}</span>
        </a-descriptions-item>
      </a-descriptions>
    </div>
    <!-- <div class="info-status">
      <div class="status-item">
        <div class="status-item-label">当前状态</div>
        <div class="status-item-value">{{ dataBase.stateName }}</div>
      </div>
      <div class="status-item">
        <div class="status-item-label">当前流程节点</div>
        <div class="status-item-value">{{ dataBase.wfNodeName }}</div>
      </div>
    </div> -->
  </div>
</template>

<script>
import { getUserInfo } from '@/api/api'
export default {
  props: {
    baseInfo: {
      type: Object,
      default: () => {
        return {}
      },
    },
  },
  data() {
    return {
      dataBase: {},
    }
  },
  watch: {
    baseInfo:{
      handler(newVal, oldVal) {
        getUserInfo().then(res=>{
          this.dataBase = JSON.parse(JSON.stringify(newVal))
          if(res.success){
            this.dataBase.realname = res.result.realname
            this.dataBase.orgName = res.result.orgName
          }
        })
      },
      immediate: true,
      deep: true
    }
  }
}
</script>

<style lang="less" scoped>
.info {
  display: flex;
  .info-avatar {
    margin: 0 20px 0 0px;
  }
  .info-content {
    flex: 3;
  }
  .info-status {
    flex: 1;
    display: flex;
    align-items: flex-start;
    justify-content: center;
    position: relative;
    .status-item {
      flex: 1;
      text-align: center;
      .status-item-label {
        font-size: 16px;
        color: rgba(0, 0, 0, 0.4);
      }
      .status-item-value {
        font-size: 14px;
        font-weight: bold;
      }
    }
  }
}
</style>