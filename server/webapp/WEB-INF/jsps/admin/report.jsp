<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/resource.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>51帮帮忙管理平台</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta name="renderer" content="webkit">
<%@ include file="/inc/page_base.jsp"%>
<link href="${RESOUCE_STATIC_URL}/css/report.min.css?t=4.8.2668563615"
	type="text/css" rel="stylesheet">
</head>
<script>
	$(function() {
		$(".prePage").bind("click", function() {
			var cPage = "1";
			var currentPage = $.trim($("#currentPage").val());
			if (currentPage != "1") {
				cPage = (parseInt(currentPage) - 1) + "";
			}
			$("#currentPage").val(cPage);
			loadData();
		});

		$(".nextPage").bind("click", function() {
			var currentPage = $.trim($("#currentPage").val());
			var totalPage = $(".totalPage").text();
			var cPage = totalPage;
			if (currentPage != totalPage) {
				cPage = (parseInt(currentPage) + 1) + "";
			}
			$("#currentPage").val(cPage);
			loadData();
		});

		$("#currentPage").keydown(function(event) {
			if (event.keyCode == 13) {
				var value = $.trim($("#currentPage").val());
				if (value.length < 1) {
					alert('请输入当前页数');
					return false;
				}
				var reg = new RegExp("^\\d+$");
				if (!reg.test(value)) {
					alert('当前页数只能输入数字');
					return false;
				}

				if (parseInt(value) == 0) {
					$("#currentPage").val(1);
				}

				var totalPage = $(".totalPage", ".paging").text();
				if (parseInt(value) > parseInt(totalPage)) {
					//Ums.box.alert('当前页数不能大于总页数');
					//return false;
					$("#currentPage").val(totalPage);
				}

				loadData();
				return false;
			}
		});

		function loadData() {
			location.href = Ibang.Config.appUrl + "/admin/reports/page/"
					+ $("#currentPage").val();
		}

		function reloadData() {
			location.reload();
		}

		$(".btnVerify").click(function() {
			var id = ($(this).data("id"));
			var res = ($(this).data("res"));
			var url = "/admin/dealreport/" + id;
			Ibang.Base.throttle.lock(url);

			$.ajax({
				type : 'post',
				url : Ibang.Config.appUrl + "/admin/dealreport/",
				data : {
					id : id,
					result : res
				},
				dataType : 'json',
				success : function(result) {
					if (result) {
						reloadData();
					} else {
						alert("审核失败");
					}
					Ibang.Base.throttle.unLock(url);
				},
				error : function() {
					alert("操作失败")
				}
			});

		})

	})
</script>

<body>
	<div id="wrapper">
		<%@ include file="/inc/page_div_head_nav.jsp"%>

		<div class="wrap-main">
			<div class="main">
				<div class="main-btn">
					<a id="add" class="btn" href="javascript:" hidefocus="true">待审核</a>
					<!-- <a id="add" class="btn" href="javascript:" hidefocus="true">全部</a> -->
				</div>
				<div class="main-content">
					<div class="table-header-content">
						<table>
							<tr class="table-header">
								<td class="c1">被举报人</td>
								<td class="sep"></td>
								<td class="c2">被举报内容</td>
								<td class="sep"></td>
								<td class="c3">被举报行为</td>
								<td class="sep"></td>
								<td class="c4">举报描述</td>
								<td class="sep"></td>
								<td class="c5">举报人</td>
								<td class="sep"></td>
								<td class="c6">处理结果</td>
							</tr>
						</table>
					</div>

					<div class="data-content">
						<div class="data-table">
							<table class="table-content">
								<c:forEach items="${reportList}" var="report">
									<tr id="${report.id}">
										<td class="data-td c1">${report.userNickname}</td>
										<td class="data-td c2">${report.reportContent}</td>
										<td class="data-td c3">${report.behave}</td>
										<td class="data-td c4">${report.content}</td>
										<td class="data-td c5">${report.reporterNickname}</td>
										<td class="data-td c6"><c:choose>
												<c:when test="${empty report.result}">
													<a id="" class="btnVerify btn" href="javascript:"
														style="color: green" hidefocus="true"
														data-id="${report.id}" data-res="true">通过</a>
													<a id="" class="btnVerify btn" href="javascript:"
														style="color: red" hidefocus="true" data-id="${report.id}"
														data-res="false">驳回</a>
												</c:when>
												<c:otherwise>${report.result}</c:otherwise>
											</c:choose></td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
					<div class="paging">
						<div>
							<input id="currentPage" name="currentPage" class="currentPage"
								value="1" /> <span class="sep">/</span>
							<c:choose>
								<c:when test="${totalPage == null || totalPage == '0'}">
									<span class="totalPage">1</span>
								</c:when>
								<c:otherwise>
									<span class="totalPage">${totalPage}</span>
								</c:otherwise>
							</c:choose>
							<a class="prePage" href="javascript:" hidefocus="true"></a> <a
								class="nextPage" href="javascript:" hidefocus="true"></a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
