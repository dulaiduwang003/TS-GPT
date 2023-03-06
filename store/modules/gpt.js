// 引用用户请求api
import {gpt003, gptTurbo} from '@/api/gpt';

import env from '../../utils/env';

import {
    getOpenKey
} from "@/utils/data";

const actions = {
    gptTurbo({commit}, param) {
        return new Promise((resolve, reject) => {
            gptTurbo({
                "model": "gpt-3.5-turbo",
                "messages": param,
				"openKey": getOpenKey(),
				"ip": `${env.ip}`,
				"port": `${env.port}`
            }).then(response => {
                resolve(response)
            }).catch(error => {
                reject(error)
            })
        })
    },
    gpt003({commit}, param) {
        return new Promise((resolve, reject) => {
            gpt003({
                "model": "text-davinci-003",
                "prompt": param,
				"openKey": `${env.key}`,
				"ip": `${env.ip}`,
				"port": `${env.port}`
            }).then(response => {
                resolve(response)
            }).catch(error => {
                reject(error)
            })
        })
    },
};

export default {
    namespaced: true,
    actions
}
