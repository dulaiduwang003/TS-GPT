<script setup>

import {onMounted, ref} from "vue";
import {ElLoading, ElNotification} from "element-plus";
import {Plus} from "@element-plus/icons-vue";

import store from "@/store";
import {
  reqAddDrawingPrompt,
  reqDeleteDrawingPrompt,
  reqGetDrawingPromptPage,
  reqUpdateDrawingPrompt
} from "@/api/drawingManagement";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])


const pageNum = ref(1)

const isUpdateVisual = ref(false)

const isAddVisual = ref(false)

const form = ref({
  drawingPromptId: undefined,
  prompt: '',
  type: ''
})

const openUpdateDialog = (data) => {
  form.value.drawingPromptId = data.drawingPromptId
  form.value.prompt = data.prompt
  form.value.type = data.type

  isUpdateVisual.value = true
}

/**
 * 修改
 * @returns {Promise<void>}
 */
const handleUpdateDrawingPrompt = async () => {
  if (!form.value.prompt) {
    ElNotification({message: '提示词不能为空', type: "error",});
    return
  }
  if (!form.value.type) {
    ElNotification({message: '类型不能为空', type: "error",});
    return
  }

  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqUpdateDrawingPrompt(form.value);
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
 * 新增提示词
 * @returns {Promise<void>}
 */
const handleAddDrawingPrompt = async () => {
  if (!form.value.prompt) {
    ElNotification({message: '提示词不能为空', type: "error",});
    return
  }
  if (!form.value.type) {
    ElNotification({message: '类型不能为空', type: "error",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqAddDrawingPrompt(form.value);
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
 * 删除
 * @param id
 * @returns {Promise<void>}
 */
const handleDeleteDrawingPrompt = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteDrawingPrompt({drawingPromptId: id});
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
    const {data} = await reqGetDrawingPromptPage(pageNum, prompt);
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
  if (store.getters.userInfo){
    init(pageNum.value, prompt.value)
  }


})

</script>

<template>
  <div class="container">
    <div class="body">
      <div class="title">管理绘图提示词</div>
      <div class="condition">
        <el-input v-model="promptTemp" placeholder="提示词内容" class="input-height"/>
        <el-button type="primary" class="input-button" @click="search">搜索提示词</el-button>
      </div>

      <!--      操作-->
      <div style="margin-top: 20px">
        <el-button type="primary"
                   class="input-button add-button"
                   @click="isAddVisual=true"
                   :icon="Plus">新增绘图提示词
        </el-button>
      </div>
      <el-table :data="tableData" stripe class="el-table"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="drawingPromptId" label="序号"/>
        <el-table-column prop="prompt" label="提示词" width="200"/>
        <el-table-column prop="type" label="类型" width="200"/>
        <el-table-column prop="createdTime" label="创建时间" width="200px"/>
        <el-table-column prop="updateTime" label="修改时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">
            <el-button link type="primary" size="small" style="color: #993BFF" @click="openUpdateDialog(scope.row)">
              修改
            </el-button>
            <el-button link type="primary" size="small" style="color: #ff3b3b"
                       @click="handleDeleteDrawingPrompt(scope.row.drawingPromptId)">
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
        修改提示词
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="提示词">
          <el-input placeholder="请设置提示词"
                    v-model="form.prompt"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          将作为绘图类功能中的随机生成提示词 如 <span>秋天,下雨....</span>
        </div>
        <el-form-item label="绘图类型">
          <el-select
              v-model="form.type"
              placeholder="请设置提示词类型"
              clearable
              style="width: 240px"
          >
            <el-option label="DALL" value="DALL"/>
            <el-option label="SD" value="SD"/>
          </el-select>
        </el-form-item>
        <div class="prompt">
          设置为DALL 会让该提示词在用户使用<span>DALL绘图</span> 点击随机获取时有几率获取改类型下的提示词, 以此类推
        </div>

      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleUpdateDrawingPrompt">
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
        新增绘图提示词
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="提示词">
          <el-input placeholder="请设置提示词"
                    v-model="form.prompt"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          将作为绘图类功能中的随机生成提示词 如 <span>秋天,下雨....</span>
        </div>
        <el-form-item label="绘图类型">
          <el-select
              v-model="form.type"
              placeholder="请设置提示词类型"
              clearable
              style="width: 240px"
          >
            <el-option label="DALL" value="DALL"/>
            <el-option label="SD" value="SD"/>
          </el-select>
        </el-form-item>
        <div class="prompt">
          设置为DALL 会让该提示词在用户使用<span>DALL绘图</span> 点击随机获取时有几率获取改类型下的提示词, 以此类推
        </div>

      </el-form>
      <div class="update-div">
        <el-button type="primary" class="input-button upload-btn" @click="handleAddDrawingPrompt">
          新增提示词
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
