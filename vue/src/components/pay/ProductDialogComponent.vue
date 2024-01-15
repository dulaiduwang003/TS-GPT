<script setup>
import {defineEmits, defineProps, onMounted, ref, watch} from "vue";
import CreatedOrdersComponent from "@/components/pay/components/CreatedOrdersComponent.vue";
import {reqCreateAliOrders, reqGetAliOrderStatus, reqGetAllProduct} from "@/api/pay";
import AlipayComponent from "@/components/pay/components/AlipayComponent.vue";
import {ElLoading, ElNotification} from "element-plus";
import SucceedComponent from "@/components/pay/components/SucceedComponent.vue";
import {reqGetCurrentUserInfo} from "@/api/auth";
import store from "@/store";

const props = defineProps({
  isVisible: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["close-dialog-event"]);

const isVisible = ref(false);

//当前激活步骤
const active = ref(0)

//商品列表
const productList = ref([])

//二维码是否失效
const isLapse = ref(false)

//支付状态获取
const payStatus = ref(false)

//当前支付状态定时器Id
const timerId = ref(undefined)

const form = ref({
  productCardId: undefined
})

const orders = ref({})


/**
 * 选中卡片
 * @param index
 */
const handleSelectedCard = (index) => {
  productList.value.forEach(p => p.isSelected = false)
  productList.value[index].isSelected = true
  form.value.productCardId = productList.value[index].productCardId
}

/**
 * 创建订单
 */
const createdAlipay = async () => {
  if (timerId.value) {
    clearInterval(timerId.value)
  }
  let service = ElLoading.service({fullscreen: true, text: '正在处理订单中'})
  try {
    const {data} = await reqCreateAliOrders(form.value);
    handleALiOrderStatus(data.ordersId)
    orders.value = data
    active.value = 1
  } catch (e) {
    ElNotification({
      message: e.msg, type: "error",
    });
    console.log(e)
  } finally {
    service.close()
  }
}


/**
 * 获取订单处理状态
 * @param ordersId
 */
const handleALiOrderStatus = (ordersId) => {

  timerId.value = setInterval(async () => {
    if (payStatus.value) {
      return
    }
    try {
      payStatus.value = true
      const {data} = await reqGetAliOrderStatus(ordersId);
      if (data) {
        ElNotification({
          title: "会员订阅", message: '支付成功', type: "success"
        });
        const res = await reqGetCurrentUserInfo();
        store.commit("setUserInfo", res.data);
        active.value = 2
        payStatus.value = false
        clearInterval(timerId.value)
        return
      }
      console.log('未支付')
      payStatus.value = false
    } catch (e) {
      ElNotification({
        title: "会员订阅", message: e.msg, type: "info",
      });
      isLapse.value = true
      payStatus.value = false
      clearInterval(timerId.value)
    }
  }, 2000);
}

/**
 * 切换激活状态
 * @param index
 */
const handleSwitchActive = (index) => {
  active.value = index
}

/**
 * 初始化数据
 * @returns {Promise<void>}
 */
const init = async () => {
  try {
    const {data} = await reqGetAllProduct();
    if (data && data.length > 0) {
      productList.value = data
      productList.value[0].isSelected = true
      form.value.productCardId = productList.value[0].productCardId
    }
  }catch (e) {
    console.log(e.msg)
  }

}

onMounted(() => {
  if (store.getters.userInfo) {
    init()
  }
})
watch(
    () => props.isVisible,
    (newValue) => {
      isVisible.value = newValue;
    },
    {
      immediate: true,
    }
);


</script>

<template>
  <el-dialog
      :append-to-body="true"
      :close-on-click-modal="false"
      v-model="isVisible"
      @close="emits('close-dialog-event')"
      align-center
      style="width: auto; border-radius: 8px;background: linear-gradient(to bottom, rgb(244, 240, 244), rgb(231, 236, 246));padding: 20px 50px"
  >
    <!--    创建订单-->
    <created-orders-component
        v-if="active===0"
        :data="productList" @handle-selected-card="handleSelectedCard" @created-orders="createdAlipay"/>
    <!--    支付宝-->
    <alipay-component @switch-active="handleSwitchActive" :is-lapse="isLapse" :data="orders" v-if="active===1"/>
    <!--    支付成功-->
    <succeed-component @switch-active="handleSwitchActive" :orders-id="orders.ordersId" v-if="active===2"/>
  </el-dialog>
</template>

<style scoped>

</style>
