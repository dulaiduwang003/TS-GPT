<script setup>
import {Refresh} from "@element-plus/icons-vue";
import {defineEmits, onMounted, ref} from "vue";

const emits = defineEmits(['update-prompt-input-text']);

const issueRaw = ref(require("../../../../data/issue.json"))

const issueNew = ref([])
/**
 * 获取欢迎语
 * @returns {string}
 */
const getGreetings = () => {
  const now = new Date()
// 获取当前时间戳
  const timestamp = now.getHours()
// 判断当前时间是早上、中午、下午、晚上还是凌晨
  if (timestamp >= 0 && timestamp < 6) {
    return "凌晨好！ 早点休息，明天才能有精神！"
  } else if (timestamp >= 6 && timestamp < 12) {
    return "上午好！ 今天过得怎么样"
  } else if (timestamp >= 12 && timestamp < 18) {
    return "下午好！ 希望您度过一个愉快的下午"
  } else if (timestamp >= 18 && timestamp < 24) {
    return "晚上好！ 很高兴在这个时间与您交流"
  }
}


/**
 * 刷新数据
 */
const flushed = () => {
  let randomItems = [];
  let itemsCopy = [...issueRaw.value]; // 复制数组对象以避免修改原始数据

  while (randomItems.length < 6 && itemsCopy.length > 0) {
    const randomIndex = Math.floor(Math.random() * itemsCopy.length);
    const randomItem = itemsCopy.splice(randomIndex, 1)[0];
    randomItems.push(randomItem);
  }
  issueNew.value = randomItems
}

onMounted(() => {
  flushed()
})


</script>

<template>
  <div class="container">
    <div class="bot-chat-model">
      <div>
        <el-avatar :size="40"
                   :src="require('../../../../assets/app/chat.svg')"/>
      </div>
      <div class="bot-chat-width">
        <div class="bot-model slide-animation">
          <div class="greeting">{{ getGreetings() }}</div>
          <div class="content">
            您好 我是<span>TS-GPT</span>,
            我能够处理和分析信息数据，并且能够根据需要进行学习和适应。除此之外，我还能理解和生成自然语言，与人类进行有效的沟通。我能够处理多种语言，并且能够理解和解释情感和语境。这些能力使我能够更好地理解和满足用户的需求。
          </div>
          <div class="introduce">
            <div>
              <div>
                你可以试着问我:
              </div>
              <div class="flushed" @click="flushed">
                <el-icon>
                  <Refresh/>
                </el-icon>
                <span>换一换</span>
              </div>
            </div>
          </div>
          <div class="layout">
            <div v-for="(item,index) in issueNew" :key="index" class="layout-div"
                 @click="emits('update-prompt-input-text',item.context)">
              <div class="wave_animation" v-if="index===0">
                <img class="wave type-1" :src="require('../../../../assets/images/public/attach.jpeg')" alt=""/>
              </div>
              <div class="positioning">
                <div class="positioning-title">
                  <span class="positioning-icon">{{ item.icon }}</span>
                  <span>{{ item.title }}</span>
                </div>
                <div class="positioning-introduce">
                  {{ item.context }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  height: 70vh;
  width: 100%;

  box-sizing: border-box;
  animation: explainAnimation 0.3s;
  padding: 50px 50px 0;
}

.wave_animation {
  width: 100%;
  animation: wave 5s ease-in-out infinite;
}

.wave {
  width: 100%;
  height: 190px;
  background-size: cover;
  filter: hue-rotate(300deg);
  transform: perspective(120px) rotateY(-50deg) rotateX(-20deg) skew(-55deg, -17deg);

}

.bot-chat-model {
  display: flex;
  justify-content: left;
  align-items: flex-start;
  margin-bottom: 35px;
  color: #747474;
}


.greeting {
  font-size: 22px;
  color: #353535
}

.positioning {
  position: absolute;
  z-index: 2;
  top: 0;
  width: 100%;
  height: 100%;
  padding: 8px
}

.positioning-title {
  font-size: 19px;

  font-weight: 550;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
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

.positioning-icon {
  padding-right: 6px
}

.positioning-introduce {
  padding-top: 10px;
  font-size: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

@keyframes wave {
  0% {
    transform: translate3d(0, 0, 0);
  }
  50% {
    transform: translate3d(10%, 10%, 0);
  }
  100% {
    transform: translate3d(0, 0, 0);
  }
}

.content {
  font-size: 12px;
  margin-top: 8px
}

.layout {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 15px;

}


.layout-div {
  width: 49%;
  height: 80px;
  border-radius: 5px;
  background-color: #F6F8FD;
  cursor: pointer;
  margin: 0.5%;
  position: relative;
  overflow: hidden;
}

.layout-div:hover {
  color: #b535ff;
}

.content span {
  color: #b535ff;
  font-weight: 600
}

.introduce {
  padding-top: 25px
}

.introduce > div {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px
}

.flushed {
  display: flex;
  align-items: center
}

.flushed span {
  padding-left: 5px
}

.flushed:hover {

  color: #993bff;
  font-weight: 600;
  cursor: pointer;
}

.bot-model {
  margin-left: 10px;
  padding: 8px 12px;
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 5px 7px rgb(0 0 0 / 6%);
  font-size: 14px;
  color: #5f5f5f;
}
</style>
