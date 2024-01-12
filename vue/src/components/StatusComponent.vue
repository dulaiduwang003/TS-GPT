<script setup>
import {defineProps} from 'vue';

const props = defineProps({
  size: {
    type: String,
    default: "40px",
  },
  icon: {
    type: String,
    default: "Document",
  },
  text: {
    type: String,
    default: '空数据',
  },
  color: {
    type: String,
    default: "#CCCCCC",
  }
});
</script>

<template>
  <div class="status-container">
    <div class="content">
      <el-icon :size="props.size" :color="props.color">
        <component :is="props.icon"/>
      </el-icon>
      <div class="text" :style="{ color: props.color }">
        {{ props.text }}
      </div>
    </div>
  </div>
</template>

<style scoped>

.status-container {
  cursor: not-allowed;
  position: absolute;
  z-index: 2;
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center
}

.content {
  text-align: center;
  font-size: 13px;
}

.text {
  padding-top: 15px;
  text-align: center;
}
</style>
