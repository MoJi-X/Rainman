<template>
  <div>
    <a-steps direction="vertical" :current="processList?processList.length-1:0">
      <a-step v-for="(item, index) in processList" 
      :key="index" 
      :title="`${item.description} (${item.name})`"
      :description="item.message"
      :sub-title="item.lastAssigneeName+'  '+item.lastHandleTimeString"
      >
      </a-step>
    </a-steps>
  </div>
</template>

<script>
import { getStepOpnion } from '@/api/api'
export default {
  props: {
    Pid: {
      type: String,
    },
    canEdit:{ //是否处于编辑状态
      type: Boolean,
      default: true,
    }
  },
  data() {
    return {
      processList: [],
    }
  },
  methods: {
    getData() {
      getStepOpnion({wfInstanceId:this.Pid}).then(res=>{
        if(res.success){
          this.processList = res.result
        }
      })
    },
  },
  mounted() {
    this.getData()
  },
}
</script>

<style lang="less" scoped>
</style>