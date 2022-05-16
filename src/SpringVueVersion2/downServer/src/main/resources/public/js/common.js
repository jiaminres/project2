var headImageAddress
var userName
var accountID

window.addEventListener('DOMContentLoaded', function () {
    var imageAndUserName = document.querySelector('#imageAndUserName')
    var img = imageAndUserName.querySelector('img')
    var span = imageAndUserName.querySelector('#userName')

    headImageAddress = img.src
    userName = span.innerText

    console.log(userName);
})

