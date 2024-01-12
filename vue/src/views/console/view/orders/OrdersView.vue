<script setup>

import {onMounted, ref} from "vue";
import {ElLoading, ElNotification} from "element-plus";
import {reqDeleteOrders, reqGetOrdersPage} from "@/api/ordersManagement";
import store from "@/store";
import router from "@/router";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])

const pageNum = ref(1)


const search = () => {
  pageNum.value = 1
  total.value = 0
  if (promptTemp.value.trim()) {
    prompt.value = promptTemp.value

    init(pageNum.value, prompt.value)
  } else {
    init(pageNum.value, '')
  }
}

/**
 * 删除订单号
 * @param id
 * @returns {Promise<void>}
 */
const handleDeleteOrders = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除'})
  try {
    await reqDeleteOrders({ordersId: id});
    //刷新数据
    await init(1, '')
    ElNotification({
      message: "删除成功",
      type: "success",
    });
  } catch (e) {
    console.log(e)
  } finally {
    setTimeout(() => {
      service.close()
    }, 300)
  }
}

const init = async (pageNum, prompt) => {
  let service = ElLoading.service({fullscreen: true, text: '正在获取数据'})
  try {
    const {data} = await reqGetOrdersPage(pageNum, prompt);
    console.log(data.total)
    if (data.records) {
      tableData.value = data.records
      total.value = data.total
    }
  } catch (e) {
    console.log(e)
  } finally {
    setTimeout(() => {
      service.close()
    }, 300)
  }
}

onMounted(() => {
  let userInfo = store.getters.userInfo;
  if (userInfo) {
    const {type} = userInfo;
    if (type !== "ADMIN") {
      router.push({
        path: "/#"
      });
      return;
    }
  } else {
    router.push({
      path: "/#"
    });
    return
  }
  init(pageNum.value, prompt.value)
})

</script>

<template>
  <div class="container">
    <div class="body">
      <div class="title">订单视图</div>
      <div class="condition">
        <el-input v-model="promptTemp" placeholder="订单号" class="input-height"/>
        <el-button type="primary" class="input-button" @click="search">查找订单</el-button>
      </div>
      <el-table :data="tableData" stripe class="el-table"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="ordersId" label="订单号" width="300px"/>
        <el-table-column prop="email" label="订阅人邮箱" width="300px"/>
        <el-table-column prop="productName" label="服务名称" width="200px">
          <template #default="scope">
            <div class="column-flex">
              <div class="member-active" >
                {{ scope.row.productName }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="交易金额"/>
        <el-table-column prop="days" label="购买天数" width="200px"/>
        <el-table-column prop="status" label="订单状态">
          <template #default="scope">
            <div class="column-flex">
              <div class="type-cancel" v-if="scope.row.status==='CANCEL'">
                取消付款
              </div>
              <div class="type-succeed" v-if="scope.row.status==='SUCCEED'">
                付款成功
              </div>
              <div class="type-wait" v-if="scope.row.status==='WAIT'">
                等待付款
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="200px"/>
        <el-table-column prop="updateTime" label="修改时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">
            <el-button link type="primary" size="small" style="color: #993BFF"
                       @click="handleDeleteOrders(scope.row.ordersId)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-padding">
        <el-pagination background layout="prev, pager, next"
                       :total="total"
                       :page-size="15"
                       @current-change="($event)=>init($event,prompt)"/>
      </div>
    </div>
  </div>

</template>

<style scoped>
@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}
.pagination-padding {
  padding-top: 10px
}

.prompt {
  margin-bottom: 35px;
  font-size: 11px;
  color: #9d9d9d
}

.prompt span {

  color: #a743c3
}

:deep(.el-form-item__label) {

  font-size: 13px;
}

.el-table {
  width: 100%;
  height: 100%
}

.update-div {
  padding-top: 20px;
  text-align: right;
  padding-right: 10px;
}

.upload-btn {
  margin-left: 0 !important;
  width: auto !important;
  font-size: 13px !important;
}

.el-dialog-box {
  padding: 20px
}

.el-dialog-title {
  color: #404040;
  font-weight: 550;
  font-size: 16px
}

.el-form {
  margin-top: 50px
}

.el-width {
  width: 280px
}


.container {
  width: 100%;
  height: 100vh;
  padding: 10px;
  overflow: hidden;
  background-color: rgb(242, 242, 242);
  animation: explainAnimation 0.3s;
}
@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}
.title {
  font-size: 17px;
  color: #535353;
  font-weight: 550
}

.body {

  display: flex;
  flex-direction: column;
  background-color: rgb(255, 255, 255);
  height: 100%;
  border-radius: 10px;
  padding: 15px;
  overflow: hidden;
}

.column-flex {
  display: flex;
  align-items: center;
  justify-content: center
}

:deep(.el-pager li:hover) {
  color: rgb(153, 59, 255);;
}

.member-default {
  width: 80px;
  height: 25px;
  border-radius: 4px;
  background-color: rgb(153, 59, 255);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.member-active {
  width: 80px;
  height: 25px;
  border-radius: 4px;
  background-color: rgb(183, 59, 255);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.type-wait {
  width: 120px;
  height: 25px;
  border-radius: 4px;
  background-color: rgb(116, 158, 213);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.type-succeed {
  width: 120px;
  height: 25px;
  border-radius: 4px;
  background-color: rgb(116, 213, 134);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.type-cancel {
  width: 120px;
  height: 25px;
  border-radius: 4px;
  background-color: rgb(93, 93, 93);
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center
}


.condition {
  padding-bottom: 10px;
  width: 400px;
  padding-top: 10px;
  display: flex;
  align-items: center
}

.input-height {
  height: 32px
}

.input-button {
  font-size: 13px;
  border: none;
  height: 32px;
  margin-left: 10px;
  width: 100px;
  background-color: rgb(153, 59, 255)
}

.input-button:hover,
.input-button:focus,
.input-button:active {
  font-size: 13px;
  border: none;
  height: 32px;
  margin-left: 10px;
  width: 100px;
  background-color: rgb(153, 59, 255)
}

:deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: rgb(153, 59, 255)
}

:deep(.el-pagination.is-background .el-pager li) {
  background-color: transparent;
}


</style>
