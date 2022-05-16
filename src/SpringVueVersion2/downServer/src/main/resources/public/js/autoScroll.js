var focus = false
window.addEventListener('DOMContentLoaded', function () {
    var publicShowBox = document.querySelector('#p_showBox')
    publicShowBox.addEventListener('scroll', function () {
        focus = true
        if ((publicShowBox.scrollHeight - publicShowBox.clientHeight - publicShowBox.scrollTop) < 5) {
            focus = false
        }
    })
})