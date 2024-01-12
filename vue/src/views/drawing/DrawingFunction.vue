<script setup>

import LeftOperationComponent from "@/views/drawing/components/LeftOperationComponent.vue";
import RightViewComponent from "@/views/drawing/components/RightViewComponent.vue";
import {ref} from "vue";
import store from "@/store";
import NotLoggedInComponent from "@/components/auth/NotLoggedInComponent.vue";

const rightRef = ref(null)

const leftRef = ref(null)

/**
 * 新增任务
 */
const handleNewTask = (data) => {
  rightRef.value.unShiftDrawingList(data)
}


/**
 * 更新SD局部绘图参数
 */
const acceptSdLocalDrawings = (data) => {
  leftRef.value.acceptSdLocalDrawings(data)
}

/**
 * 更新Dall局部绘图参数
 */
const acceptDallLocalDrawings = (data) => {
  leftRef.value.acceptDallLocalDrawings(data)
}


</script>

<template>
  <div class="body">
    <div class="body-container" v-if="store.getters.userInfo">
      <left-operation-component @handle-new-task="handleNewTask" ref="leftRef"/>
      <right-view-component ref="rightRef"
                            @accept-sd-local-drawings="acceptSdLocalDrawings"
                            @accept-dall-local-drawings="acceptDallLocalDrawings"/>
    </div>
    <not-logged-in-component v-else/>
  </div>

</template>

<style scoped>
.body {
  height: 100vh;
  padding: 10px;


}



.body-container {
  border-radius: 8px;
  display: flex;
  flex: 1;
  height: 100%;
  overflow: hidden

}

</style>
