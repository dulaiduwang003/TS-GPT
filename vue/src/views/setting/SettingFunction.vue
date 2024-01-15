<script setup>

import AppearanceComponent from "@/views/setting/components/AppearanceComponent.vue";
import ChatComponent from "@/views/setting/components/ChatComponent.vue";
import UserInfoComponent from "@/views/setting/components/UserInfoComponent.vue";
import AppComponent from "@/views/setting/components/AppComponent.vue";
import QuitComponent from "@/views/setting/components/QuitComponent.vue";
import NotLoggedInComponent from "@/components/auth/NotLoggedInComponent.vue";
import store from "@/store";
import WritingComponent from "@/views/setting/components/WritingComponent.vue";
import {onMounted, ref} from "vue";
import {reqGetAiModelList} from "@/api/chat";

const modelList = ref([])

onMounted(async () => {

  if (store.getters.userInfo){
    try {
      const {data} = await reqGetAiModelList();
      modelList.value = data
    } catch (e) {
      console.log(e)
    }
  }

})

</script>

<template>
  <div class="container">
    <div class="body" v-if="store.getters.userInfo">
      <div class="title">偏好设置</div>
      <div class="body-container">
        <user-info-component/>
        <!--        外观-->
        <appearance-component/>
        <!--        聊天-->
        <chat-component :model-list="modelList"/>
        <!--        写作-->
        <writing-component :model-list="modelList"/>
        <!--        预设-->
        <app-component/>
        <!--        退出登录-->
        <quit-component/>
      </div>
    </div>
    <not-logged-in-component v-else/>
  </div>

</template>

<style scoped>

.container {
  width: 100%;
  height: 100vh;
  padding: 10px;
  overflow: hidden;

}

@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.body {
  background-color: rgb(242, 242, 242);
  height: 100%;
  border-radius: 10px;

  overflow: hidden;
}

.title {
  font-size: 19px;
  padding: 20px 30px;
  color: #1c1c1c;
  border-bottom: 1px solid #e8e8e8
}

.body-container {
  margin: 0 auto;
  padding: 20px 15px;
  height: 89%;

  animation: explainAnimation 0.3s;
  max-width: 980px;
  scroll-behavior: smooth;
  overflow-x: auto;
}

</style>
