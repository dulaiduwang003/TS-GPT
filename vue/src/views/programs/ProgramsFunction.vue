<script setup>


import TopNavigationBarComponent from "@/views/programs/component/TopNavigationBarComponent.vue";
import WritingComponent from "@/views/programs/component/WritingComponent.vue";
import store from "@/store";
import NotLoggedInComponent from "@/components/auth/NotLoggedInComponent.vue";


</script>

<template>

  <div class="body">

    <top-navigation-bar-component v-if="store.getters.userInfo"/>
    <writing-component v-if="store.getters.userInfo"/>
    <not-logged-in-component v-else/>
  </div>

</template>

<style scoped>
.body {
  animation: explainAnimation 0.3s;
  height: 100vh;
}
@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}
</style>
