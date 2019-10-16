<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js">
</script>
</head>
<body>
<!-- 서울 지역의 201909 의 아파트 거래가정보 출력. -->

<input type="text" name="keyword" placeholder="동이름 입력" />
<button id="searchBtn">검색하기</button>
<form id="searchForm">
	<input type='hidden' name='lawd_cd' />
	<div id="searchResult"></div>
</form>
<button id="btn">가져오기</button>
<div id="resultArea">
	<table>
		<thead>
			<tr id="headerArea">
			</tr>
		</thead>
		<tbody id="listBody">
		</tbody>
	</table>

</div>

<script type="text/javascript">
let headerArea = $('#headerArea');
let listBody = $('#listBody');
let searchBtn = $('#searchBtn');
let searchResult = $('#searchResult');
let searchForm = $('#searchForm');
let cPath = "${pageContext.request.contextPath}";

searchForm.on('click','.codeList',function(){
	let aptCode = $(this).prop('id');
	$('[name="lawd_cd"]').val(aptCode);
	
});
searchBtn.on('click',function(){
	let keyword = $('[name="keyword"]').val();
	$.ajax({
	    url: cPath+"/nameList",
	    data: {"keyword":keyword},
	    dataType: "json",
	    success: function (resp) {
	    	let code ="<div>검색할 기간 선택</div><input type='date' name='deal_ymd' placeholder='년월입력'/>  ";
	    	$.each(resp,function(i,v){
   				code+="<div class='codeList' id="+v.code+">"+v.name+"</div>"
	    	});
	    	searchResult.html(code);
	    },
	    error:function(xhr){
	    	console.log(xhr.status);
	    }
	});
});
$('#btn').on("click",function(){
	let deal_ymd = $("[name='deal_ymd']").val();
	let lawd_cd = $('[name="lawd_cd"]').val();
	$.ajax({
		data:{
			"deal_ymd":deal_ymd,
			"lawd_cd":lawd_cd
		},
		dataType:"json",
		success:function(resp){
			console.log(resp);
			let response = resp.response;
			let header = response.header;
			let body = response.body;
			if(header.resultCode!="00"){
				alert(header.resultMsg);
				return;
			}
			let items =  body.items.item;
			let trHeaders = [];
			let trTags=[];
			$.each(items, function(index,item){
				let count = 0;
				let trTag=$("<tr>");
				trTags.push(trTag);
				for(let propname in item){
					if(count==0){
						trHeaders.push($("<th>").text(propname));
					}
				console.log(propname+":"+item[propname]);
				trTag.append($("<td>").text(item[propname]));
				}
				count++;
				listBody.html(trTags);
			});
			headerArea.html(trHeaders);
		},
		error:function(xhr){
			console.log(xhr.status);
		}
	});
});
</script>
</body>
</html>