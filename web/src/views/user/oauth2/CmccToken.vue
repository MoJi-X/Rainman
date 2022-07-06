<template>
  <div></div>
</template>

<script>
import Vue from 'vue'
import { welcome } from "@/utils/util"
import {cmccLTToken} from '@/api/api'
import { ACCESS_TOKEN, USER_NAME,USER_INFO} from "@/store/mutation-types"

export default {
  name: 'CmccToken',
  data() {
    return {
      access_token: '',  //参数
    }
  },
  mounted() {
      Vue.ls.remove(ACCESS_TOKEN)
      let query = this.$router.currentRoute.query
      this.access_token = query.access_token
      this.getToken()
  },
  methods: {
    getToken(){
        cmccLTToken({
            access_token:this.access_token
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
                this.$router.push({ path: "/dashboard/analysis" }).catch(()=>{
                 console.log('登录跳转首页出错,这个错误从哪里来的')
                })
            }else{
                this.$notification[ 'error' ]({
                message: res.message,
                duration: 4,
                });
            }
        })
    }
  },
}
</script>
<style scoped lang="less">
</style>