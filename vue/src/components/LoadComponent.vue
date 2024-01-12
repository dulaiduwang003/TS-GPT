<script setup>
import {defineProps} from "vue";

const props = defineProps({
  text: {
    type: String
  },
  tips: {
    type: String
  }
})
</script>


<template>
  <div class="load-container">
    <div class="animation-container">
      <div class="loadAnimation"/>
      <div class="animation-text">
        {{ props.text }}
      </div>
      <div class="tips">{{ props.tips }}</div>
    </div>
  </div>
</template>

<style scoped>
.load-container {
  animation: fadeIn 0.5s ease-in-out forwards;
  cursor: not-allowed;
  position: absolute;
  z-index: 2;
  background-color: rgba(255, 255, 255, 0.56);
  height: 100%;
  width: 100%;
  top: 0;
  display: flex;
  justify-content: center;
  align-items: center
}

.tips {
  font-size: 10px;
  color: #757575
}

.animation-container {
  display: flex;
  flex-direction: column;
  align-items: center;

}

.animation-text {

  margin-top: 40px;
  font-size: 14px;
  color: #6a6a6a
}

.loadAnimation {
  position: relative;
  width: 50px;
}

.loadAnimation:before {
  width: 20px;
  height: 20px;
  border-radius: 20px;
  content: "";
  position: absolute;
  background: #a86dbc;
  animation: loadAnimationBefore 1.5s infinite ease-in-out;
}

.loadAnimation:after {
  width: 20px;
  height: 20px;
  border-radius: 20px;
  content: "";
  position: absolute;
  background: #ae4b5d;
  left: 22px;
  animation: loadAnimationAfter 1.5s infinite ease-in-out;
}

@keyframes loadAnimationBefore {
  0% {
    transform: translateX(0px) rotate(0deg);
  }

  50% {
    transform: translateX(50px) scale(1.2) rotate(260deg);
    background: #6798e6;
    border-radius: 0;
  }

  100% {
    transform: translateX(0px) rotate(0deg);
  }
}

@keyframes loadAnimationAfter {
  0% {
    transform: translateX(0px);
  }

  50% {
    transform: translateX(-50px) scale(1.2) rotate(-260deg);
    background: #6969c1;
    border-radius: 0;
  }

  100% {
    transform: translateX(0px);
  }
}
</style>
