import {createApp} from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'
import '@/utils/Title'

// TODO ElementPlus
import ElementPlus from 'element-plus';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/theme-chalk/index.css';
import 'element-plus/theme-chalk/display.css';
import locale from 'element-plus/lib/locale/lang/zh-cn'

// TODO v-md-editor
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
// TODO Prism
import Prism from 'prismjs';
// TODO 代码高亮
import 'prismjs/components/prism-json';
// TODO Copy Code 快捷复制代码
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';

VMdEditor.use(vuepressTheme, {
    Prism
});
VMdEditor.use(createCopyCodePlugin());

let app = createApp(App)
// v-md-editor
app.use(VMdEditor);
app.use(ElementPlus, {locale})
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(store);
app.use(router);
app.mount('#app')
