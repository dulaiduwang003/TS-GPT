<script setup>

</script>

<template>
  <div class="head">
    <img :src="require('../../../assets/app/writing.svg')" alt="">
    <div class="title">TS写作助手</div>
    <div class="label">基于 GPT 驱动</div>
  </div>
</template>

<style scoped>

.head {
  height: 50px;
  position: fixed;
  z-index: 2;
  background-color: #ffffff;
  width: 100%;
  border-bottom: 1px solid #ededed;
  display: flex;
  padding: 0 20px;
  align-items: center
}

img {
  width: 35px;
  height: 35px
}

.title {
  font-family: SmileySans, serif;
  font-size: 15px;
  padding-left: 8px;
  padding-top: 3px;
  color: #5e5e5e

}

.label {
  margin-top: 5px;
  margin-left: 10px;
  font-size: 10px;
  background-color: #f3f3f3;
  padding: 2px 6px;
  border-radius: 6px;
  color: #959595
}
</style>
