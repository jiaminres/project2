(function () {
    var ul
    var lis
    var bind
    window.addEventListener('DOMContentLoaded', function () {
        ul = document.getElementById('nav')
        lis = ul.getElementsByTagName('li')
        for (var i = 0; i < lis.length; i++) {
            lis[i].style.paddingLeft = 0 + 'px'
            lis[i].style.paddingRight = 0 + 'px'

        }
        window.addEventListener('resize', setPadding)
        setPadding()
    })

    function setPadding() {
        var ulWid = ul.clientWidth
        var lisWid = 0
        var dif
        var difaveg
        for (var i = 0; i < lis.length; i++) {
            lisWid += lis[i].clientWidth
        }
        // 这里随便减个5，只是为了避免计算精度的问题，导致nav没显示全
        dif = ulWid - lisWid - 5
        difaveg = dif / (lis.length * 2)

        for (var i = 0; i < lis.length; i++) {
            var paddingLeft = parseInt(lis[i].style.paddingLeft.substring(0, lis[i].style.paddingLeft.length - 2))
            var paddingRight = parseInt(lis[i].style.paddingRight.substring(0, lis[i].style.paddingRight.length - 2))
            lis[i].style.paddingLeft = paddingLeft + difaveg + 'px'
            lis[i].style.paddingRight = paddingRight + difaveg + 'px'
        }


    }
}())