<script setup>

import SdComponent from "@/views/drawing/components/components/SdComponent.vue";
import DallComponent from "@/views/drawing/components/components/DallComponent.vue";
import {defineEmits, defineExpose, ref} from "vue";
import {ElLoading, ElNotification} from "element-plus";
import {reqPushDallGenerateTask, reqPushSdGenerateTask} from "@/api/drawing";

const emits = defineEmits(['handle-new-task']);

const sdRef = ref(null)

const dallRef = ref(null)

const active = ref('sd')

/**
 * 提交生成任务
 */
const submitGenerateTask = async () => {
  if (active.value === 'sd') {
    await handleSdTask()
  } else {
    await handleDallTask()
  }
}

const handleDallTask = async () => {
  let formParameter = dallRef.value.getFormParameter();
  if (!formParameter.prompt) {

    ElNotification({
      message: '提示词不能为空', type: 'error',
    });
    return
  }
  let service = ElLoading.service({fullscreen: true, text: '正在提交任务'});
  try {
    const {data} = await reqPushDallGenerateTask(formParameter);
    emits("handle-new-task", {
      taskId: data,
      prompt: formParameter.prompt,
      drawingType: 'DALL'
    })
    //清除表单数据
    dallRef.value.clearFormParameter()

    ElNotification({
      message: '绘图任务已提交', type: 'success',
    });
  } catch (e) {
    ElNotification({
      message: e.msg, type: 'error',
    });
  } finally {
    service.close()
  }

}

/**
 * 处理SD
 * @returns {Promise<void>}
 */
const handleSdTask = async () => {
  let formParameter = sdRef.value.getFormParameter();
  if (!formParameter.prompt) {

    ElNotification({
      message: '提示词不能为空', type: 'warning',
    });
    return
  }
  //换算噪声比例
  let v = formParameter.denoising_strength;
  formParameter.denoising_strength = v / 100
  let service = ElLoading.service({fullscreen: true, text: '正在提交任务'});
  try {
    const {data} = await reqPushSdGenerateTask(formParameter);
    emits("handle-new-task", {
      taskId: data,
      prompt: formParameter.prompt,
      drawingType: 'SD'
    })
    //清除表单数据
    sdRef.value.clearFormParameter()
    ElNotification({
      message: '绘图任务已提交', type: 'success',
    });
  } catch (e) {
    ElNotification({
      message: e.msg, type: 'error',
    });
  } finally {
    service.close()
  }
}

/**
 * 传递局部绘图参数
 * @param data
 */
const acceptSdLocalDrawings = (data) => {
  sdRef.value.acceptLocalDrawings(data)
}

/**
 * 传递局部绘图参数
 * @param data
 */
const acceptDallLocalDrawings = (data) => {
  dallRef.value.acceptLocalDrawings(data)
}


defineExpose(
    {acceptSdLocalDrawings, acceptDallLocalDrawings}
)
</script>


<template>

  <div class="container">
    <el-tabs class="tabs" v-model="active">
      <el-tab-pane label="Stable Diffusion 绘图" name="sd">
        <div class="scroll" ref="scrollRef">
          <sd-component ref="sdRef"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Dall 绘图" name="dall">
        <div class="scroll" ref="scrollRef">
          <dall-component ref="dallRef"/>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="tabs-row">
      <span>
          每个用户等待队列为3
      </span>
      <el-button round class="btn-submit" @click="submitGenerateTask">立即生成</el-button>
    </div>

  </div>

</template>

<style scoped>

.tabs {
  padding: 10px;
}

.btn-submit {
  width: 200px;
  height: 40px;
  color: white;
  background-color: #7365FF;
  border: none
}

.btn-submit:hover,
.btn-submit:focus,
.btn-submit:active {
  background-color: #7365FF;
  outline: 0;
  color: white;
}

.tabs-row {
  padding: 0 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #efefef;
  width: 100%;
  z-index: 1;
  position: absolute;
  bottom: 0;
  height: 60px;

}

.tabs-row span {
  font-size: 13px;
  color: #6b6b6b
}

::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background: #efefef;
}

::-webkit-scrollbar-track {
  /* box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); */
  border-radius: 0;
  background: #efefef;
  display: block;
}

:deep(.el-tabs__item:hover) {
  color: #8a80ec;
}

:deep(.el-tabs__item.is-active) {
  color: #8a80ec;
  font-weight: 600;
}

:deep(.el-tabs__active-bar) {
  background-color: #8a80ec;
}

.container {
  position: relative;
  height: 100%;
  width: 28%;
  background-color: #efefef;
  overflow: hidden;
  animation: slideEase 0.2s ease-in-out forwards;
}

.scroll {
  height: 100vh;
  scroll-behavior: smooth;
  overflow: auto;
  box-sizing: border-box;
  padding-bottom: 145px;
}


@keyframes slideEase {
  0% {
    transform: translateX(-1000px);
  }
  100% {
    transform: translateX(0);
  }
}
</style>
