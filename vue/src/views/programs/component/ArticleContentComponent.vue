<script setup>

import {defineEmits, defineExpose, defineProps, nextTick, ref, watch} from "vue";
import InputGenerateComponent from "@/views/programs/component/InputGenerateComponent.vue";

const emits = defineEmits(['update-article', 'ai-writing','close-input']);

const props = defineProps({
  article: {
    type: String
  },
  isVisualInputGenerate: {
    type: Boolean
  },
  isAiWritingLoading: {
    type: Boolean
  }
});

const article = ref("")

const scrollRef = ref(null)


/**
 * 自动滚动
 */
const scrollToTheBottom = () => {
  nextTick(() => {
    scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
  });
}


/**
 * modify the content of the article
 * @param e
 */
const onChangeArticleContent = (e) => {
  article.value = e
  emits('update-article', article.value)
}

const generateWriting = (e) => {

  emits('ai-writing', e)
}

watch(
    () => props.article,
    (newValue) => {
      article.value = newValue;
    },
    {
      immediate: true,
    }
);

defineExpose(
    {scrollToTheBottom}
)


</script>

<template>

  <div class="input-container" ref="scrollRef">
    <el-input
        v-model="article"
        :disabled="isAiWritingLoading"
        style="margin-top: 10px"
        autosize
        placeholder="这里是编辑器, 你可以使用智能写作生成内容,或者直接打字写作 粘贴文本"
        type="textarea"
        @input="onChangeArticleContent"/>
    <input-generate-component
        @close="emits('close-input')"
        :is-loading="isAiWritingLoading"
        :is-visual="isVisualInputGenerate"
        @send="generateWriting"
    />

  </div>
</template>

<style scoped>


.inputBox {

}

.input-container {
  height: 100%;
  scroll-behavior: smooth;
  overflow: auto;
  padding: 5px 20px 30px;
}


::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background: #ffffff;
}

::-webkit-scrollbar-track {
  /* box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); */
  border-radius: 0;
  background: #ffffff;
  display: block;
}

:deep(.el-overlay) {
  background-color: transparent;
}

>>> .inputBox > .el-input__wrapper {
  background-color: #ffffff;
  outline: none;
  box-shadow: none;
  flex: 1;
  padding: 0;
  font-size: 25px;
}

:deep(.el-textarea.is-disabled .el-textarea__inner) {
  background-color: #ffffff;
}

:deep(.el-input.is-disabled .el-input__inner) {
  background-color: #ffffff;
}

:deep(.el-textarea__inner) {
  margin-top: 20px;
  padding: 0;
  font-size: 17px;

  background-color: #ffffff;
  box-shadow: none !important;
  resize: none !important;
  outline: none !important;

}

</style>
