const username = $.cookie("username");
if(username === undefined){
    $(".login-success").addClass("login-hidden");
}else {
    $(".login").addClass("login-hidden");
}