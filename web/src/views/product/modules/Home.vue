<template>
  <div>
    <a-card :bordered="false">
      <div class="home">
        <div class="home-info">
          <a-avatar :size="64" icon="user" :src="userInfo.avatar" />
          <div class="info-text">
            <div class="info-text-title">{{ timeS }},{{ userInfo.username }},祝你开心每一天</div>
            <div class="info-text-development">
              {{ userInfo.development }}
            </div>
          </div>
        </div>
        <div class="home-todo">
          <div class="home-todo-sub">
            <div class="sub-label">项目数量</div>
            <div class="sub-value">{{ userInfo.delFlag }}</div>
          </div>
          <div class="line"></div>
          <div class="home-todo-sub">
            <div class="sub-label">今日待办/已办</div>
            <div class="sub-value">
              {{ userInfo.delFlag }}<span class="done">/{{ userInfo.delFlag }}</span>
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
        </a-tab-pane>
        <a-tab-pane key="2">
          <span slot="tab">
            <a-icon :style="{ fontSize: '18px' }" type="inbox" />
            已办事项
          </span>
        </a-tab-pane>
      </a-tabs>
      <s-table ref="tableDom" size="default" :columns="columns" :data="loadTableData" :showAlertInfo="true">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">发起流程</a>
          <a @click="handleEdit(record)">流程详情</a>
          <a @click="handleEdit(record)">流程审批</a>
        </span>
        <span slot="action" slot-scope="text, record">
          <span>{{ record.sysName }}</span>
        </span>
      </s-table>
    </a-card>

    <a-card title="消息通知" :bordered="false" :style="{ marginTop: '12px' }">
      <a-list item-layout="horizontal" :data-source="messageList">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-list-item-meta :description="item.describe">
            <a slot="title">{{ item.title }}</a>
            <a-avatar :size="48" slot="avatar" :src="item.avatar" />
          </a-list-item-meta>
        </a-list-item>
      </a-list>
    </a-card>
  </div>
</template>

<script>
import STable from '@/components/table/'
import { getHomeInfo } from '@/api/api'
export default {
  components: { STable },
  name: 'Home',
  data() {
    return {
      userInfo: {},
      queryParam: {},
      activityIndex: '1',
      messageList: [
        {
          describe: '3天前',
          title: '林东东 在 Ant Design Pro 发布了 留言',
        },
      ],
      columns: [
        {
          title: '系统名称',
          dataIndex: 'no',
          scopedSlots: { customRender: 'sysName' },
        },
        {
          title: '流程阶段',
          dataIndex: 'no',
        },
        {
          title: '当前流程节点',
          dataIndex: 'no',
        },
        {
          title: '上一步处理结果',
          dataIndex: 'no',
        },
        {
          title: '上一步流程节点',
          dataIndex: 'no',
        },
        {
          title: '上一步流程处理人',
          dataIndex: 'no',
        },
        {
          title: '上一步处理时间',
          dataIndex: 'no',
        },
        {
          title: '操作',
          width: '150px',
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
  activated() {
    this.userInfo = this.$store.state.user.info || {}
  },
  methods: {
    getUserInfo() {
      getHomeInfo().then((res) => {
      })
    },
    changeTab(e) {
      this.activityIndex = e
    },
    // 加载数据方法 必须为 Promise 对象
    loadTableData(parameter) {
      return this.getServiceList(Object.assign(parameter, this.queryParam)).then((res) => {
        return res.result
      })
    },
    getServiceList() {},
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
</style>