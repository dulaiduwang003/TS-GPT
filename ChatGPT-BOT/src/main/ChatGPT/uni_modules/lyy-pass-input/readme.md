# lyy-pass-input

#### 介绍
仿微信支付密码输入组件

(依赖`uni-popup`组件，css语言为`scss`)
## 1、使用说明
1、点击<span class="banner"><a class="btn btn-hx-import hmt-btn-hx-import" onclick="download_plugin(1, 0)" href="javascript:void(0)">
使用 HBuilderX 导入插件</a></span>  
2、引入`uni-popup`  
3、在自己的项目中添加、配置`<lyy-pass-input></lyy-pass-input>`组件标签

## 2、属性说明
| 属性 | 类型 | 默认值 | 说明 |
|---|---|---|---|
|title|String|-|提示文字-标题|
|money|Number|-|提示文字-金额，无标题不显示|
|digit|Number|6|密码位数，范围：4~8|
|@close|Function|-|关闭事件|
|@confirm|Function|-|输入完成事件，返回值：`{pass:''}`|


## 3、插槽

|插槽名称|说明|
|---|---|
|extend|扩展插槽，显示在金额和密码框之间|

## 4、ref方法

|方法|说明|示例|
|---|---|---|
|open|打开组件|`this.$refs.passInput.open()`|
|close|关闭组件|`this.$refs.passInput.close()`|
|clear|清空组件|`this.$refs.passInput.clear()`|

## 5、使用方法

`<lyy-pass-input ref="passInput" title="提现" money="100" @confirm="confirm" @close="close">
</lyy-pass-input>`

为组件添加`ref`属性，页面调用`this.$refs[ref值].open()`打开组件