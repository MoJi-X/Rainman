<template>
  <div class="Step">
    <a-steps progress-dot :current="activity">
      <a-step v-for="(item, index) in PtitleList" :key="index" :title="item.title" />
    </a-steps>
    <a-divider />
    <a-row v-if="SubList && SubList.length > 0" style="display:flex;">
      <a-col v-for="(item, index) in SubList" :key="index" :style="index!=SubList.length-1?'flex:1':'flex:0'">
        <a-steps progress-dot direction="vertical" size="small" :current="item.activity">
          <a-step
            v-for="(subItem, subIndex) in item.sublist"
            :key="subIndex"
            :title="subItem.name || ''"
            :description="
              (subItem.description || '') + (subItem.lastHandleTimeString ? '-' + subItem.lastHandleTimeString : '')
            "
          ></a-step>
        </a-steps>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { getStepInfo } from '@/api/api'
export default {
  name: 'Step',
  props: {
    Pid: {
      type: String,
    },
    processType: {
      //1系统定级 2立项评审 3特需流程 4建设入网 5安全验收 6变更报备 7安全运维 8风险评估 9处置备查 10安全退网
      type: Number,
    },
  },
  data() {
    return {
      whiteList: [
        ['10', '30', '40', '50'],
        ['10', '30', '40'],
        ['10', '20', '30', '40', '50'],
        ['10', '30', '60', '70'],
        ['10', '30', '60', '80'],
        ['10', '20'],
        ['10', '20'],
        ['10', '20'],
        ['10', '20'],
        ['10', '20'],
      ],
      PtitleList: [],
      SubList: [],
      activity: 0,
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getData() {
      //获取流程信息
      getStepInfo({
        wfInstanceId: this.Pid,
      }).then((res) => {
        let data = res.result //流程数组
        if (!data) {
          return
        }
        data.forEach((element) => {
          if (
            this.whiteList[this.processType - 1].findIndex((item) => {
              return item === element.code
            }) !== -1
          ) {
            this.PtitleList.push({
              title: element.name,
              code: element.code,
            })
          }
        })
        let index = data.findIndex((item) => {
          return item.activation
        })
        if (index === -1) {
          this.activity = this.PtitleList.length
        } else {
          for(let k=0;k<this.PtitleList.length;k++){
            if(this.PtitleList[k].code<=data[index].code){
              this.activity=k;
            }
          }
        }
        if (index === -1) {
          index = data.length-1
        }
        this.SubList = []
        for(let i=0;i<this.PtitleList.length;i++){
          let objs = {
            sublist:[],
            activity:-1
          }
          let pushIndex = data.findIndex(item=>{
            return item.code === this.PtitleList[i].code
          })
          for(let j = pushIndex;j<data.length;j++){
            if(!data[index]||(data[index]&&data[j].code<=data[index].code)){
              objs.activity ++
            }
            if((this.PtitleList[i+1]&&data[j].code<this.PtitleList[i+1].code)||i===this.PtitleList.length-1){
              objs.sublist.push(data[j])
            }
          }
          this.SubList.push(objs)
        }
        console.log(this.SubList)
      })
    },
  },
}
</script>

<style lang="less" scoped>
</style>