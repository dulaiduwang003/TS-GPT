<script setup>

import {onMounted, ref} from "vue";
import {ElLoading, ElNotification} from "element-plus";
import {Plus} from "@element-plus/icons-vue";
import {reqAddProduct, reqDeleteProduct, reqGetProductPage, reqUpdateProduct} from "@/api/productManagement";
import store from "@/store";
import router from "@/router";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])


const pageNum = ref(1)

const isUpdateVisual = ref(false)

const isAddVisual = ref(false)

const form = ref({
  productCardId: undefined,
  productName: '',
  days: undefined,
  price: undefined
})

const openUpdateDialog = (data) => {
  form.value.productCardId = data.productCardId
  form.value.productName = data.productName
  form.value.days = data.days
  form.value.price = data.price

  isUpdateVisual.value = true
}

/**
 * 修改商品
 * @returns {Promise<void>}
 */
const handleUpdateProduct = async () => {
  if (!form.value.productName) {
    ElNotification({message: '商品名称不能为空', type: "error",});
    return
  }
  if (!form.value.price) {
    ElNotification({message: '商品价格不能为空', type: "error",});
    return
  }
  if (!form.value.days) {
    ElNotification({message: '商品所含天数不能为空', type: "error",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqUpdateProduct(form.value);
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

/**
 * 新增商品
 * @returns {Promise<void>}
 */
const handleAddProductCard = async () => {
  if (!form.value.productName) {
    ElNotification({message: '商品名称不能为空', type: "error",});
    return
  }
  if (!form.value.price) {
    ElNotification({message: '商品价格不能为空', type: "error",});
    return
  }
  if (!form.value.days) {
    ElNotification({message: '商品所含天数不能为空', type: "error",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqAddProduct(form.value);
    //刷新数据
    await init(1, '')
    ElNotification({
      message: '添加成功',
      type: "success",
    });
    isAddVisual.value = false
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


/**
 * 删除商品
 * @param id
 * @returns {Promise<void>}
 */
const handleDeleteProduct = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteProduct({productCardId: id});
    //刷新数据
    await init(1, '')
    ElNotification({
      message: '删除成功',
      type: "success",
    });
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
    const {data} = await reqGetProductPage(pageNum, prompt);
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
      <div class="title">商品视图</div>
      <div class="condition">
        <el-input v-model="promptTemp" placeholder="商品名称" class="input-height"/>
        <el-button type="primary" class="input-button" @click="search">搜索商品</el-button>
      </div>

      <!--      操作-->
      <div style="margin-top: 20px">
        <el-button type="primary"
                   class="input-button add-button"
                   @click="isAddVisual=true"
                   :icon="Plus">新增商品
        </el-button>
      </div>
      <el-table :data="tableData" stripe class="el-table"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="productCardId" label="序号"/>
        <el-table-column prop="productName" label="服务名称" width="200"/>
        <el-table-column prop="price" label="价格" width="200"/>
        <el-table-column prop="days" label="所含天数" width="150">
          <template #default="scope">
            <div class="column-flex">
              <div class="type-default">
                {{ scope.row.days }} 天
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="200px"/>
        <el-table-column prop="updateTime" label="修改时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">
            <el-button link type="primary" size="small" style="color: #993BFF" @click="openUpdateDialog(scope.row)">
              修改
            </el-button>
            <el-button link type="primary" size="small" style="color: #ff3b3b"
                       @click="handleDeleteProduct(scope.row.productCardId)">
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
  <!--  修改-->
  <el-dialog
      v-model="isUpdateVisual"
      :show-close="false"
      align-center
      width="510px"
      @close="isUpdateVisual=false"
      style="border-radius:  8px;"
  >
    <div class="el-dialog-box">
      <div class="el-dialog-title">
        修改商品
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="商品名称">
          <el-input placeholder="请设置商品名称"
                    v-model="form.productName"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          将作为用户可见的商品名称 如 <span>7天卡</span>
        </div>
        <el-form-item label="所含天数">
          <el-input placeholder="请设置所含天数"
                    v-model="form.days"
                    type="number"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          例如 想上架一张七天卡 则设置 数字 <span>7</span> 即可
        </div>
        <el-form-item label="价格">
          <el-input placeholder="请设置价格"
                    v-model="form.price"
                    :step="0.1"
                    type="number"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          设置本商品的价格 如 <span>7.99</span>
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleUpdateProduct">
          保存修改
        </el-button>
      </div>
    </div>
  </el-dialog>

  <!--  新增-->
  <el-dialog
      v-model="isAddVisual"
      :show-close="false"
      align-center
      width="510px"
      @close="isAddVisual=false"
      style="border-radius:  8px;"
  >
    <div class="el-dialog-box">
      <div class="el-dialog-title">
        新增商品
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="商品名称">
          <el-input placeholder="请设置商品名称"
                    v-model="form.productName"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          将作为用户可见的商品名称 如 <span>7天卡</span>
        </div>
        <el-form-item label="所含天数">
          <el-input placeholder="请设置所含天数"
                    v-model="form.days"
                    type="number"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          例如 想上架一张七天卡 则设置 数字 <span>7</span> 即可
        </div>
        <el-form-item label="价格">
          <el-input placeholder="请设置价格"
                    v-model="form.price"
                    :step="0.1"
                    type="number"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          设置本商品的价格 如 <span>7.99</span>
        </div>
      </el-form>
      <div class="update-div">
        <el-button type="primary" class="input-button upload-btn" @click="handleAddProductCard">
          新增商品
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
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

.pagination-padding {
  padding-top: 10px
}

.add-button {
  margin-left: 0 !important;
  margin-right: 8px;
  width: auto !important;
  background-color: #62b04f !important;
}

.apply-button {
  margin-left: 0 !important;
  margin-right: 8px;
  width: auto !important;
  background-color: #695fff !important;
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
  width: 240px
}


.container {
  width: 100%;
  height: 100vh;
  animation: explainAnimation 0.3s;
  padding: 10px;
  overflow: hidden;
  background-color: rgb(242, 242, 242);
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


</style>
