<script setup>

import {defineEmits, defineExpose, nextTick, onMounted, ref} from "vue";
import {getCurrentZhCnDate} from "@/utils/Utils";
import ImageProtractStatusComponent from "@/views/drawing/components/components/ImageProtractStatusComponent.vue";
import {reqDeleteDrawingOpus, reqGetDrawingOpus, reqGetDrawingTask} from "@/api/drawing";
import LocalDrawingComponent from "@/views/drawing/components/components/LocalDrawingComponent.vue";
import {ElLoading, ElNotification} from "element-plus";
import store from "@/store";


const emits = defineEmits(['accept-sd-local-drawings', 'accept-dall-local-drawings']);

const drawingList = ref([])

const drawingTimerList = ref([])

const scrollRef = ref(null)

const isSdPartialRepaintVisible = ref(false)

const isDallPartialRepaintVisible = ref(false)

const sdPartialRepaintImage = ref('')

const dallPartialRepaintImage = ref('')

const removeVisibility = ref(true)

const timeList = []


/**
 * 追加任务
 */
const unShiftDrawingList = async (data) => {
  drawingList.value.push({
    taskId: data.taskId,
    drawingType: data.drawingType,
    image: '',
    status: 'PENDING',
    prompt: data.prompt,
    createdTime: new Date()
  })
  // 添加定时器列表
  drawingTimerList.value.push({
    taskId: data.taskId,
    mark: false
  });
  createdDrawingListener(data.taskId, drawingList.value.length - 1, drawingTimerList.value.length - 1)
  await nextTick(() => {
    scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
  });
}

/**
 * 提交SD绘图任务
 * @param data
 */
const submitSdPartialDrawing = (data) => {
  emits('accept-sd-local-drawings', data)
}

/**
 * 提交Dall绘图任务
 * @param data
 */
const submitDallPartialDrawing = (data) => {
  emits('accept-dall-local-drawings', data)
}


/**
 * 匹配局部绘图
 */
const switchPartialRepaint = (type, image) => {
  if (type === 'SD') {
    handleSdPartialRepaint(image)
  } else {
    handleDallPartialRepaint(image)
  }
}

/**
 * 打开SD局部重绘
 * @param image
 */
const handleSdPartialRepaint = (image) => {
  sdPartialRepaintImage.value = image
  isSdPartialRepaintVisible.value = true
}

/**
 * 打开Dall局部重绘
 * @param image
 */
const handleDallPartialRepaint = (image) => {
  dallPartialRepaintImage.value = image
  isDallPartialRepaintVisible.value = true
}


/**
 * 删除指定图片
 * @param taskId
 */
const handleDeleteDrawing = async (taskId) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteDrawingOpus({
      taskId: taskId
    });
    await init()
    ElNotification({
      message: '删除成功',
      type: "success",
    });
  } catch (e) {
    ElNotification({
      message: e.msg,
      type: "error",
    });
  } finally {
    setTimeout(() => {
      service.close()
    }, 300)
  }

}

/**
 * 创建  侦听器
 * @param taskId
 * @param index
 * @param number
 */
const createdDrawingListener = (taskId, index, number) => {
  let interval = setInterval(async () => {
    if (!drawingTimerList.value[number].mark) {
      drawingTimerList.value[number].mark = true;
      let res = await reqGetDrawingTask(taskId);
      if (res.data) {
        let status = res.data.status;
        drawingList.value[index].status = status
        switch (status) {
            //EXECUTION
          case 'PROCESSING': {
            if (res.data.extra) {
              const {current_image, progress} = res.data.extra
              if (current_image) {
                drawingList.value[index].image = 'data:image/png;base64,' + current_image;
              }
              if (progress !== 0) {
                drawingList.value[index].progress = progress
              }
            }
            drawingTimerList.value[number].mark = false;
            break
          }
          case 'SUCCEED':
            if (res.data.image) {
              drawingList.value[index].image = res.data.image;
              drawingList.value[index].progress = 100;
              drawingTimerList.value[number].mark = false;
              clearInterval(interval);
            }
            break
          case 'DISUSE':
            drawingTimerList.value[number].mark = false;
            clearInterval(interval);
            break
          default:
            drawingTimerList.value[number].mark = false;
        }
      } else {
        clearInterval(interval);
      }
    }
  }, 2000)
  timeList.push(interval)
}

/**
 * 初始化
 * @returns {Promise<void>}
 */
const init = async () => {
  //关闭定时器
  if (timeList.length > 0) {
    timeList.forEach(t => {
      clearInterval(t)
    })
  }

  const {data} = await reqGetDrawingOpus();
  if (data) {
    drawingList.value = data
    const arr = data.filter((item) => (item.status === 'PENDING' || item.status === 'PROCESSING'));
    //启动监听器
    if (arr.length > 0) {
      arr.forEach(a => {
        //获取坐标
        let index = drawingList.value.findIndex(item => {
          const {taskId} = item
          return taskId === a.taskId
        });
        // 添加定时器列表
        drawingTimerList.value.push({
          taskId: a.taskId,
          mark: false
        });
        createdDrawingListener(a.taskId, index, drawingTimerList.value.length - 1)
      })
    }
    // await nextTick(() => {
    //   scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
    // });

  }
}

onMounted(() => {
  if (store.getters.userInfo) {
    init()
  }
})


defineExpose(
    {unShiftDrawingList}
)

</script>

<template>
  <div class="container">
    <!--    列表区域-->
    <div ref="scrollRef" class="content-container" v-if="drawingList.length>0">
      <div v-for="item in drawingList" :key="item.taskId">
        <el-card class="card-container">
          <template #header>
            <div class="drawing-operation">
              <div>
                {{ getCurrentZhCnDate(item.createdTime) }}
              </div>
              <div>
                <img class="delete-img" :src="require('../../../assets/app/chat/deleted.svg')" alt=""
                     @click="handleDeleteDrawing(item.taskId)" v-if="removeVisibility">
              </div>
            </div>
            <div class="card-title">
              该任务由{{ item.drawingType }}模型生成 ,task_id: {{ item.taskId }}
            </div>
          </template>
          <image-protract-status-component :image="item.image"
                                           :progress="item.progress"
                                           :status="item.status"
                                           :type="item.drawingType"
                                           @partial-repaint="switchPartialRepaint"/>
        </el-card>
      </div>
    </div>
    <div class="empty-data" v-else>
      <div>
        <img :src="require('../../../assets/app/empty.svg')" class="empty-img" alt="">
        <div class="empty-prompt">
          空空如也 ╮ ( ￣▽￣ ) ╭
        </div>
      </div>
    </div>
    <!--    局部重绘-->
    <local-drawing-component :isVisible="isSdPartialRepaintVisible" :image="sdPartialRepaintImage"
                             @close="isSdPartialRepaintVisible=false" @submit="submitSdPartialDrawing"/>
    <!--    dall-->
    <local-drawing-component :isVisible="isDallPartialRepaintVisible" :image="dallPartialRepaintImage"
                             @close="isDallPartialRepaintVisible=false" @submit="submitDallPartialDrawing"/>
  </div>

</template>

<style scoped>
.container {
  width: 72%;
  background-color: #efefef;
  padding: 11px;

  animation: explainAnimation 0.3s;
}

@keyframes explainAnimation {
  from {
    transform: scale(0);
  }

  to {
    transform: scale(1);
  }
}

.card-container {
  width: 100%;
  margin-bottom: 10px;
}

.delete-img {
  width: 15px;
  height: 15px;
  cursor: pointer;
}

.content-container {
  font-size: 12px;
  padding: 8px;
  height: 100%;
  width: 100%;
  background-color: white;
  border-radius: 10px;
  scroll-behavior: smooth;
  overflow: auto;
  box-sizing: border-box;
  font-weight: 400;

}

.card-title {
  margin-top: 10px;
  color: #9b9b9b;
}

.empty-data {
  height: 100%;
  width: 100%;
  background-color: white;
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.drawing-operation {
  display: flex;
  justify-content: space-between;
  font-weight: 400;
  color: #575757;
}

.empty-img {
  width: 250px;
  height: 250px
}

.empty-prompt {
  text-align: center;
  padding-bottom: 80px;
  color: #898989
}
</style>
