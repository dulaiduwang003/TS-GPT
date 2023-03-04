
import request from './../utils/request';


export function gptTurbo(data) {
	return request({
		url: 'v1/chat/completions',
		method: 'POST',
		data
	})
}

