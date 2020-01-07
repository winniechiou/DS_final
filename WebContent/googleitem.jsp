

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
		<title>Songify</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<link rel="stylesheet" href="assets/css/main.css" />

</head>
<body>
<body class="subpage">
		<!-- Header -->
			<header id="header" class="alt">
				<div class="logo"><a href="index.html">Songify-Song as that</span></a></div>
			</header>

			<section id="post" class="wrapper bg-img" data-bg="Polyphia-COVER.jpg">
				<div class="inner">
					<article class="box">

						<header>
                       <form action="#" method="post">

						
						<div class="field">
						
						<div class="12u$ 12u$">
							<input type="text" name="keyword" id="message" placeholder="Search Songify"/></div>
						</div>
						<ul class="actions">
							<li><input value="Search" class="button alt" type="submit" style="position:absolute; margin-left:278px"></li>

						</ul>
					</form>

							
							
						</header>
						<div class="content">
						<%
							String[][] orderList = (String[][])  request.getAttribute("query");
							for(int i =0 ; i < orderList.length;i++){%>
								<h style="font-size:30px;">
								<a href='<%= orderList[i][1] %>'>
								<%= orderList[i][0] %>
								</a>
								
								<br>
								<h style="font-size:20px;">
								<%= orderList[i][1]%>
								
								<br>
								<br>
							
							<%
							}
							%>
							<% String keywords = (String)request.getAttribute("relative keywords"); out.println(keywords);%>
						</div>
						
					</article>
				</div>
			</section>



			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/jquery.scrolly.min.js"></script>
			<script src="assets/js/jquery.scrollex.min.js"></script>
			<script src="assets/js/skel.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>
</body>
</html>