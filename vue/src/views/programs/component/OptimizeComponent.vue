<script setup>

import {defineEmits, defineProps, onMounted, ref, watch} from "vue";
import {getWritingConfig, setWritingConfig} from "@/utils/Utils";
import {ElNotification} from "element-plus";

const emits = defineEmits(['close']);

const props = defineProps({
  isVisual: {
    type: Boolean
  }
});

const isVisual = ref(true)

const options = [
  {
    label: '学习类',
    children: [
      {
        value: '学术论文',
        label: '学术论文',
      },
      {
        value: '议论文',
        label: '议论文',
      }, {
        value: '作业',
        label: '议论文',
      }
    ],
  },
  {
    label: '求学/求职类',
    children: [
      {
        value: '自我介绍',
        label: '自我介绍',
      },
      {
        value: '求职信',
        label: '求职信',
      }, {
        value: '个人陈述',
        label: '个人陈述',
      }, {
        value: '个人简历',
        label: '个人简历',
      }
    ],
  },
  {
    label: '工作类',
    children: [
      {
        value: '沟通邮件',
        label: '沟通邮件',
      },
      {
        value: '技术文档',
        label: '技术文档',
      }, {
        value: '法律文书',
        label: '法律文书',
      }, {
        value: '合同文书',
        label: '合同文书',
      }
    ],
  },
  {
    label: '营销类',
    children: [
      {
        value: '产品介绍',
        label: '产品介绍',
      },
      {
        value: '活动通知',
        label: '活动通知',
      }, {
        value: '活动通知',
        label: '活动通知',
      }, {
        value: '广告文案',
        label: '广告文案',
      }
    ],
  },
  {
    label: '自媒体/创作类',
    children: [
      {
        value: '新闻稿件',
        label: '新闻稿件',
      },
      {
        value: '博客',
        label: '博客',
      }, {
        value: '影评',
        label: '影评',
      }, {
        value: '书评',
        label: '书评',
      }, {
        value: '小说',
        label: '小说',
      }, {
        value: '散文',
        label: '散文',
      }, {
        value: '诗歌',
        label: '诗歌',
      }, {
        value: '剧本',
        label: '剧本',
      }
    ],
  },
  {
    label: '生活类',
    children: [
      {
        value: '日记',
        label: '日记',
      },
      {
        value: '短信息',
        label: '短信息',
      }, {
        value: '情书',
        label: '情书',
      }
    ],
  },
]

const reader = ref([
  {
    label: "领导",
    isSelected: false,
  },
  {
    label: "下属",
    isSelected: false,
  },
  {
    label: "合作者",
    isSelected: false,
  }, {
    label: "亲人",
    isSelected: false,
  },
  {
    label: "爱人",
    isSelected: false,
  }, {
    label: "朋友",
    isSelected: false,
  },
  {
    label: "同行",
    isSelected: false,
  }, {
    label: "雇主",
    isSelected: false,
  },
  {
    label: "顾客",
    isSelected: false,
  }, {
    label: "商家",
    isSelected: false,
  },
  {
    label: "用户",
    isSelected: false,
  },
  {
    label: "老师",
    isSelected: false,
  },
  {
    label: "学生",
    isSelected: false,
  }, {
    label: "群众",
    isSelected: false,
  },
  {
    label: "一般读者",
    isSelected: false,
  }
])

const writingStyle = ref([
  {
    label: "专业",
    isSelected: false,
  },
  {
    label: "口语",
    isSelected: false,
  },
  {
    label: "幽默",
    isSelected: false,
  },
  {
    label: "热情",
    isSelected: false,
  },
  {
    label: "礼貌",
    isSelected: false,
  },
  {
    label: "简洁",
    isSelected: false,
  },
  {
    label: "优雅",
    isSelected: false,
  }
])

const language = ref([{
  label: "中文",
  isSelected: false,
},
  {
    label: "英文",
    isSelected: false,
  }])

const writingConfiguration = ref({
  type: '广告文案',
  reader: '群众',
  writingStyle: '礼貌',
  language: '中文'
})


const onChangeReader = (index) => {
  reader.value.forEach(r => r.isSelected = false)
  reader.value[index].isSelected = true
  writingConfiguration.value.reader = reader.value[index].label
}

const onChangeWritingStyle = (index) => {
  writingStyle.value.forEach(r => r.isSelected = false)
  writingStyle.value[index].isSelected = true
  writingConfiguration.value.writingStyle = writingStyle.value[index].label
}

const onChangeLanguage = (index) => {
  language.value.forEach(r => r.isSelected = false)
  language.value[index].isSelected = true
  writingConfiguration.value.language = language.value[index].label
}

const preservationPlacement = () => {
  setWritingConfig(writingConfiguration.value)
  ElNotification({
    title: "生成意向",
    message: '保存成功',
    type: "success",
  });

}
onMounted(() => {
  let writingConfig = getWritingConfig();
  if (writingConfig) {
    writingConfiguration.value = writingConfig
    let number1 = writingStyle.value.findIndex(item => item.label === writingConfig.writingStyle);
    writingStyle.value[number1].isSelected = true


    let number2 = reader.value.findIndex(item => item.label === writingConfig.reader);
    reader.value[number2].isSelected = true

    let number3 = language.value.findIndex(item => item.label === writingConfig.language);
    language.value[number3].isSelected = true
  }
})

watch(
    () => props.isVisual,
    (newValue) => {
      isVisual.value = newValue;
    },
    {
      immediate: true,
    }
);

</script>

<template>
  <el-drawer v-model="isVisual" direction="rtl" @close="emits('close')">
    <template #header>

      <span class="title">写作配置</span>
    </template>
    <template #default>
      <div class="title-child">
        生成类型
      </div>
      <div>
        <el-cascader v-model="writingConfiguration.type" :options="options" :show-all-levels="false"/>
      </div>
      <div class="title-child">
        目标读者
      </div>
      <div class="reader">
        <div :class="item.isSelected?'reader-div-active':'reader-div'"
             v-for="(item,index) in reader"
             :key="index"
             @click="onChangeReader(index)">
          {{ item.label }}
        </div>
      </div>
      <div class="title-child">
        生成风格
      </div>
      <div class="reader">
        <div :class="item.isSelected?'reader-div-active':'reader-div'"
             v-for="(item,index) in writingStyle"
             :key="index"
             @click="onChangeWritingStyle(index)"
        >
          {{ item.label }}
        </div>
      </div>
      <div class="title-child">
        生成语种
      </div>
      <div class="reader">
        <div :class="item.isSelected?'languages-active':'languages'"
             v-for="(item,index) in language"
             :key="index"
             @click="onChangeLanguage(index)"
        >
          {{ item.label }}
        </div>
      </div>
    </template>
    <template #footer>
      <div>
        <el-button type="primary"
                   @click="preservationPlacement"
                   class="btn-submit">保存
        </el-button>
      </div>
    </template>
  </el-drawer>
</template>

<style scoped>


.btn-submit {
  width: 100px;
  background-color: #783CFA;
  border: none;
  border-radius: 4px
}

.btn-submit:hover,
.btn-submit:focus,
.btn-submit:active {
  background-color: #783CFA;
  outline: 0;
  color: white;
}

.languages {
  padding: 3px 25px;
  font-size: 10px;
  border: 1px solid #dedede;
  color: #8b8b8b;
  border-radius: 5px;
  margin-right: 10px;
  transition: background-color 0.3s;
  cursor: pointer;
}

.languages:hover {
  border: 1px solid rgb(120, 60, 250);
  background-color: rgb(247, 240, 255);
}

.languages-active {
  padding: 3px 25px;
  font-size: 10px;
  border: 1px solid rgb(120, 60, 250);
  color: rgb(120, 60, 250);
  border-radius: 5px;
  margin-right: 10px;
  background-color: rgb(247, 240, 255);
}

.title {
  font-size: 17px;
  font-weight: 550;
  color: #2b2b2b
}

.reader {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.reader-div {
  border: 1px solid #dedede;
  border-radius: 15px;
  font-size: 11px;
  padding: 2px 10px;
  color: #8b8b8b;
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: background-color 0.3s;

}

.reader-div:hover {
  border: 1px solid rgb(120, 60, 250);
  background-color: rgb(247, 240, 255);
}

.reader-div-active {
  border: 1px solid rgb(120, 60, 250);
  border-radius: 15px;
  background-color: rgb(247, 240, 255);
  color: rgb(120, 60, 250);;
  font-size: 11px;
  padding: 2px 10px;
  margin-right: 10px;
  margin-bottom: 10px;
}

.title-child {
  font-size: 13px;
  color: #4b4b4b;
  margin-top: 20px;
  padding-bottom: 10px
}

:deep(.el-input__wrapper) {
  border-radius: 10px;
  font-size: 12px;
}
</style>
