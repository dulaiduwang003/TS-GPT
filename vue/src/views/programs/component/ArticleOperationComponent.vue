<script setup>
import {defineEmits, ref} from "vue";

const emits = defineEmits(['handle-ai-writing-visual', 'handle-visual-setting','handle-ai-continued']);

const isWritingActive = ref(false)


</script>

<template>

  <div class="essay-operation">
    <div :class="isWritingActive?'trigger-active':'trigger-default'"
         @click="emits('handle-ai-writing-visual', true)">
      <img :src="require('../../../assets/app/writing-star.svg')"
           class="trigger-img"
           alt="">
      <span class="trigger-span">智能写作</span>
    </div>
    <div class="fixed-right">
      <div :class="1===2?'trigger-active':'trigger-default'" @click="emits('handle-visual-setting',true)">
        <img :src="require('../../../assets/app/optimize.svg')"
             class="trigger-img"
             alt=""
        >
        <span class="trigger-span">写作配置</span>
      </div>
      <div :class="4===3?'trigger-active':'trigger-default'" @click="emits('handle-ai-continued')">
        <img :src="require('../../../assets/app/topic.svg')"
             class="trigger-img"
             alt="">
        <span class="trigger-span">文章续写</span>
      </div>
    </div>
  </div>

</template>

<style scoped>


.essay-operation {
  max-width: 1300px;
  background-color: white;
  width: 100%;
  display: flex;

  height: 70px;

  justify-content: space-between;
  padding-bottom: 20px;
  border-bottom: 1px solid #ededed;
}

.fixed-right {
  display: flex

}

.trigger-img {
  width: 15px;
  height: 15px
}

.trigger-span {
  font-size: 13px;
  padding-left: 8px;

  color: #535353
}

.trigger-default {
  cursor: pointer;
  box-shadow: 0px 1px #e3e3e3;
  margin-right: 12px;
  background-color: #ffffff;
  border-radius: 6px;
  padding: 5px 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #e3e3e3
}

.trigger-active {
  cursor: pointer;
  box-shadow: 0 1px #e3e3e3;
  margin-right: 12px;
  background-color: #fef6ff;
  border-radius: 6px;
  padding: 5px 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #e3e3e3
}
</style>
