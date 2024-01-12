<script setup>
import {CircleCheckFilled} from "@element-plus/icons-vue";
import {defineEmits, defineProps} from "vue";

const emits = defineEmits(["switch-active"]);

const props = defineProps({
  ordersId: {
    type: String
  }
});
</script>

<template>

  <div class="succeed">
    <div>
      <div class="succeed-icon">
        <el-icon color="rgb(153,59,255)" size="50px">
          <CircleCheckFilled/>
        </el-icon>
      </div>

      <div class="succeed-prompt">支付成功</div>
      <div class="succeed-order">订单商户号: {{props.ordersId}}</div>
      <div class="succeed-btn">
        <div @click="emits('switch-active',0)">
          重新购买
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.succeed {
  width: 400px;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center
}

.succeed-icon {
  display: flex;
  justify-content: center
}

.succeed-prompt {
  text-align: center;
  padding-top: 15px;
  font-size: 18px;
  font-weight: 500
}

.succeed-order {
  text-align: center;
  padding-top: 5px;
  font-size: 11px;
  color: #a0a0a0
}

.succeed-btn {
  margin-top: 40px;
  display: flex;
  justify-content: center
}

.succeed-btn > div {
  font-size: 13px;
  border: 1px solid #7f7f7f;
  border-radius: 25px;
  height: 30px;
  width: 120px;
  display: flex;
  justify-content: center;
  align-items: center
}
</style>
