<script setup>

import {ref} from "vue";
import ArticleOperationComponent from "@/views/programs/component/ArticleOperationComponent.vue";
import ArticleContentComponent from "@/views/programs/component/ArticleContentComponent.vue";
import ArticleBottomComponent from "@/views/programs/component/ArticleBottomComponent.vue";
import store from "@/store";
import OptimizeComponent from "@/views/programs/component/OptimizeComponent.vue";
import {ElNotification} from "element-plus";
import {getWritingConfig} from "@/utils/Utils";

//设置可视化
const isVisualSetting = ref(false)

//input可视化
const isVisualInputGenerate = ref(false)
//加载中
const isAiWritingLoading = ref(false)
//内容
const article = ref("")
//链接地址
const wssAddress = ref(process.env.VUE_APP_WSS + '/chat-api/gpt/')
//socket实例
const webSocket = ref(null)

const articleContentRef = ref(null)

/**
 * 修改
 */
const handleUpdateArticle = (data) => {
  article.value = data
}

/**
 * 可视状态修改
 * @param bol
 */
const handleAiWritingVisual = (bol) => {
  isVisualInputGenerate.value = bol
  articleContentRef.value.scrollToTheBottom()
}

/**
 * Ai生成
 */
const handleAiWriting = (e) => {
  let writingConfig = getWritingConfig();
  if (writingConfig) {
    const {type, reader, writingStyle, language} = writingConfig
    const str = '请注意生成内容要求为' + type + '类型,读者为:' + reader + ',生成风格为:' + writingStyle + ',生成内容语言为:' + language
    establishConnection(e + str)
    return
  }
  establishConnection(e)
}

/**
 * 文章续写
 */
const handleAiContinued = () => {
  if (!article.value.trim() && article.value.length < 20) {
    ElNotification({title: "文章续写", message: '编辑器中至少需要包含20个字符', type: "info",});
    return
  }
  let s = article.value.slice(-500);
  establishConnection('我将会提供你一段内容,这个内容需要你实现文章续写,注意只需要你提供文章内容即可,其他无关内容不要回复,这个是需要你续写的内容:' + s)
}


const establishConnection = (e) => {
  isVisualInputGenerate.value = true
  isAiWritingLoading.value = true
  const messages = []
  //初始化通信
  const modelIndex = store.getters.userSetting.writingModelIndex;
  webSocket.value = new WebSocket(wssAddress.value + localStorage.getItem("token"));
  let socket = webSocket.value;
  messages.push({
    role: "user",
    content: e
  })
  const parameter = {
    //压缩数据结构
    messages: messages,
    //对话类型
    type: 'DIALOGUE',
    extra: {
      //模型坐标
      modelIndex: modelIndex,
      //是否过滤自定义敏感词
      isFiltration: false
    }
  };
  //将发送数据转为STRING 并建立socket连接
  socket.onopen = () => {
    socket.send(JSON.stringify(parameter));
  };
  //接收数据
  socket.onmessage = (event) => {
    article.value += event.data
    articleContentRef.value.scrollToTheBottom()
  };
  //关闭链接
  socket.onclose = () => {
    isAiWritingLoading.value = false
    socketClose()
    handleAiWritingVisual(false)
    articleContentRef.value.scrollToTheBottom()
  };
  //处理错误
  socket.onerror = () => {
    isAiWritingLoading.value = false
    handleAiWritingVisual(false)
    articleContentRef.value.scrollToTheBottom()
  }
}

/**
 * 处理异常
 */
const socketClose = () => {
  const answer = article.value[article.value.length - 1];
  if (answer === "ø") {
    ElNotification({
      message: "抱歉,服务貌似出了点问题,请稍后重试",
      type: "error",
    });
  } else if (answer === "⏓") {
    ElNotification({
      message: "抱歉,您的会员已过期",
      type: "error",
    });
  }
}
const handleSetting = (bol) => {
  isVisualSetting.value = bol
}

</script>

<template>
  <div class="body-container">
    <!--   头部操作-->
    <article-operation-component @handle-visual-setting="handleSetting"
                                 @handle-ai-continued="handleAiContinued"
                                 @handle-ai-writing-visual="handleAiWritingVisual"
                                 v-show="!isAiWritingLoading"/>
    <!--    填写-->
    <article-content-component
        ref="articleContentRef"
        :is-ai-writing-loading="isAiWritingLoading"
        :is-visual-input-generate="isVisualInputGenerate"
        :article="article"
        @ai-writing="handleAiWriting"
        @close-input="handleAiWritingVisual(false)"
        @update-article="handleUpdateArticle"/>
    <!--    底部计数器-->
    <article-bottom-component :article="article"/>
    <!--    润词-->
    <optimize-component :is-visual="isVisualSetting" @close="isVisualSetting=false"/>
  </div>
</template>

<style scoped>
:deep(.el-drawer__header) {
  margin-bottom: 0px !important;
}

.body-container {
  position: relative;
  margin: 0 auto;
  padding: 70px 15px 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  animation: explainAnimation 0.3s;
  max-width: 1300px;
  overflow-x: auto;
  flex: 1;

}


</style>
