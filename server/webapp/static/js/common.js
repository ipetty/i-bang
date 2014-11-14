//请求安全锁
$.namespace("Ibang.Base.throttle");
Ibang.Base.throttle = {
	time:15*1000,
	data:{},
	reg:function(key,time){
		var time = time || this.time;
		this.data[key] = {lock:false,time:time,thread:null};
		return this.data[key];
	},
	lock:function(key,time){
		var data = this.getData(key);
		if(!data){
			data = this.reg(key,time);
		}
		clearTimeout(data.thread);
		var that = this;
		this.getData(key).lock = true;

		if(time==-1){return}

		this.getData(key).thread = setTimeout(function(){
			that.getData(key).lock = false;
		},data.time)
	},
	unLock:function(key){
		var data = this.getData(key);
		if(!data){
			//data = this.reg(key);
			printf("unlock error key: "+key);
			return;
		}
		clearTimeout(data.thread);
		this.getData(key).lock = false;
	},
	isLock:function(key){
		var data = this.getData(key);
		if(!data){
			return false;
		}
		return data.lock;
	},
	getData:function(key){
		return this.data[key];
	}	
};

var regexEnum = 
{
	account:"^[\u4e00-\u9fa5A-Za-z0-9]+$", // 中文、英文字母（大小写）、数字
	email:"^[\\w]+([\.\-][\\w]+)*@[\\w]+(\-[\\w]+)*(\.\\w{1,31}){1,3}$", // 英文字母（大小写）、数字、“_”、“@”、“.”、"-"
	emailChar: "^[\\w@\.\-]+$", // 邮箱的字符：英文字母（大小写）、数字、“_”、“@”、“.”、"-"
	name: "^[\u4e00-\u9fa5A-Za-z0-9]+$", // 中文、英文字母（大小写）、数字
	password:"^[A-Za-z0-9]+$", // 英文字母(大小写)、数字
	mobile:"^[0-9]+$", // 数字
	extNum:"^[0-9\-]+$", // 数字、"-"
	department: "^[\u4e00-\u9fa5A-Za-z0-9\s ]+$", // 中文、英文字母（大小写）、数字、空格
	jobNum: "^[A-Za-z0-9]+$", // 英文字母（大小写）、数字
	position: "^[\u4e00-\u9fa5A-Za-z0-9\s ]+$", // 中文、英文字母（大小写）、数字、空格
	seat: "^[\u4e00-\u9fa5A-Za-z0-9\s \-]+$", // 中文、英文字母（大小写）、数字、空格、"-"
	note: "^[\u4e00-\u9fa5A-Za-z0-9\s ]+$", // 中文、英文字母（大小写）、数字、空格
	number: "^[0-9]+$", // 数字
	ip: "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])[\.](25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)[\.](25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)[\.](25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$",
	brand: "^[\u4e00-\u9fa5A-Za-z0-9]+$" // 中文、英文字母（大小写）、数字
};

// 账号字符验证
$.namespace("Ibang.Base.account");
Ibang.Base.account = {
	isEmpty: function(value){
		if(typeof(value) == "undefined"){
			return true;
		}
		if(value == null){
			return true;
		}
		if(value == ""){
			return true;
		}
		
		return false;
	},
	isNotEmpty: function(value){
		if(typeof(value) == "undefined"){
			return false;
		}
		if(value == null){
			return false;
		}
		if(value == ""){
			return false;
		}
		if(value.length > 0){
			return true;
		}
		
		return true;
	},
	checkName : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.name,value)) {
			Ibang.box.alert('名称只能输入中文、英文字母、数字', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 32)) {
			Ibang.box.alert("名称长度不能超过16位汉字", callback);
			return false;
		}
		
		return true;
	},
	checkAccount : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.account,value)) {
			Ibang.box.alert('用户名只能输入中文、英文字母、数字', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 32)) {
			Ibang.box.alert("用户名长度不能超过16位汉字", callback);
			return false;
		}
		
		return true;
	},
	checkPassword : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.password,value)) {
			Ibang.box.alert('密码只能输入英文字母、数字', callback);
			return false;
		};
		
		if(value.length < 6 || value.length > 16) {
			Ibang.box.alert("密码长度为6~16位", callback);
			return false;
		}
		
		return true;
	},
	checkNumber : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.number,value)) {
			Ibang.box.alert('设备号码只能输入数字', callback);
			return false;
		};
		
		if(value.length > 13) {
			Ibang.box.alert("设备号码长度不能超过13位", callback);
			return false;
		}
		
		return true;
	},
	checkDeviceIP : function(value, callback){
		if ("0.0.0.0" == value) {
			Ibang.box.alert('设备IP地址不能为0.0.0.0', callback);
			return false;
		};
		
		if (!Ibang.Base.validation.test(regexEnum.ip,value)) {
			Ibang.box.alert('设备IP地址不合法', callback);
			return false;
		};
		
		return true;
	},
	checkGatewayIP : function(value, callback){
		if ("0.0.0.0" == value) {
			Ibang.box.alert('网关IP不能为0.0.0.0', callback);
			return false;
		};
		
		if (!Ibang.Base.validation.test(regexEnum.ip,value)) {
			Ibang.box.alert('网关IP不合法', callback);
			return false;
		};
		
		return true;
	},
	checkUserMobile : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.mobile,value)) {
			Ibang.box.alert('手机只能输入数字', callback);
			return false;
		};
		
		if(value.length > 11) {
			Ibang.box.alert("手机长度不能超过11位", callback);
			return false;
		}
		
		return true;
	},
	checkMobile : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.mobile,value)) {
			Ibang.box.alert('手机只能输入数字', callback);
			return false;
		};
		
		if(value.length > 11) {
			Ibang.box.alert("手机长度不能超过11位", callback);
			return false;
		}
		
		return true;
	},
	checkExtNum : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.extNum,value)) {
			Ibang.box.alert('分机号码只能输入数字、“-”', callback);
			return false;
		};
		
		if(value.length > 20) {
			Ibang.box.alert("分机号码长度不能超过20位", callback);
			return false;
		}
		
		return true;
	},
	checkEmail : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.emailChar,value)) {
			Ibang.box.alert('邮箱地址只能输入英文字母、数字、“_”、“@”、“.”、“-”字符', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 48)) {
			Ibang.box.alert("邮箱地址长度不能超过48位", callback);
			return false;
		}
		
		if (!Ibang.Base.validation.test(regexEnum.email,value)) {
			Ibang.box.alert('邮箱地址不合法', callback);
			return false;
		};
		
		return true;
	},
	checkBrand : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.brand,value)) {
			Ibang.box.alert('设备品牌只能输入中文、英文字母、数字', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 32)) {
			Ibang.box.alert("设备品牌长度不能超过16位汉字", callback);
			return false;
		}
		
		return true;
	},
	checkDepartment : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.department,value)) {
			Ibang.box.alert('部门只能输入中文、英文字母、数字、空格', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 32)) {
			Ibang.box.alert("部门长度不能超过16位汉字", callback);
			return false;
		}
		
		return true;
	},
	checkJobNum : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.jobNum,value)) {
			Ibang.box.alert('工号只能输入英文字母、数字', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 16)) {
			Ibang.box.alert("工号长度不能超过16位字符", callback);
			return false;
		}
		
		return true;
	},
	checkPosition : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.position,value)) {
			Ibang.box.alert('职务只能输入中文、英文字母、数字、空格', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 32)) {
			Ibang.box.alert("职务长度不能超过16位汉字", callback);
			return false;
		}
		
		return true;
	},
	checkSeat : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.seat,value)) {
			Ibang.box.alert('座位只能输入中文、英文字母、数字、空格、“-”', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 32)) {
			Ibang.box.alert("座位长度不能超过16位汉字", callback);
			return false;
		}
		
		return true;
	},
	checkNote : function(value, callback){
		if (!Ibang.Base.validation.test(regexEnum.note,value)) {
			Ibang.box.alert('备注只能输入中文、英文字母、数字、空格', callback);
			return false;
		};
		
		if(!Ibang.Base.validation.checkLength(value, 200)) {
			Ibang.box.alert("备注长度不能超过100位汉字", callback);
			return false;
		}
		
		return true;
	}
};

$.namespace("Ibang.Base.input");
Ibang.Base.input = {
	addReadOnly: function(dom, unbindfocus) {
		if (!dom.hasClass("disabled")) {
			dom.addClass("disabled");
		}
		dom.attr("readonly","readonly");
		dom.unbind();
		if(!unbindfocus) {
			dom.bind("focus", function(){
				dom.blur();
			});
		}
	},
	removeReadOnly: function(dom){
		dom.unbind();
		dom.removeClass("disabled");
		dom.removeAttr("readonly");
	},
	addDisabled: function(dom, unbindfocus){
		if (!dom.hasClass("disabled")) {
			dom.addClass("disabled");
		}
		dom.unbind();
		if(!unbindfocus) {
			dom.bind("focus", function(){
				dom.blur();
			});
		}
	},
	removeDisabled: function(dom){
		dom.unbind();
		dom.removeClass("disabled");
	}
};

//字符验证
$.namespace("Ibang.Base.validation");
Ibang.Base.validation = {
	// 自定义的正则表达式
	test:function(reg,value){
		if(this.isString(value)){
			reg = new RegExp(reg);
		}
		if(!reg.test(value)){
			return false;
		}
		return true;
	},
	checkLength:function(value, len) {
		// 行业中定制验证， 一个汉字占两个字符长度
		var str=value.replace(/[\u4e00-\u9fa5]/g,"**");
		if (str.length > len) {
			return false;
		}
		return true;
	},
	//是否字符串
	isString:function(value){
		return typeof(value) == "string" ? true : false;
	}
};

//字符验证
$.namespace("Ibang.box");
Ibang.box = {
	init: function(){
		this.initEvent();
	},
	initEvent: function(){
		var that = this;
		// 提示信息确定
		$(".confirm", "#alertWrapper").bind("click",function(){
			that.closeAlert();
		});
		
		// 提示信息关闭
		$(".W_close", "#alertWrapper").bind("click",function(){
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
			$(this).addClass("W_close_down");
			that.closeAlert();
		}).hover(function(){
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
			$(this).addClass("W_close_hover");
		}, function(){
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
		});
		
		// 确认弹出框关闭
		$(".W_close", "#confirmWrapper").bind("click",function(){
			that.closeConfirm();
		});
	},
	alert: function(msg, okCallback, closeCallback){
		var that = this;
		$.dialog({
			padding: 0,
			id:"alertWrapperWindow",
			lock: true,
			opacity: 0.50,	// 透明度
			content: document.getElementById('alertWrapper'),
			close: function () {//关闭清空数据
			},
			cancel:false, // 隐藏关闭按钮
			drag: false // 不允许拖拽
		});
		$(".msg", "#alertWrapper").text(msg);
		
		// 提示信息确定
		$(".confirm", "#alertWrapper").unbind();
		$(".confirm", "#alertWrapper").bind("click",function(){
			if(typeof okCallback == "function"){
				okCallback();
		    }
			that.closeAlert();
		});
		
		// 提示信息关闭
		$(".W_close", "#alertWrapper").unbind();
		$(".W_close", "#alertWrapper").bind("click",function(){
			if(typeof closeCallback == "function"){
				closeCallback();
		    }
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
			$(this).addClass("W_close_down");
			that.closeAlert();
		}).hover(function(){
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
			$(this).addClass("W_close_hover");
		}, function(){
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
		});
		
		$(".W_close", "#alertWrapper").removeClass("W_close_down");
		$(".W_close", "#alertWrapper").removeClass("W_close_hover");
	},
	closeAlert: function(){
		$.dialog({id: 'alertWrapperWindow'}).close();
	},
	confirm: function(msg, okCallback, cancelCallback){
		var that = this;
		$.dialog({
			padding: 0,
			id:"confirmWrapper",
			lock: true,
			opacity: 0.50,	// 透明度
			content: document.getElementById('confirmWrapper'),
			close: function () {//关闭清空数据
			},
			cancel:false, // 隐藏关闭按钮
			drag: false // 不允许拖拽
		});
		// 确认弹出框确定
		$(".confirm", "#confirmWrapper").unbind();
		$(".confirm", "#confirmWrapper").bind("click",function(){
			if(typeof okCallback == "function"){
				okCallback();
		    }
			that.closeConfirm();
		});
		$(".msg", "#confirmWrapper").text(msg);
		
		// 确认弹出框取消
		$(".cancel", "#confirmWrapper").unbind();
		$(".cancel", "#confirmWrapper").bind("click",function(){
			if(typeof cancelCallback == "function"){
				cancelCallback();
		    }
			that.closeConfirm();
		});
		
		// 确认弹出框关闭
		$(".W_close", "#confirmWrapper").unbind();
		$(".W_close", "#confirmWrapper").bind("click",function(){
			if(typeof cancelCallback == "function"){
				cancelCallback();
		    }
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
			$(this).addClass("W_close_down");
			that.closeConfirm();
		}).hover(function(){
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
			$(this).addClass("W_close_hover");
		}, function(){
			$(this).removeClass("W_close_down");
			$(this).removeClass("W_close_hover");
		});
		
		$(".W_close", "#confirmWrapper").removeClass("W_close_down");
		$(".W_close", "#confirmWrapper").removeClass("W_close_hover");
	},
	closeConfirm: function(){
		$.dialog({id: 'confirmWrapper'}).close();
	}
};

$.namespace("Ibang.tree");
Ibang.tree = {
	getSelectedId: function(treeId) {
		var treeObj = $.fn.zTree.getZTreeObj(treeId + "");
		var nodes = treeObj.getSelectedNodes();
		var id = nodes[0].id;
		return id;
	},
	getSelectedName: function(treeId) {
		var treeObj = $.fn.zTree.getZTreeObj(treeId + "");
		var nodes = treeObj.getSelectedNodes();
		var name = nodes[0].name;
		return name;
	}
};
