var stompClient = null;

function connect() {
    var socket = new SockJS("/chat")
    stompClient = Stomp.over(socket)
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/simple/greeting', function (greeting) {
            showGreeting(JSON.parse(greeting.body))
        })
        stompClient.subscribe('/simple/welcome', function(welcome){
            findAlive(JSON.parse(welcome.body))
        })
        stompClient.subscribe('/simple/exit',function(exit){
            findExit(JSON.parse(exit.body))
        })
        stompClient.subscribe('/simple/youAreWelcome',function(opposite){
            asserted(JSON.parse(opposite.body))
        })
        stompClient.subscribe('/user/'+userName+'/simple/confirmConnect',function(apply){
            confirm(JSON.parse(apply.body))
        })
        //连接上，向全体人员发送广播，以发现在线人员
        stompClient.send('/simple/welcome',{},JSON.stringify({'userName': userName}))
        stompClient.send('/complex/applyConnect',{},JSON.stringify({'userName': userName}))
    })



}

function disconnect() {
    if (stompClient !== null) {
        stompClient.send('/simple/exit',{},JSON.stringify({'userName': userName}))
        stompClient.disconnect()
    }
}

function showGreeting(obj) {

    var p = document.createElement('p')
    var textArea = document.querySelector('#editBox').querySelector('textarea')
    var publicShow = document.querySelector('#p_showBox')

    p.innerHTML = "<span style='color:red'>" + obj.userName.substring(0,obj.userName.lastIndexOf('@')) + "</span>" + " : " + obj.content
    publicShow.appendChild(p)
    textArea.value = ''

    if (focus == true) return

    publicShow.scrollTop = publicShow.scrollHeight - publicShow.clientHeight
}

function findAlive(obj){
    var _userName = obj.userName
    var userList = document.querySelector('#userList').querySelectorAll('li')
    var publicShow = document.querySelector('#p_showBox')
    for(var i = 0; i < userList.length; i++){
        // console.log(userList[i].querySelector('img').alt + "  " + _userName)
        if(userList[i].querySelector('img').alt === _userName){
            //设置上线时，字体图标的颜色变绿
            userList[i].querySelector('i').style.color = 'green'
            //设置上线时，进行广播公告
            var p = document.createElement('p')
            p.innerHTML = "<span style='color:green'>" + _userName+ "上线了</span>"
            publicShow.appendChild(p)
            //告知对方自己的存在
            stompClient.send('/simple/youAreWelcome',{},JSON.stringify({'userName': userName}))
            return
        }
    }
}

function findExit(obj){
    var _userName = obj.userName
    var userList = document.querySelector('#userList').querySelectorAll('li')
    var publicShow = document.querySelector('#p_showBox')
    for(var i = 0; i < userList.length; i++){
        // console.log(userList[i].querySelector('img').alt + "  " + _userName)
        if(userList[i].querySelector('img').alt === _userName){
            //设置下线时，字体图标的颜色变蓝
            userList[i].querySelector('i').style.color = 'red'
            //设置下线时，进行广播公告
            var p = document.createElement('p')
            p.innerHTML = "<span style='color:red'>" + _userName+ "下线了</span>"
            publicShow.appendChild(p)
            return
        }
    }
}

function asserted(obj){
    var _userName = obj.userName
    var userList = document.querySelector('#userList').querySelectorAll('li')
    var publicShow = document.querySelector('#p_showBox')
    for(var i = 0; i < userList.length; i++) {
        if (userList[i].querySelector('img').alt === _userName) {
            //记录当前在线人数
            userList[i].querySelector('i').style.color = 'green'
            return
        }
    }
}

function confirm(obj){

}

window.onload = function () {
    window.onbeforeunload = function () {
        disconnect()
    }
    connect()
}