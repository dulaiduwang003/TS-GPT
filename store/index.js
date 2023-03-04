import Vue from 'vue'
import Vuex from 'vuex'
import gpt from './modules/gpt'

Vue.use(Vuex)
const store = new Vuex.Store({
	modules: {
		gpt
	}
});

export default store
