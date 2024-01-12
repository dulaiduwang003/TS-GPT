<script setup>

import {Promotion} from "@element-plus/icons-vue";

import {defineEmits, defineExpose, defineProps, ref, watch} from "vue";
import store from "@/store";
import {ElNotification} from "element-plus";


const props = defineProps({
  plugInsIndex: {
    type: Number
  },
  isLoading: {
    type: Boolean,
    default: false
  },
  imageRecognitionBase64: {
    type: String,
    default: '',
  }
})

const emits = defineEmits(['input', 'send', 'toggle-plug-ins', 'assignment-recognition-base64']);

const input = ref('')

watch(input, (newVal) => {
  emits("input", newVal);
});

const clear = () => {
  input.value = ''
}

/**
 * 快捷键
 * @param e
 */
const handleKeyDown = (e) => {
 if (store.getters.userInfo){
   let shortcutIndex = store.getters.userSetting.shortcutIndex;
   if (shortcutIndex === 0) {
     // 判断是否按下了 Shift 键和 Enter 键
     if (e.keyCode === 13) {
       emits('send')
     }
     return
   }
   if (shortcutIndex === 1) {
     // 判断是否按下了 Shift 键和 Enter 键
     if (e.shiftKey && e.keyCode === 13) {
       emits('send')
     }
     return
   }
   if (shortcutIndex === 2) {
     // 判断是否按下了 ctrlKey 键和 enter 键
     if (e.ctrlKey && e.keyCode === 13) {
       emits('send')
     }
     return;
   }
   if (shortcutIndex === 3) {
     // 判断是否按下了 Shift 键和 Enter 键
     if (e.altKey && e.keyCode === 13) {
       emits('send')
     }

   }
 }
}

/**
 * 启用插件
 * @param e
 * @param t
 */
const handlePlug = (e, t) => {
  if (props.plugInsIndex === e) {
    emits('toggle-plug-ins', -1)
  } else {
    ElNotification({title: "绘图", message: t, type: "success",});
    emits('toggle-plug-ins', e)
  }
}
/**
 * 上传图片
 * @param e
 * @returns {boolean}
 */
const onChangeFileTemp = (e) => {
  if (e.raw.type === 'image/jpg' || e.raw.type === 'image/png' || e.raw.type === 'image/jpeg') {
    if (e.raw.size / 1024 / 1024 > 2) {

      ElNotification({
        message: '图像大小不得超过2MB', type: 'error',
      });
      return false
    }
    new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (event) => {
        emits('assignment-recognition-base64', event.target.result)
        resolve(e);
      };
      reader.onerror = (error) => {
        reject(error);
      };
      reader.readAsDataURL(e.raw);
    });
  } else {

    ElNotification({
      message: '请上传正确的图片', type: 'error',
    });
    return false
  }
}
/**
 * 更新文本
 * @param data
 */
const updateInputText = (data) => {
  input.value = data
}

defineExpose({clear, updateInputText})
</script>

<template>
  <div class="footer">
    <div class="tools">
      <div class="tools-left">
        <div>
          <img
              :src="props.plugInsIndex===1?
              require('../../../../assets/app/chat/generate-active.svg')
              :
              require('../../../../assets/app/chat/generate.svg') "
              @click="handlePlug(1,'图片生成模式已开启')"
              alt="">
        </div>
        <div class="tools-padding-left">
          <img :src="props.plugInsIndex===2?
              require('../../../../assets/app/chat/upload-active.svg')
              :
              require('../../../../assets/app/chat/upload.svg') "
               @click="handlePlug(2,'识图模式已开启')"
               alt="">
        </div>

      </div>
      <div>
        <img :src="require('../../../../assets/app/chat/deleted.svg')" alt="" @click="clear">
      </div>
    </div>
    <div class="input-container">
      <el-upload :auto-upload="false" v-if="plugInsIndex===2 && !isLoading"
                 :on-change="($event)=>onChangeFileTemp($event)"
                 :show-file-list="false">
        <div class="identify-container">
          <div class="identify-interior" v-if="!props.imageRecognitionBase64">
            <div>
              <div class="identify-btn">
                上传图片
              </div>
              <div class="identify-prompt">
                图片大小限制在2MB
              </div>
            </div>
          </div>
          <el-image :src="props.imageRecognitionBase64" class="upload-img" fit="cover" v-else/>
          <div class="image-prompt" v-if="props.imageRecognitionBase64">
            已就绪
          </div>
        </div>
      </el-upload>

      <el-input
          @keydown="handleKeyDown"
          :placeholder="isLoading ? '思考中...' : '输入你想问的...'"
          type="textarea"
          class="input"
          v-model="input"
          :disabled="isLoading"
          :maxlength="store.getters.userSetting?store.getters.userSetting.memorySize:2000"
      />
      <div class="animation-dot" v-if="props.isLoading">
        <div class="dot_0"></div>
        <div class="dot_1"></div>
        <div class="dot_2"></div>
        <div class="dot_3"></div>
      </div>
      <div class="send-icon" v-else @click="emits('send')">
        <el-icon :size="20">
          <Promotion/>
        </el-icon>
      </div>
    </div>
  </div>
</template>

<style scoped>
.footer {
  width: 100%;
  height: 23vh;
  border-top: 1px solid #e8e8e8;
  background: #F2F2F2;
  padding: 10px

}

.tools {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 10%;
}

.tools img {
  width: 20px;
  height: 20px
}

.tools-left {
  display: flex
}

.tools-padding-left {
  padding-left: 15px
}

.send-icon {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  color: white;
  cursor: pointer;
  background: #8a80ec;
  border-radius: 50%;
  justify-content: center;
  align-items: center;
  display: flex;

}

.animation-dot {
  display: flex;

}


:deep(.el-textarea__inner) {
  height: 100% !important;
  background-color: #f2f2f2;
  box-shadow: none !important;
  resize: none !important;
  outline: none !important;

}

.identify-container {
  animation: slideEase 0.5s ease-in-out forwards;
  border: 3px dashed #d0d0d0;
  height: 130px;
  width: 180px;
  background-color: #f4f4f4;
  box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.identify-interior {
  display: flex;
  justify-content: center;
  text-align: center
}

.identify-btn {
  font-size: 13px;
  background-color: #7365FF;
  padding: 3px 8px;
  border-radius: 15px;
  color: white
}

.identify-prompt {
  font-size: 9px;
  color: #7f7f7f;
  padding-top: 10px
}

:deep(.el-textarea.is-disabled .el-textarea__inner) {
  background-color: #F2F2F2;
}

.input-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 100%;

}

.input{
  height: 100%;
}

.send-icon {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  color: white;
  cursor: pointer;
  background: #8a80ec;
  border-radius: 50%;
  justify-content: center;
  align-items: center;
  display: flex;

}

@keyframes slideEase {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.image-prompt {
  position: absolute;
  z-index: 2;
  flex: 1;
  display: flex;
  justify-content: center;
  color: #c9c3ff;
  text-decoration: underline
}

.upload-img {
  width: 100%;
  height: 100%;
  border-radius: 12px;
  filter: brightness(0.8);
}
</style>
