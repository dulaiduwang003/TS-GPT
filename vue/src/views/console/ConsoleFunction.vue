<script setup>

import IndexComponent from "@/views/console/component/IndexComponent.vue";
import {onMounted} from "vue";
import store from "@/store";
import router from "@/router";

onMounted(() => {
  let userInfo = store.getters.userInfo;
  if (userInfo) {
    const {type} = userInfo;
    if (type !== "ADMIN") {
      router.push({
        path: "/#"
      });
    }
  } else {
    router.push({
      path: "/#"
    });
  }
})
</script>

<template>
  <div class="container">
    <div class="body">
      <index-component/>
    </div>
  </div>
</template>

<style scoped>

.container {
  width: 100%;
  height: 100vh;
  padding: 10px;
  overflow: hidden;

}

.body {
  display: flex;
  flex-direction: column;
  background-color: rgb(242, 242, 242);
  height: 100%;
  border-radius: 10px;
  padding: 15px;
  overflow: auto;
}
</style>
