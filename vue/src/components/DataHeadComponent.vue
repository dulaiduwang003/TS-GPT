<script setup>
import {defineProps} from "vue";
import store from "../store";

const props = defineProps({
  color: {
    type: String,
    default: '#ff93e1'
  },

  type: {
    type: Number
  },
  date: {
    type: String
  }
});
</script>

<template>
  <div :class="props.type===1?'bot-container':'user-container'">
    <div class="bot-name">
      {{ props.type === 1 ? store.getters.userSetting.chatBotName : store.getters.userInfo.nickName }}
    </div>
    <div class="bot-label" :style="'background-color: '+props.color" v-if="props.type!==0">
      {{ store.getters.userSetting.chatBotTag?store.getters.userSetting.chatBotTag:'GPT' }}
    </div>
    <div class="bot-date" v-if="props.type!==0">
      {{ props.date }}
    </div>
  </div>
</template>

<style scoped>

.bot-container {
  display: flex;
  align-items: center;
  color: #a7a7a7;
  margin-left: 10px;
  font-size: 12px;
  margin-bottom: 6px
}

.user-container {
  display: flex;
  align-items: center;
  color: #a7a7a7;
  justify-content: right;
  margin-right: 10px;
  font-size: 12px;
  margin-bottom: 6px
}

.bot-name {
  font-family: SmileySans, serif;
}

.bot-label {
  padding: 0 4px;
  border-radius: 3px;
  margin-left: 10px;
  font-size: 12px;
  color: white;
}

.bot-date {
  color: #afafaf;
  padding-left: 10px;
  font-size: 12px;
}

</style>
