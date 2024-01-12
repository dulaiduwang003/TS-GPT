<script setup>

import {defineEmits, defineProps} from "vue";
import {ArrowLeftBold} from "@element-plus/icons-vue";

const emits = defineEmits(["switch-active"]);

const props = defineProps({
  isLapse: {
    type: Boolean
  },
  data: {
    type: Object
  }
});

</script>

<template>
  <div class="vip-container">
    <div class="previous" @click="emits('switch-active',0)">
      <el-icon size="15px" >
        <ArrowLeftBold/>
      </el-icon>
    </div>
    <img :src="require('../../../assets/app/alipay.svg')" class="vip-icon" alt=""/>
    使用支付宝扫一扫
  </div>
  <div class="alipay-container">
    <div class="alipay-qr-box">
      <div style="position: relative">
        <img :src="props.data.qrCode" alt=""/>
        <div class="cover-div" v-show="props.isLapse">
          <div class="lapse">
            <div>
              二维码已经失效
            </div>
            <div class="lapse-prompt">
              请返回上一页重新创建订单
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="alipay-data">
      <div class="alipay-row">
        <div class="alipay-row-label">商品名称</div>
        <div>{{ props.data.productName }}</div>
      </div>
      <div class="alipay-row">
        <div class="alipay-row-label">商户订单号</div>
        <div>{{ props.data.ordersId }}</div>
      </div>
      <div class="alipay-row">
        <div class="alipay-row-label">创建时间</div>
        <div class="created-time">{{ props.data.createdTime }}</div>
      </div>
      <div class="pay-text">
        需支付
        <div>￥{{ props.data.price }}元</div>
      </div>
    </div>
  </div>
  <div class="alipay-prompt">
    <div>
      <img :src="require('../../../assets/app/sweepIt.svg')" alt="" class="alipay-prompt-img"/>
    </div>
    <div class="alipay-prompt-text">手机支付宝扫一扫完成付款</div>
  </div>

</template>

<style scoped>
.pay-text {
  padding-top: 66px;
  display: flex;
  align-items: center
}

.previous {
  position: absolute;
  z-index: 2;
  left: -20px;
  color: #5f5f5f
}

.submit-btn {
  border: none;
  width: 240px;
  height: 45px;
  border-radius: 20px;
  background: linear-gradient(to right, rgb(151, 125, 189), rgb(119, 103, 191));
}

.btn-model {
  text-align: center;
  padding-top: 30px
}

.pay-text > div {
  padding-left: 15px;
  font-size: 25px;
  font-weight: 600;
  padding-bottom: 8px
}

.alipay-prompt {
  display: flex;
  align-items: center;
  font-size: 12px;
  padding-left: 5px;
  width: 200px;
  justify-content: center
}

.lapse {
  text-align: center
}

.lapse-prompt {
  text-align: center;
  font-size: 13px;
  padding-top: 10px
}

.alipay-prompt-img {
  width: 15px;
  height: 15px
}

.alipay-prompt-text {
  padding-left: 8px;
  padding-bottom: 4px;
  color: #a0a0a0
}

.vip-container {
  position: relative;
  padding: 8px;
  font-size: 19px;
  font-weight: 550;
  color: #171717;
  display: flex;
  align-items: center;;
  margin-bottom: 8px;
}

.vip-icon {
  width: 25px;
  height: 25px;
  margin-right: 5px
}

.alipay-data {
  width: 400px;
  height: 200px;
  padding-left: 30px;
  font-size: 13px
}

.alipay-row {
  width: 100%;
  display: flex;
  padding-bottom: 10px
}


.alipay-row-label {

  width: 100px
}

.created-time {
  width: 280px;
  padding-left: 3px
}

.alipay-container {
  display: flex;
  align-items: center;
  margin-top: 20px
}

.alipay-qr-box {

  display: flex;
  justify-content: center;
  align-items: center
}

.alipay-qr-box > div {
  position: relative;

}

.cover-div {

  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 48%;
  left: 49%;
  height: 200px;
  width: 200px;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.93);
  padding: 10px;
  font-size: 20px;
  border-radius: 5px;
}

.alipay-qr-box > div > img {
  width: 200px;
  height: 200px
}

.vip-container > span {
  font-weight: 600
}
</style>
