<script setup>

import {onMounted, ref} from "vue";
import {
  reqAddGptModel,
  reqDeleteGptModel,
  reqGetGptModelPage,
  reqLoadGptStructure,
  reqUpdateGptModel
} from "@/api/gptManagement";
import {ElLoading, ElNotification} from "element-plus";
import {MessageBox, Plus} from "@element-plus/icons-vue";
import store from "@/store";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])

const pageNum = ref(1)

const isUpdateVisual = ref(false)

const isAddVisual = ref(false)

const form = ref({
  gptModelId: undefined,
  isSeniorModel: '',
  modelName: '',
  topP: undefined,
  maxTokens: undefined,
  temperature: undefined
})

const openUpdateDialog = (data) => {
  form.value.maxTokens = data.maxTokens
  form.value.gptModelId = data.gptModelId
  form.value.isSeniorModel = data.isSeniorModel
  form.value.modelName = data.modelName
  form.value.topP = data.topP
  form.value.temperature = data.temperature
  isUpdateVisual.value = true
}

/**
 * 修改密钥
 * @returns {Promise<void>}
 */
const handleUpdateGptKey = async () => {
  if (!form.value.modelName) {
    ElNotification({message: '模型名称不能为空', type: "info",});
    return
  }
  if (form.value.isSeniorModel !== false && form.value.isSeniorModel !== true && form.value.isSeniorModel) {
    ElNotification({message: '模型类型不能为空', type: "info",});
    return
  }
  if (!form.value.topP) {
    ElNotification({message: '生成文本多样性不能为空', type: "info",});
    return
  }
  if (!form.value.maxTokens) {
    ElNotification({message: '最大生成文本数不能为空', type: "info",});
    return
  }
  if (!form.value.temperature) {
    ElNotification({message: '随机程度不能为空', type: "info",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqUpdateGptModel(form.value);
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
 * 新增模型
 * @returns {Promise<void>}
 */
const handleAddGptModel = async () => {
  if (!form.value.modelName) {
    ElNotification({message: '模型名称不能为空', type: "info",});
    return
  }
  if (form.value.isSeniorModel !== false && form.value.isSeniorModel !== true && form.value.isSeniorModel) {
    ElNotification({message: '模型类型不能为空', type: "info",});
    return
  }
  if (!form.value.topP) {
    ElNotification({message: '生成文本多样性不能为空', type: "info",});
    return
  }
  if (!form.value.maxTokens) {
    ElNotification({message: '最大生成文本数不能为空', type: "info",});
    return
  }
  if (!form.value.temperature) {
    ElNotification({message: '随机程度不能为空', type: "info",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqAddGptModel(form.value);
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
 *  更新GPT配置
 * @returns {Promise<void>}
 */
const handleLoadGptStructure = async () => {
  let service = ElLoading.service({fullscreen: true, text: '正在处理中'})
  try {
    await reqLoadGptStructure();
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
 * 删除模型
 * @param id
 * @returns {Promise<void>}
 */
const handleDeleteGptModel = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteGptModel({gptModelId: id});
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
    const {data} = await reqGetGptModelPage(pageNum, prompt);
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
  if (store.getters.userInfo) {
    init(pageNum.value, prompt.value)
  }
})

</script>

<template>
  <div class="container">
    <div class="body">
      <div class="title">管理对话模型</div>
      <div class="condition">
        <el-input v-model="promptTemp" placeholder="模型名称" class="input-height"/>
        <el-button type="primary" class="input-button" @click="search">查找模型</el-button>
      </div>

      <!--      操作-->
      <div style="margin-top: 20px">
        <el-button type="primary"
                   class="input-button add-button"
                   @click="isAddVisual=true"
                   :icon="Plus">新增模型
        </el-button>
        <el-button type="primary"
                   class="input-button apply-button"
                   @click="handleLoadGptStructure"
                   :icon="MessageBox">载入到配置池中
        </el-button>
      </div>
      <el-table :data="tableData" stripe class="el-table"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="gptModelId" label="序号"/>
        <el-table-column prop="modelName" label="模型名称" width="500"/>
        <el-table-column prop="isSeniorModel" label="模型类型">
          <template #default="scope">
            <div class="column-flex">
              <div :class="scope.row.isSeniorModel?'type-default':'type-active'">
                {{ scope.row.isSeniorModel ? '高级模型' : '低级模型' }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="topP" label="top_p(文本多样性)" width="200px"/>
        <el-table-column prop="maxTokens" label="max_tokens(最大生成文字数)" width="300px"/>
        <el-table-column prop="temperature" label="temperature(随机程度)" width="200px"/>
        <el-table-column prop="createdTime" label="创建时间" width="200px"/>
        <el-table-column prop="updateTime" label="修改时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">
            <el-button link type="primary" size="small" style="color: #993BFF" @click="openUpdateDialog(scope.row)">
              修改
            </el-button>
            <el-button link type="primary" size="small" style="color: #ff3b3b"
                       @click="handleDeleteGptModel(scope.row.gptModelId)">
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
        修改模型
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="模型名称">
          <el-input placeholder="请设置模型名称"
                    v-model="form.modelName"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          创建一个对话模型 如 <span>gpt-3.5-turbo-16k-0613</span>
        </div>
        <el-form-item label="top_p">
          <el-input placeholder="请设置top_p(文本多样性)"
                    v-model="form.topP"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          top_p应该设置在<span>0.5到0.95之间</span>，这样可以保证生成的内容既有一定的多样性，又不会太过随机或错误。如果top_p太高，比如接近1，那么模型会从所有可能的token中选择一个，这样可能会生成一些不合逻辑或不相关的内容。如果top_p太低，比如接近0，那么模型会只从最有可能的一个或几个token中选择一个，这样可能会生成一些重复或无聊的内容。你可以根据你的任务和数据来调整top_p的值
        </div>
        <el-form-item label="maxTokens(最大生成文字数)">
          <el-input placeholder="请设置maxTokens(最大生成文字数)"
                    v-model="form.maxTokens"
                    type="number"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          参数指定模型可以生成的最大可以生成的文本数量
        </div>
        <el-form-item label="temperature(随机程度)">
          <el-input placeholder="请设置temperature(随机程度)"
                    type="number"
                    v-model="form.temperature"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          可以使得回答更加人性化。通常temperature参数值在0-1之间，当<span>temperature=0</span>时，结果是保守的，会更符合语法，但也缺乏创意。当<span>temperature=1</span>时，它的回答则会更加随机和多元化。所以我们在输出一些需要创意的内容，比如故事，小说的时候
        </div>
        <el-form-item label="模型类型">
          <el-select
              v-model="form.isSeniorModel"
              placeholder="请设置模型类型"
              clearable
              style="width: 240px"
          >
            <el-option label="高级模型" :value="true"/>
            <el-option label="普通模型" :value="false"/>
          </el-select>
        </el-form-item>
        <div class="prompt">
          <span>如果是高级模型</span> 则在使用该模型对话时,只会从 "高级"密钥池中获取访问密钥(OPEN AI KEY).以此类推
        </div>

      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleUpdateGptKey">
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
        新增模型
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="模型名称">
          <el-input placeholder="请设置模型名称"
                    v-model="form.modelName"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          创建一个对话模型 如 <span>gpt-3.5-turbo-16k-0613</span>
        </div>
        <el-form-item label="top_p">
          <el-input placeholder="请设置top_p(文本多样性)"
                    v-model="form.topP"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          top_p应该设置在<span>0.5到0.95之间</span>，这样可以保证生成的内容既有一定的多样性，又不会太过随机或错误。如果top_p太高，比如接近1，那么模型会从所有可能的token中选择一个，这样可能会生成一些不合逻辑或不相关的内容。如果top_p太低，比如接近0，那么模型会只从最有可能的一个或几个token中选择一个，这样可能会生成一些重复或无聊的内容。你可以根据你的任务和数据来调整top_p的值
        </div>
        <el-form-item label="maxTokens(最大生成文字数)">
          <el-input placeholder="请设置maxTokens(最大生成文字数)"
                    v-model="form.maxTokens"
                    type="number"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          参数指定模型可以生成的最大可以生成的文本数量
        </div>
        <el-form-item label="temperature(随机程度)">
          <el-input placeholder="请设置temperature(随机程度)"
                    type="number"
                    v-model="form.temperature"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          可以使得回答更加人性化。通常temperature参数值在0-1之间，当<span>temperature=0</span>时，结果是保守的，会更符合语法，但也缺乏创意。当<span>temperature=1</span>时，它的回答则会更加随机和多元化。所以我们在输出一些需要创意的内容，比如故事，小说的时候
        </div>
        <el-form-item label="模型类型">
          <el-select
              v-model="form.isSeniorModel"
              placeholder="请设置模型类型"
              clearable
              style="width: 240px"
          >
            <el-option label="高级模型" :value="true"/>
            <el-option label="普通模型" :value="false"/>
          </el-select>
        </el-form-item>
        <div class="prompt">
          <span>如果是高级模型</span> 则在使用该模型对话时,只会从 "高级"密钥池中获取访问密钥(OPEN AI KEY).以此类推
        </div>

      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleAddGptModel">
          新增模型
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
