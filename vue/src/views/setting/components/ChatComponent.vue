<script setup>

//快捷键数组
import {defineProps, onMounted, ref} from "vue";
import store from "@/store";

import {ElNotification} from "element-plus";

const props = defineProps({
  modelList: {
    type: Array
  }
});

//快捷键配置列表
const shortcutList = ref([
  {
    text: 'enter'
  },
  {
    text: 'shift + enter'
  },
  {
    text: 'ctrl + enter'
  },
  {
    text: 'alt + enter'
  }
])
//模型坐标
const modelIndex = ref(0)
//机器人柔性
const chatBotAvatar = ref('')
//机器人昵称
const chatBotName = ref('')
//快捷键坐标
const shortcutIndex = ref(0)

//输出速率
const outputRate = ref(0)
//上下文
const memorySize = ref(0)
//函数
const rowSize = ref(4)
//bottag
const chatBotTag = ref('')


/**
 * 上传图片
 * @param e
 * @returns {boolean}
 */
const onChangeChatBotAvatar = (e) => {
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
        chatBotAvatar.value = event.target.result
        //获取原有配置
        let oldData = store.getters.userSetting;
        oldData.chatBotAvatar = chatBotAvatar.value
        store.commit("setUserSetting", oldData);
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
 * 更新快捷键
 * @param e
 */
const onChangeShortcut = (e) => {
  //获取原有配置
  let oldData = store.getters.userSetting;
  oldData.shortcutIndex = e
  store.commit("setUserSetting", oldData);
}

/**
 * 切换模型
 * @param e
 */
const onChangeAiModel = (e) => {
  //获取原有配置
  let oldData = store.getters.userSetting;
  oldData.modelIndex = e
  store.commit("setUserSetting", oldData);
}


/**
 * 更新BOT昵称
 */
const onBlurChatBotName = () => {
  let value = chatBotName.value;
  if (value.trim().length > 0) {
    //获取原有配置
    let oldData = store.getters.userSetting;
    oldData.chatBotName = value
    store.commit("setUserSetting", oldData);
  } else {
    ElNotification({
      message: 'BOT昵称不能为空', type: 'error',
    });
  }
}

/**
 * 更新BOT狗牌
 */
const onBlurChatBotTag = () => {
  let value = chatBotTag.value;
  if (value.trim().length > 0) {
    //获取原有配置
    let oldData = store.getters.userSetting;
    oldData.chatBotTag = value
    store.commit("setUserSetting", oldData);
  } else {

    ElNotification({
      message: '狗牌昵称不能为空', type: 'error',
    });
  }
}



/**
 * 更新速率
 */
const onBlurOutputRate = () => {
  let value = outputRate.value;
  if (value && value > 0) {
    //获取原有配置
    let oldData = store.getters.userSetting;
    oldData.outputRate = value
    store.commit("setUserSetting", oldData);
  } else {
    ElNotification({
      message: '回复速率至少需要大于0', type: 'error',
    });
  }
}
/**
 * 更新记忆
 */
const onBlurRowSize = () => {
  let value = rowSize.value;
  if (value && value > 0) {
    //获取原有配置
    let oldData = store.getters.userSetting;
    oldData.rowSize = value
    store.commit("setUserSetting", oldData);
  } else {

    ElNotification({
      message: '记忆至少需要大于0', type: 'error',
    })
  }
}

/**
 * 更新记忆
 */
const onBlurMemory = () => {
  let value = memorySize.value;
  if (value && value > 0) {
    //获取原有配置
    let oldData = store.getters.userSetting;
    oldData.memorySize = value
    store.commit("setUserSetting", oldData);
  } else {

    ElNotification({
      message: '上下文至少大于0', type: 'error',
    });
  }
}


onMounted(async () => {
  if (store.getters.userInfo) {
    //读取个人设置信息
    let userSetting = store.getters.userSetting;
    modelIndex.value = userSetting.modelIndex
    chatBotName.value = userSetting.chatBotName
    shortcutIndex.value = userSetting.shortcutIndex
    outputRate.value = userSetting.outputRate
    memorySize.value = userSetting.memorySize
    rowSize.value = userSetting.rowSize
    chatBotAvatar.value = userSetting.chatBotAvatar
    chatBotTag.value = userSetting.chatBotTag

  }
})
</script>

<template>
  <div class="appearance-title">
    聊天配置
  </div>
  <div class="appearance-body">
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          机器人头像
          <div class="prompt-label">更改此处将会替换对话机器人头像</div>
        </div>
        <div>
          <el-upload :auto-upload="false"
                     :on-change="($event)=>onChangeChatBotAvatar($event)"
                     :show-file-list="false">
            <img :src="chatBotAvatar?chatBotAvatar:require('../../../assets/app/chat.svg')" class="bot-upload"
                 alt="">
          </el-upload>
        </div>
      </div>
    </div>
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          BOT昵称
          <div class="prompt-label">更改此处将会替换对话机器人昵称</div>
        </div>
        <div>
          <el-input class="input-width" placeholder="请设置BOT昵称" v-model="chatBotName" @blur="onBlurChatBotName"/>
        </div>
      </div>
    </div>
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          BOT狗牌
          <div class="prompt-label">更改此处将会替换对话机器人狗牌昵称</div>
        </div>
        <div>
          <el-input class="input-width" placeholder="请设置BOT狗牌昵称" v-model="chatBotTag" @blur="onBlurChatBotTag"/>
        </div>
      </div>
    </div>
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          快捷发送
          <div class="prompt-label">选择发送信息快捷键</div>
        </div>
        <div>
          <el-select class="input-width" v-model="shortcutIndex" @change="onChangeShortcut">
            <el-option
                v-for="(item,index) in shortcutList"
                :key="index"
                :label="item.text"
                :value="index"
            />
          </el-select>
        </div>
      </div>
    </div>
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          对话模型
          <div class="prompt-label">选择与您对话的自然模型</div>
        </div>
        <div>
          <el-select class="input-width" v-model="modelIndex" @change="onChangeAiModel">
            <el-option
                v-for="item in props.modelList"
                :key="item.modelIndex"
                :label="item.modelName"
                :value="item.modelIndex"
            />
          </el-select>
        </div>
      </div>
    </div>

    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          上下文压缩阈值
          <div class="prompt-label">如果超出设定值将会压缩数据用于保证对话功能连贯性</div>
        </div>
        <div>
          <el-input class="input-width" placeholder="请设置上下文压缩阈值" type="number" v-model="memorySize"
                    @blur="onBlurMemory"/>
        </div>
      </div>
    </div>
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          记忆行数
         <div class="prompt-label">表示每次对话模型将会识别多少条最新记录</div>
        </div>
        <div>
          <el-input class="input-width" placeholder="请设置记忆行数" type="number" v-model="rowSize"
                    @blur="onBlurRowSize"/>
        </div>
      </div>
    </div>
    <div class="div-width">
      <div class="div-row">
        <div class="prompt-title">
          对话输出速率
          <div class="prompt-label">文字刷新率 强制输出文字以设定值频率输出数据用于保证视觉效果流畅性 注意此值过低容易造成输出过快导致回显效果差</div>
        </div>
        <div>
          <el-input class="input-width" placeholder="请设置回复速率" type="number" v-model="outputRate"
                    @blur="onBlurOutputRate"/>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

.appearance-title {
  font-size: 14px;
  padding-left: 10px;
  font-weight: 550;
}

.appearance-body {
  background-color: white;
  margin-top: 10px;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 30px;
}

.input-width {
  width: 240px;
}

.div-width {
  width: 100%
}

.div-row {
  padding-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  font-size: 14px;
  border-bottom: 1px solid #f3f3f3;
  margin-bottom: 15px;
  color: #8b8b8b;
}

.input-textarea {
  align-items: flex-start
}

:deep(.el-textarea__inner) {
  height: 130px;
  background-color: #F2F2F2;
  box-shadow: none !important;
  resize: none !important;
  outline: none !important;

}

:deep(.el-input__wrapper) {
  box-shadow: none;
  font-weight: 550;
}

.bot-upload {
  border-radius: 100%;
  width: 45px;
  height: 45px
}

:deep(.el-input__inner) {
  text-align: right;
}

.prompt-label {
  width: 340px;
  font-size: 12px;
  color: #9d9d9d;
  padding-top: 8px
}
.prompt-title{
  color: #5b5b5b;
}
</style>
