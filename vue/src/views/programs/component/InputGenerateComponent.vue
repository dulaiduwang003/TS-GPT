<script setup>

import {Promotion} from "@element-plus/icons-vue";
import {defineEmits, defineProps, onMounted, ref, watch} from "vue";
import {ElNotification} from "element-plus";



const emits = defineEmits(['send', 'close']);

const props = defineProps({
  isVisual: {
    type: Boolean,
    default: false,
  },
  isLoading: {
    type: Boolean,
    default: true
  }
});

const editInputRef = ref(null)

const isVisual = ref(false)


const randomJson = ref(require('../../../data/writing.json'))

const template = ref([])

watch(
    () => props.isVisual,
    (newValue) => {
      isVisual.value = newValue;
    },
    {
      immediate: true,
    }
);


const getRandomJson = () => {
  let randomItems = [];
  let itemsCopy = [...randomJson.value]; // 复制数组对象以避免修改原始数据

  while (randomItems.length < 3 && itemsCopy.length > 0) {
    const randomIndex = Math.floor(Math.random() * itemsCopy.length);
    const randomItem = itemsCopy.splice(randomIndex, 1)[0];
    randomItems.push(randomItem);
  }
  template.value = randomItems
}

/**
 * 点击模板
 * @param content
 */
const handleClickTemplate = (content) => {
  const paragraphs = editInputRef.value.querySelectorAll('p');
  for (let i = 0; i < paragraphs.length; i++) {
    editInputRef.value.removeChild(paragraphs[i]);
  }
  editInputRef.value.innerHTML = content
}

const handleGetInputContent = () => {
  let innerText = editInputRef.value.innerText;
  if (innerText.trim()) {
    emits('send', innerText)
  } else {
    ElNotification({
      message: "请输入内容",
      type: "warning",
    });
  }

}

onMounted(() => {
  getRandomJson()
})

</script>

<template>
  <div class="generate-container" v-show="isVisual">
    <div class="input-container">
      <div class="input">
        <div class="row-twice">
          <div class="edit-input">
            <div v-if="props.isLoading" class="p-loading">
              正在为您生成内容...
            </div>
            <div v-else ref="editInputRef" :contenteditable="!props.isLoading" style=" outline: none;">

            </div>
          </div>
          <div class="send" v-if="!props.isLoading">
            <el-icon :size="20" @click="handleGetInputContent">
              <Promotion/>
            </el-icon>
          </div>
        </div>

        <div class="input-operation" v-if="!props.isLoading">
          <div class="template">
            <div class="template-label">GenerateTemplate</div>
            <div class="template-box" v-for="(item,index) in template" :key="index"
                 @click="handleClickTemplate(item.html)">
              <div class="t-padding-right">{{ item.icon }}</div>
              <div>{{ item.title }}</div>
            </div>
          </div>
          <div class="input-close" @click="emits('close')">
            关闭显示
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>
.row-twice {
  display: flex;
  justify-content: space-between;
  box-sizing: border-box;
  align-items: flex-end;
}

.input-operation {
  display: flex;
  justify-content: space-between;;
  align-items: center;
  border-top: 1px solid #efefef;
  margin-top: 10px;
}

.input-close {
  font-size: 10px;
  padding-top: 2px
}

.input-close:hover {
  color: #B535FF;
  cursor: pointer;
}

.template {

  padding: 8px 0;
  font-size: 13px;
  color: #9d9d9d;
  display: flex;
  align-items: center
}

.template-label {
  padding-right: 20px;
  font-weight: 550;
}

.t-padding-right {
  padding-right: 3px;
}

.template-box {
  transition: background-color 1s;
  cursor: pointer;
  border: 1px solid #e5e5e5;
  margin-right: 6px;
  border-radius: 5px;
  padding: 0 6px;
  display: flex;
  align-items: center;
}

.template-box:hover {
  background-color: rgba(220, 70, 255, 0.13);
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

.p-loading {
  color: #b6b6b9;
  font-weight: 500;
}


.generate-container {
  opacity: 1;
  transform: none;
  margin-top: 10px;
  animation: explainAnimation 0.7s;

}

@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.input-container {
  box-shadow: 0 5px 20px 5px rgb(0 0 0 / 6%);
  width: 100%;
  border-radius: 12px;
  border: 1px solid #ececec;
  padding: 3px;
  background: linear-gradient(-15deg, #dae, #c84fef, #79dbfa, #ff4687, #66f);
  animation: gradient 6s ease infinite;
  background-size: 200% 200%
}

.send {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 2px;
  width: 30px;
  height: 30px;
  cursor: pointer;
}

.send:hover {
  color: #783CFA;
}

p {
  line-height: 30px;
  word-break: break-all;
}

.edit-input {
  overflow-wrap: break-word;
  outline: none;
  box-shadow: none;
  min-width: 40%;
  padding-top: 1px;
  padding-bottom: 3px;
  font-size: 14px;
  font-weight: 550;
}

.input {
  background-color: white;

  padding: 4px 10px;
  border-radius: 12px;

}

</style>
