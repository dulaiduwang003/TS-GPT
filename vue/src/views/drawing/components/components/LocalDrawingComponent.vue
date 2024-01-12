<script setup>

import {defineEmits, defineProps, nextTick, ref, watch} from "vue";
import {RefreshLeft} from "@element-plus/icons-vue";
import {getImageOriginSize, getOssDoMain, scaleImage} from "@/utils/Utils";
import {ElNotification} from "element-plus";


const props = defineProps({
  isVisible: {
    type: Boolean
  },
  image: {
    type: String
  }
});

const emits = defineEmits(['close', 'submit']);

const isVisible = ref(false)

const canvasRef = ref(null)

const imgRef = ref(null)

const ctx = ref(null)

const isDrawing = ref(false)

const lastX = ref(0)

const lastY = ref(0)

const paintbrushSize = ref(10)

const drawMask = (x, y) => {
  ctx.value.beginPath();
  ctx.value.moveTo(lastX.value, lastY.value);
  ctx.value.lineTo(x, y);
  ctx.value.strokeStyle = 'rgba(0, 0, 0, 1)'; // 设置遮罩颜色（黑色）
  ctx.value.lineWidth = paintbrushSize.value; // 设置遮罩宽度
  ctx.value.lineCap = 'round';  // 设置线条末端形状为圆形
  ctx.value.stroke();
  [lastX.value, lastY.value] = [x, y];
}

const clearMask = () => {
  let value = canvasRef.value;
  ctx.value.clearRect(0, 0, value.width, value.height);
}

const drawComplete = async () => {
  const image = getOssDoMain() + props.image

  const canvas = canvasRef.value;
  const ctx = canvas.getContext('2d');
  const imageData = ctx.getImageData(0, 0, canvas.clientWidth, canvas.clientHeight);
  const imageDataContent = imageData.data;
  // 获取原图尺寸
  let imageOriginSize = await getImageOriginSize(image);
  let promise = await new Promise((resolve) => {
    // 修改图像数据中的像素颜色
    for (let i = 0; i < imageDataContent.length; i += 4) {
      // 判断透明度是否小于阈值
      if (imageDataContent[i + 3] < 128) {
        // 将透明部分设置为黑色
        imageDataContent[i] = 0;
        imageDataContent[i + 1] = 0;
        imageDataContent[i + 2] = 0;
        imageDataContent[i + 3] = 255;
      } else {
        // 将线条部分设置为白色
        imageDataContent[i] = 255;
        imageDataContent[i + 1] = 255;
        imageDataContent[i + 2] = 255;
        imageDataContent[i + 3] = 255;
      }
    }
    // 将修改后的图像数据导出为图片格式
    const exportCanvas = document.createElement('canvas');
    //计算canvas大小
    exportCanvas.width = canvas.clientWidth;
    exportCanvas.height = canvas.clientHeight;
    const exportCtx = exportCanvas.getContext('2d');
    exportCtx.putImageData(imageData, 0, 0);
    const finalMask = scaleImage(exportCanvas.toDataURL(), imageOriginSize.width, imageOriginSize.height);
    resolve(finalMask);
  });
  emits('submit', {
    mask: promise,
    images: image,
    width: imageOriginSize.width,
    height: imageOriginSize.height
  })
  ElNotification({
    message: '局部绘制选项已添加', type: 'success',
  });
  emits('close')
}

watch(
    () => props.isVisible,
    (newValue) => {
      isVisible.value = newValue;
      if (isVisible.value) {
        nextTick(() => {
          canvasRef.value.width = imgRef.value.width
          canvasRef.value.height = imgRef.value.height
          ctx.value = canvasRef.value.getContext('2d')
          canvasRef.value.addEventListener('mousedown', function (e) {
            isDrawing.value = true;
            [lastX.value, lastY.value] = [e.offsetX, e.offsetY];
          });
          canvasRef.value.addEventListener('mousemove', function (e) {
            if (!isDrawing.value) return;
            drawMask(e.offsetX, e.offsetY);
          });
          canvasRef.value.addEventListener("mouseup", function () {
            isDrawing.value = false;
          });
        })

      }
    },
    {
      immediate: true,
    }
);


</script>

<template>
  <el-dialog
      v-model="isVisible"
      style="background-color: #ffffff;border-radius: 8px;padding: 20px;width: 600px"
      :show-close="false"
      @close="emits('close')"
  >
    <div class="image-layout">
      <img ref="imgRef" class="image" :src="getOssDoMain()+props.image" alt="">
      <div class="canvas">
        <canvas ref="canvasRef"></canvas>
      </div>
    </div>
    <div class="sd-body">
      <div class="hedge">
        <div class="paintbrush">
          <div>画笔尺寸({{ paintbrushSize }})</div>
          <div class="slider-style">
            <el-slider :min="10" :max="100" v-model="paintbrushSize"/>
          </div>
        </div>

        <div class="reset">
          <el-icon size="20px" @click="clearMask">
            <RefreshLeft/>
          </el-icon>
        </div>
      </div>
      <div>
        <el-button class="btn-submit" @click="drawComplete">
          确定
        </el-button>
      </div>
    </div>

  </el-dialog>

</template>

<style scoped>

.sd-body {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.paintbrush {
  display: flex;
  align-items: center;
  color: #5b5b5b;
}

.slider-style {
  width: 100px;
  margin-left: 15px
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

.image {
  width: 100%;
  background-color: #ededed;
}

.reset {
  padding-top: 7px;
  margin-left: 8px
}

.btn-submit {
  width: 100px;
  height: 30px;
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

.hedge {
  display: flex;
  align-items: center
}

.canvas {
  background-color: transparent;
  position: absolute;
  z-index: 6;
  top: 0;

}

.image-layout {

  position: relative
}

</style>
