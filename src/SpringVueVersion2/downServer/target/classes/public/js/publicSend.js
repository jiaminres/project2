
window.addEventListener('DOMContentLoaded', function () {
    var _userName = userName
    var laba = document.querySelector('.laba')
    var sendBtn = document.querySelector('#send')
    var closeBtn = document.querySelector('#close')
    var textArea = document.querySelector('#editBox').querySelector('textarea')
    var publicShow = document.querySelector('#p_showBox')
    laba.addEventListener('click', function () {
        if (laba.innerText == '') {
            // 禁喇叭字体图标
            laba.innerText = ''
            sendBtn.removeEventListener('click', privateClickCallback)
            sendBtn.addEventListener('click', send)
            textArea.style.backgroundColor = '#0b0c10'
            textArea.style.color = '#fff000'
            sendBtn.style.color = '#fff000'
            closeBtn.style.color = '#fff000'
        } else {
            laba.innerText = ''
            sendBtn.addEventListener('click', privateClickCallback)
            sendBtn.removeEventListener('click', send)
            textArea.style.backgroundColor = "#fff"
            textArea.style.color = '#000'
            sendBtn.style.color = '#000'
            closeBtn.style.color = '#000'
        }

    })

    function send() {
        var msg = textArea.value
        stompClient.send("/complex/hello", {}, JSON.stringify({ 'userName': _userName, 'content': msg }))

    }
})