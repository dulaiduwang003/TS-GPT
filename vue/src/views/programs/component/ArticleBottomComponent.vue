<script setup>

import {CopyDocument} from "@element-plus/icons-vue";
import {defineProps} from "vue";

const props = defineProps({
  article: {
    type: String
  }
})
</script>

<template>
  <div class="bottom-container">
    <el-icon size="13px">
      <CopyDocument/>
    </el-icon>
    <div class="font-10px">
      {{ props.article ? props.article.length : '0' }}
    </div>
  </div>
</template>

<style scoped>
.bottom-container {
  width: 100%;
  max-width: 1300px;
  background-color: #ffffff;
  height: 20px;
  display: flex;
  justify-content: right;
  align-items: center;
}


.font-10px {
  padding-left: 10px;
  font-size: 12px
}
</style>
