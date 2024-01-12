<script setup>

import {onMounted, ref} from "vue";
import {reqGetUserPage, reqUpdateUserInfo} from "@/api/userManagement";
import {ElLoading, ElNotification} from "element-plus";
import store from "@/store";
import router from "@/router";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])

const pageNum = ref(1)

const isUpdateVisual = ref(false)

const form = ref({
  userId: undefined,
  nickName: '',
  expirationTime: '',
  type: ''
})

const openUpdateDialog = (data) => {
  form.value.userId = data.userId
  form.value.nickName = data.nickName
  form.value.expirationTime = data.expirationTime
  form.value.type = data.type
  isUpdateVisual.value = true
}

const handleUpdateUserInfo = async () => {
  if (!form.value.nickName) {
    ElNotification({message: '用户昵称不能为空', type: "error",});
    return
  }
  if (!form.value.expirationTime) {
    ElNotification({message: '会员过期时间不能为空', type: "error",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqUpdateUserInfo(form.value);
    //刷新数据
    await init(1, '')
    ElNotification({
      message: '修改成功',
      type: "success",
    });
    isUpdateVisual.value = false
  } catch (e) {
    ElNotification({
      message: e.msg,
      type: "error",
    });
  } finally {
    setTimeout(() => {
      service.close()
    }, 300)
  }
}

const search = () => {
  console.log(promptTemp.value)
  pageNum.value = 1
  total.value = 0
  if (promptTemp.value.trim()) {
    prompt.value = promptTemp.value

    init(pageNum.value, prompt.value)
  } else {
    init(pageNum.value, '')
  }
}

const init = async (pageNum, prompt) => {
  let service = ElLoading.service({fullscreen: true, text: '正在获取数据'})
  try {
    const {data} = await reqGetUserPage(pageNum, prompt);
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
      <div class="title">用户视图</div>
      <div class="condition">
        <el-input v-model="promptTemp" placeholder="邮箱、昵称" class="input-height"/>
        <el-button type="primary" class="input-button" @click="search">查找用户</el-button>
      </div>
      <el-table :data="tableData" stripe class="el-table"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="userId" label="序号"/>
        <el-table-column prop="nickName" label="昵称"/>
        <el-table-column prop="email" label="邮箱" width="200px"/>
        <el-table-column prop="type" label="用户类型">
          <template #default="scope">
            <div class="column-flex">
              <div :class="scope.row.type==='ADMIN'?'type-default':'type-active'">
                {{ scope.row.type === 'ADMIN' ? '管理员' : '普通用户' }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="isMember" label="会员是否到期">
          <template #default="scope">

            <div class="column-flex">
              <div :class="scope.row.isMember?'member-default':'member-active'">
                {{ scope.row.isMember ? '正常' : '已过期' }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="daysRemaining" label="会员过期时间">
          <template #default="scope">
            <div class="column-flex">
              {{ scope.row.daysRemaining ? scope.row.daysRemaining + '天' : '-' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="200px"/>
        <el-table-column prop="updateTime" label="修改时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">
            <el-button link type="primary" size="small" style="color: #993BFF" @click="openUpdateDialog(scope.row)">
              修改用户信息
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
  <el-dialog
      v-model="isUpdateVisual"
      :show-close="false"
      align-center
      width="450px"
      @close="isUpdateVisual=false"
      style="border-radius:  8px;"
  >

    <div class="el-dialog-box">
      <div class="el-dialog-title">
        修改用户信息
      </div>
      <el-form :inline="true" label-width="120px" label-position="left" class="el-form">
        <el-form-item label="用户昵称">
          <el-input placeholder="请设置用户昵称" v-model="form.nickName" class="el-width"/>
        </el-form-item>
        <div class="prompt">
          设置用户的昵称
        </div>
        <el-form-item label="用户类型">
          <el-select
              v-model="form.type"
              placeholder="请设置用户类型"
              clearable
              style="width: 280px"
          >
            <el-option label="管理员" value="ADMIN"/>
            <el-option label="普通用户" value="USER"/>
          </el-select>
        </el-form-item>
        <div class="prompt">
          指定用户类型
        </div>
        <el-form-item label="会员到期时间">
          <el-date-picker
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              v-model="form.expirationTime"
              type="datetime"
              placeholder="请设置会员到期时间"

              style="width: 280px"
          />
        </el-form-item>
        <div class="prompt">
          指定该用户会员过期时间
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleUpdateUserInfo">
          保存修改
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
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
  background-color: rgb(255, 59, 75);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.type-default {
  width: 120px;
  height: 25px;
  border-radius: 4px;
  background-color: rgb(59, 180, 255);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.type-active {
  width: 120px;
  height: 25px;
  border-radius: 4px;
  background-color: rgb(74, 171, 105);
  color: white;
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

@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}
</style>
