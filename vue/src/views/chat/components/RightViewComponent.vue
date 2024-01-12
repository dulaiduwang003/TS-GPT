<script setup>

import {defineEmits, defineExpose, nextTick, reactive, ref} from "vue";
import {getChatCache, getCurrentFormattedTime, getOssDoMain, setChatCache} from "@/utils/Utils";
import PopoverComponent from "@/views/chat/components/components/PopoverComponent.vue";
import {ElNotification} from "element-plus";
import {UserFilled, VideoPause} from "@element-plus/icons-vue";
import DateHeadComponent from "@/components/DataHeadComponent.vue";
import InputComponent from "@/views/chat/components/components/InputComponent.vue";
import TopNavigationComponent from "@/views/chat/components/components/TopNavigationComponent.vue";
import store from "@/store";
import IndexTemplateComponent from "@/views/chat/components/components/IndexTemplateComponent.vue";
import {pushDialogueImageTask} from "@/api/drawing";
import AuthDialogComponent from "@/components/auth/AuthDialogComponent.vue";
import ChatTemplateComponent from "@/views/chat/components/components/ChatTemplateComponent.vue";
import DrawingTemplateComponent from "@/views/chat/components/components/DrawingTemplateComponent.vue";


const emits = defineEmits([ 'left-control-enable','flushed-chat-cache']);

//当前渲染数据
const chatTemplate = ref([])
//用户输入内容
const inputText = ref('')
//链接地址
const wssAddress = ref(process.env.VUE_APP_WSS + '/chat-api/gpt/')
//socket实例
const webSocket = ref(null)
//内容滑块引用
const scrollRef = ref(null)
// 消息队列
const messageQueue = reactive([]);
// 是否正在显示消息 标记
let isDisplaying = false;
//滚动标记
const scrollIsLock = ref(false)
// 输入框加载中
const isLoading = ref(false)
//文本引用
const inputRef = ref(null)
//当前激活插件
const plugInsIndex = ref(-1)
//登录
const authDialogIsVisible = ref(false)

//图片预览
const previewImageData = ref({
  visible: false,
  image: ''
})

//图片识别
const imageRecognitionBase64 = ref('')

//图片生成
const isImageGenerate = ref(false)

/**
 * 临时图片缓存
 * @param e
 */
const assignmentRecognitionBase64 = (e) => {
  imageRecognitionBase64.value = e
}

/**
 * 切换插件
 * @param e
 */
const togglePlugIns = (e) => {
  plugInsIndex.value = e
}


/**
 * 处理对话绘图任务
 */
const handleDialogueImage = async (input) => {
  isImageGenerate.value = true
  isLoading.value = true
  emits('left-control-enable', true)
  //写入模板数据
  chatTemplate.value.push({
    type: 'DRAWING',
    issue: input,
    answer: '',
    image: '',
    error: {
      isError: false,
      type: ''
    },
    date: getCurrentFormattedTime()
  })
  scrollToTheBottom()
  const index = chatTemplate.value.length - 1;
  try {
    const {data} = await pushDialogueImageTask({prompt: input});
    chatTemplate.value[index].answer = data.revisedPrompt
    chatTemplate.value[index].image = getOssDoMain() + data.url
  } catch (e) {
    console.log(e)
    if (e.msg) {
      if (e.msg.trim() === "ø") {
        chatTemplate.value[index].error.isError = true
        chatTemplate.value[index].error.type = "EXCEPTION_REPORTED"
      } else if (e.msg.trim() === "⏓") {
        chatTemplate.value[index].error.isError = true
        chatTemplate.value[index].error.type = "MEMBERSHIP_EXPIRES"
      }
      chatTemplate.value[index].answer = e.msg
      scrollToTheBottom()
    } else {
      ElNotification({
        message: "抱歉此任务已丢失,请重新发送任务",
        type: "success",
      });
      chatTemplate.value.splice(index, 1)
    }
  }
  isImageGenerate.value = false
  saveChatCache()
  inputText.value = ''
  inputRef.value.clear()
  isLoading.value = false
  emits('left-control-enable', false)
}

/**
 * 处理对话任务
 * @param data
 * @returns {Promise<void>}
 */
const handleDialogue = async (data) => {
  isLoading.value = true
  emits('left-control-enable', true)
  //初始化通信
  const modelIndex = store.getters.userSetting.modelIndex;
  webSocket.value = new WebSocket(wssAddress.value + localStorage.getItem("token"));
  let socket = webSocket.value;
  imageRecognitionBase64.value = ''
  //写入模板数据
  chatTemplate.value.push({
    type: 'CHAT',
    issue: data,
    answer: '',
    error: {
      isError: false,
      type: ''
    },
    date: getCurrentFormattedTime()
  })
  nextTickScroll()
  //记录当前聊天数据坐标
  let index = chatTemplate.value.length - 1;
  //转化数据结构
  const conversionList = conversionDataFormat(index);
  let compressArrayData = compressArray(conversionList, store.getters.userSetting.memorySize);
  const parameter = {
    //压缩数据结构
    messages: compressArrayData,
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
    //根据坐标写入数据
    messageQueue.push({
      msg: event.data,
      index: index,
    }); // 将接收到的消息存储到队列中
    displayMessages(); // 显示消息
  };
  //处理连接关闭
  socket.onclose = () => {
    socketClose(index)
  };
  //处理错误
  socket.onerror = () => {
    ElNotification({
      message: "当前登录信息已过期,请重新登录", type: "error",
    });
    store.commit("logout");
    location.reload();
  }
}


/**
 * 处理对话识图任务
 * @param data
 * @returns {Promise<void>}
 */
const handleDialogueRecognition = async (data) => {
  if (!imageRecognitionBase64.value) {
    await handleDialogue(data);
    return
  }
  isLoading.value = true
  emits('left-control-enable', true)
  webSocket.value = new WebSocket(wssAddress.value + localStorage.getItem("token"));
  let socket = webSocket.value;
  //写入模板数据
  chatTemplate.value.push({
    type: 'RECOGNITION',
    issue: data,
    image: imageRecognitionBase64.value,
    answer: '',
    error: {
      isError: false,
      type: ''
    },
    date: getCurrentFormattedTime()
  })
  nextTickScroll()
  //记录当前聊天数据坐标
  let index = chatTemplate.value.length - 1;

  const parameter = {
    messages: [
      {
        role: "user",
        content: [
          {
            type: "text",
            text: data
          },
          {
            type: "image_url",
            image_url: {
              "url": imageRecognitionBase64.value
            }
          }
        ]
      }
    ],
    //对话类型
    type: 'RECOGNITION',
    extra: {
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
    //根据坐标写入数据
    messageQueue.push({
      msg: event.data,
      index: index,
    }); // 将接收到的消息存储到队列中
    displayMessages(); // 显示消息
  };

  //监听关闭
  socket.onclose = () => {
    socketClose(index)
  };
  //处理错误
  socket.onerror = () => {
    ElNotification({
      message: "当前登录信息已过期,请重新登录", type: "error",
    });
    store.commit("logout");
    location.reload();
  }
}

/**
 * 发送消息
 */
const handleSendChat = () => {

  if (!store.getters.userInfo) {
    authDialogIsVisible.value = true
    return
  }

  let data = inputText.value;
  if (data.trim() && isLoading.value === false) {
    switch (plugInsIndex.value) {
      case -1:
        handleDialogue(data)
        break
      case 1:
        handleDialogueImage(data)
        break
      case 2:
        handleDialogueRecognition(data)
        break
    }
  }
}


/**
 * 处理CHAT异常
 */
const socketClose = (index) => {
  let interval = setInterval(() => {
    if (messageQueue.length === 0) {
      const answer = chatTemplate.value[index].answer;
      if (!answer) {

        //删除
        handleDeleteChat(index)
      } else if (answer === "ø") {
        chatTemplate.value[index].error.isError = true
        chatTemplate.value[index].error.type = "EXCEPTION_REPORTED"
      } else if (answer === "⏓") {
        chatTemplate.value[index].error.isError = true
        chatTemplate.value[index].error.type = "MEMBERSHIP_EXPIRES"
      }
      //存储聊天数据
      saveChatCache()
      scrollIsLock.value = false
      inputRef.value.clear()
      webSocket.value = null
      isLoading.value = false
      emits('left-control-enable', false)
      imageRecognitionBase64.value = ''
      clearInterval(interval)

    }
  }, 50);
}

/**
 * 更新缓存数据
 */
const saveChatCache = () => {
  const chatCache = getChatCache();
  const currentId = chatCache.currentId;
  const index = chatCache.dataList.findIndex(item => item.id === currentId);

  const valueElement = chatTemplate.value[chatTemplate.value.length - 1];
  if (valueElement) {
    if (valueElement.answer) {
      const {answer, issue} = valueElement;
      chatCache.dataList[index].prompt = answer.trim().slice(0, 30);
      if (issue.trim()) {
        chatCache.dataList[index].initial = issue.charAt(0)
      } else {
        chatCache.dataList[index].initial = '绘'
      }
      chatCache.dataList[index].updateTime = Date.now()
      chatCache.dataList[index].content = chatTemplate.value
      setChatCache(chatCache)
      emits("flushed-chat-cache")
    }
  } else {
    chatCache.dataList[index].content = []
    setChatCache(chatCache)
    emits("flushed-chat-cache")
  }
}

/**
 * 转化数据格式
 */
const conversionDataFormat = (index) => {
  const messages = []

  chatTemplate.value.forEach((c, itemIndex) => {
    const {error, issue, answer} = c
    if (!error.isError) {
      messages.push({
        role: "user",
        content: issue
      })
      // 判断是否为最后一条
      if (itemIndex !== index) {
        messages.push({
          role: "system",
          content: answer
        })
      }
    }
  })

  return messages;
}

/**
 * 关闭连接
 */
const handleCloseSocket = () => {
  if (webSocket.value) {
    messageQueue.length = 0;
    webSocket.value.close();
    webSocket.value = null;
  }
}

/**
 * 数据压缩算法
 * @param messages
 * @param threshold
 * @returns {*|*[]}
 *
 */
const compressArray = (messages, threshold) => {
  const totalLength = messages.reduce((acc, cur) => acc + cur.content.length, 0);
  if (totalLength <= threshold) {
    return messages;
  }
  const compressed = [];
  for (let i = messages.length - 1; i >= 0; i--) {
    const currentLength = messages[i].content.length;
    if (currentLength <= threshold) {
      compressed.push(messages[i]);
      threshold -= currentLength;
    } else {
      compressed.push({
        role: messages[i].role,
        content: messages[i].content.substr(0, threshold),
      });
      break;
    }
  }
  //压缩后的结果
  let reverse = compressed.reverse();
  //判断是否启用
  let userSetting = store.getters.userSetting;
  if (userSetting.isPreset) {
    if (userSetting.presetWordIssue.trim()) {
      reverse.unshift({
        role: "user",
        content: userSetting.presetWordIssue
      })
    }
    if (userSetting.presetWordAnswer.trim()) {
      reverse.unshift({
        role: "system",
        content: userSetting.presetWordAnswer
      })
    }
  }

  return reverse
}


/**
 * 触点滑动
 * @param event
 */
const handleScroll = (event) => {
  // 判断滚动方向
  if (event.deltaY < 0) {
    if (!scrollIsLock.value) {
      if (inputText.value) {
        scrollIsLock.value = true
        setTimeout(() => {
          scrollIsLock.value = false;
        }, 8000); // 延迟8秒后将scrollIsLock.value设置为false
      }
    }
  }
}

/**
 * 自动滚动
 */
const scrollToTheBottom = () => {
  if (!scrollIsLock.value) {
    nextTick(() => {
      scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
    });
  }
}


/**
 * 强制单字输出内容
 */
const displayMessages = () => {
  const outputRate = store.getters.userSetting.outputRate;
  if (isDisplaying) {
    return; // 如果正在显示消息，则直接返回，等待下一次调用
  }
  isDisplaying = true;
  const message = messageQueue.shift(); // 取出队列中的第一个消息
  if (message) {
    let i = 0;
    const {index, msg} = message;

    // eslint-disable-next-line no-inner-declarations
    function displayNextCharacter() {
      const character = msg.charAt(i++);
      if (character) {
        chatTemplate.value[index].answer += character;
        if (i === msg.length) { // 只有在内容更新时才滚动到最底部
          scrollToTheBottom();
          setTimeout(displayNextCharacter, outputRate); //平滑控制更新频率
        } else {
          requestAnimationFrame(displayNextCharacter);
        }
      } else {
        isDisplaying = false; // 重置标志以便下次能够正确显示消息
        displayMessages(); // 显示下一条消息
      }
    }

    displayNextCharacter();
  } else {
    isDisplaying = false; // 重置标志以便下次能够正确显示消息
  }
}

/**
 * 滚动
 */
const nextTickScroll = () => {
  //滚动一次 注意这里不能使用方法来滚动
  nextTick(() => {
    scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
  });
}

/**
 * 更新输入框
 * @param e
 */
const updateInputText = (e) => {
  inputText.value = e
}

/**
 * 复制内容
 * @param data
 */
const handleCopyContent = (data) => {
  navigator.clipboard.writeText(data);
  ElNotification({
    message: "复制成功",
    type: "success",
  });
}

/**
 * 删除某条聊天记录
 * @param index
 */
const handleDeleteChat = (index) => {
  chatTemplate.value.splice(index, 1);
  saveChatCache()
}

/**
 * 复制代码
 * @param code
 */
const handleCopyCode = (code) => {
  navigator.clipboard.writeText(code);
  ElNotification({
    message: "复制成功",
    type: "success",
  });
}

/**
 * 替换聊天内容
 */
const replaceChatTemplate = (data) => {
  chatTemplate.value = data
}

/**
 * 预览图片
 * @param image
 */
const previewImage = (image) => {
  previewImageData.value.image = image
  previewImageData.value.visible = true

}

/**
 * 下载预览图片
 * @param data
 */
const downloadImage = (data) => {
  window.open(data, '_blank');
}

/**
 * 处理提示词
 * @param data
 */
const handlePrompt = (data) => {
  inputRef.value.updateInputText(data)
}


defineExpose(
    {replaceChatTemplate, scrollToTheBottom}
)

</script>

<template>
  <div class="container">
    <top-navigation-component/>
    <!--    聊天控件-->
    <div v-if="chatTemplate.length>0">
      <div class="content-body" ref="scrollRef" @wheel="handleScroll">
        <div v-for="(item,index) in chatTemplate" :key="index" class="slide-animation">
          <div class="user-chat-model" v-if="item.issue">
            <div>
              <!--标签-->
              <date-head-component :date="item.date" :type="0"/>
              <el-popover placement="top-end" width="80px" :disabled="isLoading">
                <popover-component @copy-content-event="handleCopyContent(item.issue)"
                                   @delete-chat-event="handleDeleteChat(index)"/>
                <template #reference>
                  <div class="user-chat">
                    {{ item.issue }}
                    <div v-if="item.type==='RECOGNITION'">
                      <img class="recognition-img" @click="previewImage(item.image)"
                           :src="item.image" alt="" @load="scrollToTheBottom"/>
                    </div>
                  </div>

                </template>
              </el-popover>
            </div>
            <div>
              <el-avatar :icon="UserFilled" :size="40"
                         :src="store.getters.userInfo.avatar?getOssDoMain()+store.getters.userInfo.avatar: require('../../../assets/app/default-avatar.png')"/>
            </div>
          </div>
          <div class="bot-chat-model">
            <div>
              <el-avatar :size="40"
                         :src="store.getters.userSetting.chatBotAvatar
                         ?
                         store.getters.userSetting.chatBotAvatar
                         :
                         require('../../../assets/app/chat.svg')"/>
            </div>
            <div class="bot-chat-width" v-if="item.answer">
              <!--标签-->
              <date-head-component :date="item.date" :type="1"/>
              <!--普通模板-->
              <div class="bot-answer" v-if="item.type==='CHAT'||item.type==='RECOGNITION'">
                <chat-template-component
                    :error="item.error"
                    :is-loading="isLoading"
                    :answer="item.answer"
                    :index="index"
                    @handle-copy-code="handleCopyCode"
                    @handle-delete-chat="handleDeleteChat"
                    @handleCopyContent="handleCopyContent"
                />

              </div>
              <div class="drawing-model" v-else>
                <drawing-template-component
                    :image="item.image"
                    :error="item.error"
                    @preview-image="previewImage"
                    @scroll-to-bottom="scrollToTheBottom"
                />
              </div>
            </div>
            <div class="bot-chat" v-else>
              <div class="dot-flex">
                <div class="dot_0"></div>
                <div class="dot_1"></div>
                <div class="dot_2"></div>
                <div class="dot_3"></div>
                <div class="dot_4"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--    开屏-->
    <div v-else class="template">
      <index-template-component @update-prompt-input-text="handlePrompt"/>
    </div>
    <input-component ref="inputRef"
                     @input="updateInputText"
                     @send="handleSendChat"
                     :plug-ins-index="plugInsIndex"
                     :image-recognition-base64="imageRecognitionBase64"
                     :is-loading="isLoading"
                     @assignment-recognition-base64="assignmentRecognitionBase64"
                     @toggle-plug-ins="togglePlugIns"/>
    <div class="close-div" v-show="isLoading&&!isImageGenerate">
      <div @click="handleCloseSocket">
        <el-icon size="16px">
          <VideoPause/>
        </el-icon>
        <span>暂停输出</span>
      </div>
    </div>
  </div>
  <!--  图片预览-->
  <el-dialog v-model="previewImageData.visible" style="background-color: transparent" width="35%"
             :show-close="false">
    <el-image :src="previewImageData.image"
              :preview-src-list="[previewImageData.image]"
              fit="contain"/>
    <div class="download_image">
      <span @click="downloadImage(previewImageData.image)">下载此图片</span>
    </div>
  </el-dialog>
  <auth-dialog-component :is-visible="authDialogIsVisible" @close-dialog-event="authDialogIsVisible = false"/>
</template>

<style scoped>

.container {
  width: 100%;
  background-color: #F2F2F2;
  position: relative;
}

.content-body {
  width: 100%;
  scroll-behavior: smooth;
  overflow: auto;
  box-sizing: border-box;
  height: 70vh;
  padding: 50px 50px 0;
  animation: explainAnimation 0.3s;
}


:deep(.v-md-editor__right-area) {
  width: auto !important;
}

.slide-animation {
  animation: slideEase 0.5s ease-in-out forwards;
}

@keyframes slideEase {
  0% {
    transform: translateX(-100px);
  }

  100% {
    transform: translateX(0);
  }
}

@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.close-div {
  top: 67vh;
  width: 100%;
  animation: explainAnimation 0.3s;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center
}

.close-div div {
  font-size: 13px;
  color: #5b5b5b;
  text-align: center;
  cursor: pointer;
  padding: 6px 15px;
  border-radius: 10px;
  box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
  background-color: #ffffff;

  display: flex;
  align-items: center;
}

.close-div div:hover {
  box-shadow: 0 5px 7px rgba(0, 0, 0, 0.16);
}

.close-div div > span {
  padding-left: 6px;
}

.user-chat-model {
  display: flex;
  justify-content: right;
  align-items: flex-start;
  margin-bottom: 35px;
}

.user-chat {
  background-color: #7365FF;
  color: #f4f4f4;
  margin-right: 10px;
  padding: 8px 12px;
  font-size: 14px;
  border-radius: 8px;

}

.bot-chat-model {
  display: flex;
  justify-content: left;
  align-items: flex-start;
  margin-bottom: 35px;
  color: #909090;
}

.bot-chat {
  min-width: 50px;
  background: #ffffff;
  box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
  padding: 8px 12px;
  border-radius: 8px;
  margin-left: 10px;
  font-weight: 500;

}

.bot-chat-width {
  width: 100%;
}

.drawing-answer {
  color: #5f5f5f;
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 8px;
  background-color: #ffffff;
  max-width: 40%;
  margin-left: 10px;
  box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
}

.drawing-answer-error {
  color: #5f5f5f;
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 8px;
  background-color: #ffffff;
  margin-left: 10px;
  box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
}

.drawing-img {
  width: 200px
}

:deep(.vuepress-markdown-body:not(.custom) ) {
  padding: 0;
  color: #5f5f5f;
  background-color: #ffffff !important;
  font-size: 14px;

}

:deep(.v-md-editor) {
  background-color: #ffffff;

}

.bot-answer {
  display: flex;
  width: 100%
}

.dot-flex {
  display: flex;
  padding: 5px 9px
}

.template {
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden
}

.drawing-model {
  display: flex
}

.download_image {
  display: flex;
  color: #b4abff;
  justify-content: right;
  font-size: 13px;
  text-decoration: underline
}

.download_image span {
  cursor: pointer
}

:deep(.el-dialog) {
  box-shadow: none !important;
}

:deep(.el-dialog__header) {
  padding: 0 !important;
}

:deep(.el-dialog__body) {
  padding: 0 !important;
}

.recognition-img {
  width: 200px
}
</style>
