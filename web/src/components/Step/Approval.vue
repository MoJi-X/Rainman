<template>
  <div>
    <a-form-model ref="ruleFormApproval" :model="checkForm" :rules="rules">
      <a-form-model-item label="审批结果" prop="opinion">
        <a-radio-group v-model="checkForm.opinion" @change="changeResult">
          <a-radio :value="true" :disabled="!canEdit"> 同意 </a-radio>
          <a-radio :value="false" :disabled="!canEdit"> 驳回 </a-radio>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item label="审批意见" :prop="checkForm.opinion ? '' : 'message'">
        <a-input :readOnly="!canEdit" v-model="checkForm.message" type="textarea" placeholder="请输入审批意见" />
      </a-form-model-item>
    </a-form-model>
  </div>
</template>

<script>
export default {
  name: 'Approval',
  props: {
    canEdit: {
      //是否处于编辑状态
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      checkForm: {
        opinion: true,
      },
      rules: {
        opinion: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
        message: [{ required: true, message: '请填写审批意见', trigger: 'blur' }],
      },
    }
  },
  methods: {
    checkValid() {
      let pass = true
      if(!this.checkForm.opinion&&!this.checkForm.message){
        pass=false
        this.$notification.warning({
          message: '请填写审批意见'
        })
      }
      return pass
    },
    changeResult(e){
      this.$emit('changeResult',e.target.value)
    }
  },
}
</script>

<style>
</style>