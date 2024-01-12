<script setup>

</script>

<template>
  <div class="wechat-login">
    <div>
      <img :src="require('../../../assets/images/wechat.jpg')" alt="">
      <div class="prompt">此功能小程序写完后开放</div>
      <div class="wechat-login-prompt">打开微信扫一扫快速注册/登录</div>
    </div>
  </div>
</template>

<style scoped>


.wechat-login {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}
.prompt {
  font-weight: 550;
  padding-top: 20px
}
.wechat-login img {
  width: 240px;
  height: 300px
}

.wechat-login-prompt {
  font-size: 12px;
  padding-top: 20px;
  color: #8b8b8b
}
</style>
