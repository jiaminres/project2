import Vue from 'vue'
import Vuex from 'vuex'




const group = {
    namespaced: true,
    actions: {
        init(context, val) {
            context.commit('INIT', val)
            // console.log(socket + '***-----------------------------------------------');
        },
        connect(context) {
            if (window.WebSocket) {
                let socket = {}
                socket = new WebSocket("ws://localhost:8888/ws")
                socket.onopen = () => {
                    socket.send(JSON.stringify({ type: '/simple/welcome', text: '{' + '"userName": ' + '"' + context.getters.fullName + '"' + '}' }))
                }
                socket.onclose = (e) => {
                    socket.send(JSON.stringify({ type: '/simple/exit', text: '{' + '"userName": ' + '"' + context.getters.fullName + '"' + '}' }))
                }
                socket.onmessage = (e) => {
                    let data = JSON.parse(e.data)
                    let type = data.type;
                    if (type === '/simple/greeting') {
                        context.commit('updateMsgList', data.text)
                    } else if (type === '/simple/welcome') {
                        context.commit('updateAliveUsers', data.text.userName)
                        context.commit('updateMsgList', { userName: data.text.userName + '上线了@', content: '' })
                        socket.send(
                            JSON.stringify({
                                type: "/simple/youAreWelcome",
                                text:
                                    "{" +
                                    '"userName": ' +
                                    '"' +
                                    context.getters.fullName +
                                    '"' +
                                    "}",
                            })
                        )
                    } else if (type === '/simple/exit') {
                        context.commit('updateAliveUsers', state.aliveUsers.filters((userName) => {
                            return userName !== data.text.userName
                        }))
                        context.commit('updateMsgList', { userName: data.text.userName + '下线了@', content: '' })
                    } else if (type === '/simple/youAreWelcome') {
                        context.commit('updateAliveUsers', data.text.userName)
                    } else if (type === '/user/' + context.getters.fullName + '/simple/applyConnect') {
                        context.commit('updateApplyUsers', data.text.sourceUserName)
                    } else if (type === '/user/' + context.getters.fullName + '/simple/agreeConnect') {
                        context.commit('updateAgreeUsers', data.text.sourceUserName)
                    } else if (type === '/user/' + context.getters.fullName + '/simple/sendMessageToUser') {
                        context.commit('updatePrivateMsg', { direction: 'opposite', content: data.text })
                    }
                }
                context.commit('updateSocket', socket)
            } else {
                alert('该浏览器不支持WebSocket,请更换浏览器重新打开网址');
            }
        },
        disconnect(context) {
            if (context.state.socket !== null) {
                context.state.socket.close()
            }
        },
        sendPrivate(context, obj) {
            context.state.socket.send(JSON.stringify(obj))
            let privateMsg = {
                direction: 'owner',
                content: JSON.parse(obj.text)
            }
            context.commit('updatePrivateMsg', privateMsg)
        },
        sendPublic(context, obj) {
            context.state.socket.send(JSON.stringify(obj))
        },
        sendApply(context, obj) {
            context.state.socket.send(JSON.stringify(obj))
        },
        sendAgree(context, obj) {
            context.state.socket.send((JSON.stringify(obj)))
            let tar = JSON.parse(obj.text).targetUserName
            context.commit('deleteApplyUsers', tar)
            context.commit('updateAgreeUsers', tar)
        }


    },
    mutations: {
        INIT(state, val) {
            state.host = val.host
            state.users = val.users
        },

        updateSocket(state, socket) {
            state.socket = socket
        },
        updateMsgList(state, msg) {
            msg.userName = msg.userName.substring(0, msg.userName.lastIndexOf('@'))
            state.msgList.push(msg)
        },
        updateAliveUsers(state, aliveUser) {
            if (!(aliveUser instanceof Array))
                state.aliveUsers.push(aliveUser)
            else {
                state.aliveUsers = aliveUser
            }
        },
        updateApplyUsers(state, applyUser) {
            state.applyUsers.push(applyUser)
        },
        deleteApplyUsers(state, applyUser) {
            state.applyUser = state.applyUser.filter((name) => {
                name !== applyUser
            })
        },
        updateAgreeUsers(state, agreeUser) {
            state.agreeUser.push(agreeUser)
        },
        deleteAgreeUsers(state, agreeUser) {
            state.agreeUser = state.agreeUser.filter((name) => {
                name !== agreeUser
            })
        },
        updatePrivateMsg(state, privateMsg) {
            state.privateMsg.push(privateMsg)
        },


    },
    state: {
        host: {},
        users: [],
        socket: {},
        msgList: [],
        aliveUsers: [],
        applyUsers: [],
        agreeUsers: [],
        privateMsg: []

    },
    getters: {
        fullName(state) {
            return state.host.name + '@' + state.host.accountID
        }
    }
}



Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        group
    }
})