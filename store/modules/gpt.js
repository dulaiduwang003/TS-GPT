// 引用用户请求api
import {gptTurbo} from '../../api/gpt';

const actions = {
    gptTurbo({commit}, param) {
        return new Promise((resolve, reject) => {
            gptTurbo({
                "model": "gpt-3.5-turbo",
                "messages": param
            }).then(response => {
                resolve(response)
            }).catch(error => {
                resolve(error)
            })
        })
    }
};

export default {
    namespaced: true,
    actions
}
