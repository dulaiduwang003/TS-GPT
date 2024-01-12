<script setup>
import {defineEmits, defineProps} from "vue";
import {getOssDoMain} from "@/utils/Utils";
import {EditPen} from "@element-plus/icons-vue";

const props = defineProps({
  image: {
    type: String
  },
  status: {
    type: String,
    default: "PENDING"
  },
  progress: {
    type: Number,
    default: 0
  },
  type: {
    type: String
  }
});
const emits = defineEmits(['partial-repaint']);
</script>

<template>
  <div class="image-container">
    <!--    列队中-->
    <div class="image-size await-img-default" v-if="props.status==='PENDING'">
      <div class="progress">
        <div class="progress-digit">{{ props.progress }}%</div>
        <div class="progress-prompt">这条任务正在列队中</div>
      </div>
    </div>
    <!--    预览-->
    <div class="preview-container" v-if="props.status==='PROCESSING'&&props.image">
      <el-image fit="cover"
                :src="props.image"
                class="image-size"/>
      <div class="preview-model">
        <div class="progress">
          <div class="progress-digit">{{ props.progress }}%</div>
          <div class="progress-prompt">这条任务正在处理中</div>
        </div>
      </div>
    </div>
    <!--    执行中-->
    <div class="image-size  await-img-active" v-if="props.status==='PROCESSING'&&!props.image">
      <div class="progress">
        <div class="progress-digit">{{ props.progress }}%</div>
        <div class="progress-prompt">这条任务正在处理中</div>
      </div>
    </div>
    <!--    失败-->
    <div class="image-size await-img-default" v-if="props.status==='DISUSE'">
      <div class="progress">
        <div class="progress-digit">
          <img :src="require('../../../../assets/app/image_failed_to_load.svg')" class="disuse-img" alt="">
        </div>
        <div class="progress-prompt">处理该任务时服务器出错</div>
      </div>
    </div>

    <!--    完成-->
    <el-image fit="cover" v-if="props.status==='SUCCEED'"
              :src="getOssDoMain()+ props.image"
              :preview-src-list="[getOssDoMain()+ props.image]"
              class="image-size"/>
    <!--    IMG操作-->
    <div v-if="props.status==='SUCCEED'"
         class="image-size img-operation-div">
      <div>
        <!--        <div class="img-round">-->
        <!--          <el-icon size="15px">-->
        <!--            <ZoomIn/>-->
        <!--          </el-icon>-->
        <!--        </div>-->
        <div class="img-round" @click="emits('partial-repaint',props.type, props.image)" v-if="props.type!=='DALL'">
          <el-icon size="15px">
            <EditPen/>
          </el-icon>
        </div>
        <!--        <div class="img-round">-->
        <!--          <el-icon size="15px">-->
        <!--            <CirclePlus/>-->
        <!--          </el-icon>-->
        <!--        </div>-->
        <!--        <div class="img-round">-->
        <!--          <el-icon size="15px">-->
        <!--            <View/>-->
        <!--          </el-icon>-->
        <!--        </div>-->
      </div>
    </div>
  </div>
</template>

<style scoped>

.image-container {
  position: relative
}

.progress {
  text-align: center
}

.disuse-img {
  width: 100px;
  height: 80px
}

.progress-digit {
  font-size: 30px;
  font-weight: 550
}

.progress-prompt {
  padding-top: 15px;
  font-size: 13px
}

.preview-container {
  position: relative
}

.preview-model {
  color: white;
  position: absolute;
  width: 520px;
  height: 320px;
  background-color: rgba(72, 72, 72, 0.54);
  top: 0;
  z-index: 5;
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center
}

.await-img-active {
  color: white;
  font-size: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(-25deg, #dae, #d966ff, #33a3cc, #09f, #66f);
  animation: gradient 6s ease infinite;
  background-size: 200% 200%
}

.await-img-default {
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  background-color: #282828;
}

@keyframes gradient {
  0% {
    background-position: 0 12%;
  }

  50% {
    background-position: 100% 100%;
  }

  100% {
    background-position: 0 12%;
  }
}

.image-size {
  width: 520px;
  height: 320px;
  border-radius: 5px;
}

.img-operation-div {
  position: absolute;
  z-index: 2;
  top: 0;
  height: 30px;
  color: #efefef;
  padding: 10px 5px 5px;

}

.img-round {
  background-color: rgba(0, 0, 0, 0.28);
  border-radius: 100%;
  width: 30px;
  height: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 6px;

}

.img-operation-div > div {
  display: flex;
  align-items: center;
  justify-content: right;
}

</style>
