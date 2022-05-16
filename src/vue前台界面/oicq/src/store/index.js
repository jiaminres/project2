import Vue from 'vue'
import Vuex from 'vuex'




const group = {
    namespaced: true,
    actions: {
        initRoom(context, room) {
            context.commit('INITROOM', room)
        },
        initHost(context, host) {
            context.commit('INITHOST', host)
        },
        initUsers(context, users) {
            context.commit('INITUSERS', users)
        },
        initGroups(context, groups) {
            context.commit('INITGROUPS', groups);
        },
        updateSelected(context, selected) {
            context.commit('UPDATESELECTED', selected);
        },
        connect(context) {
            if (window.WebSocket) {
                let socket = {}
                socket = new WebSocket("ws://localhost/ws")
                socket.onopen = () => {
                    socket.send(JSON.stringify({ type: '/simple/welcome', text: '{' + '"userName": ' + '"' + context.getters.fullName + '"' + '}' }))
                }
                socket.onclose = (e) => {
                    // socket.send(JSON.stringify({ type: '/simple/exit', text: '{' + '"userName": ' + '"' + context.getters.fullName + '"' + '}' }))
                }
                socket.onmessage = (e) => {
                    let data = JSON.parse(e.data)
                    let type = data.type;
                    if (type === '/simple/greeting') {
                        context.commit('updateMsgList', data.text)
                    } else if (type === '/simple/welcome') {
                        if (data.text.userName === context.getters.fullName) return
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
                        let selectedUserName = context.getters.totalUsers[context.state.selected]
                        if (context.state.agreeUsers.indexOf(data.text.userName) !== -1) {
                            context.commit('updateRecordUsers', data.text.userName)
                            context.commit('updateAgreeUsers', context.state.agreeUsers.filter(userName => userName !== data.text.userName))
                        }
                        //有用户退出时，指针也要随之变化
                        if (selectedUserName === data.text.userName) {
                            context.commit('UPDATESELECTED', context.state.agreeUsers.length - 1)
                        } else {
                            context.commit('UPDATESELECTED', context.getters.totalUsers.indexOf(selectedUserName))
                        }

                        context.commit('updateAliveUsers', context.state.aliveUsers.filter(userName => userName !== data.text.userName))
                        context.commit('updateApplyUsers', context.state.applyUsers.filter(userName => userName !== data.text.userName))
                        context.commit('updateMsgList', { userName: data.text.userName + '下线了@', content: '' })
                    } else if (type === '/simple/youAreWelcome') {
                        if (data.text.userName === context.getters.fullName) return
                        if (context.state.aliveUsers.indexOf(data.text.userName) !== -1) return
                        context.commit('updateAliveUsers', data.text.userName)
                    } else if (type === '/user/' + context.getters.fullName + '/simple/applyConnect') {
                        if (context.state.applyUsers.indexOf(data.text.sourceUserName) !== -1) return
                        context.commit('updateApplyUsers', data.text.sourceUserName)
                    } else if (type === '/user/' + context.getters.fullName + '/simple/agreeConnect') {
                        context.commit('updateRecordUsers', context.state.recordUsers.filter(userName => userName !== data.text.sourceUserName))
                        context.commit('updateApplyUsers', context.state.applyUsers.filter(userName => userName !== data.text.sourceUserName))
                        context.commit('updateAgreeUsers', data.text.sourceUserName)
                    } else if (type === '/user/' + context.getters.fullName + '/simple/sendMessageToUser') {
                        context.commit('updatePrivateMsg', data.text)
                        if (data.text.sourceName === context.state.agreeUsers[context.state.selected]) return
                        if (context.state.newMsgUsers.indexOf(data.text.sourceName) !== -1) return
                        context.commit('updateNewMsgUsers', data.text.sourceName)
                    }
                }
                context.commit('updateSocket', socket)
            } else {
                alert('该浏览器不支持WebSocket,请更换浏览器重新打开网址');
            }
        },
        disconnect(context) {
            if (context.state.socket !== null) {
                //浏览器退出时，可能不是在交流页出现，所以可能socket仍没创建,只是个空对象
                //检查socket.send方法是否存在
                if (context.state.socket.send) {
                    context.state.socket.send(JSON.stringify({ type: '/simple/exit', text: '{' + '"userName": ' + '"' + context.getters.fullName + '"' + '}' }))
                    context.state.socket.close()
                    //关闭后应该重新设置为空对象，以免浏览器退出时调用该函数，存在已经关闭的socke对象，仍然调用socket方法
                    context.state.socket = {}
                }
            }
        },
        destroyHost(context) {
            context.commit("DESTROYHOST")
        },
        destroyData(context) {
            context.commit('DESTROYDATA')
        },
        destroyMsg(context) {
            context.commit('DESTROYMSG')
        },
        sendPrivate(context, obj) {
            context.state.socket.send(JSON.stringify(obj))
            context.commit('updatePrivateMsg', JSON.parse(obj.text))
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
            context.commit('updateRecordUsers', context.state.recordUsers.filter((userName) => {
                return userName !== tar
            }))
        }
    },
    mutations: {
        DESTROYHOST(state) {
            state.host = {}
        },
        DESTROYDATA(state) {
            //删除群组的联系，再次进入交流页面时，群组联系已经发生变化
            state.aliveUsers = []
            state.applyUsers = []
            state.agreeUsers = []
            state.recordUsers = []
            state.totalUsers = []
            state.newMsgUsers = []
            state.selected = 0
        },
        DESTROYMSG(state) {
            state.msgList = []
            state.room = {}
            state.privateMsg = {}
        },
        UPDATESELECTED(state, selected) {
            state.selected = selected
        },
        INITROOM(state, room) {
            state.room = room;
        },
        INITUSERS(state, users) {
            state.users = users
        },
        INITHOST(state, host) {
            state.host = host
        },
        INITGROUPS(state, groups) {
            if (!(groups instanceof Array)) {
                state.groups.push(groups)
            } else {
                state.groups = groups
            }
        },
        updateHeadImage(state, headImageAddress) {
            state.host.headImageAddress = headImageAddress
        },
        updateUserName(state, userName) {
            state.host.userName = userName
        },
        updateDetail(state, obj) {
            state.host.detail.sex = obj.sex;
            state.host.detail.age = obj.age;
            state.host.detail.birthday = obj.birthday;
            state.host.detail.location = obj.location;
            state.host.detail.persionalSign = obj.persionalSign;
        },
        updateSocket(state, socket) {
            state.socket = socket
        },
        updateMsgList(state, msg) {
            msg.userName = msg.userName.substring(0, msg.userName.lastIndexOf('@'))
            state.msgList.push(msg)
        },
        updateAliveUsers(state, aliveUser) {
            if (!(aliveUser instanceof Array)) {
                state.aliveUsers.push(aliveUser)
            } else {
                state.aliveUsers = aliveUser
            }
        },
        updateApplyUsers(state, applyUser) {
            if (!(applyUser instanceof Array)) {
                state.applyUsers.push(applyUser)
            }
            else {
                state.applyUsers = applyUser
            }
        },
        deleteApplyUsers(state, applyUser) {
            state.applyUsers = state.applyUsers.filter((name) => name !== applyUser)
        },
        updateAgreeUsers(state, agreeUser) {
            if (!(agreeUser instanceof Array)) {
                state.agreeUsers.push(agreeUser)
                // if (!(state.privateMsg[agreeUser])) {
                //     Vue.set(state.privateMsg, agreeUser, [])
                // }
            }
            else {
                state.agreeUsers = agreeUser
            }

        },
        updateNewMsgUsers(state, newMsgUsers) {
            if (!(newMsgUsers instanceof Array))
                state.newMsgUsers.push(newMsgUsers)
            else {
                state.newMsgUsers = newMsgUsers
            }
        },
        updateRecordUsers(state, recordUser) {
            if (!(recordUser instanceof Array))
                state.recordUsers.push(recordUser)
            else {
                state.recordUsers = recordUser
            }
        },
        deleteAgreeUsers(state, agreeUser) {
            state.agreeUsers = state.agreeUsers.filter((name) => {
                name !== agreeUser
            })
        },
        updatePrivateMsgBefore(state, privateMsg) {
            if (privateMsg.sourceID !== state.host.userID) {
                state.privateMsg[privateMsg.sourceName].unshift(privateMsg)
            }
            else {
                state.privateMsg[privateMsg.targetName].unshift(privateMsg)
            }
        },
        updatePrivateMsg(state, privateMsg) {
            if (privateMsg.sourceID !== state.host.userID) {
                if (!(state.privateMsg[privateMsg.sourceName])) {
                    Vue.set(state.privateMsg, privateMsg.sourceName, [])
                }
                state.privateMsg[privateMsg.sourceName].push(privateMsg)
            }
            else {
                if (!(state.privateMsg[privateMsg.targetName])) {
                    Vue.set(state.privateMsg, privateMsg.targetName, [])
                }
                state.privateMsg[privateMsg.targetName].push(privateMsg)
            }
        },


    },
    // aliveUsers -> applyUsers -> agreeUsers -> newMsgUsers
    // agreeUsers 和 applyUsers互斥
    // agreeUsers 和 recordUsers 互斥

    state: {
        host: {},
        room: {},
        users: [],
        groups: [],
        socket: {},
        msgList: [],
        aliveUsers: [],
        applyUsers: [],

        agreeUsers: [],
        recordUsers: [],
        totalUsers: [],

        newMsgUsers: [],
        privateMsg: {},
        selected: 0

    },
    getters: {
        fullName(state) {
            return state.host.userName + '@' + state.host.userID
        },
        totalUsers(state) {
            return state.agreeUsers.concat(state.recordUsers)
        }
    }
}



Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        group
    }
})