<script setup>

import {CopyDocument, View} from "@element-plus/icons-vue";
import {defineEmits, defineProps} from "vue";
import {ElNotification} from "element-plus";

const props = defineProps({
  data: {
    type: Array
  }
});

const emits = defineEmits(['handle-use-template']);


/**
 * 应用模板
 * @param index
 */
const handleApplyTemplate = (id, index) => {
  let datum = props.data.list[index];
  if (datum.isSelected) {
    emits('handle-use-template', id, datum.englishIssue,datum.englishAnswer)
  } else {
    emits('handle-use-template', id, datum.chineseIssue,datum.chineseAnswer)
  }
}

/**
 * 复制内容
 * @param index
 */
const handleCopyContent = (index) => {
   let datum = props.data[index];
   if (datum.isSelected){
     navigator.clipboard.writeText(datum.englishIssue);
   }else {
     navigator.clipboard.writeText(datum.chineseIssue);
   }
  ElNotification({
    message: "复制成功",
    type: "success",
  });
}

</script>

<template>
  <div class="content-body" >
    <div class="card-container">
      <div class="card" v-for="(item,index) in props.data" :key="index">
        <div class="row-flex">
          <div class="icon">
            {{item.icon}}
          </div>
          <span class="title">
            {{ item.title }}
          </span>
          <div class="card-copy" @click="handleCopyContent(index)">
            <el-icon size="14px">
              <CopyDocument/>
            </el-icon>
          </div>
        </div>
        <div class="introduce">
          {{ item.introduce }}
        </div>
        <div class="content" @click="item.isSelected=!item.isSelected">
          {{ item.isSelected ? item.englishIssue : item.chineseIssue }}
        </div>
        <div class="card-operation">
          <div class="bottom">
            <el-icon size="13px">
              <View/>
            </el-icon>
            <span class="view-count">{{ item.visits }}</span>
          </div>
          <div class="card-use" @click="handleApplyTemplate(item.microAppId,index)">
            应用此模板
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.content-body {
  background-color: #F9F9F9;
  margin-top: 20px;
  border-radius: 6px;
  animation: explainAnimation 0.3s;
  width: 100%;
  padding: 10px;
}

.bottom {
  display: flex;
  align-items: center;
  color: #bcbcbc;
  margin-top: 5px;
  font-size: 10px
}

.view-count {
  padding-left: 5px;
}

.category {
  color: #2b2b2b;
  padding: 2px 8px;
  margin: 1%;
  font-size: 13px;
  box-shadow: 0 2px 0 rgba(170, 170, 170, 0.1);
  border-radius: 3px;
  display: inline-block;
  background-color: #ffffff;
}

.introduce {
  font-size: 13px;
  margin-top: 8px;
  color: #868686;
  height: 47px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.content {
  font-size: 13px;
  margin-top: 8px;
  color: #434343;
  height: 200px;
  scroll-behavior: smooth;
  overflow: auto;
  box-sizing: border-box;
  padding-bottom: 30%;
  transition: background-color 0.3s;
}

.title {
  font-size: 15px;
  font-weight: 600;
  margin-left: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  cursor: default;
  flex: 1;
  color: #303030;
}

.title:hover {
  color: #993bff;
}

.row-flex {
  display: flex;
  align-items: center;
}

:deep(.el-space) {
  width: 100% !important;
}

.card {
  box-shadow: 0 2px 0 rgba(170, 170, 170, 0.1);
  margin: 1%;
  width: 23%;

  background-color: #ffffff;
  border-radius: 6px;
  padding: 10px;
  height: 345px;
  overflow: hidden;
}

.category-flex {
  display: flex;
  align-items: center
}

.card-use {
  margin-top: 5px;
  font-size: 13px;
  color: #d0d0d0;
  border-radius: 5px;
  padding: 2px 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.card-copy {
  border-radius: 5px;
  padding: 2px 8px;
  transition: background-color 0.3s;
  cursor: pointer;
}


.card-copy {
  border-radius: 5px;
  padding: 2px 8px;
  transition: background-color 0.3s;
  color: #5b5b5b;
}

.card-copy:hover {
  background-color: #993bff;
  color: white;
}


.card-use:hover {
  background-color: #993bff;
  color: white;
}

@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.category-flex span {
  padding-left: 5px;
  color: #747474;

}

.card:hover {
  box-shadow: 0 3px 3px rgba(170, 170, 170, 0.2);
}

.card-container {
  display: flex;
  flex-wrap: wrap;
  width: 100%
}

.icon {
  font-size: 20px
}

::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background: #e8e8e8;
}

::-webkit-scrollbar-track {
  /* box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); */
  border-radius: 0;
  background: transparent;
  display: block;
}

.content:hover {
  color: #993bff;
}
.card-operation {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 8px
}
</style>
