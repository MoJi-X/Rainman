<template>
  <a-card :loading="loading" :body-style="bodyStyle?bodyStyle:{padding: '20px 24px 12px' }" :bordered="false">
    <div class="chart-card-header">
      <div class="icon">
        <slot name="icon"></slot>
      </div>
      <div style="flex:1">
        <div class="meta" v-if="!hideKey||hideKey.indexOf('title')===-1">
          <span class="chart-card-title">{{ title }}</span>
          <span class="chart-card-action">
            <slot name="action"></slot>
          </span>
        </div>
        <div class="total" v-if="!hideKey||hideKey.indexOf('total')===-1">
          <span>{{ total }}</span>
          <slot name="subtotal"></slot>
        </div>
      </div>
    </div>
    <div class="chart-card-content" v-if="!hideKey||hideKey.indexOf('content')===-1">
      <div class="content-fix">
        <slot></slot>
      </div>
    </div>
    <div class="chart-card-footer" v-if="!hideKey||hideKey.indexOf('footer')===-1">
      <div class="field">
        <slot name="footer"></slot>
      </div>
    </div>
  </a-card>
</template>

<script>
export default {
  name: 'ChartCard',
  props: {
    bodyStyle:{
      type:Object
    },
    title: {
      type: String,
      default: '',
    },
    total: {
      type: String,
      default: '',
    },
    loading: {
      type: Boolean,
      default: false,
    },
    hideKey:{
      type:String|Array
    }
  },
}
</script>

<style lang="less" scoped>
.chart-card-header {
  position: relative;
  overflow: hidden;
  width: 100%;
  display: flex;
  .icon{
    float: right;
  }
  .meta {
    position: relative;
    overflow: hidden;
    width: 100%;
    color: rgba(0, 0, 0, 0.45);
    font-size: 14px;
    line-height: 22px;
  }
}

.chart-card-action {
  cursor: pointer;
  position: absolute;
  top: 0;
  right: 0;
}

.chart-card-footer {
  border-top: 1px solid #e8e8e8;
  padding-top: 9px;
  margin-top: 8px;

  > * {
    position: relative;
  }

  .field {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin: 0;
  }
}

.chart-card-content {
  margin-bottom: 12px;
  position: relative;
  height: 46px;
  width: 100%;

  .content-fix {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
  }
}

.total {
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
  color: #000;
  margin-top: 4px;
  margin-bottom: 0;
  font-size: 18px;
  line-height: 38px;
  height: 38px;
}
</style>