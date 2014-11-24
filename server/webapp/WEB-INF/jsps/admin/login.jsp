<%@ page contentType="text/html;charset=UTF-8"%>
<%-- <%@ include file="/inc/common/resource.jsp"%> --%>
<%@ include file="/resource.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>51帮帮忙管理平台</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta name="renderer" content="webkit">
<%@ include file="/inc/page_base.jsp"%>
<link href="${RESOUCE_STATIC_URL}/css/login.min.css?t=4.8.2668563615"
	type="text/css" rel="stylesheet">
<script src="${RESOUCE_STATIC_URL}/js/login.min.js?t=4.83494827540"></script>
<script type="text/javascript">
	$(document).ready(function() {
		Ibang.login.init();
		$("#username").val("ibang");
		$("#password").focus();
	});
</script>
<style type="text/css">
</style>
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<div class="header-content">
				<span class="ent"></span> <span class="default-head"></span>
			</div>
		</div>

		<div id="content">
			<div id="main">
				<div class="title">
					<span>51帮帮忙管理平台</span>
				</div>
				<div class="login">
					<div class="item-username">
						<label>用户名</label> <input type="hidden" id="md5Encode"
							name="md5Encode" value="${md5Encode }" /> <input type="hidden"
							id="oldPassword" name="oldPassword" value="${password }" /> <input
							type="text" id="username" name="username"
							class="input-text input-login" value="${username}" />
					</div>
					<div class="item-password">
						<label>密码</label> <input type="password" id="password"
							name="password" class="input-text input-login"
							value="${password}" />
					</div>
					<div class="item-msg">
						<span class="msg">请输入用户名!</span>
					</div>
					<div class="item-btn">
						<a id="loginBtn" class="btn-login">登&nbsp;&nbsp;录</a>
					</div>
				</div>
			</div>
		</div>

		<div id="footer">
			<span>Copyright&nbsp;&#169;2014&nbsp;&nbsp;51帮帮忙&nbsp;&nbsp;</span>
		</div>
	</div>
</body>
</html>

