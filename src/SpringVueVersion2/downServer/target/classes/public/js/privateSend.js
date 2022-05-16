var _userName = null
var sendBtn = null
var showBox = null
var input = null
var privateClickCallback = function () {
    var user = document.createElement('div')
    user.className = 'user'

    var bind = document.createElement('div')
    bind.className = 'bind'

    var userName = document.createElement('span')
    userName.className = 'userName'
    userName.innerText = _userName

    var userImage = document.createElement('img')
    userImage.className = 'userImage'
    userImage.src = headImageAddress

    var br = document.createElement('br')
    var widthLimit = document.createElement('div')
    widthLimit.className = 'widthLimit'

    var userMessage = document.createElement('p')
    userMessage.className = 'userMessage'
    userMessage.innerHTML = input.value

    var triangle = document.createElement('i')
    triangle.className = 'triangle'

    bind.appendChild(userName)
    bind.appendChild(userImage)
    // console.log(bind.clientWidth);

    widthLimit.appendChild(userMessage)
    widthLimit.appendChild(triangle)

    user.appendChild(bind)
    user.appendChild(widthLimit)

    showBox.appendChild(user)


    var msgWid = userMessage.offsetWidth + 36
    if (msgWid < widthLimit.clientWidth) {
        userMessage.style.left = widthLimit.clientWidth - msgWid + 'px'
    } else {
        userMessage.style.width = widthLimit.clientWidth - 36 - 20 + 'px'
    }

    bind.style.left = 'calc(100% - ' + bind.clientWidth + 'px)'

    showBox.scrollTop = showBox.scrollHeight - showBox.clientHeight
    input.value = ''
}

window.addEventListener('DOMContentLoaded', function () {
    _userName = userName
    sendBtn = document.querySelector('#send')
    showBox = document.querySelector('#showBox')
    input = document.querySelector('#editBox').getElementsByTagName('textArea')[0]
    send.addEventListener('click', privateClickCallback)

})
