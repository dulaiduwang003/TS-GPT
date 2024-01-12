<script setup>

import {onMounted, ref} from "vue";
import {ElLoading, ElNotification} from "element-plus";
import {Plus} from "@element-plus/icons-vue";
import {
  reqAddMicroApp,
  reqDeleteMicroApp,
  reqGetMicroAppPage,
  reqGetMicroCategoryList,
  reqUpdateMicroApp
} from "@/api/microManagement";
import store from "@/store";

const prompt = ref('')

const promptTemp = ref('')

const total = ref(0);

const tableData = ref([])

const microCategoryList =ref([])

const pageNum = ref(1)

const isUpdateVisual = ref(false)

const isAddVisual = ref(false)

const form = ref({
  microAppId: undefined,
  icon: '',
  title: '',
  introduce: '',
  chineseIssue: '',
  englishIssue: '',
  chineseAnswer: '',
  englishAnswer: '',
  microCategoryId: undefined
})

const openUpdateDialog = (data) => {
  form.value.microCategoryId = data.microCategoryId
  form.value.icon = data.icon
  form.value.microAppId = data.microAppId
  form.value.title = data.title
  form.value.introduce = data.introduce
  form.value.chineseIssue = data.chineseIssue
  form.value.englishIssue = data.englishIssue
  form.value.chineseAnswer = data.chineseAnswer
  form.value.englishAnswer = data.englishAnswer
  isUpdateVisual.value = true
}

/**
 * 修改
 * @returns {Promise<void>}
 */
const handleUpdateMicroApp = async () => {
  if (!form.value.title) {
    ElNotification({message: '预设词标题不能为空', type: "error",});
    return
  }
  if (!form.value.introduce) {
    ElNotification({message: '预设词介绍不能为空', type: "error",});
    return
  }
  if (!form.value.chineseIssue) {
    ElNotification({message: '中文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.englishIssue) {
    ElNotification({message: '英文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.chineseAnswer) {
    ElNotification({message: '中文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.englishAnswer) {
    ElNotification({message: '英文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.microCategoryId) {
    ElNotification({message: '请选择预设词所属类目', type: "error",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqUpdateMicroApp(form.value);
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
 * 新增预设词
 * @returns {Promise<void>}
 */
const handleAddMicroApp = async () => {
  if (!form.value.title) {
    ElNotification({message: '预设词标题不能为空', type: "error",});
    return
  }
  if (!form.value.introduce) {
    ElNotification({message: '预设词介绍不能为空', type: "error",});
    return
  }
  if (!form.value.chineseIssue) {
    ElNotification({message: '中文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.englishIssue) {
    ElNotification({message: '英文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.chineseAnswer) {
    ElNotification({message: '中文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.englishAnswer) {
    ElNotification({message: '英文提问模板不能为空', type: "error",});
    return
  }
  if (!form.value.microCategoryId) {
    ElNotification({message: '请选择预设词所属类目', type: "error",});
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '请稍等'})
  try {
    await reqAddMicroApp(form.value);
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
 * 删除模型
 * @param id
 * @returns {Promise<void>}
 */
const handleDeleteGptModel = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteMicroApp({microAppId: id});
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
    const {data} = await reqGetMicroAppPage(pageNum, prompt);
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

const handleGetMicroCategory = async () => {
  try {
    const {data} = await reqGetMicroCategoryList();
    microCategoryList.value = data
  } catch (e) {
    console.log(e)
  }
}

onMounted(() => {
  if (store.getters.userInfo){
    init(pageNum.value, prompt.value)
    handleGetMicroCategory()
  }

})

</script>

<template>
  <div class="container">
    <div class="body">
      <div class="title">管理预设词库</div>
      <div class="condition">
        <el-input v-model="promptTemp" placeholder="预设词标题" class="input-height"/>
        <el-button type="primary" class="input-button" @click="search">查找模型</el-button>
      </div>

      <!--      操作-->
      <div style="margin-top: 20px">
        <el-button type="primary"
                   class="input-button add-button"
                   @click="isAddVisual=true"
                   :icon="Plus">新增预设词
        </el-button>
      </div>
      <el-table :data="tableData" stripe class="el-table"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="microAppId" label="序号"/>
        <el-table-column prop="title" label="预设词标题" width="200"/>
        <el-table-column prop="introduce" label="介绍" width="300"/>
        <el-table-column prop="categoryName" label="所属类别" width="150">
          <template #default="scope">
            <div class="column-flex">
              <div class="type-default">
                {{ scope.row.categoryName }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="icon" label="emoji图标" width="200"/>
        <el-table-column prop="chineseIssue" label="中文问题模板" width="200"/>
        <el-table-column prop="englishIssue" label="英文问题模板" width="200"/>
        <el-table-column prop="chineseAnswer" label="中文回答模板" width="200"/>
        <el-table-column prop="englishAnswer" label="英文回答模板" width="200"/>
        <el-table-column prop="createdTime" label="创建时间" width="200px"/>
        <el-table-column prop="updateTime" label="修改时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">
            <el-button link type="primary" size="small" style="color: #993BFF" @click="openUpdateDialog(scope.row)">
              修改
            </el-button>
            <el-button link type="primary" size="small" style="color: #ff3b3b"
                       @click="handleDeleteGptModel(scope.row.microAppId)">
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
        修改预设词
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="预设词标题">
          <el-input placeholder="请设置预设词标题"
                    v-model="form.title"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          预设词标题 如 <span>让GPT成为一个小红书文案生成器</span>
        </div>
        <el-form-item label="emoji图标">
          <el-input placeholder="请设置emoji图标"
                    v-model="form.icon"
                    maxlength="1"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          设置一个emoji表情作为图标 <span>可以使用微软输入法中的emoji表情菜单</span>
        </div>
        <el-form-item label="预设词介绍">
          <el-input placeholder="预设词介绍"
                    v-model="form.introduce"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          请用一段简单的话来介绍这个预设词的作用
        </div>
        <el-form-item label="中文提问模板">
          <el-input placeholder="请设置中文提问模板"
                    v-model="form.chineseIssue"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 我想让你充当诗词翻译器
        </div>
        <el-form-item label="英文提问模板">
          <el-input placeholder="请设置英文提问模板"
                    v-model="form.englishIssue"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 <span>i want you to act as a poetry translator</span>(我想让你充当诗词翻译器)
        </div>
        <el-form-item label="中文回答模板">
          <el-input placeholder="请设置中文回答模板"
                    v-model="form.chineseAnswer"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 <span>好的!我将充当诗词翻译器,请提供你的诗句</span>
        </div>
        <el-form-item label="英文回答模板">
          <el-input placeholder="请设置英文回答模板"
                    v-model="form.englishAnswer"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 <span>Okay! I'm going to act as a poetry translator, please provide me with your verses</span>(好的!我将充当诗词翻译器,请提供你的诗句)
        </div>
        <el-form-item label="预设词所属类型">
          <el-select
              v-model="form.microCategoryId"
              placeholder="请设置预设词所属类型"
              clearable
              style="width: 240px"
          >
            <el-option :label="item.categoryName" :value="item.microCategoryId"  v-for="(item,index) in microCategoryList" :key="index"/>

          </el-select>
        </el-form-item>
        <div class="prompt">
          这个选项将决定预设词归于哪个类目下
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleUpdateMicroApp">
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
        新增预设词
      </div>
      <el-form :inline="true" label-width="220px" label-position="left" class="el-form">
        <el-form-item label="预设词标题">
          <el-input placeholder="请设置预设词标题"
                    v-model="form.title"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          预设标题 如 <span>让GPT成为一个小红书文案生成器</span>
        </div>
        <el-form-item label="emoji图标">
          <el-input placeholder="请设置emoji图标"
                    v-model="form.icon"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          设置一个emoji表情作为图标 <span>可以使用微软输入法中的emoji表情菜单</span>
        </div>
        <el-form-item label="预设词介绍">
          <el-input placeholder="预设词介绍"
                    v-model="form.introduce"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          请用一段简单的话来介绍这个预设词的作用
        </div>
        <el-form-item label="中文提问模板">
          <el-input placeholder="请设置中文提问模板"
                    v-model="form.chineseIssue"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 我想让你充当诗词翻译器
        </div>
        <el-form-item label="英文提问模板">
          <el-input placeholder="请设置英文提问模板"
                    v-model="form.englishIssue"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 <span>i want you to act as a poetry translator</span>(我想让你充当诗词翻译器)
        </div>
        <el-form-item label="中文回答模板">
          <el-input placeholder="请设置中文回答模板"
                    v-model="form.chineseAnswer"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 <span>好的!我将充当诗词翻译器,请提供你的诗句</span>
        </div>
        <el-form-item label="英文回答模板">
          <el-input placeholder="请设置英文回答模板"
                    v-model="form.englishAnswer"
                    type="textarea"
                    class="el-width"/>
        </el-form-item>
        <div class="prompt">
          比如 <span>Okay! I'm going to act as a poetry translator, please provide me with your verses</span>(好的!我将充当诗词翻译器,请提供你的诗句)
        </div>
        <el-form-item label="预设词所属类型">
          <el-select
              v-model="form.microCategoryId"
              placeholder="请设置预设词所属类型"
              clearable
              style="width: 240px"
          >
            <el-option :label="item.categoryName" :value="item.microCategoryId"  v-for="(item,index) in microCategoryList" :key="index"/>

          </el-select>
        </el-form-item>
        <div class="prompt">
          这个选项将决定预设词归于哪个类目下
        </div>
      </el-form>
      <div class="update-div">

        <el-button type="primary" class="input-button upload-btn" @click="handleAddMicroApp">
          新增预设词
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
