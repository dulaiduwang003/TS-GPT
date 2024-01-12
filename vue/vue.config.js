const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    // 基本路径
    publicPath:'./',
    outputDir:'dist'
})
