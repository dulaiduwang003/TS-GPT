<script setup>
import {defineProps} from "vue";

const props = defineProps({
  isLoading: {
    type: Boolean
  }
});

</script>

<template>
  <div class="loading-container" :style="{ opacity: props.isLoading ? 1 : 0 }">
    <div class="preloader">
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
    </div>

  </div>
</template>

<style scoped>

.loading-container {
  position: fixed;
  z-index: 8;
  top: 50px;
  left: 45%;
  width: 10%;
  display: flex;
  justify-content: center;
  transition: opacity 0.5s ease-in-out;

}

.preloader {
  position: relative;
}

.preloader span {
  border-radius: 4px;
  display: block;
  bottom: 0px;
  width: 13px;
  height: 5px;
  background: #9b59b6;
  position: absolute;
  animation: preloader 1.5s infinite ease-in-out;
}

.preloader span:nth-child(2) {
  left: 11px;
  animation-delay: .2s;
}

.preloader span:nth-child(3) {
  left: 22px;
  animation-delay: .4s;
}

.preloader span:nth-child(4) {
  left: 33px;
  animation-delay: .6s;
}

.preloader span:nth-child(5) {
  left: 44px;
  animation-delay: .8s;
}

@keyframes preloader {
  0% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }
  25% {
    height: 30px;
    transform: translateY(15px);
    background: #ff6fa0;
  }
  50% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }
  100% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }
}
</style>
