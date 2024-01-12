<script setup>

import {defineEmits, defineProps, ref} from "vue";

const emits = defineEmits(["handle-selected-card", "created-orders"]);

const props = defineProps({
  data: {
    type: Array
  },
});

const scrollDiv = ref(null);

const scrollLeft = ref(0);

const tableData = ref([
  {
    equityNickname: '模型对话',
    isMember: '✔️',
    notMember: '❌'
  },
  {
    equityNickname: '对话识图',
    isMember: '✔️',
    notMember: '❌'
  },
  {
    equityNickname: '对话生图',
    isMember: '✔️',
    notMember: '❌'
  },
  {
    equityNickname: '输出长度调节',
    isMember: '✔️',
    notMember: '❌'
  },
  {
    equityNickname: '局部重绘',
    isMember: '✔️',
    notMember: '❌'
  },
  {
    equityNickname: 'DALL绘图模型',
    isMember: '✔️',
    notMember: '❌'
  },
  {
    equityNickname: 'SD绘图模型',
    isMember: '✔️',
    notMember: '❌'
  }
]);
const handleScroll = (event) => {
  // 阻止默认行为（防止页面滚动）
  event.preventDefault();

  // 获取滚动方向（正为向前，负为向后）
  const delta = event.deltaY;

  // 根据滚动方向移动滚动条
  if (delta > 0) {
    scrollLeft.value += 100; // 向右滚动100px
  } else {
    scrollLeft.value -= 100; // 向左滚动100px
  }

  // 应用滚动位置到 scrollDiv 元素上
  if (scrollDiv.value) {
    scrollDiv.value.scrollLeft = scrollLeft.value;
  }
};

const tableRowClassName = ({rowIndex}) => {
  if (rowIndex % 2 === 0) {
    return 'warning-row';
  }
  return '';
};

</script>

<template>
  <div style="width: 700px">
    <div class="vip-container">
      <img :src="require('../../../assets/app/member.svg')" class="vip-icon" alt=""/>
      选择您的<span>TS-GPT</span>订阅计划!
    </div>
    <div v-if="props.data.length>0">
      <div ref="scrollDiv" class="scroll-div" @wheel="handleScroll">
        <div>
          <div :class="item.isSelected?'card-active':'card-default'" v-for="(item,index) in props.data" :key="index"
               @click="emits('handle-selected-card',index)">
            <div class="card-title">
              {{ item.productName }}
            </div>
            <div class="card-price">
              ￥ {{ item.price }}
            </div>
          </div>
        </div>
      </div>
      <div class="rights">
        <div>
          加入TS会员 享受所有功能
        </div>
      </div>
      <el-table
          class="el-table"
          :data="tableData"
          style="width: 100%;height: 240px;margin-top: 20px"
          :row-class-name="tableRowClassName"
      >
        <el-table-column prop="equityNickname" label="权益对比" width="180"/>
        <el-table-column prop="isMember" label="会员" width="180"/>
        <el-table-column prop="notMember" label="非会员"/>
      </el-table>
      <div class="btn-model">
        <el-button type="primary"
                   @click="emits('created-orders')"
                   class="submit-btn">
          我选好了,创建订单
        </el-button>
      </div>
    </div>
    <div class="empty-product">
      <div>
        <img :src="require('../../../assets/app/empty.svg')" alt="">
        <div>抱歉,当前未上架任何商品</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.empty-product {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 500px;
  text-align: center;
  padding-bottom: 80px
}

.empty-product img {
  width: 180px;
  height: 180px;
  padding-top: 40px
}

.submit-btn {
  border: none;
  width: 240px;
  height: 45px;
  border-radius: 20px;
  background: linear-gradient(to right, rgb(151, 125, 189), rgb(119, 103, 191));
}

.btn-model {
  text-align: center;
  padding-top: 10px
}

.scroll-div {
  width: 100%;
  overflow-x: scroll;
  white-space: nowrap;
  padding: 8px;

}

.scroll-div > div {
  display: flex;
}

.vip-icon {
  width: 25px;
  height: 25px;
  margin-right: 5px
}


.vip-container {
  padding: 8px;
  font-size: 19px;
  font-weight: 550;
  color: #171717;
  display: flex;
  align-items: center;;
  margin-bottom: 8px;
}

.vip-container > span {
  font-weight: 600
}

.rights {
  text-align: center;
  margin-top: 40px
}

.rights > div {
  font-size: 18px;
  font-weight: 600;
  background-clip: text;
  background-image: linear-gradient(90deg, #5d51fe, #a049f7 29.61%, #cb4da4 65.91%, #ff8080 97.79%);
  color: transparent;
}

.card-default {
  cursor: pointer;
  min-width: 250px;
  padding: 12px;
  color: white;
  font-weight: 500;
  margin-right: 10px;
  height: 120px;
  background-image: url('../../../assets/app/vip.svg');
  background-size: cover;
  background-position: center;
  border-radius: 8px;
  overflow: hidden;
  width: 250px;

  filter: hue-rotate(-100deg) brightness(0.5);
}


.card-active {
  cursor: pointer;
  min-width: 250px;
  padding: 12px;
  color: white;
  font-weight: 500;
  margin-right: 10px;
  height: 120px;
  background-image: url('../../../assets/app/vip.svg');
  background-size: cover;
  background-position: center;
  filter: hue-rotate(300deg);
  border-radius: 8px;
  overflow: hidden;
  width: 250px;

}

.card-title {
  font-size: 18px;
}

.card-price {
  font-size: 30px;
  padding-top: 10px
}
</style>
