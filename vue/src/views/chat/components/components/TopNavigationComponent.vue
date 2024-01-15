<script setup>

import {ref} from "vue";

import AuthDialogComponent from "@/components/auth/AuthDialogComponent.vue";
import store from "@/store";
import {removalTime} from "../../../../utils/Utils";
import ProductDialogComponent from "@/components/pay/ProductDialogComponent.vue";


//登录框可见性
const authDialogIsVisible = ref(false)

const productDialogIsVisible = ref(false)

const isFullScreen = ref(false)


const handleCloseDialog = () => {
  authDialogIsVisible.value = false
}

const handleFullScreen = () => {
  isFullScreen.value = !isFullScreen.value
  if (document.fullscreenElement) {
    document.exitFullscreen();
  } else {
    document.documentElement.requestFullscreen();
  }
}

</script>

<template>

  <div class="container">
    <div class="flex-wrapper">
      <div class="flex-wrapper-model">
        <!--控件-->
        <div class="conversation-div" v-if="store.getters.userInfo">
          <img :src="isMember ? require('../../../../assets/app/member.svg') : require('../../../../assets/app/overdue.svg')" class="member-icon" alt="">
          <div class="conversation-text">
            {{
              store.getters.userInfo.member.isMember ? removalTime(store.getters.userInfo.member.expirationTime) : '会员已过期'
            }}
          </div>
          <div class="conversation-icon" @click="productDialogIsVisible=true">
            <img alt="" :src="require('../../../../assets/images/public/recharge.svg')">
          </div>
        </div>
        <!--        对话类设置-->
        <img alt=""
             :src="require(isFullScreen?'../../../../assets/images/public/shrink.svg':'../../../../assets/images/public/more.svg')"
             class="setting"
             @click="handleFullScreen"
             v-if="store.getters.userInfo">

        <div class="btn-join-login" @click="authDialogIsVisible=true" v-else>
          登录
        </div>
      </div>

    </div>
  </div>
  <auth-dialog-component :is-visible="authDialogIsVisible" @close-dialog-event="handleCloseDialog"/>
  <product-dialog-component :is-visible="productDialogIsVisible" @close-dialog-event="productDialogIsVisible=false"/>
</template>


<style scoped>
.container {
  width: 100%;
}

.member-icon {
  width: 20px;
  height: 20px
}

.flex-wrapper {
  width: 100%;
  padding: 5px 0;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #F2F2F2;
  border-bottom: 1px solid #eaeaea;
}


.flex-wrapper-model {
  background-color: white;
  padding: 5px 8px;
  width: 100%;
  display: flex;
  justify-content: right;
  align-items: center;

}


.right-layout {
  display: flex;
  justify-content: space-between;
  align-items: center
}

.conversation-div {
  background-color: #515151;
  font-size: 10px;
  border-radius: 20px;
  padding: 0 8px;
  padding-right: 2px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
  height: 28px;
}

.conversation-text {
  padding: 0 5px 0 4px;
  font-family: SmileySans, serif;
}

.conversation-icon {
  padding-top: 5px
}

.conversation-icon img {
  width: 25px;
  height: 25px
}

.logo {
  width: 35px;
  height: 35px
}

.more-div {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left: 20px
}

.more-div img {
  width: 23px;
  height: 23px;
  cursor: pointer;
}


:deep(.el-dropdown-menu__item:hover, .el-dropdown-menu__item:focus ) {
  background: rgba(140, 223, 216, 0.15) !important;
  box-shadow: none !important;
  color: #7365FF !important;
}

.logo-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: SmileySans, serif;
  color: white;
}

.logo-wrapper span {
  padding-left: 10px;
  font-size: 20px
}

.btn-join-login {
  width: 60px;
  height: 28px;
  background: #7A3EFA;
  border-radius: 6px;
  padding: 0 10px;
  font-size: 12px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
}

.setting {
  width: 25px;
  height: 25px;
  cursor: pointer;
  margin-left: 10px;
}

</style>
