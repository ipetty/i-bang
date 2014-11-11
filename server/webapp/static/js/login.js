$.namespace("Ibang.login");
Ibang.login = {
	init: function(){
		this.initEvent();
	}, 
	initEvent: function(){
		var that = this;
		
		$("#loginBtn").bind("click", function(){
			var url = "/admin/web/login";
			
			if (Ibang.Base.throttle.isLock(url)) {
				return false;
			}
			Ibang.Base.throttle.lock(url); // 正在操作中..
			Ibang.login.hideLoginMsg();
			
			var data = Ibang.login.checkData();
			if (data == false) {
				Ibang.Base.throttle.unLock(url);
				return false;
			}
			
			$("#loginBtn").addClass("disabled");

			$.ajax({
				type: 'post',
				url: Ibang.Config.appUrl + url,
				data: data,
				dataType: 'json',
				success: function(msg){
					if (msg.success) {
						location.href=Ibang.Config.appUrl + "/admin/verify";
					} else {
						Ibang.login.showLoginMsg(msg.description);
					}
					$("#loginBtn").removeClass("disabled");
					Ibang.Base.throttle.unLock(url);
				},
				error: function(){
					$("#loginBtn").removeClass("disabled");
					Ibang.login.showLoginMsg("登录异常");
					Ibang.Base.throttle.unLock(url);
				}
			});
		});
		
		$("#username").keydown(function(event){
			if (!$(this).hasClass("selected")) {
				$(this).addClass("selected");
			}
			if (event.keyCode == 13) {
				$("#loginBtn").click();
				return false;
			}
		}).blur(function(){
			$(this).removeClass("selected");
		});
		
		$("#password").keydown(function(event){
			if (!$(this).hasClass("selected")) {
				$(this).addClass("selected");
			}
			if (event.keyCode == 13) {
				$("#loginBtn").click();
				return false;
			}
		}).blur(function(){
			$(this).removeClass("selected");
		});
		
	},
	checkData: function(){
		var data = {
			username: $.trim($("#username").val()),
			password: $.trim($("#password").val())
		};
		
		if (data.username == '') {
			Ibang.login.showLoginMsg("请输入用户名");
			$("#username").focus();
			return false;
		}

		if (data.password == '') {
			Ibang.login.showLoginMsg("请输入密码");
			$("#password").focus();
			return false;
		}
		

		return data;
	},
	showLoginMsg: function(msg) {
		$(".msg", ".item-msg").text(msg);
		$(".item-msg", "#main").show();
	},
	hideLoginMsg: function() {
		$(".item-msg", "#main").hide();
		$(".msg", ".item-msg").text('');
	}
};