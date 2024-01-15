<script setup>

import {reqLogout} from "@/api/auth";
import {ElLoading, ElNotification} from "element-plus";

/**
 * 退出登录
 */
const handleLogout = async () => {
  let service = ElLoading.service({fullscreen: true, text: '正在注销'})
  try {
    await reqLogout();
    localStorage.removeItem("token");
    localStorage.removeItem("userInfo");
    location.reload();
  } catch (e) {
    ElNotification({
      message: e.msg, type: 'error',
    });
  } finally {
    service.close()
  }

}

</script>

<template>

  <div class="appearance-body">
    <el-button type="primary" class="quit-btn" @click="handleLogout">退出登录</el-button>
  </div>
</template>

<style scoped>

.quit-btn {
  width: 100%;
  height: 50px;
  background-color: rgb(255, 73, 73);
  border: none
}
.quit-btn:hover,
.quit-btn:focus,
.quit-btn:active {
  background-color: rgb(255, 73, 73);
  outline: 0;
}

.appearance-body {
  background-color: white;
  margin-top: 10px;
  border-radius: 8px;

  margin-bottom: 30px;
}

</style>
