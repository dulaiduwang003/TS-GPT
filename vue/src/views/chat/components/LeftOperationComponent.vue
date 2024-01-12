<script setup>

import {ArrowLeft, ArrowRight, Plus} from "@element-plus/icons-vue";
import {computed, defineEmits, defineExpose, defineProps, onMounted, ref} from "vue";
import store from "@/store";
import {conversionTime, getChatCache, setChatCache} from "@/utils/Utils";
import {ElNotification} from "element-plus";
import LoadComponent from "@/components/LoadComponent.vue";
import StatusComponent from "@/components/StatusComponent.vue";

//数据缓存
const chatCache = ref({})

const scrollRef = ref(null)

const emits = defineEmits(['replacement-chat-template', 'scroll-down']);

const props = defineProps({
  leftForbid: {
    type: Boolean
  },

})

//搜索关键词
const searchParameter = ref('')

//控制收缩
const isSideBarOpen = ref(true)


const filteredList = computed(() => {
  if (!searchParameter.value) {
    return chatCache.value.dataList; // 如果搜索为空，返回全部数据
  }
  return chatCache.value.dataList.filter(item => item.title.includes(searchParameter.value)); // 返回包含搜索词的项
});

/**
 * 随机生成编码
 * @returns {string}
 */
const randomlyGenerated = () => {
  const result = [];
  for (let i = 0; i < 8; i++) {
    result.push(String.fromCharCode(Math.floor(Math.random() * 26) + 65));
  }
  return result.join('');
}

/**
 * 切换主题
 */
const handleChooseTopic = (id) => {

  if (id !== chatCache.value.currentId) {
    const index = chatCache.value.dataList.findIndex(item => item.id === id);
    const data = chatCache.value.dataList[index]
    if (!data.isEdit) {
      chatCache.value.currentId = data.id
      //删除当前元素 并把该元素置换到顶层
      chatCache.value.dataList.splice(index, 1)
      //置换
      chatCache.value.dataList.unshift(data)
      //更新内容
      emits('replacement-chat-template', data.content)
      setChatCache(chatCache.value)
      //滚动顶部
      scrollRef.value.scrollTop = 0
    }
  }

}


/**
 * 创建新的主题
 */
const handleCreatedTopic = () => {
  const id = randomlyGenerated();
  chatCache.value.dataList.unshift({
    title: "未命名的主题",
    updateTime: Date.now(),
    prompt: '新对话',
    content: [],
    id: id,
    initial: 'N',
    isDelete: false,
    isEdit: false
  });
  //切换当前主题
  emits('replacement-chat-template', [])
  chatCache.value.currentId = id
  //修正当前主题
  setChatCache(chatCache.value)

  ElNotification({
    message: '新的主题已就绪', type: 'success',
  });

}

/**
 * 删除指定主题
 */
const handleDeleteTopic = (id) => {
  const {dataList, currentId} = chatCache.value;
  if (dataList.length === 1) {
    ElNotification({
      message: '至少保留一个主题', type: 'info',
    });

    return
  }
  const index = chatCache.value.dataList.findIndex(item => item.id === id);
  //删除
  chatCache.value.dataList.splice(index, 1);
  //是否删除激活主题
  if (id === currentId) {
    //获取列表第一个作为主题参数
    const element = chatCache.value.dataList[0];
    chatCache.value.currentId = element.id
    //重置对话
    emits('replacement-chat-template', element.content)
  }
  setChatCache(chatCache.value)
}

/**
 * 保存编辑
 */
const editTopicTitle = (title, id) => {

  const index = chatCache.value.dataList.findIndex(item => item.id === id);

  if (!title.trim()) {
    chatCache.value.dataList[index].title = '未命名的主题'
  }
  chatCache.value.dataList[index].isEdit = false
  setChatCache(chatCache.value)
}

/**
 * 主题标题
 */
const handleTopicTitle = (id) => {
  chatCache.value.dataList.forEach(t => t.isEdit = false)
  const index = chatCache.value.dataList.findIndex(item => item.id === id);
  chatCache.value.dataList[index].isEdit = true
}

/**
 * 刷新数据源
 */
const flushedChatCache = () => {
  chatCache.value = getChatCache();
}

onMounted(() => {
  if (store.getters.userInfo) {
    //读取用户缓存
    const data = getChatCache();
    if (!data) {
      //如果没有任何数据则需要加载一条新对话
      let id = randomlyGenerated();
      chatCache.value = {
        currentId: id,
        dataList: [
          {
            title: "未命名的主题",
            updateTime: Date.now(),
            prompt: '新对话',
            content: [],
            id: id,
            initial: 'N',
            isDelete: false,
            isEdit: false
          }
        ]
      }
      //切换当前主题
      emits('replacement-chat-template', [])
      //滚动
      emits('scroll-down')
      //保存一次缓存数据
      setChatCache(chatCache.value)
    } else {
      //如果存在
      chatCache.value = data
      const id = chatCache.value.currentId;
      const index = chatCache.value.dataList.findIndex(item => item.id === id);
      emits('replacement-chat-template', chatCache.value.dataList[index].content)
    }
  }
})


defineExpose(
    {flushedChatCache}
)
</script>

<template>
  <div :class="isSideBarOpen?'container-show':'container-hide'">
    <!--  锁定动画-->
    <load-component text="请等待回复完毕" tips="please wait" v-if="props.leftForbid&&isSideBarOpen"/>
    <div v-if="isSideBarOpen">
      <div class="head">
        <el-input class="inputBox" placeholder="搜索主题" border="none" v-model="searchParameter"/>
        <el-button class="button" @click="handleCreatedTopic">
          <el-icon size="16px">
            <Plus/>
          </el-icon>
        </el-button>
      </div>
      <div class="scroll" ref="scrollRef" v-if="filteredList&&filteredList.length>0">
        <div :class="item.id===chatCache.currentId?'row-active':'row-default'"
             v-for="(item) in filteredList"
             :key="item.id" @click="handleChooseTopic(item.id)"
             @mouseenter="item.isDelete=true"
             @mouseleave="item.isDelete=false">
          <div class="icon">
            <div :class="item.id===chatCache.currentId?'icon-div-active':'icon-div-default'">{{ item.initial }}</div>
          </div>
          <div class="row-input">
            <div class="row-input-title">
              <div v-if="!item.isEdit" @click.stop="handleTopicTitle(item.id)">
                {{ item.title }}
              </div>
              <el-input v-model="item.title" @blur.stop="editTopicTitle(item.title,item.id)" v-else/>
            </div>
            <div :class="item.id===chatCache.currentId?'row-input-prompt-active':'row-input-prompt-default'">
              {{ item.prompt }}
            </div>
          </div>
          <div class="row-input-date">
            <div>
              {{ conversionTime(item.updateTime) }}
            </div>
            <div class="delete-flux">
              <div>
                <img :src="require('../../../assets/app/chat/deleted.svg')"
                     alt=""
                     v-show="item.isDelete"
                     @click.stop="handleDeleteTopic(item.id)">
              </div>
            </div>
          </div>
        </div>
      </div>
      <status-component color="#949494" icon="Connection" text="没有找到任何主题" v-else/>
    </div>

    <div class="shrink">
      <div>
        <el-icon>
          <ArrowLeft v-if="isSideBarOpen" @click="isSideBarOpen=false"/>
          <ArrowRight v-else @click="isSideBarOpen=true"/>
        </el-icon>
      </div>
    </div>
  </div>
</template>

<style scoped>

.container-show {
  position: relative;
  height: 100%;
  width: 30%;
  background-color: white;
}

.container-hide {
  position: relative;
  height: 100%;
  width: 25px;
  background-color:  #E5E5E5;
}


:deep(.el-input__inner) {
  font-size: 13px;
}

.head {
  display: flex;
  align-items: center;
  padding: 5px;
}

.button {
  margin-left: 5px;
  font-size: 13px;
  background-color: #ffffff;
  color: #6B6B6B;
  border: 1px solid #e1e1e1;
}

.button:hover,
.button:focus,
.button:active {
  background-color: #ffffff;
  color: #6b6b6b;
  border: 1px solid #e1e1e1;
}

>>> .inputBox > .el-input__wrapper {
  background-color: #efefef;
  outline: none;
  box-shadow: none;
}

.scroll {
  height: 94vh;
  scroll-behavior: smooth;
  overflow: auto;
  box-sizing: border-box;
}

.row-default {
  border-radius: 5px;
  background-color: #ffffff;
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  cursor: pointer;
  transition: background-color 0.5s;
}

.row-default:hover {
  background-color: #eaeaea;
}

.row-active {
  border-radius: 5px;
  background-color: #7365FF;
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  color: white;
  width: 100%;
  cursor: pointer;
  transition: background-color 0.5s;
}

.icon {
  width: 40px;
  height: 40px;
  border-radius: 100%;
  overflow: hidden;
  background-color: #bca8ff
}

.icon-div-default {
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  font-weight: 660;
}

.icon-div-active {
  font-weight: 660;
  color: #b57afd;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
  width: 100%;
  height: 100%
}

.row-input-default {
  padding-left: 10px;
  color: #282828;
  width: 150px;

}

.row-input {
  padding-left: 10px;
  flex: 1;
}

.row-input-title {
  font-size: 13px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  width: 70%;
}

.row-input-prompt-default {
  color: #878787;
  font-size: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.row-input-prompt-active {
  color: #dedede;
  font-size: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.row-input-date {
  font-size: 10px;
  width: 80px;

  text-align: right;
}

::-webkit-scrollbar {
  width: 10px;
  height: 10px;
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

.delete-flux {
  padding-top: 10px;
  display: flex;
  justify-content: right
}

.delete-flux div {

  width: 15px;
  height: 15px
}

.delete-flux img {
  color: white;
  width: 100%;
  height: 100%
}


.shrink {
  position: absolute;
  z-index: 4;
  right: -15px;
  top: 13%;

}


.shrink div {
  font-weight: 700;
  background-color: white;
  width: 30px;
  height: 30px;
  border: 1px solid #e1e1e1;
  color: #6B6B6B;
  border-radius: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: width 0.3s, height 0.3s;
}
.shrink div:hover {
  width: 35px;
  color: white;
  font-size:20px;
  border: 1px solid rgb(155, 66, 255);
  background-color: rgb(155, 66, 255);
  height: 35px;
  cursor: pointer;

}
</style>
