<script setup>

import {onMounted, ref} from "vue";
import GptKeyComponent from "@/views/console/view/gpt/GptKeyComponent.vue";
import GptModelComponent from "@/views/console/view/gpt/GptModelComponent.vue";
import DallKeyComponent from "@/views/console/view/gpt/DallKeyComponent.vue";
import store from "@/store";
import router from "@/router";

const active = ref('CHAT KEY CONFIG')

onMounted(()=>{
  let userInfo = store.getters.userInfo;
  if (userInfo) {
    const {type} = userInfo;
    if (type !== "ADMIN") {
      router.push({
        path: "/#"
      });

    }
  } else {
    router.push({
      path: "/#"
    });

  }
})
</script>

<template>
  <div class="container">
    <div class="body">
      <el-tabs v-model="active">
        <el-tab-pane label="对话密钥管理" name="CHAT KEY CONFIG" style="height: 100%">
          <gpt-key-component/>
        </el-tab-pane>
        <el-tab-pane label="绘图密钥管理" name="DALL KEY CONFIG" style="height: 100%">
          <dall-key-component/>
        </el-tab-pane>
        <el-tab-pane label="对话模型管理" name="CHAT MODEL CONFIG" style="height: 100%">
          <gpt-model-component/>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<style scoped>

:deep(.el-tabs__content) {
  height: 86vh !important;
}

@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}
.container {
  animation: explainAnimation 0.3s;
  width: 100%;
  height: 100vh;
  padding: 10px;
  overflow: hidden;
  background-color: rgb(242, 242, 242);
}

.title {
  font-size: 17px;
  color: #535353;
  font-weight: 550
}

:deep(.el-tabs__item:hover) {
  color: #8a80ec;
}

:deep(.el-tabs__item.is-active) {
  color: #8a80ec;
  font-weight: 600;
}

:deep(.el-tabs__active-bar) {
  background-color: #8a80ec;
}

.body {

  display: flex;
  flex-direction: column;
  background-color: rgb(255, 255, 255);
  height: 100%;
  border-radius: 10px;
  padding: 15px;
  overflow: hidden;
}
</style>
