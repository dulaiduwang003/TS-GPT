<script setup>

import PopoverComponent from "@/views/chat/components/components/PopoverComponent.vue";
import {defineEmits, defineProps} from "vue";


const emits = defineEmits(['handle-copy-content', 'handle-delete-gpt', 'handle-copy-code']);


const props = defineProps({
  isLoading: {
    type: Boolean
  },
  answer: {
    type: String
  },
  index: {
    type: Number
  },
  error: {
    type: Object
  }
});

</script>

<template>
  <div class="bot-chat">
    <el-popover placement="top-end" :width="80" :disabled="props.isLoading">
      <popover-component @copy-content-event="emits('handle-copy-content',props.answer)"
                         @delete-chat-event="emits('handle-delete-chat',props.index)"/>
      <template #reference>
        <div v-if="error.isError">
          <!--        普通失败-->
          <div v-show="error.type==='EXCEPTION_REPORTED'">
            <div class="error-message">
              <div>
                <img :src="require('../../../../assets/app/server_error.svg')" alt=""/>
              </div>
              <div class="error-message-text">
                <div class="error-message-title">您好,这是一条错误信息</div>
                <div class="error-message-detail">
                  <div>1.可能当前系统密钥已过期或未配置任何密钥</div>
                  <div>2.当前系统密钥已被官方封禁或限制速率</div>
                  <div>3.发送消息过于频繁</div>
                </div>
                <div class="error-message-prompt">
                  已通知站点管理员修复该问题,可稍后再试,此条信息不会影响后续对话使用
                </div>
              </div>
            </div>
          </div>
          <!--        会员过期-->
          <div v-show="error.type==='MEMBERSHIP_EXPIRES'">
            <div class="error-message">
              <div>
                <img :src="require('../../../../assets/app/member_overdue.svg')" alt=""/>
              </div>
              <div class="error-message-text">
                <div class="error-message-title">您的会员已经到期</div>
                <div class="error-message-detail">
                  <div>您的会员已经过期,请您在右上角点击+号订阅服务<br/>或使用兑换码后继续</div>
                </div>

              </div>
            </div>
          </div>
        </div>

        <v-md-editor
            v-else
            :model-value="props.answer"
            mode="preview"
            @copy-code-success="emits('handle-copy-code')"
        />
      </template>
    </el-popover>
  </div>
</template>

<style scoped>


.error-message {
  display: flex;
  align-items: center
}

.error-message img {
  width: 120px;
  height: 120px
}

.error-message-text {
  padding-left: 10px
}

.error-message-title {
  font-size: 14px;
  font-weight: 550;
  color: #646464
}

.error-message-detail {
  padding-top: 10px;
  font-size: 11px
}

.error-message-prompt {
  padding-top: 10px;
  font-size: 11px;
  color: #535353
}
</style>
