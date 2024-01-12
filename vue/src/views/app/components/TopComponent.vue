<script setup>

import {Reading, Search, ToiletPaper} from "@element-plus/icons-vue";
import {defineEmits, defineProps, ref} from "vue";

const props = defineProps({
  isSearchLoading: {
    type: Boolean,
    default: false,
  }
});

const emits = defineEmits(['search', 'update-prompt', 'update-search-type']);

const prompt = ref('')

const handleSearch = () => {
  if (prompt.value.trim()){
    emits("updatePrompt", prompt.value);
    emits('search')
  }else{
    emits('update-search-type',false)
  }

}


</script>

<template>
  <!--搜索-->
  <div class="search">
    <el-icon size="15px">
      <Search/>
    </el-icon>
    <div class="search-text">
      搜索
    </div>
    <el-input class="inputBox"
              placeholder="Press enter to search"
              v-model="prompt"
              @keyup.enter="handleSearch"
              :disabled="props.isSearchLoading"/>
  </div>
  <div class="sift">
    <div class="public active-default">
      <el-icon>
        <Reading/>
      </el-icon>
      <span>公共预设词</span>
    </div>
    <div class="private active">
      <el-icon>
        <ToiletPaper/>
      </el-icon>
      <span>输入后按回车进行搜索</span>
    </div>
  </div>
</template>

<style scoped>

.sift {
  display: flex;
  justify-content: right;
  padding-top: 20px;
  font-size: 12px
}

.public {
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  background-color: #ffffff;
  padding: 5px 8px;
  text-align: center;
  color: #4a4a4a;
  display: flex;
  align-items: center;
}

.public > span {
  padding-left: 5px;
}

.private {
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  background-color: #ffffff;
  padding: 5px 8px;
  text-align: center;
  color: #4a4a4a;
  display: flex;
  align-items: center;
}

.active-default {
  background-color: #ffffff;
  color: #464646;
}

.active {
  background-color: #171717;
  color: #e5e5e5;
}

.private > span {
  padding-left: 5px;
}

.search-text {
  font-weight: 700;
  font-size: 16px;
  color: #666;
  margin-left: 5px;
  width: 60px;
}

>>> .inputBox > .el-input__wrapper {
  background-color: #ffffff;
  outline: none;
  box-shadow: none;
  flex: 1;
  padding-top: 2px;
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  background-color: #ffffff;
  box-shadow: none;
}

.search {
  box-shadow: 0 0 8px #d8d8d8;
  background: #fff;
  width: 100%;
  height: 55px;
  border-radius: 6px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  animation: slideEase 0.5s ease-in-out forwards;
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
