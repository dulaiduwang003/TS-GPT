import {createStore} from 'vuex'

export default createStore({
    state: {
        //用户数据
        userInfo: undefined,
        //用户设置
        userSetting: undefined,
        theme: undefined,
    },
    getters: {
        userInfo: (state) => state.userInfo,
        userSetting: (state) => state.userSetting,
    },
    mutations: {
        logout(state) {
            state.userInfo = undefined
            localStorage.removeItem("token")
            localStorage.removeItem("userInfo")
        },
        setUserInfo(state, info) {
            state.userInfo = info;
            localStorage.setItem("userInfo", JSON.stringify(info));
        },

        setUserSetting(state, info) {
            state.userSetting = info;
            localStorage.setItem("userSetting", JSON.stringify(info));
        },
        initState(state) {
            let token = localStorage.getItem("token");
            if (token) {
                let user = localStorage.getItem("userInfo");
                let setting = localStorage.getItem("userSetting");
                state.userInfo = JSON.parse(user);
                state.userSetting = JSON.parse(setting);
            }
        },
    },
    actions: {},
    modules: {}
})
