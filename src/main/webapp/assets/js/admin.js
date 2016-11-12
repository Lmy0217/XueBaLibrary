
$(document).ready(function(){
    $(".adminloginout").click(function(){
				if(localStorage.getItem("admin") == null){
					alert("未登录，请重新登录！");
					location.href = "admin-login.html";
				}else{
					adminloginout();
				}
				
		})
})

function adminloginout(){
		var url = 'loginout.action';
			$.ajax({
					url: url,
					type: "POST",
					data: {type:"admin"},
					success: function (result) {
						if(result.success == "true"){
							alert("注销成功，请重新登录！");
							localStorage.removeItem("admin");
							location.href = result.url;
							}else{
							alert(result.info);
							localStorage.removeItem("admin");
							location.href = "admin-login.html";
						}
					},
					error:function (result){
					},
			});
}
function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]); return null; //返回参数值
        }