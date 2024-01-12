<script setup>

import {defineProps} from "vue";
import {useRoute} from "vue-router";
import store from "@/store";
import {getOssDoMain} from "@/utils/Utils";


const props = defineProps({
  active: {
    type: Number
  },
  menuList: {
    type: Array
  }
});

let route = useRoute();


</script>

<template>
  <!--  触发控件-->
  <div class="chat-record-div">
    <div class="custom-div">
      <div class="inner-div" v-if="store.getters.userInfo">
        <img
            :src="store.getters.userInfo.avatar?getOssDoMain()+store.getters.userInfo.avatar: require('../../../assets/app/default-avatar.png')"
            alt="" class="avatar">
      </div>
      <div class="inner-div" v-for="(item,index) in props.menuList" :key="index">
        <router-link :to="item.to" class="embedded" active-class="embedded-active">
          <img
              :src="require('../../../assets/menu/'+(route.path===item.to ?item.iconActive:item.iconDefault))"
              class="inner-img" alt=""/>
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.chat-record-div {
  position: fixed;
  background-color: rgba(164, 156, 156, 0.54);
  left: -4px;
  z-index: 20;
}


.custom-div {
  height: 100vh;
  background-color: rgb(229, 229, 229);
  width: 65px;
}

.inner-div {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 80px;

}

.inner-img {
  width: 25px;
  height: 25px;
}


::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}


::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background: #151515;
}

::-webkit-scrollbar-track {
  /* box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); */
  border-radius: 0;
  background: #151515;
  display: block;
}

:deep(.el-drawer__body) {
  padding: 0;
}


.drawer-logo img {
  width: 60px;
  height: 60px
}


@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.embedded {
  padding: 8px 10px 3px;
  border-radius: 5px;
  cursor: pointer;
}

.embedded-active {
  cursor: pointer;
  padding: 8px 10px 3px;
  border-radius: 5px;
  background-color: #bababa;
  animation: explainAnimation 0.3s;
}

@keyframes slideEase {
  0% {
    transform: translateX(-100px);
  }

  100% {
    transform: translateX(0);
  }
}


@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.avatar {
  width: 35px;
  height: 35px;
  border-radius: 100%
}

</style>
