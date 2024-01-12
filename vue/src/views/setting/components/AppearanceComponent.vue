<script setup>

import {ref} from "vue";

const appearance = ref({
  active: 0,
  list: [
    {
      introduce: '白天模式',
      image: 'midday.png'
    },
    {
      introduce: '黑夜模式',
      image: 'night.png'
    }
  ]
})

</script>

<template>
  <div class="appearance-title">
    外观设置
  </div>
  <div class="appearance-body">
    <div class="appearance-spacing" v-for="(item,index) in appearance.list" :key="index">
      <div :class="index===appearance.active?'appearance-img-active':'appearance-img-default'">
        <img :src="require('../../../assets/app/'+item.image)" alt=""/>
      </div>
      <div class="img-prompt">
        {{ item.introduce }}
      </div>
    </div>
  </div>
</template>

<style scoped>

.appearance-title {
  font-size: 14px;
  padding-left: 10px;
  font-weight: 550;
}

.appearance-body {
  background-color: white;
  margin-top: 10px;
  border-radius: 8px;
  padding: 12px;
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.appearance-img-default {

  border-radius: 8px;
  width: 140px;
  height: 90px;
  overflow: hidden;
  border: 3px solid white
}

.appearance-img-active {

  border-radius: 8px;
  width: 140px;
  height: 90px;
  overflow: hidden;
  border: 3px solid #9a93ff
}

.appearance-img img {
  width: 100%;
  height: 100%
}

.img-prompt {
  padding-top: 6px;
  font-size: 13px
}

.appearance-spacing {
  margin-right: 10px;
}
</style>
