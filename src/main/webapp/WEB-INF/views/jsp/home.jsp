<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Load Generator</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script type="text/javascript" src="resources/js/home.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/home.css">

</head>
<body>
	<div class="container">
	<center>
		<div class="row">
		
			<br>
				<img src="resources/images/heading.png"><br> <br> <input
					id="testplan" type="image" src="resources/images/testplan.png"
					alt="Create TestPlan">
		</div>
		</center>
		<div id="params" class="row" style="display:none">
		
				<center>
				<div class="form-group">
					<form:form method="POST" action="/LoadGen/loadgen">
					
						<table class="table table-striped table-bordered table-condensed well"  style="width:500px">
						
					
						<tbody>
							<tr><th colspan="2" class="ui-helper-center">LoadGen Parameters</th></tr>
							<tr>
								<td><form:label path="reqRate">Request Rate(reqs/sec):</form:label></td>
								<td><form:input class="form-control"
										placeholder="Enter Request Rate" path="reqRate" /></td>
							</tr>


							<tr>
								<td><form:label path="duration">Duration(secs):</form:label></td>
								<td><form:input class="form-control"
										placeholder="Enter Duration" path="duration" /></td>
							</tr>


							<tr>
								<td colspan="2"><input id="start" type="image"
									src="resources/images/start.png" alt="Start"></td>
							</tr>
							</tbody>
						</table>
					</form:form>
					</div>
					</center>
				
			
			
		</div>


		<div class="row">

			<div id="features" class="col-lg-3" style="display: none">
				
					<table class="table table-striped table-bordered table-condensed well">
						<tr>
							<td><input id="httpreq" type="image"
								src="resources/images/httpreq.png" alt="Http Request" width="250px"></td>
						</tr>
						<tr>
							<td><input id="consttimer" type="image"
								src="resources/images/timer.png" alt="Constant Timer" width="250px"></td>
						</tr>
						<tr>
							<td><input id="regexex" type="image"
								src="resources/images/regexext.png" alt="Regex Extractor" width="250px"></td>
						</tr>
						<!-- 		<button id="httpreq" width="200px">Http Request</button></br>
		<button id="consttimer" width="200px">Constant Timer</button></br>
		<button id="regexex" width="200px">Regex Extractor</button></br> -->
					</table>
				
			</div>
			<div id="testtable" class="col-lg-8 well" style="display: none">
				
					<table id="ttable"
						class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th>Test Feature</th>
								<th>Parameters</th>
								<th></th>
							</tr>
						</thead>

						<tbody id="tbody"></tbody>
					</table>
				
			</div>
		</div>
		<div class="col-lg-4">
			<div id="status" class="well" style="display: none"></div>
		</div>
		<div id="output" class="col-lg-10 well" style="display: none">
			
		</div>

	</div>
</body>
</html>