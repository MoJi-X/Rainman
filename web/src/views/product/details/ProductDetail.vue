<template>
  <a-card :border="false" :style="{ marginTop: '-12px' }">
    <!-- <a-tabs
      :default-active-key="0"
      tabPosition="left"
    >
      <a-tab-pane v-for="(item,index) in processList" :key="index" :tab="item.ProcessName">
          <component :is="item.filename"
          :isView="true"
        ></component>
      </a-tab-pane>
    </a-tabs> -->
    <a-row>
        <a-col :span="6">
          <a-tree :load-data="onLoadData" :tree-data="processList" @select="selectNode">
            <template slot="title" slot-scope="record">
              {{record.ProcessName}}
            </template>
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div v-for="(item,index) in processList" :key="index">
            <component :is="item.filename"
              v-if="activeName === item.filename"
              :isView="true"
            ></component>
          </div>
        </a-col>
    </a-row>
  </a-card>
</template>

<script>
import sysDetail from '@/views/product/details/SysDetail.vue'
import reviewDetail from '@/views/product/details/ReviewDetail.vue'
import specialDetail from '@/views/product/details/SpecialDetail.vue'
import constrDetail from '@/views/product/details/ConstrDetail.vue'
import safeDetail from '@/views/product/details/SafeDetail.vue'
import changeDetail from '@/views/product/details/ChangeDetail.vue'
import safeRunDetail from '@/views/product/details/SafeRunDetail.vue'
import riskDetail from '@/views/product/details/RiskDetail.vue'
import disposalDetail from '@/views/product/details/DisposalDetail.vue'
import logoutDetail from '@/views/product/details/LogoutDetail.vue'
import { productTreeNode } from '@/api/api'
export default {
    components:{
        sysDetail,
        reviewDetail,
        specialDetail,
        constrDetail,
        safeDetail,
        changeDetail,
        safeRunDetail,
        riskDetail,
        disposalDetail,
        logoutDetail
    },
    data(){
        return {
            activeName:'',
            processList:[]
        }
    },
    created(){
        this.processList = this.$ls.get('productDetailList')
        this.activeName = this.processList[0].filename
        for(let i=0;i<this.processList.length;i++){
            this.$ls.set(this.processList[i]['filename']+'Id', this.processList[i].WfInstanceId)
        }
    },
    methods:{
      onLoadData(node){
        let obj = node.dataRef
        if(!obj.isLeaf){
          return new Promise((resolve,reject)=>{
            productTreeNode({
              bdProjectId:obj.bdProjectId,
              wfCode:obj.wfCode
            }).then(res=>{
              if(res.success){
                let nodeArr = []
                res.result.forEach(element => {
                    nodeArr.push({
                      ...element,
                      ProcessName:element.name,
                      isLeaf:true,
                      filename:obj.filename
                    })
                });
                obj.children = nodeArr
                this.processList = [...this.processList];
                resolve()
              }else{
                resolve()
              }
            })
          })
        }
      },
      selectNode(selectedKeys, {selected, selectedNodes, node, event}){
        let obj = node.dataRef
        if(obj.isLeaf&&node.getNodeChildren().length===0){
          this.activeName = obj.filename
        }
      }
    }
}
</script>

<style>

</style>