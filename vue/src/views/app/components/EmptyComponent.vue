<script setup>
import {defineProps} from 'vue';

const props = defineProps({
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
      <div>
        <img :src="require('../../../assets/app/empty.svg')" class="empty-img" alt="">
      </div>
      <div class="text" :style="{ color: props.color }">
        {{ props.text }}
      </div>
    </div>
  </div>
</template>

<style scoped>

.status-container {
  cursor: not-allowed;
  height: 80vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
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

.content {
  text-align: center;
  font-size: 13px;
}

.empty-img {
  width: 200px;
  height: 200px
}

.text {
  padding-top: 20px;
  text-align: center;
}
</style>
