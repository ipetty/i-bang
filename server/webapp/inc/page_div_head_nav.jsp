<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link type="text/css" rel="stylesheet" href="${RESOUCE_STATIC_URL}/css/headNav.min.css?t=4.81686198614" />
<div id="head-nav" class="head-nav clearfix">
<div class="head-nav-bg" style="z-index:1000">
	<div class="head-nav-inner">
		<ul class="head-nav-left">
			<li class="logo"><a>51帮帮忙管理平台</a></li>
	        <li class="item meeting"><a href="${RESOUCE_APP_URL}/admin/verify" class="link">实名认证</a></li>
			<li class="item meeting"><a href="${RESOUCE_APP_URL}/admin/reports" class="link">举报处理</a></li>
        </ul>      
		<ul class="head-nav-right">
			<li class="item">
	            <span class="link-no-hover">
		            <span class="head-nav-nickname">${user.username}</span>
	            </span> 
	        </li>
	        <li class="item item-line"></li>
	        <li class="item logout" title="注销">	           
	            <s></s> 
	        </li>
	        <li class="item item-line"></li>
		</ul>
	</div>
	</div>
</div>
<script type="text/javascript">		
$(function(){
	$('.js-hover .setting-s, .nav-sublist-lev1').live('mouseenter', function(){
		$(".js-hover").addClass("hover");
		$(".setPopLayer").show();
	}).live('mouseleave', function() {
		$(".js-hover").removeClass("hover");
		$(".setPopLayer").hide();
	});
	
	//注销登录
	$("li.logout","#head-nav").live("click",function(){
		var logoutUrl ="/admin/web/logout";
		location.href=logoutUrl;
	});
});
</script>

