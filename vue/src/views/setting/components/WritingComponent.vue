<script setup>
//快捷键数组
import {defineProps, onMounted, ref} from "vue";
import store from "@/store";



const props = defineProps({
  modelList: {
    type: Array
  }
});

//模型坐标
const writingModelIndex = ref(0)



/**
 * 切换模型
 * @param e
 */
const onChangeAiModel = (e) => {
  //获取原有配置
  let oldData = store.getters.userSetting;
  oldData.writingModelIndex = e
  store.commit("setUserSetting", oldData);
}



onMounted(async () => {
  if (store.getters.userInfo) {
    //读取个人设置信息
    let userSetting = store.getters.userSetting;
    writingModelIndex.value = userSetting.writingModelIndex

  }
})
</script>

<template>
  <div class="appearance-title">
    写作
  </div>
  <div class="appearance-body">
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          驱动模型
          <div class="prompt-label">选择用于驱动智能写作的模型</div>
        </div>
        <div>
          <el-select class="input-width" v-model="writingModelIndex" @change="onChangeAiModel">
            <el-option
                v-for="item in props.modelList"
                :key="item.modelIndex"
                :label="item.modelName"
                :value="item.modelIndex"
            />
          </el-select>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.appearance-title {
  font-size: 14px;
  padding-left: 10px;
  font-weight: 550;
}

.appearance-body {
  background-color: white;
  margin-top: 10px;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 30px;
}

.input-width {
  width: 240px;
}

.div-width {
  width: 100%
}

.div-row {
  padding-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  font-size: 14px;
  border-bottom: 1px solid #f3f3f3;
  margin-bottom: 15px;
  color: #8b8b8b;
}

.input-textarea {
  align-items: flex-start
}

:deep(.el-textarea__inner) {
  height: 130px;
  background-color: #F2F2F2;
  box-shadow: none !important;
  resize: none !important;
  outline: none !important;

}

:deep(.el-input__wrapper) {
  box-shadow: none;
  font-weight: 550;
}

.bot-upload {
  border-radius: 100%;
  width: 45px;
  height: 45px
}

:deep(.el-input__inner) {
  text-align: right;
}

.prompt-label {
  width: 340px;
  font-size: 12px;
  color: #9d9d9d;
  padding-top: 8px
}
.prompt-title{
  color: #5b5b5b;
}
</style>
