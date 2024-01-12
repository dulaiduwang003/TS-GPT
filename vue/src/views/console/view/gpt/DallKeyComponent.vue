<script setup>

import {onMounted, ref} from "vue";
import {
  reqAddDallKey,
  reqDeleteDallKey,
  reqGetDallExtraConfig,
  reqGetDallKeyPage,
  reqLoadDallStructure,
  reqSetDallExtraConfig,
  reqUpdateDallKey
} from "@/api/gptManagement";
import {ElLoading, ElNotification} from "element-plus";
import {MessageBox, Plus, Sugar} from "@element-plus/icons-vue";
import store from "@/store";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])

const pageNum = ref(1)

const isExtraVisual = ref(false)

const isUpdateVisual = ref(false)

const isAddVisual = ref(false)

const form = ref({
  dallKeyId: undefined,
  openAiKey: ''
})

const formExtra = ref({
  requestUrl: ''
})

const openUpdateDialog = (data) => {
  form.value.dallKeyId = data.dallKeyId
  form.value.openAiKey = data.openAiKey
  isUpdateVisual.value = true
}

/**
 * 修改密钥
 * @returns {Promise<void>}
 */
const handleUpdateDallKey = async () => {
  if (!form.value.openAiKey) {
    ElNotification({message: 'GPT密钥不能为空', type: "info",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqUpdateDallKey(form.value);
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
 * 新增密钥
 * @returns {Promise<void>}
 */
const handleAddGptKey = async () => {
  if (!form.value.openAiKey) {
    ElNotification({message: 'GPT密钥不能为空', type: "info",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqAddDallKey(form.value);
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
 * 新增密钥
 * @returns {Promise<void>}
 */
const handleSetDallExtraConfig = async () => {
  if (!formExtra.value.requestUrl) {
    ElNotification({message: '绘图请求链不能为空', type: "error",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqSetDallExtraConfig(formExtra.value);
    //刷新数据
    await init(1, '')
    ElNotification({
      message: '添加成功',
      type: "success",
    });
    isExtraVisual.value = false
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
 *  更新GPT配置
 * @returns {Promise<void>}
 */
const handleLoadDallStructure = async () => {
  let service = ElLoading.service({fullscreen: true, text: '正在处理中'})
  try {
    await reqLoadDallStructure();
    ElNotification({
      message: '操作成功',
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

/**
 * 删除密钥
 * @param id
 * @returns {Promise<void>}
 */
const handleDeleteDallKey = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteDallKey({dallKeyId: id});
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
    const {data} = await reqGetDallKeyPage(pageNum, prompt);
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

/**
 * 获取Dall额外配置
 */
const getDallExtraConfig = async () => {
  try {
    const {data} = await reqGetDallExtraConfig();
    formExtra.value = data
  } catch (e) {
    ElNotification({
      message: e.msg, type: "error",
    });

  }
}

onMounted(() => {
  if (store.getters.userInfo) {
    init(pageNum.value, prompt.value)
    getDallExtraConfig()
  }
})

</script>

<template>
  <div class="container">
    <div class="body">
      <div class="title">管理绘图密钥</div>
      <div class="condition">
        <el-input v-model="promptTemp" placeholder="密钥" class="input-height"/>
        <el-button type="primary" class="input-button" @click="search">查找密钥</el-button>
      </div>

      <!--      操作-->
      <div style="margin-top: 20px">
        <el-button type="primary"
                   class="input-button add-button"
                   @click="isAddVisual=true"
                   :icon="Plus">新增密钥
        </el-button>
        <el-button type="primary"
                   class="input-button apply-button"
                   @click="handleLoadDallStructure"
                   :icon="MessageBox">载入到配置池中
        </el-button>
        <el-button type="primary"
                   class="input-button apply-button"
                   @click="isExtraVisual=true"
                   :icon="Sugar">额外配置
        </el-button>
      </div>
      <el-table :data="tableData" stripe class="el-table"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="dallKeyId" label="序号" width="200"/>
        <el-table-column prop="openAiKey" label="密钥"/>
        <el-table-column prop="createdTime" label="创建时间" width="200px"/>
        <el-table-column prop="updateTime" label="修改时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">
            <el-button link type="primary" size="small" style="color: #993BFF" @click="openUpdateDialog(scope.row)">
              修改
            </el-button>
            <el-button link type="primary" size="small" style="color: #ff3b3b"
                       @click="handleDeleteDallKey(scope.row.dallKeyId)">
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
      width="450px"
      @close="isUpdateVisual=false"
      style="border-radius:  8px;"
  >
    <div class="el-dialog-box">
      <div class="el-dialog-title">
        修改密钥
      </div>
      <el-form :inline="true" label-width="120px" label-position="left" class="el-form">
        <el-form-item label="GPT密钥">
          <el-input placeholder="请设置密钥"
                    v-model="form.openAiKey"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          用于在调用OPEN AI对话接口时的安全验证许可证
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleUpdateDallKey">
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
      width="450px"
      @close="isAddVisual=false"
      style="border-radius:  8px;"
  >
    <div class="el-dialog-box">
      <div class="el-dialog-title">
        新增密钥
      </div>
      <el-form :inline="true" label-width="120px" label-position="left" class="el-form">
        <el-form-item label="GPT密钥">
          <el-input placeholder="请设置密钥" v-model="form.openAiKey" class="el-width"/>
        </el-form-item>
        <div class="prompt">
          用于在调用OPEN AI对话接口时的安全验证许可证
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleAddGptKey">
          添加密钥
        </el-button>
      </div>
    </div>
  </el-dialog>

  <!--  额外-->
  <el-dialog
      v-model="isExtraVisual"
      :show-close="false"
      align-center
      width="450px"
      @close="isAddVisual=false"
      style="border-radius:  8px;"
  >
    <div class="el-dialog-box">
      <div class="el-dialog-title">
        额外配置管理
      </div>
      <el-form :inline="true" label-width="120px" label-position="left" class="el-form">
        <el-form-item label="请求链接">
          <el-input placeholder="请设置绘图请求链接" v-model="formExtra.requestUrl" class="el-width"/>
        </el-form-item>
        <div class="prompt">
          用于与openai 绘图服务建立连接使用 如<span> https://api.scvn.cn/v1</span> 。请注意格式
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleSetDallExtraConfig">
          保存配置
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
  width: 280px
}


.container {
  width: 100%;
  height: 100%;

  padding: 10px;
  overflow: hidden;
  background-color: rgb(242, 242, 242);
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
