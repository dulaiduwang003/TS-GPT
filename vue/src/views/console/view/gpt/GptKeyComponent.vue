<script setup>

import {onMounted, ref} from "vue";
import {
  reqAddGptKey,
  reqDeleteGptKey,
  reqGetGptExtra,
  reqGetGptKeyPage,
  reqLoadGptStructure,
  reqSetGptExtra,
  reqUpdateGptKey
} from "@/api/gptManagement";
import {ElLoading, ElNotification} from "element-plus";
import {MessageBox, Plus, Sugar} from "@element-plus/icons-vue";
import store from "@/store";
import router from "@/router";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])

const pageNum = ref(1)

const isUpdateVisual = ref(false)

const isAddVisual = ref(false)

const isExtraVisual = ref(false)

const form = ref({
  gptKeyId: undefined,
  isSeniorModel: '',
  openAiKey: ''
})

const formExtra = ref({
  requestUrl: '',
  picturePreview: {
    model: '',
    max_tokens: undefined,
    detail: ''
  }
})

const openUpdateDialog = (data) => {
  form.value.gptKeyId = data.gptKeyId
  form.value.isSeniorModel = data.isSeniorModel
  form.value.openAiKey = data.openAiKey
  isUpdateVisual.value = true
}

/**
 * 修改密钥
 * @returns {Promise<void>}
 */
const handleUpdateGptKey = async () => {
  if (!form.value.openAiKey) {
    ElNotification({message: 'GPT密钥不能为空', type: "info",});
    return
  }
  if (form.value.isSeniorModel !== false && form.value.isSeniorModel !== true && form.value.isSeniorModel) {
    ElNotification({message: '作用域不能为空', type: "info",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqUpdateGptKey(form.value);
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
  if (form.value.isSeniorModel !== false && form.value.isSeniorModel !== true && form.value.isSeniorModel) {
    ElNotification({message: '作用域不能为空', type: "info",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqAddGptKey(form.value);
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
 * 删除密钥
 * @param id
 * @returns {Promise<void>}
 */
const handleDeleteGptKey = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteGptKey({gptKeyId: id});
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
    const {data} = await reqGetGptKeyPage(pageNum, prompt);
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

const getGptExtraConfig = async () => {
  try {
    const {data} = await reqGetGptExtra();
    if (data) {
      formExtra.value = data
    }
  } catch (e) {
    ElNotification({
      message: e.msg,
      type: "error",
    });
  }
}

const handleSetGptExtraConfig = async () => {
  if (!formExtra.value.requestUrl) {
    ElNotification({message: '请求地址不能为空', type: "error"});
    return
  }
  if (!formExtra.value.picturePreview.max_tokens) {
    ElNotification({message: '最大上下文不能为空', type: "error"});
    return
  }
  if (!formExtra.value.picturePreview.detail) {
    ElNotification({message: '识别清晰度不能为空', type: "error"});
    return
  }
  if (!formExtra.value.picturePreview.model) {
    ElNotification({message: '识别模型不能为空', type: "error"});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '正在保存配置'})
  try {
    await reqSetGptExtra(formExtra.value);
    ElNotification({
      message: '配置保存成功',
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


onMounted(() => {
  let userInfo = store.getters.userInfo;
  if (userInfo) {
    const {type} = userInfo;
    if (type !== "ADMIN") {
      router.push({
        path: "/#"
      });
      return
    }
  } else {
    router.push({
      path: "/#"
    });
    return
  }
  init(pageNum.value, prompt.value)
  getGptExtraConfig()

})

</script>

<template>
  <div class="container">
    <div class="body">
      <div class="title">管理对话密钥</div>
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
                   @click="handleLoadGptStructure"
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
        <el-table-column prop="gptKeyId" label="序号"/>
        <el-table-column prop="openAiKey" label="密钥" width="500"/>
        <el-table-column prop="isSeniorModel" label="密钥作用域">
          <template #default="scope">
            <div class="column-flex">
              <div :class="scope.row.isSeniorModel?'type-default':'type-active'">
                {{ scope.row.isSeniorModel ? '高级模型' : '低级模型' }}
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
                       @click="handleDeleteGptKey(scope.row.gptKeyId)">
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
          <el-input placeholder="请设置密钥" v-model="form.openAiKey" class="el-width"/>
        </el-form-item>
        <div class="prompt">
          用于在调用OPEN AI对话接口时的安全验证许可证
        </div>
        <el-form-item label="作用域">
          <el-select
              v-model="form.isSeniorModel"
              placeholder="请设置模型作用域"
              clearable
              style="width: 280px"
          >
            <el-option label="高级模型" :value="true"/>
            <el-option label="普通模型" :value="false"/>
          </el-select>
        </el-form-item>
        <div class="prompt">
          指定密钥只允许哪种类型的模型使用
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
          <el-input placeholder="请设置密钥"
                    v-model="form.openAiKey"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          用于在调用OPEN AI对话接口时的安全验证许可证
        </div>
        <el-form-item label="作用域">
          <el-select
              v-model="form.isSeniorModel"
              placeholder="请设置模型作用域"
              clearable
              style="width: 280px"
          >
            <el-option label="高级模型" :value="true"/>
            <el-option label="普通模型" :value="false"/>
          </el-select>
        </el-form-item>
        <div class="prompt">
          指定密钥只允许哪种类型的模型使用
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleAddGptKey">
          添加密钥
        </el-button>
      </div>
    </div>
  </el-dialog>

  <!--  gpt额外配置-->
  <el-dialog
      v-model="isExtraVisual"
      :show-close="false"
      align-center
      width="570px"
      @close="isExtraVisual=false"
      style="border-radius:  8px;"
  >
    <div class="el-dialog-box">
      <div class="el-dialog-title">
        GPT请求代理地址
      </div>
      <el-form :inline="true" label-width="240px" label-position="left" class="el-form">
        <el-form-item label="请求地址">
          <el-input placeholder="请设置GPT请求地址"
                    v-model="formExtra.requestUrl"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          调用OPEN AI的请求链接 如 <span> https://api.scvn.cn/v1</span> 。请注意格式
        </div>
        <div class="el-dialog-title">
          图片识别模型配置
        </div>
        <el-form :inline="true" label-width="240px" label-position="left" class="el-form">
          <el-form-item label="模型名称">
            <el-input placeholder="请设置图片识别模型名称"
                      v-model="formExtra.picturePreview.model"
                      class="el-width"/>
          </el-form-item>
          <div class="prompt">
            指定对话页面中的图片识别使用哪个模型。如 <span>gpt-4-vision-preview</span>
          </div>
          <el-form-item label="图像识别质量">
            <el-select
                v-model="formExtra.picturePreview.detail"
                placeholder="请设置图像识别质量"
                clearable
                style="width: 280px"
            >
              <el-option label="low" value="low"/>
              <el-option label="height" value="height"/>
            </el-select>
          </el-form-item>
          <div class="prompt">
            <span>低保真或高保真图片</span>
            <p>设置为low将收到低分辨率 512 x 512 版本的图片，并以 65 个token来处理图片。这允许 API
              返回更快的响应，并为不需要高细节的用例使用更少的输入令牌</p>
            <p>设置为height允许模型查看低分辨率图片，然后根据输入图片大小将输入图片创建为 512px
              正方形的详细裁剪。每个详细的作物都使用两倍的代币预算（65 个token），总共 129 个token</p>
          </div>
          <el-form-item label="max_tokens(最大生成文本数)">
            <el-input placeholder="请设置max_tokens(最大生成文本数)"
                      v-model="formExtra.picturePreview.max_tokens"
                      type="number"
                      class="el-width"/>
          </el-form-item>
          <div class="prompt">
            参数指定模型可以生成的最大可以生成的文本数量
          </div>
        </el-form>

      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleSetGptExtraConfig">
          保存设置
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
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
