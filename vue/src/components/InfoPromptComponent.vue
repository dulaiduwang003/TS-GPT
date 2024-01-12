<script setup>

import {defineProps} from "vue";
import {InfoFilled} from "@element-plus/icons-vue";

const props = defineProps({
  title: {
    type: String
  },
  content: {
    type: String
  }
});

</script>

<template>
  <el-popover placement="bottom" :title="props.title" :width="300"
              trigger="hover" :content="props.content">
    <template #reference>
      <el-icon>
        <info-filled/>
      </el-icon>
    </template>
  </el-popover>
</template>

<style scoped>

</style>
