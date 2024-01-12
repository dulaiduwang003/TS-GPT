<template>
  <!--  左侧导航栏-->
  <left-hand-navigation-pane/>
</template>

<script setup>

import {useStore} from "vuex";
import LeftHandNavigationPane from "@/components/navigationBar/LeftHandNavigationPane.vue";
import {onMounted} from "vue";
import {getCurrentUserInfo} from "@/api/auth";

let store = useStore();
store.commit("initState");

//是否登录
if (store.getters.userInfo) {
  //这里需要判断是否拥有配置 如果没有则需要设置默认值
  let userSetting = store.getters.userSetting;
  if (!userSetting) {
    userSetting = {
      //默认使用第一个
      modelIndex: 0,
      //默认使用第一个
      shortcutIndex: 0,
      //默认使用Name
      chatBotName: '聊天机器人',
      //狗牌
      chatBotTag: 'GPT',
      //上下文
      memorySize: 4000,
      //速率 ms
      outputRate: 10,
      //写作模型
      writingModelIndex: 0,
      //记忆行数
      rowSize: 4,
      //前置预设词
      presetWordIssue: '',
      //后置预设此
      presetWordAnswer: '',
      //是否启用预设此
      isPreset: false,
      //使用的头像
      chatBotAvatar: ''
    }
    store.commit("setUserSetting", userSetting);
  }
}

onMounted(async () => {
  if (store.getters.userInfo){
    try {
      const res = await getCurrentUserInfo();
      store.commit("setUserInfo", res.data);
    } catch (e) {
      console.log(e)
    }
  }
})


</script>

<style>
#app {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  letter-spacing: 1px;
  color: var(--el-text-color-primary);
  line-height: 1.6;
  font-family: SF
}

.el-table {
  border-radius: 8px;
}

.el-table thead {
  color: #565656 !important;
}

.el-scrollbar {
  font-size: 13px;
}

.el-table .cell {
  text-align: center;
}

::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}

.el-table .warning-row {
  --el-table-tr-bg-color: #f0f1f8 !important;

}

.el-table .success-row {
  --el-table-tr-bg-color: #FDF6EC !important;
}

.el-loading-spinner .path {
  stroke: #a143ff !important;
}


.el-loading-spinner .el-loading-text {
  padding-top: 10px;
  color: #949494 !important;
}


::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background: #b0b0b0;
}

::-webkit-scrollbar-track {
  /* box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); */
  border-radius: 0;
  background: #F2F2F2;
  display: block;
}

@font-face {
  font-family: SmileySans;
  src: url(@/assets/fonts/SmileySans.otf);
  font-display: swap;
}

@font-face {
  font-family: SF;
  src: url(@/assets/fonts/medium.otf);
}

html, body {
  padding: 0;
  margin: 0;
}

:deep(.el-overlay) {
  background-color: rgba(0, 0, 0, 0.89);
}

main {
  flex: 1;
  background-color: #F2F2F2;
}

* {
  box-sizing: border-box;
}

.dot_0,
.dot_1,
.dot_2,
.dot_3 {
  background: rgb(166, 129, 236);
  width: 15px;
  height: 15px;
  border-color: #F2F2F2;
  border-radius: 50%;
}

.dot_0 {
  animation: jumpT 1.3s -0.64s linear infinite;
}

.dot_1 {
  animation: jumpT 1.3s -0.32s linear infinite;
}

.dot_2 {
  animation: jumpT 1.3s -0.16s linear infinite;
}

.dot_3 {
  animation: jumpT 1.3s linear infinite;
}

@keyframes jumpT {
  0%,
  80%,
  100% {
    transform: scale(0);
    background-color: #F2F2F2;
  }

  40% {
    transform: scale(1);
    background-color: rgb(186, 156, 241);
  }
}

.el-dialog__title {
  color: white !important;
}

.el-dialog {
  box-shadow: none !important;
}

.el-dialog__header {
  padding: 0 !important;
}

.el-dialog__body {
  padding: 0 !important;
}


</style>
