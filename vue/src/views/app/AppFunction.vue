<script setup>

import TopComponent from "@/views/app/components/TopComponent.vue";
import AllContentComponent from "@/views/app/components/AllContentComponent.vue";
import {onMounted, ref} from "vue";
import LoadComponent from "@/components/LoadComponent.vue";
import LoadingComponent from "@/views/app/components/LoadingComponent.vue";
import {reqGetMicroAppPage, searchMicroApp, useMicroApp} from "@/api/chat";
import SearchContentComponent from "@/views/app/components/SearchContentComponent.vue";
import EmptyComponent from "@/views/app/components/EmptyComponent.vue";
import {ElLoading, ElNotification} from "element-plus";
import store from "@/store";
import AuthDialogComponent from "@/components/auth/AuthDialogComponent.vue";

const authDialogIsVisible = ref(false)

const allMicroList = ref([])

const searchMicroList = ref([])

const prompt = ref('')

const isSearchLoading = ref(false)

const isLoading = ref(false)

const allPageNum = ref(0)

const isSearch = ref(false)

const searchPageNum = ref(0)

/**
 * 搜索
 * @returns {Promise<void>}
 */
const searchPageData = async () => {
  let value = prompt.value;
  if (!isSearchLoading.value && value.trim()) {
    isSearch.value = true
    try {
      searchPageNum.value = 0
      const {data} = await searchMicroApp(searchPageNum.value, value);
      if (data.records && data.records.length > 0) {
        searchMicroList.value.push(...data.records)
      }
    } catch (e) {

      console.log(e)
    } finally {
      isSearchLoading.value = false
    }
  }
}

/**
 * 加载搜索分页数据
 * @returns {Promise<void>}
 */
const loadSearchPageData = async () => {
  if (!isLoading.value) {
    try {
      searchPageNum.value++
      isLoading.value = true;
      const {data} = await searchMicroApp(searchPageNum.value, prompt.value);
      if (data.records && data.records.length > 0) {
        searchMicroList.value.push(...data.records)
      } else {
        searchPageNum.value--
      }
    } catch (e) {
      console.log(e)
    } finally {
      setTimeout(() => {
        isLoading.value = false
      }, 500)
    }
  }
}

/**
 * 加载所有分页数据
 * @returns {Promise<void>}
 */
const loadAllPageData = async () => {
  if (!isLoading.value) {
    try {
      allPageNum.value++
      isLoading.value = true;
      const {data} = await reqGetMicroAppPage(allPageNum.value);
      if (data.records && data.records.length > 0) {
        allMicroList.value.push(...data.records)
      } else {
        allPageNum.value--
      }
    } catch (e) {
      console.log(e)
    } finally {
      setTimeout(() => {
        isLoading.value = false
      }, 500)
    }
  }
}

/**
 * 应用模板
 * @param id
 * @param issue
 * @param answer
 * @returns {Promise<void>}
 */
const handleUseTemplate = async (id, issue, answer) => {
  if (!store.getters.userInfo) {
    authDialogIsVisible.value = true
    return
  }

  let service = ElLoading.service({fullscreen: true, text: '正在应用此模板'});
  try {
    await useMicroApp({microAppId: id})
    let userSetting = store.getters.userSetting;
    userSetting.presetWordIssue = issue
    userSetting.presetWordAnswer = answer
    store.commit("setUserSetting", userSetting);
    ElNotification({
      title: "公共提示库",
      message: '应用成功,您可以前往设置中查看前置预设词',
      type: "success",
    });
  } catch (e) {
    ElNotification({
      title: "提示词库", message: e.msg, type: "error"
    });
  } finally {
    service.close()
  }
}

/**
 * 更新变量
 * @param e
 */
const uploadPrompt = (e) => {
  prompt.value = e
}

/**
 * 更新变量
 * @param e
 */
const uploadSearchType = (e) => {
  isSearch.value = e
  allPageNum.value = 0
}

onMounted(() => {
  loadAllPageData()
})

</script>

<template>
  <div class="body">
    <loading-component :is-loading="isLoading"/>
    <div class="body-container">
      <!--顶部-->
      <top-component
          @search="searchPageData"
          @update-prompt="uploadPrompt"
          :isSearchLoading="isSearchLoading"
          @update-search-type="uploadSearchType"
      />
      <!--生成数据渲染-->
      <div class="content">
        <ul
            v-if="!isSearch&&allMicroList.length>0"
            v-infinite-scroll="loadAllPageData"
            :infinite-scroll-immediate="false">
          <li>
            <all-content-component v-for="(item,index) in allMicroList" :key="index" :data="item"
                                   @handle-use-template="handleUseTemplate"/>
          </li>
        </ul>
        <empty-component text="这里空空如也" v-if="!isSearch&&  allMicroList.length<=0"/>
        <ul
            v-if="isSearch&&searchMicroList.length>0"
            v-infinite-scroll="loadSearchPageData"
            :infinite-scroll-immediate="false">
          <li>
            <search-content-component :data="searchMicroList" @handle-use-template="handleUseTemplate"/>
          </li>
        </ul>
        <empty-component text="没有搜索到任何数据" v-if="isSearch&&searchMicroList.length<=0"/>
        <load-component text="正在搜索中" v-if="isSearchLoading"/>
      </div>

    </div>
  </div>
  <auth-dialog-component :is-visible="authDialogIsVisible" @close-dialog-event="authDialogIsVisible = false"/>
</template>

<style scoped>
.body {
  margin: 0;
  padding: 10px;
  background-color: #F3F3F3;
  min-height: 100vh;
  max-height: 100%;
  display: block;
  position: relative;
}

.content {
  position: relative;

}

.body-container {
  margin: 0 auto;
  padding-top: 20px;
  flex: 1;

  //overflow: hidden;
  max-width: 1000px;
}

ul {
  margin: 0;
  padding-left: 1px;
  height: 100%;
}
</style>
