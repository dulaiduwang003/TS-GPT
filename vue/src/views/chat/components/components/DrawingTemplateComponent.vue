<script setup>

import {defineEmits, defineProps} from "vue";


const emits = defineEmits(['preview-image', 'scroll-to-bottom']);


const props = defineProps({
  image: {
    type: String
  },
  answer: {
    type: String
  },
  error: {
    type: Object
  }
});

</script>

<template>
  <div class="bot-chat">
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
    <div :class="props.image?'drawing-answer':'drawing-answer-error'" v-else>
      <div>{{ props.answer }}</div>
      <img class="drawing-img" v-if="props.image" @click="emits('preview-image',props.image)"
           :src="props.image" alt="" @load="emits('scroll-to-bottom')"/>
    </div>

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
