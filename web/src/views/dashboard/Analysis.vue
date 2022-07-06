<template>
  <div>
    <a-card :bordered="false">
      <div class="home">
        <div class="home-info">
          <a-avatar :size="64" icon="user" :src="userInfo.avatar" />
          <div class="info-text">
            <div class="info-text-title">{{ timeS }},{{ userInfo.realname }},祝你开心每一天</div>
            <div class="info-text-development">
              {{ userInfo.realname }}{{ userInfo.orgName ? '-' + userInfo.orgName : '' }}
            </div>
          </div>
        </div>
        <div class="home-todo">
          <div class="home-todo-sub">
            <div class="sub-label">项目数量</div>
            <div class="sub-value">{{ projectTotalNum }}</div>
          </div>
          <div class="line"></div>
          <div class="home-todo-sub">
            <div class="sub-label">待办/已办</div>
            <div class="sub-value">
              {{ todoTotal }}<span class="done">/{{ doneTotal }}</span>
            </div>
          </div>
        </div>
      </div>
    </a-card>
    <a-card :bordered="false" :style="{ marginTop: '12px' }">
      <a-tabs :activeKey="activityIndex" @change="changeTab">
        <a-tab-pane key="1">
          <span slot="tab">
            <a-icon :style="{ fontSize: '18px' }" type="home" />
            待办事项
          </span>
          <!-- <s-table ref="tableDom" size="default" :columns="columns" :data="loadData" :showAlertInfo="true">
            <span slot="action" slot-scope="text, record">
              <a v-if="record.stateCode === 'returned'" @click="handleEdit(record)">修改</a>
              <a v-if="record.stateCode === 'not_started'" @click="handleEdit(record)">发起流程</a>
              <a v-if="record.stateCode === 'finished'" @click="handleEdit(record)">流程详情</a>
              <a v-if="record.stateCode === 'in_progress'" @click="handleEdit(record)">流程处理</a>
            </span>
            <span slot="name" slot-scope="text, record">
              <span>{{ record.name }}</span>
            </span>
          </s-table> -->
        </a-tab-pane>
        <a-tab-pane key="2">
          <span slot="tab">
            <a-icon :style="{ fontSize: '18px' }" type="inbox" />
            已办事项
          </span>
          <!-- <s-table ref="tableDom" size="default" :columns="columns" :data="loadDataDone" :showAlertInfo="true">
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record, 'view')">查看详情</a>
            </span>
            <span slot="name" slot-scope="text, record">
              <span>{{ record.name }}</span>
            </span>
          </s-table> -->
        </a-tab-pane>
      </a-tabs>
      <s-table
        :scroll="{ x: '100%' }"
        v-show="activityIndex === '1'"
        ref="tableDom"
        size="default"
        :columns="columns"
        :data="loadData"
        :showAlertInfo="true"
      >
        <span slot="action" slot-scope="text, record">
          <a v-if="record.stateCode === 'returned'" @click="handleEdit(record)">修改</a>
          <a v-if="record.stateCode === 'not_started'" @click="handleEdit(record)">发起流程</a>
          <a v-if="record.stateCode === 'finished'" @click="handleEdit(record)">流程详情</a>
          <a v-if="record.stateCode === 'in_progress'" @click="handleEdit(record)">流程处理</a>
        </span>
        <span slot="name" slot-scope="text, record">
          <span>{{ record.name }}</span>
        </span>
      </s-table>
      <s-table
        :scroll="{ x: '100%' }"
        v-show="activityIndex === '2'"
        ref="tableDom"
        size="default"
        :columns="columns"
        :data="loadDataDone"
        :showAlertInfo="true"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record, 'view')">查看详情</a>
        </span>
        <span slot="name" slot-scope="text, record">
          <span>{{ record.name }}</span>
        </span>
      </s-table>
    </a-card>

    <a-card title="消息通知" :bordered="false" :bodyStyle="{ paddingTop: '0px' }" :style="{ marginTop: '12px' }">
      <a-list item-layout="horizontal" :data-source="messageList">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-list-item-meta>
            <div class="message-item" slot="title">
              <a-popconfirm
                title="是否要跳转到消息对应页面？"
                @confirm="
                  () => {
                    goPage(item)
                  }
                "
              >
                <a class="message-item-title">{{ item.titile }}</a>
              </a-popconfirm>
              <div
                class="message-item-content"
                style="
                  text-overflow: -o-ellipsis-lastline;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  display: -webkit-box;
                  -webkit-line-clamp: 3;
                  line-clamp: 3;
                  -webkit-box-orient: vertical;
                "
                v-html="item.msgContent"
              >
                {{ item.msgContent }}
              </div>
              <a-button
                type="primary"
                :class="item.readFlag==='1'?'button-color-green':''"
                @click="
                  () => {
                    viewMessage(item)
                  }
                "
                style="position: absolute; right: 10px; top: 0px"
                >{{item.readFlag==='1'?'已阅':'查看详情'}}</a-button
              >
            </div>
          </a-list-item-meta>
        </a-list-item>
      </a-list>
      <a-button
        v-if="messageList.length < messageQuery.total"
        type="dashed"
        style="width: 100%"
        :loading="messageLoading"
        @click="loadingMore"
      >
        加载更多
      </a-button>
    </a-card>

    <a-modal
      v-model="messageDetailModal"
      :keyboard="false"
      :closable="false"
      :maskClosable="false"
      :dialogStyle="{ top: '20px' }"
      width="60%"
      title="消息详情"
    >
      <div class="message-item">
        <span class="message-item-title">{{ currentMessageObj.titile }}</span>
      </div>
      <div>
        <span>发布人：{{ currentMessageObj.sender }}</span>
        <span>发布时间：{{ currentMessageObj.sendTime }}</span>
      </div>
      <a-divider />
      <div v-html="currentMessageObj.msgContent">
        {{ currentMessageObj.msgContent }}
      </div>
      <div slot="footer">
        <a-button
          @click="
            () => {
              closeMessage()
            }
          "
          >关闭</a-button
        >
      </div>
    </a-modal>
  </div>
</template>

<script>
import STable from '@/components/table/'
import {
  getUserInfo as getUserName,
  todoListData,
  doneListData,
  messageListData,
  projectTotal,
  messageRead,
} from '@/api/api'
export default {
  components: { STable },
  name: 'Home',
  data() {
    return {
      userInfo: {},
      queryParam: {},
      activityIndex: '1',
      messageList: [],
      messageLoading: false,
      todoTotal: 0,
      doneTotal: 0,
      projectTotalNum: 0,
      messageQuery: {
        pageNo: 1,
        pageSize: 10,
        total: 0,
      },
      currentMessageObj: {}, //当前的消息详情
      messageDetailModal: false,
      columns: [
        {
          title: '流程名称',
          width: '360px',
          scopedSlots: { customRender: 'name' },
        },
        {
          title: '流程阶段',
          width: '120px',
          dataIndex: 'wfName',
        },
        {
          title: '当前流程节点',
          width: '120px',
          dataIndex: 'wfNodeName',
        },
        {
          title: '上一步处理结果',
          width: '150px',
          dataIndex: 'previousStepMessage',
        },
        {
          title: '上一步流程节点',
          width: '150px',
          dataIndex: 'previousStepWfNodeName',
        },
        {
          title: '上一步流程处理人',
          width: '150px',
          dataIndex: 'previousStepAssigneeName',
        },
        {
          title: '上一步处理时间',
          width: '180px',
          dataIndex: 'previousStepEndTimeString',
        },
        {
          title: '操作',
          width: '150px',
          fixed: 'right',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  computed: {
    timeS() {
      let now = new Date()
      let hour = now.getHours()
      if (hour < 12) {
        return '早上好'
      } else if (hour < 14) {
        return '中午好'
      } else if (hour < 18) {
        return '下午好'
      } else {
        return '晚上好'
      }
    },
  },
  mounted() {
    this.userInfo = this.$store.state.user.info || {}
    this.getUserInfo()
    this.getMessageList()
    projectTotal().then((res) => {
      if (res.success) {
        this.projectTotalNum = res.result
      }
    })
  },
  methods: {
    loadingMore() {
      this.messageQuery.pageNo++
      this.getMessageList()
    },
    getMessageList() {
      this.messageLoading = true
      messageListData(this.messageQuery)
        .then((res) => {
          this.messageLoading = false
          if (res.success) {
            this.messageList = this.messageList.concat(res.result.records)
            this.messageQuery.total = res.result.total
          }
        })
        .catch((err) => {
          this.messageLoading = false
        })
    },
    getUserInfo() {
      getUserName().then((res) => {
        if (res.success) {
          this.userInfo.realname = res.result.realname
          this.userInfo.orgName = res.result.orgName
        }
      })
    },
    changeTab(e) {
      this.activityIndex = e
    },
    //点击流程详情
    handleEdit(record, type = '') {
      //判断对应的模块
      let idName = ''
      let path = ''
      if (record.wfCode === 'project_rank') {
        idName = 'sysDetailId'
        path = '/planning/sysDetail'
      } else if (record.wfCode === 'project_check') {
        idName = 'reviewDetailId'
        path = '/planning/reviewDetail'
      } else if (record.wfCode === 'ineed_check') {
        idName = 'specialDetailId'
        path = '/planning/specialDetail'
      } else if (record.wfCode === 'network_access') {
        idName = 'constrDetailId'
        path = '/construction/constrDetail'
      } else if (record.wfCode === 'accept') {
        idName = 'safeDetailId'
        path = '/construction/safeDetail'
      } else if (record.wfCode === 'alter_report') {
        idName = 'changeDetailId'
        path = '/running/changeDetail'
      } else if (record.wfCode === 'disposal') {
        idName = 'disposalDetailId'
        path = '/running/disposalDetail'
      } else if (record.wfCode === 'network_exit') {
        idName = 'logoutDetailId'
        path = '/running/logoutDetail'
      } else if (record.wfCode === 'risk_assessment') {
        idName = 'riskDetailId'
        path = '/running/riskDetail'
      } else if (record.wfCode === 'operation') {
        idName = 'safeRunDetailId'
        path = '/running/safeRunDetail'
      }
      this.$ls.set(idName, record.wfInstanceId + (type ? ',view' : ''))
      this.$router.push({
        path: path,
      })
    },
    viewMessage(item) {
      this.$set(this, 'currentMessageObj', item)
      this.messageDetailModal = true
    },
    closeMessage() {
      if(this.currentMessageObj.readFlag==='1'){
        this.messageDetailModal = false
        return
      }
      messageRead({ anntId: this.currentMessageObj.anntId }).then((res) => {
        if (res.success) {
          this.$notification.success({
            message: '消息已阅',
          })
          this.currentMessageObj.readFlag = '1'
          this.messageList  = [...this.messageList]
          this.messageDetailModal = false
        }
      })
    },
    goPage(item) {
      let params = {
        wfCode: item.openPage.split(',')[0],
        wfInstanceId: item.openPage.split(',')[2],
      }
      this.handleEdit(params, 'view')
    },
    // 加载数据方法 必须为 Promise 对象
    loadData(parameter) {
      return todoListData(Object.assign(parameter, this.queryParam)).then((res) => {
        this.todoTotal = res.result.total
        return res.result
      })
    },
    loadDataDone(parameter) {
      return doneListData(Object.assign(parameter, this.queryParam)).then((res) => {
        this.doneTotal = res.result.total
        return res.result
      })
    },
  },
}
</script>

<style lang="less" scoped>
.home {
  display: flex;
  align-items: center;
  .home-info {
    flex: 3;
    display: flex;
    align-items: center;
    .info-text {
      height: 64px;
      margin-left: 20px;
      .info-text-title {
        font-size: 20px;
        font-weight: bold;
      }
      .info-text-development {
        margin-top: 10px;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.4);
      }
    }
  }
  .home-todo {
    height: 64px;
    flex: 2;
    display: flex;
    align-items: flex-start;
    justify-content: center;
    position: relative;
    .home-todo-sub {
      flex: 1;
      text-align: center;
      .sub-label {
        font-size: 18px;
        color: rgba(0, 0, 0, 0.4);
      }
      .sub-value {
        font-size: 24px;
        font-weight: bold;
        .done {
          font-size: 14px;
          font-weight: 400;
          color: rgba(0, 0, 0, 0.4);
        }
      }
    }
    .line {
      margin-top: 12px;
      height: 40px;
      width: 1px;
      background: rgba(0, 0, 0, 0.1);
    }
  }
}
.message-item {
  position: relative;
  .message-item-title {
    font-size: 18px;
    font-weight: bold;
  }
  .message-item-content {
    margin-top: 10px;
  }
}
</style>