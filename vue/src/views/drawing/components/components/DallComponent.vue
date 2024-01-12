<script setup>

import {defineExpose, ref} from "vue";

import {convertUrlToBase64} from "@/utils/Utils";
import {getPromptWordsRandomly} from "@/api/drawing";

//图片大小
const sizeList = ref([
  {
    proportion: '1:1',
    text: '256x256',
    image: 'size-1-1.f9b344b9.svg',
    isSelected: true,
    width: 256,
    height: 256
  },
  {
    proportion: '1:1',
    text: '512x512',
    image: 'size-1-2.62c2da58.svg',
    isSelected: false,
    width: 512,
    height: 512
  },
  {
    proportion: '1:1',
    text: '1024x1024',
    image: 'size-3-4.ba364264.svg',
    isSelected: false,
    width: 1024,
    height: 1024
  }
])

//提交表单
const form = ref({
  image: '',
  prompt: '',
  size: '512x512',
  mask: ''
})

const part = ref({
  width: 0,
  height: 0
})


/**
 * 随机获取提示词
 */
const handleClickRandomPrompt = async () => {
  try {
    const {data} = await getPromptWordsRandomly('DALL');
    form.value.prompt = data.prompt
  } catch (e) {
    console.log(e)
  }

}

/**
 * 接受局部绘图参数
 * @param data
 */
const acceptLocalDrawings = async (data) => {

  form.value.mask = data.mask
  form.value.image = await convertUrlToBase64(data.images);
  part.value.height = data.height
  part.value.width = data.width

}

/**
 * 返回表单参数

 */
const getFormParameter = () => {
  return form.value
}

/**
 * 清空表单参数

 */
const clearFormParameter = () => {
  form.value.image = ''
  form.value.prompt =''
  form.value.mask = ''
}

/**
 * 选择图片大小
 * @param index
 */
const onChangeSize = (index) => {
  sizeList.value.forEach(s => s.isSelected = false)
  sizeList.value[index].isSelected = true
  let {width, height} = sizeList.value[index];
  form.value.size = width + 'x' + height
}

/**
 * 选择参考图
 * @param e
 * @returns {boolean}
 */
// const onChangeFileTemp = (e) => {
//   if (e.raw.type === 'image/jpg' || e.raw.type === 'image/png' || e.raw.type === 'image/jpeg') {
//     if (e.raw.size / 1024 / 1024 > 2) {
//       ElNotification({
//         title: "MISTAKE",
//         message: '图像大小不得超过2MB',
//         type: "warning",
//       });
//       return false
//     }
//     new Promise((resolve, reject) => {
//       const reader = new FileReader();
//       reader.onload = (event) => {
//         form.value.image = event.target.result
//         resolve(e);
//       };
//       reader.onerror = (error) => {
//         reject(error);
//       };
//       reader.readAsDataURL(e.raw);
//     });
//   } else {
//     ElNotification({
//       title: "参考图",
//       message: '请上传正确的图片',
//       type: "error",
//     });
//     return false
//   }
// }

defineExpose(
    {getFormParameter, acceptLocalDrawings,clearFormParameter}
)
</script>

<template>
  <div class="row">
    <div class="row-title">
      <div class="drawing-prompt">
        <span>
          提示词
        </span>
        <img :src="require('../../../../assets/app/random.svg')" alt="" @click="handleClickRandomPrompt">
      </div>
    </div>
    <div class="textarea-top">
      <el-input type="textarea" v-model="form.prompt"/>
    </div>
  </div>

  <div class="row" v-if="form.mask">
    <div class="row-title">
      <div class="model-info fistula">
        <div>
          当前重绘区域
        </div>
        <div>
          <img :src="require('../../../../assets/app/chat/deleted.svg')" class="fistula-delete" alt=""/>
        </div>
      </div>
    </div>
    <div class="part">
      <div>
        <el-image class="part-image" fit="cover"
                  :src="form.mask"/>
      </div>
      <div class="part-context">
        <div class="part-size">
          图片大小 {{ part.width }}x{{ part.height }}
        </div>
        <div class="part-noise">
          重绘噪声强度
        </div>
        <div>
          <el-slider :min="0" :max="100" v-model="form.denoising_strength"/>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="row-title">
      高级
    </div>
    <div class="drawing-vae">
      <span>图片大小</span>
    </div>
    <div class="size-container">
      <div :class="sizeItem.isSelected?'size-model-selected':'size-model'"
           v-for="(sizeItem,index) in sizeList" :key="index" @click="onChangeSize(index)">
        <div>
          <div class="size-logo">
            <img :src="require('../../../../assets/drawing/'+sizeItem.image)" alt=""/>
          </div>
          <div class="size-proportion">
            {{ sizeItem.proportion }}
          </div>
          <div class="size-text">{{ sizeItem.text }}</div>
        </div>
      </div>
    </div>
    <!--    <div class="drawing-vae">-->
    <!--      <span>参考图</span>-->
    <!--    </div>-->
    <!--    <el-upload class="upload-file" :auto-upload="false"-->
    <!--               :on-change="($event)=>onChangeFileTemp($event)"-->
    <!--               :show-file-list="false">-->

    <!--      <el-image fit="cover" class="referenceDiagram" v-if="form.image" :src="form.image" alt=""/>-->
    <!--      <el-icon v-else>-->
    <!--        <Plus/>-->
    <!--      </el-icon>-->
    <!--    </el-upload>-->
  </div>

</template>

<style scoped>
.size-logo img {
  width: 23px;
  height: 23px
}

.size-logo {
  padding-top: 10px;
  padding-bottom: 2px
}

.row {
  border-radius: 8px;
  background-color: white;
  padding: 10px;
  margin-bottom: 10px;
}

.size-model {
  border-radius: 12px;
  margin: 2% auto;
  width: 30%;
  height: 40%;
  background-color: #efefef;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 5px solid #efefef;
}

.size-model-selected {
  border-radius: 12px;
  margin: 5px auto;
  width: 30%;
  height: 40%;
  background-color: #efefef;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 5px solid #9189ff;
}

.row-title {
  font-weight: 550;
  font-size: 14px;
  color: #3c3c3c
}

:deep(.el-textarea__inner) {
  height: 80px;
  background-color: #efefef;
  box-shadow: none !important;
  resize: none !important;
  outline: none !important;
  border-radius: 15px;
}

.drawing-prompt {
  display: flex;
  justify-content: space-between
}

.drawing-prompt img {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.drawing-vae {
  margin-bottom: 5px;
  font-size: 13px;
  margin-top: 10px;
  display: flex;
  align-items: center
}

.drawing-vae span {
  padding-right: 5px
}

.size-proportion {
  padding-bottom: 2px;
  font-size: 14px;
  font-weight: 550;
  color: #a2a2a2
}

.size-text {
  font-size: 11px;

  color: #535353;
  padding-bottom: 10px;
}

.size-container {

  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 10px;
}

.textarea-top {
  margin-top: 5px
}

.model-info {
  display: flex;
  align-items: center
}

.fistula {
  display: flex;
  align-items: center;
  justify-content: space-between
}

.fistula-delete {
  width: 20px;
  height: 20px
}

.part {
  border-radius: 10px;
  border: 1px solid #e5e5e5;
  padding: 10px 10px;
  margin-top: 10px;
  display: flex;
  align-items: flex-start
}

.part-image {
  width: 80px;
  height: 80px;
  background-color: #efefef
}

:deep(.el-slider__button) {
  background-color: #6b5df8;
  border: none;
}


:deep(.el-slider__bar) {
  background-color: #6b5df8;
}

:deep(.el-slider__runway ) {
  background-color: #c5c5c5;
}

:deep(.el-slider__button ) {
  width: 15px;
  height: 15px;
}

.part-context {
  padding-left: 8px
}

.part-size {
  font-size: 14px;
  font-weight: 550
}

.upload-file {

  border-radius: 8px;
  background-color: #efefef;
  width: 100px;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.part-noise {
  font-size: 12px;
  margin-top: 10px;
  color: #767676
}
</style>
