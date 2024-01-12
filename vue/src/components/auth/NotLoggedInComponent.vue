<script setup>

import {ref} from "vue";
import AuthDialogComponent from "@/components/auth/AuthDialogComponent.vue";

const authDialogIsVisible = ref(false)
</script>

<template>
  <auth-dialog-component :is-visible="authDialogIsVisible" @close-dialog-event="authDialogIsVisible = false"/>
  <div class="not-container">
    <div>
      <img :src="require('../../assets/app/empty.svg')" class="empty-img" alt="">
      <div class="empty-prompt">
        登陆后即可访问 ╮ ( ￣▽￣ ) ╭
      </div>
      <div class="login-container">
        <el-button type="primary" class="login-btn" @click="authDialogIsVisible=true">唤起登录</el-button>
      </div>

    </div>
  </div>
</template>

<style scoped>
.empty-img {
  width: 300px;
  height: 300px
}

.empty-prompt {
  text-align: center;
  padding-bottom: 10px;
  color: #898989
}

.login-btn {
  width: 190px;
  height: 40px;
  border-radius: 30px;
  background-color: #000000;
  color: white;
  border: none;
}
.login-btn:hover,
.login-btn:focus,
.login-btn:active {
  background-color: #000000;
  outline: 0;
}
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 10px
}

.not-container {
  height: 100%;
  width: 100%;
  background-color: white;
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 80px;
  animation: explainAnimation 0.3s;
}
@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}
</style>
