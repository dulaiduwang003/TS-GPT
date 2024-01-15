const {defineConfig} = require('@vue/cli-service')

const TerserPlugin = require('terser-webpack-plugin');
module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    productionSourceMap: false,
    // 基本路径
    publicPath:'./',
    outputDir:'dist',
    configureWebpack: {

        optimization: {
            minimizer: [
                new TerserPlugin({
                    terserOptions: {
                        compress: {
                            drop_console: true
                        },
                        mangle: true, // 开启代码混淆
                        output: {
                            comments: true // 删除注释
                        }
                    }
                })
            ]
        }
    }
})
