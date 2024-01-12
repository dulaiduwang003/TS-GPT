<script setup>

import {onMounted, ref} from "vue";

import {useRouter} from "vue-router";
import {reqDeleteException, reqGetExceptionPage, reqGetSiteData} from "@/api/ahiManagement";
import {ElLoading, ElNotification} from "element-plus";
import store from "@/store";

const router = useRouter();

const menu = ref([
  {
    icon: 'orders.svg',
    text: '订单管理',
    to: '/console/orders'
  },
  {
    icon: 'user.svg',
    text: '用户管理',
    to: '/console/user'
  },
  {
    icon: 'chat.svg',
    text: 'GPT配置',
    to: '/console/gpt'
  },
  // {
  //   icon: 'drawing.svg',
  //   text: '绘图配置',
  //   to: '/orders'
  // },
  // {
  //   icon: 'applets.svg',
  //   text: '小程序管理',
  //   to: '/orders'
  // },
  {
    icon: 'prompt.svg',
    text: '提示词管理',
    to: '/console/micro'
  },
  {
    icon: 'commodity.svg',
    text: '商品管理',
    to: '/console/product'
  }
  // ,
  // {
  //   icon: 'redeem.svg',
  //   text: '兑换码管理',
  //   to: '/orders'
  // }
])

const tableData = ref([])

const siteData = ref({
  newUsers: 0,
  totalUsers: 0,
  newRevenue: 0,
  totalRevenue: 0,
  newTrade: 0,
  totalTrade: 0,
  newVisits: 0,
  totalVisits: 0
})

const total = ref(0)

const pageNum = ref(1)

/**
 * 获取站点数据
 */
const getSiteData = async () => {
  try {
    const {data} = await reqGetSiteData();
    siteData.value = data
  } catch (e) {
    console.log(e)
  }
}

/**
 * 删除异常
 * @param id
 */
const handleDeleteException = async (id) => {
  let service = ElLoading.service({fullscreen: true, text: '正在删除中'})
  try {
    await reqDeleteException({exceptionId: id});
    //刷新数据
    await getExceptionPage(1)
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
 * 获取异常分页信息
 * @param pageNum
 * @returns {Promise<void>}
 */
const getExceptionPage = async (pageNum) => {
  let service = ElLoading.service({fullscreen: true, text: '正在获取数据'})
  try {
    const {data} = await reqGetExceptionPage(pageNum);
    if (data.records) {
      tableData.value = data.records
      total.value = data.total
    }
  } catch (e) {
    console.log(e)
  } finally {
    setTimeout(() => {
      service.close()
    }, 300)
  }
}

onMounted(() => {
  let userInfo = store.getters.userInfo;
  if (userInfo) {
    const {type} = userInfo;
    if (type !== "ADMIN") {
      router.push({
        path: "/#"
      });
      return
    }
  } else {
    router.push({
      path: "/#"
    });
    return
  }

  getSiteData()
  getExceptionPage(pageNum.value)
})

</script>

<template>

  <div class="card">
    <div class="card-background">
      <div class="card-title">
        项目参数
      </div>
      <div>
        <div class="project-flex">
          <div class="project-width">
            <div>项目名称</div>
          </div>
          <div>
            TS-GPT
          </div>
        </div>
        <div class="project-flex">
          <div class="project-width">
            <div>项目版本</div>
          </div>
          <div>
            0.9.1
          </div>
        </div>
        <div class="project-flex">
          <div class="project-width">
            <div>项目地址</div>
          </div>
          <div class="link">
            https://github.com/dulaiduwang003/TS-GPT
          </div>
        </div>
      </div>
    </div>
    <div class="card-background card-width-60">
      <div class="card-title">
        今日数据
      </div>
      <div class="day-box">
        <div class="day-row">
          <div>新用户</div>
          <div class="day-data">
            {{ siteData.newUsers ? siteData.newUsers : 0 }}
          </div>
          <div class="day-additional">
            总用户 {{ siteData.totalUsers ? siteData.totalUsers : 0 }}
          </div>
        </div>
        <div class="day-row">
          <div>今日收入</div>
          <div class="day-data">
            {{ siteData.newRevenue ? siteData.newRevenue : 0 }}
          </div>
          <div class="day-additional">
            总收入 {{ siteData.totalRevenue ? siteData.totalRevenue : 0.00 }}
          </div>
        </div>
        <div class="day-row">
          <div>交易订单数</div>
          <div class="day-data">
            {{ siteData.newTrade ? siteData.newTrade : 0 }}
          </div>
          <div class="day-additional">
            总订单交易数 {{ siteData.totalTrade ? siteData.totalTrade : 0 }}
          </div>
        </div>
        <div class="day-row">
          <div>今日访问量</div>
          <div class="day-data">
            {{ siteData.newVisits ? siteData.newVisits : 0 }}
          </div>
          <div class="day-additional">
            总访问量 {{ siteData.totalVisits ? siteData.totalVisits : 0 }}
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="operating">

    <div class="card card-under-60 matter">
      <div class="card-title">
        待处理事项
      </div>
      <el-table :data="tableData" stripe class="el-table" v-if="tableData.length>0"
                :header-cell-style="{'text-align':'center','font-size':'13px','font-weight':'500'}"
                :cell-style="{'font-size':'12px'}"
      >
        <el-table-column prop="serverName" label="服务名称" width="140"/>
        <el-table-column prop="level" label="异常级别">
          <template #default="scope">
            <div class="column-flex">
              <div class="type-low" v-if="scope.row.level==='LOW'">
                {{ scope.row.level }}
              </div>
              <div class="type-middle" v-if="scope.row.level==='MIDDLE'">
                {{ scope.row.level }}
              </div>
              <div class="type-height" v-if="scope.row.level==='HEIGHT'">
                {{ scope.row.level }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="cause" label="信息" width="300" />
        <el-table-column prop="createdTime" label="触发时间" width="200px"/>
        <el-table-column fixed="right" label="操作" width="200px">
          <template #default="scope">

            <el-button link type="primary" size="small" style="color: #ff3b3b"
                       @click="handleDeleteException(scope.row.exceptionId)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-padding"  v-if="tableData.length>0">
        <el-pagination background layout="prev, pager, next"
                       :total="total"
                       :page-size="15"
                       @current-change="($event)=>getExceptionPage($event)"/>
      </div>
      <div class="matter-empty" v-else>
        <div>
          <div>
            <img :src="require('../../../assets/app/empty.svg')" alt=""/>
          </div>
          <div class="matter-empty-prompt">
            当前服务器没有提示任何状况
          </div>
        </div>
      </div>
    </div>
    <div class="card card-under-39">
      <div style="width: 100%">
        <div class="card-title">
          操作入口
        </div>
        <div class="card-btn">
          <div class="operating-wrap" v-for="(item,index) in menu" :key="index"
               @click="router.push({ path: item.to })">
            <div>
              <div class="wrap-btn">
                <img :src="require('../../../assets/console/'+item.icon)" alt=""/>
              </div>
              <div class="wrap-text">{{ item.text }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wrap-text {
  text-align: center;
  padding-top: 6px;
  color: #a0a0a0
}

:deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: rgb(153, 59, 255)
}

:deep(.el-pagination.is-background .el-pager li) {
  background-color: transparent;
}

.column-flex {
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-padding {
  padding-top: 10px
}

.type-low {
  width: 130px;
  height: 25px;
  font-size: 11px;
  border-radius: 4px;
  background-color: rgb(59, 180, 255);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.type-middle {
  width: 130px;
  height: 25px;
  font-size: 11px;
  border-radius: 4px;
  background-color: rgb(255, 199, 59);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.type-height {
  width: 130px;
  height: 25px;
  font-size: 11px;
  border-radius: 4px;
  background-color: rgb(250, 36, 36);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center
}

.el-table {
  width: 100%;
  height: 100%
}

.operating {
  flex: 1;
  display: flex;
  justify-content: space-between
}

.matter {
  flex-direction: column
}

.matter-empty {
  text-align: center;
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center
}

.matter-empty img {
  width: 200px;
  height: 200px
}

.matter-empty-prompt {
  font-size: 14px;
  color: #787878;
  padding-top: 10px
}

.card-btn {
  display: flex;
  align-items: center;
  flex-wrap: wrap
}

.operating-wrap {
  display: flex;
  margin: 10px 12px;

}

.wrap-btn {
  transition: transform 0.3s; /* 定义过渡效果的时间 */
  border-radius: 5px;
  background-color: #ededed;
  width: 60px;
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 1%;
  cursor: pointer;
}

.wrap-btn:hover {
  transform: scale(1.2);
}

.wrap-btn img {
  width: 30px;
  height: 30px;
}

.card-under-60 {
  margin-top: 1%;
  border-radius: 5px;
  background-color: white;
  width: 60%;
  padding: 15px
}

.card-under-39 {
  margin-top: 1%;
  border-radius: 5px;
  background-color: white;
  width: 39%;
  padding: 15px
}

.day-additional {
  padding-top: 10px;
  font-size: 11px;

}

.day-box {
  display: flex;
  justify-content: space-between;
  font-size: 14px
}

.day-row {
  text-align: center;
  font-size: 13px;
}

.day-data {
  font-size: 16px;
  padding-top: 10px;
  font-weight: 600
}

.card {
  display: flex;
  justify-content: space-between;
  font-size: 11px
}

.link {
  color: #4e98cc
}

.card-background {
  background-color: #ffffff;
  width: 39%;
  border-radius: 5px;
  padding: 15px
}

.card-width-60 {
  width: 60%;
}

.card-title {
  padding-bottom: 15px;
  border-bottom: 1px solid #efefef;
  color: #979797;
  margin-bottom: 20px
}

.project-width {
  padding-bottom: 15px;
  width: 70px;
}

.project-flex {
  display: flex
}
</style>
