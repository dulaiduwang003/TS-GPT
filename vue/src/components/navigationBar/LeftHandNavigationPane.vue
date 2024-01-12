<template>
  <!--菜单栏-->
  <menu-component :menu-list="menuList"/>
  <!--视图渲染-->
  <render-component/>
</template>


<script setup>
import {onMounted, ref} from "vue";
import MenuComponent from "@/components/navigationBar/components/MenuComponent.vue";
import RenderComponent from "@/components/navigationBar/components/RenderComponent.vue";
import store from "@/store";

const menuList = ref([
  {
    iconDefault: 'chat.svg',
    iconActive: 'active/chat-active.svg',
    to: "/",
  },
  {
    iconDefault: 'drawing.svg',
    iconActive: 'active/drawing-active.svg',
    to: "/drawing",
  }, {
    iconDefault: 'app.svg',
    iconActive: 'active/app-active.svg',
    to: "/app",
  }
  , {
    iconDefault: 'programs.svg',
    iconActive: 'active/programs-active.svg',
    to: '/programs',
  }, {
    iconDefault: 'setting.svg',
    iconActive: 'active/setting-active.svg',
    to: '/setting',
  }
])

onMounted(() => {
  if (store.getters.userInfo) {
    if (store.getters.userInfo.type === "ADMIN") {
      menuList.value.push({
        iconDefault: 'console.svg',
        iconActive: 'active/console-active.svg',
        to: '/console',
      })
    }
  }
})

</script>

<style scoped>

</style>
