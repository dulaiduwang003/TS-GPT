<script setup>

import {CopyDocument, Delete} from "@element-plus/icons-vue";
import {defineEmits} from "vue";

const emits = defineEmits(['copy-content-event','delete-chat-event']);


</script>

<template>
  <div class="operation-icon">
    <div class="hover-icon">
      <el-icon size="20px" @click="emits('copy-content-event')">
        <CopyDocument/>
      </el-icon>
    </div>
<!--    <div class="hover-icon">-->
<!--      <el-icon size="20px">-->
<!--        <Microphone/>-->
<!--      </el-icon>-->
<!--    </div>-->
<!--    <div class="hover-icon">-->
<!--      <el-icon size="20px">-->
<!--        <Share/>-->
<!--      </el-icon>-->
<!--    </div>-->
    <div class="hover-icon" @click="emits('delete-chat-event')">
      <el-icon size="20px">
        <Delete/>
      </el-icon>
    </div>
  </div>
</template>

<style scoped>
.operation-icon {
  display: flex;
  //justify-content: space-between;
  align-items: center;

}

.hover-icon{
  margin: auto;
}

.hover-icon:hover {
  color: #7365FF;
}

</style>
