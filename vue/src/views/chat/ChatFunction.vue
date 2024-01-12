<script setup>

import LeftOperationComponent from "@/views/chat/components/LeftOperationComponent.vue";
import RightViewComponent from "@/views/chat/components/RightViewComponent.vue";
import {ref} from "vue";
import store from "@/store";

const rightRef = ref(null)

const leftRef = ref(null)

const leftForbid = ref(false)

/**
 * 替换聊天
 */
const replacementChatTemplate = (data) => {
  rightRef.value.replaceChatTemplate(data)
}

/**
 * 往下滚动
 */
const scrollDown = () => {
  rightRef.value.scrollToTheBottom()
}

/**
 * 刷新左侧数据源
 */
const flushedChatCache = () => {
  leftRef.value.flushedChatCache()
}


/**
 * 刷新禁用变量
 */
const leftControlEnable = (bol) => {
  leftForbid.value = bol
}


</script>

<template>
  <div class="body">
    <!--    左侧操作菜单-->
    <left-operation-component ref="leftRef"
                              v-if="store.getters.userInfo"
                              @replacement-chat-template="replacementChatTemplate"
                              @scroll-down="scrollDown"
                              :leftForbid="leftForbid"/>
    <!--    聊天窗口-->
    <right-view-component ref="rightRef"
                          @flushed-chat-cache="flushedChatCache"
                          @left-control-enable="leftControlEnable"/>
  </div>
</template>

<style scoped>
.body {
  display: flex;
  flex: 1;
  height: 100vh;
}


</style>
