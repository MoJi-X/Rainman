<template>
  <div>
    <a-result status="success" title="提交成功">
    </a-result>
    <div>
      <Step :processType="processType" :Pid="Pid"></Step>
    </div>
    <div class="btns">
      <a-button
        class="btn-item"
        key="console"
        type="primary"
        @click="
          () => {
            changeRouter(parentPath)
          }
        "
        >返回列表</a-button
      >
      <!-- <a-button
       class="btn-item"
        key="buy"
        @click="
          () => {
            goDetail()
          }
        "
        >查看详情</a-button
      > -->
    </div>
  </div>
</template>

<script>
import Step from '@/components/Step/Step'
export default {
  components: {
    Step
  },
  props: {
    Pid: {
      type: String,
    },
    parentPath: {
      type: String,
      default: '/dashboard/analysis',
    },
    detailPath: {
      type: String,
      default: '/dashboard/analysis',
    },
    moduleName: {
      type: String,
    },
    processType:{ //1系统定级 2立项评审 3特需流程 4建设入网 5安全验收 6变更报备 7安全运维 8风险评估 9处置备查 10安全退网
      type:Number
    }
  },
  methods: {
    changeRouter(path) {
      this.$router.push({
        path: path,
      })
    },
    goDetail(){
      this.$emit('goDetail')
    }
  },
}
</script>

<style lang="less" scoped>
  .btns{
    display: flex;
    align-items: center;
    justify-content: center;
    .btn-item{
      margin:0px 10px;
    }
  }
</style>