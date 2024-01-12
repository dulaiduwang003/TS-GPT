import {createRouter, createWebHashHistory} from 'vue-router'
import ChatFunction from "@/views/chat/ChatFunction.vue";
import DrawingFunction from "@/views/drawing/DrawingFunction.vue";
import AppFunction from "@/views/app/AppFunction.vue";
import SettingFunction from "@/views/setting/SettingFunction.vue";
import ProgramsFunction from "@/views/programs/ProgramsFunction.vue";
import ConsoleFunction from "@/views/console/ConsoleFunction.vue";
import UserView from "@/views/console/view/user/UserView.vue";
import GptView from "@/views/console/view/gpt/GptView.vue";
import OrdersView from "@/views/console/view/orders/OrdersView.vue";
import MicroView from "@/views/console/view/micro/MicroView.vue";
import ProductView from "@/views/console/view/product/ProductView.vue";


const routes = [
    {
        path: '/',
        name: 'chat-function',
        component: ChatFunction,
        meta: {title: 'CHAT', keepAlive: true}
    },
    {
        path: '/drawing',
        name: 'drawing-function',
        component: DrawingFunction,
        meta: {title: 'DRAWING', keepAlive: true}
    },
    {
        path: '/app',
        name: 'app-function',
        component: AppFunction,
        meta: {title: 'APP', keepAlive: true}
    },
    {
        path: '/programs',
        name: 'programs-function',
        component: ProgramsFunction,
        meta: {title: 'WRITING', keepAlive: true}
    },
    {
        path: '/setting',
        name: 'setting-function',
        component: SettingFunction,
        meta: {title: 'SETTING', keepAlive: false}
    },
    {
        path: '/console',
        name: 'console-function',
        component: ConsoleFunction,
        meta: {title: 'CONSOLE', keepAlive: false},
    },
    {
        path: '/console/user',
        name: 'console-user',
        component: UserView,
        meta: {title: 'CONSOLE-USER', keepAlive: false}
    },
    {
        path: '/console/gpt',
        name: 'console-gpt',
        component: GptView,
        meta: {title: 'CONSOLE-GPT', keepAlive: false}
    },
    {
        path: '/console/orders',
        name: 'console-orders',
        component: OrdersView,
        meta: {title: 'CONSOLE-ORDERS', keepAlive: false}
    },
    {
        path: '/console/micro',
        name: 'console-micro',
        component: MicroView,
        meta: {title: 'CONSOLE-MICRO', keepAlive: false}
    },
    {
        path: '/console/product',
        name: 'console-product',
        component: ProductView,
        meta: {title: 'CONSOLE-PRODUCT', keepAlive: false}
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})


// TODO 全局前置守卫
router.beforeEach(async (to) => {
    let {title} = to.meta
    // TODO 设置浏览器Title
    document.title = (title ? title : '') + '- TS GPT'
})

export default router
