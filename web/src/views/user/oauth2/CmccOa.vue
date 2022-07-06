<template>
  <div></div>
</template>

<script>
import Vue from 'vue'
import { welcome } from "@/utils/util"
import {cmccOaToken} from '@/api/api'
import { ACCESS_TOKEN, USER_NAME,USER_INFO} from "@/store/mutation-types"

export default {
  name: 'CmccOa',
  data() {
    return {
      wfInstanceId: '',  //流程id
      UserInfo: '', //用户密钥
      wfCode: '', //流程标识
    }
  },
  mounted() {
      Vue.ls.remove(ACCESS_TOKEN)
      let query = this.$router.currentRoute.query
      this.wfInstanceId = query.wfInstanceId
      this.UserInfo = query.UserInfo
      this.getToken()
  },
  methods: {
    getToken(){
        cmccOaToken({
            wfInstanceId:this.wfInstanceId,
            UserInfo:this.UserInfo
        }).then(res=>{
            if(res.success){
                const result = res.result
                const userInfo = result.userInfo
                Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
                Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
                Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
                this.$store.commit('SET_TOKEN', result.token)
                this.$store.commit('SET_INFO', userInfo)
                this.$store.commit('SET_NAME', { username: userInfo.username,realname: userInfo.realname, welcome: welcome() })
                this.$store.commit('SET_AVATAR', userInfo.avatar)
                this.wfCode = res.result.wfInstance.wfCode
                this.handleEdit()
            }else{
                this.$notification[ 'error' ]({
                message: res.message,
                duration: 4,
                });
            }
        })
    },
    handleEdit() {
      //判断对应的模块
      let idName = ''
      let path = ''
      if (this.wfCode === 'project_rank') {
        idName = 'sysDetailId'
        path = '/planning/sysDetail'
      } else if (this.wfCode === 'project_check') {
        idName = 'reviewDetailId'
        path = '/planning/reviewDetail'
      } else if (this.wfCode === 'ineed_check') {
        idName = 'specialDetailId'
        path = '/planning/specialDetail'
      } else if (this.wfCode === 'network_access') {
        idName = 'constrDetailId'
        path = '/construction/constrDetail'
      } else if (this.wfCode === 'accept') {
        idName = 'safeDetailId'
        path = '/construction/safeDetail'
      } else if (this.wfCode === 'alter_report') {
        idName = 'changeDetailId'
        path = '/running/changeDetail'
      } else if (this.wfCode === 'disposal') {
        idName = 'disposalDetailId'
        path = '/running/disposalDetail'
      } else if (this.wfCode === 'network_exit') {
        idName = 'logoutDetailId'
        path = '/running/logoutDetail'
      } else if (this.wfCode === 'risk_assessment') {
        idName = 'riskDetailId'
        path = '/running/riskDetail'
      } else if (this.wfCode === 'operation') {
        idName = 'safeRunDetailId'
        path = '/running/safeRunDetail'
      }
      this.$ls.set(idName, this.wfInstanceId + ',view')
      this.$router.push({
        path: path,
      })
    },
  },
}
</script>
<style scoped lang="less">
</style>