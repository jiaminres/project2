import Vue from 'vue'
import Vuex from 'vuex'

const session = {
    namespaced: true,
    state: {
        host: {
            accountID: "1234567",
            name: "小七",
            headImageAddress: "image/用户7.jpeg",
        },
        users: [
            {
                accountID: "1234561",
                userName: "小明",
                headImageAddress: "image/用户1.jpeg",
            },
            {
                accountID: "1234562",
                userName: "小红",
                headImageAddress: "image/用户2.jpeg",
            },
            {
                accountID: "1234563",
                userName: "张三",
                headImageAddress: "image/用户3.jpeg",
            },
            {
                accountID: "1234564",
                userName: "李四",
                headImageAddress: "image/用户4.jpeg",
            },
            {
                accountID: "1234565",
                userName: "华五",
                headImageAddress: "image/用户5.jpeg",
            },
            {
                accountID: "1234566",
                userName: "小六",
                headImageAddress: "image/用户6.jpeg",
            },

        ],
    },
    getters: {
        fullName(state) {
            return state.name + state.accountID
        }
    }
}



const chat = {
    namespaced: true,
    action: {

    },
    mutations: {
        connect(state, host) {
            if (window.WebSocket) {
                state.socket = new WebSocket("ws://localhost:8888/ws")
                let socket = state.socket
                socket.onopen = () => {
                    socket.send(JSON.stringify({ type: '/simple/welcome', text: '{' + '"userName": ' + '"' + host + '"' + '}' }))
                }
                socket.onclose = (e) => {
                    socket.send(JSON.stringify({ type: '/simple/exit', text: '{' + '"userName": ' + '"' + host + '"' + '}' }))
                }
                socket.onmessage = (e) => {
                    let data = JSON.parse(e.data)
                    let type = data.type;
                    if (type === '/simple/greeting') {
                        state.msgLists.push(data.text)
                    } else if (type === '/simple/welcome') {
                        state.aliveUsers.push(data.text)
                        state.msgLists.push({ userName: data.text.userName + '上线了', content: '' })
                    } else if (type === '/simple/exit') {
                        findExit(data.text)
                    } else if (type === '/simple/youAreWelcome') {
                        asserted(data.text)
                    } else if (type === '/user/' + host + '/simple/applyConnect') {
                        apply(data.text)
                    } else if (type === '/user/' + host + '/simple/agreeConnect') {
                        agree(data.text)
                    } else if (type === '/user/' + host + '/simple/sendMessageToUser') {
                        send(data.text)
                    }
                }
            } else {
                alert('该浏览器不支持WebSocket,请更换浏览器重新打开网址');
            }
        }
    },
    state: {
        socket: {},
        msgLists: [],
        aliveUsers: []
    },
    getters: {

    }
}



Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        chat,
        session
    }
})