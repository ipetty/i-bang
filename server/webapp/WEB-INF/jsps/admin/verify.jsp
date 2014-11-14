<%@ page contentType="text/html;charset=UTF-8" %>
<%-- <%@ include file="/inc/common/resource.jsp"%> --%>
<%@ include file="/resource.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>51帮帮忙管理平台</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<meta name="renderer" content="webkit">
<%@ include file="/inc/page_base.jsp"%>
<link href="${RESOUCE_STATIC_URL}/css/verify.min.css?t=4.8.2668563615" type="text/css" rel="stylesheet">


</head>

<body>
	<div id="wrapper">
	<%@ include file="/inc/page_div_head_nav.jsp"%>

	<div class="wrap-main">
		<div class="main">
			<div class="main-btn">
				<a id="add" class="btn" href="javascript:" hidefocus="true">待审核</a>
				<a id="add" class="btn" href="javascript:" hidefocus="true">已审核</a>
			</div>
			<div class="main-content">
				<div class="table-header-content">
					<table>
						<tr class="table-header">
							<td class="c1">
								姓名</td>
							<td class="sep"></td>
							<td class="c2">身份证号码</td>
							<td class="sep"></td>
							<td class="c3">手持照片图片</td>
							<td class="sep"></td>
							<td class="c4">状态</td>
						</tr>
					</table>
				</div>
				
				<div class="data-content">
					<div class="data-table">
						<table class="table-content">
							<c:forEach items="${listVerify}" var="identityVerification">
							<tr id="${identityVerification.id }">
								<td class="data-td c1" title="${identityVerification.realName }">
									${identityVerification.realName}
								</td>
								<td class="data-td c2" title="${identityVerification.idNumber}">${identityVerification.idNumber}</td>
								<td class="data-td c3" title="${identityVerification.idCardInHand}"><img src="${identityVerification.idCardInHand}" width="36" alt="" /></td>
								<td class="data-td c4" title="${identityVerification.status }">${identityVerification.status}<a id="" class="btn" href="javascript:" hidefocus="true">审核</a></td>
							</tr>
							</c:forEach>
						</table>
					</div>
				</div>
				<div class="paging">
					<div>
						<input id="currentPage" name="currentPage" class="currentPage" value="1" />
						<span class="sep">/</span>
						<c:choose>
						<c:when test="${totalPage == null || totalPage == '0'}">
							<span class="totalPage">1</span>
						</c:when>
						<c:otherwise>
							<span class="totalPage">${totalPage }</span>
						</c:otherwise>
						</c:choose>
						<a class="prePage" href="javascript:" hidefocus="true"></a>
						<a class="nextPage" href="javascript:" hidefocus="true"></a>
					</div>
				</div>
			</div>
		</div>
	</div>








	</div>
</body>
</html>

  
