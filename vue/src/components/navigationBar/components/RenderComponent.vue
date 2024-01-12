<script setup>

</script>

<template>
  <div class="rightContent">
    <RouterView v-slot="{ Component }">
      <KeepAlive>
        <component
            :is="Component"
            :key="$route.name"
            v-if="$route.meta.keepAlive"
        ></component>
      </KeepAlive>
      <component
          :is="Component"
          :key="$route.name"
          v-if="!$route.meta.keepAlive"
      ></component>
    </RouterView>
  </div>
</template>

<style scoped>

.rightContent{
  padding-left:  60px;
}

</style>
