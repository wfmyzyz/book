function needLogin() {
    if ($.cookie("token") == undefined || $.cookie("token") == ""){
        layer.alert("请登录！",function () {
            window.location.href = "/backstage/login.html";
        })
    }
}

function goLogin() {
    layer.alert("请登录！",function () {
        $.cookie("token","",{expires: -1,path: '/backstage'});
        $.cookie("publicKey","",{expires: -1,path: '/backstage'});
        window.location.href = "/backstage/login.html";
    })
}