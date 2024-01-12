<script setup>

import {onMounted, ref} from "vue";
import MicroAppComponent from "@/views/console/view/micro/MicroAppComponent.vue";
import MicroCategoryComponent from "@/views/console/view/micro/MicroCategoryComponent.vue";
import store from "@/store";
import router from "@/router";
import DrawingPromptComponent from "@/views/console/view/micro/DrawingPromptComponent.vue";


const active = ref('MICRO_APP')

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
        <el-tab-pane label="预设词" name="MICRO_APP" style="height: 100%">
          <micro-app-component/>
        </el-tab-pane>
        <el-tab-pane label="预设词类目" name="MICRO_CATEGORY" style="height: 100%">
          <micro-category-component/>
        </el-tab-pane>
        <el-tab-pane label="绘图提示词类目" name="DRAWING_PROMPT" style="height: 100%">
          <drawing-prompt-component/>
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
  width: 100%;
  height: 100vh;
  padding: 10px;
  overflow: hidden;
  background-color: rgb(242, 242, 242);
  animation: explainAnimation 0.3s;
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
