<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Load Generator</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"> -->
<!-- <script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script> -->
<!-- <link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"> -->

<link rel="stylesheet" type="text/css"
	href="resources/css/jquery-ui.css">
<script src="//d3js.org/d3.v3.min.js"></script>
<script src="resources/js/jquery-1.11.3.min.js"></script>
<!-- <script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script> -->
<script src="resources/js/jquery-1.10.2.js"></script>
<script src="resources/js/jquery-ui.js"></script>
<script src="resources/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.tabletojson.js"></script>
<script type="text/javascript" src="resources/js/jquery.json.js"></script>
<script type="text/javascript" src="resources/js/graph.js"></script>
<script type="text/javascript" src="resources/js/home.js"></script>
<script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="resources/js/bootstrap.file-input.js"></script>
<link rel="stylesheet" type="text/css"
	href="resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="resources/css/graph.css">
<link rel="stylesheet" type="text/css" href="resources/css/home.css">


<link rel="icon" type="image/png" href="resources/images/lg.png">
<!-- <script type="text/javascript" src="https://www.google.com/jsapi"></script>
     <script type="text/javascript">

      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});
      google.load('visualization', '1.1', {packages: ['line']});
	   // google.setOnLoadCallback(drawChart);

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);
      </script>-->

</head>
<body>

	<div id="home" class="container">

<center>
			<div class="row">

				<br>
				<!-- <img src="resources/images/heading.png" > -->
				<input id="logo" type="image" src="resources/images/heading.png"
					alt="Home">
			</div>
			<br> <br>

			<div id="testtypes" class="row">
				<input type="button" id="normaltest" class="btn btn-default"
					value="Normal Test"> <input type="button" id="randomtest"
					class="btn btn-default" value="Random Test"> <input
					type="button" id="dbtest" class="btn btn-default"
					value="Database Test">
			</div>
			<div class="row" id="testhome">
				<div id="normaltesthome" style="display: none">
					<input type="button" id="normalcrtest" class="btn btn-primary"
						value="Create Test"> <input type="button"
						id="normalopentest" class="btn btn-primary" value="Open Test">
				</div>
				<div id="randomtesthome" style="display: none">
					<input type="button" id="randomcrtest" class="btn btn-primary"
						value="Create Test"> <input type="button"
						id="randomopentest" class="btn btn-primary" value="Open Test">
				</div>
				<div id="dbtesthome" style="display: none">
					<input type="button" id="dbcrtest" class="btn btn-primary"
						value="Create Test"> <input type="button" id="dbopentest"
						class="btn btn-primary" value="Open Test">
				</div>
			</div>
			<div class="row" id="testplanhome">
				<div id="normaltestplan" style="display: none">
					<input type="button" id="addnormaltestplan" class="btn btn-primary"
						value="Add TestPlan">
				</div>
				<div id="randomtestplan" class="row" style="display: none">

					<input type="button" id="addrandomtestplan" class="btn btn-primary"
						value="Add TestPlan">

				</div>
				<div id="dbtestplan" class="row" style="display: none">

					<input type="button" id="adddbtestplan" class="btn btn-primary"
						value="Add TestPlan">

				</div>
			</div>




			<div id="openfile" class="row">
				<form name="normaluploadForm" id="normaluploadForm" method="POST"
					action="javascript:;" enctype="multipart/form-data"
					style="display: none">
					<div class="input-group">
						<table cellpadding='10' cellspacing='10'>
							<tr>
								<td><input type="file" name="fileName" id="fileName"
									form="normaluploadForm" data-filename-placement="inside"></td>
								<td><input type="submit" id="normaluploadbutton"
									class="btn btn-primary" value="Upload"> <!-- <input id="normaluploadbutton" type="image"
									src="resources/images/upload.png" alt="upload"> --></td>
						</table>
					</div>

				</form>

				<form name="randomuploadForm" id="randomuploadForm" method="POST"
					action="javascript:;" enctype="multipart/form-data"
					style="display: none">
					<div class="input-group">
						<table cellpadding='10' cellspacing='10'>
							<tr>
								<td><input type="file" name="fileName" id="fileName"
									form="randomuploadForm" data-filename-placement="inside"></td>
								<td><input type="submit" id="randomuploadbutton"
									class="btn btn-primary" value="Upload"> <!-- <input id="uploadbutton" type="image"
									src="resources/images/upload.png" alt="upload"> --></td>
						</table>
					</div>

				</form>

				<form name="dbuploadForm" id="dbuploadForm" method="POST"
					action="javascript:;" enctype="multipart/form-data"
					style="display: none">
					<div class="input-group">
						<table cellpadding='10' cellspacing='10'>
							<tr>
								<td><input type="file" name="fileName" id="fileName"
									form="dbuploadForm" data-filename-placement="inside"></td>
								<td><input type="submit" id="dbuploadbutton"
									class="btn btn-primary" value="Upload"> <!-- <input id="uploadbutton" type="image"
									src="resources/images/upload.png" alt="upload"> --></td>
						</table>
					</div>

				</form>

			</div>

			<br>
			<div id="params" class="row" style="display: none">

				<table id="normalparamtable"
					class="table table-striped table-bordered table-condensed well"
					style="width: 500px" style="display: none">

					<tr>
						<th colspan="2" class="ui-helper-center">LoadGen Parameters</th>
					</tr>
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Request Rate(reqs/sec):</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Request Rate'
							value="" /></td>
					</tr>


					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Duration(secs):</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Duration'
							value="" /></td>
					</tr>
					<tr style="height: 50px;">
						<td class='ui-helper-center' style='vertical-align: middle;'>
							<div style='display: inline;'>
								<input type="checkbox" name="delay" id="delay"
									onchange="checkChange();" /> <label class="control-label">
									Delay TestPlan Start</label>
							</div>
						</td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' id="delaybox" class='form-control'
							placeholder='Enter Delay(secs)' value="" style="display: none" /></td>
					</tr>

				</table>

				<table id="randomparamtable"
					class="table table-striped table-bordered table-condensed well"
					style="width: 500px" style="display: none">



					<tr>
						<th colspan="2" class="ui-helper-center">LoadGen Parameters</th>
					</tr>
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Max Request Rate(reqs/sec):</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Request Rate'
							value="" /></td>
					</tr>


					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Total Duration(secs):</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Duration'
							value="" /></td>
					</tr>
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Epoch(secs):</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Epoch'
							value="" /></td>
					</tr>




				</table>

				<table id="dbparamtable"
					class="table table-striped table-bordered table-condensed well"
					style="width: 500px" style="display: none">

					<tr>
						<th colspan="2" class="ui-helper-center">JDBC Parameters</th>
					</tr>
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Request Rate(reqs/sec):</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Request Rate'
							value="" /></td>
					</tr>


					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Duration(secs):</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Duration'
							value="" /></td>
					</tr>
					<tr style="height: 50px;">
						<td class='ui-helper-center' style='vertical-align: middle;'>
							<div style='display: inline;'>
								<input type="checkbox" name="delay" id="dbdelay"
									onchange="checkChange();" /> <label class="control-label">
									Delay TestPlan Start</label>
							</div>
						</td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' id="dbdelaybox" class='form-control'
							placeholder='Enter Delay(secs)' value="" style="display: none" /></td>
					</tr>

				</table>


			</div>


			<div id="features" class="row" style="display: none">

				<div class="col-lg-3" style='vertical-align: middle;'>

					<table
						class="table table-striped table-bordered table-condensed well">
						<tr>
							<td><input type="button" id="httpreq"
								class="btn btn-primary btn-lg btn-block" value="Http Request">
								<!-- <input id="httpreq" type="image"
							src="resources/images/httpreq.png" alt="Http Request"
							width="250px"> --></td>
						</tr>
						<tr>
							<td><input type="button" id="consttimer"
								class="btn btn-primary btn-lg btn-block" value="Constant Timer">
								<!-- <input id="consttimer" type="image"
							src="resources/images/timer.png" alt="Constant Timer"
							width="250px"> --></td>
						</tr>
						<tr>
							<td><input type="button" id="regexex"
								class="btn btn-primary btn-lg btn-block" value="Regex Extractor">
								<!-- <input id="regexex" type="image"
							src="resources/images/regexext.png" alt="Regex Extractor"
							width="250px"> --></td>
						</tr>
						<!-- 		<button id="httpreq" width="200px">Http Request</button></br>
		<button id="consttimer" width="200px">Constant Timer</button></br>
		<button id="regexex" width="200px">Regex Extractor</button></br> -->
					</table>

				</div>





				<div id="testtable" class="col-lg-8 well" style="display: none">
					<!-- 		<div id="resizable" class="ui-widget-content"> -->
					<table id="ttable"
						class="table table-striped table-bordered table-condensed">
						<col style="width: 30%">
						<col style="width: 60%">
						<col style="width: 10%">

						<thead>
							<tr>
								<th class='ui-helper-center' style='vertical-align: middle;'>Test
									Feature</th>
								<th class='ui-helper-center' style='vertical-align: middle;'>Parameters</th>
								<th></th>
							</tr>
						</thead>

						<tbody id="tbody"></tbody>
					</table>

					<!-- </div> -->
				</div>
			</div>

			<div id="dbfeatures" class="row" style="display: none">


				<table id="dbconfigtable"
					class="table table-striped table-bordered table-condensed well"
					style="width: 600px">
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Database URL</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Database URL'
							value="" /></td>
					</tr>
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">JDBC Driver Class</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control'
							placeholder='Enter JDBC Driver Class' value="" /></td>
					</tr>
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Username</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control' placeholder='Enter Username'
							value="" /></td>
					</tr>
					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Password</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='password' class='form-control' placeholder='Enter Password'
							value="" /></td>
					</tr>

					<tr>
						<td class='ui-helper-center' style='vertical-align: middle;'><label
							class="control-label">Maximum Connections</label></td>
						<td class='ui-helper-center' style='vertical-align: middle;'><input
							type='text' class='form-control'
							placeholder='Enter Max. Connections' value="" /></td>
					</tr>

				</table>

				<div class="col-lg-3" style='vertical-align: middle;'>

					<table
						class="table table-striped table-bordered table-condensed well">
						<tr>
							<td><input type="button" id="dbquery"
								class="btn btn-primary btn-lg btn-block" value="Add Query">
							</td>
						</tr>
					</table>
				</div>
				<div id="querytable" class="col-lg-7 well" style="display: none">
					<table id="dbquerytable"
						class="table table-striped table-bordered table-condensed well"
						style="width: 600px">
							<thead>
							<tr>
								<th class='ui-helper-center' style='vertical-align: middle;'>Query List</th>
							</tr>
						</thead>

						<tbody id="dbtbody"></tbody>
						
					</table>
				</div>
			</div>

			<div class="row" id="saveplan">
				<div id="savenormalplan" style="display: none">
					<input type="button" id="savenormaltestplan"
						class="btn btn-primary" value="Save TestPlan">
					<!-- <input id="savenormaltestplan" type="image"
						src="resources/images/savetestplan.png" alt="Save TestPlan"> -->
				</div>
				<div id="saverandomplan" class="row" style="display: none">
					<input type="button" id="saverandomtestplan"
						class="btn btn-primary" value="Save TestPlan">
					<!-- <input id="saverandomtestplan" type="image"
						src="resources/images/savetestplan.png" alt="Save TestPlan"> -->
				</div>
				<div id="savedbplan" class="row" style="display: none">
					<input type="button" id="savedbtestplan" class="btn btn-primary"
						value="Save TestPlan">
				</div>
			</div>
			<br>

			<div id="buttons" class="row" style="display: none">
				<table
					class="table table-striped table-bordered table-condensed well"
					style="width: 300px">
					<tr>
						<td>
							<div class="ui-grid-a">
								<div class="ui-block-a">
									<input type="button" id="start"
										class="btn btn-success btn-block" value="Start"
										style="display: none">
								</div>
								<div class="ui-block-b">

									<input type="button" id="stop" class="btn btn-danger btn-block"
										value="Stop" style="display: none">
									<!-- <input id="start" type="image"
							src="resources/images/start.png" alt="Start Test"
							style="display: none"><input id="stop" type="image"
							src="resources/images/stop.png" alt="Stop Test"
							style="display: none"> -->
								</div>
							</div>

						</td>
						<td>
							<!-- <input id="savetofile" type="image"
							src="resources/images/save.png" alt="Save To File"
							style="display: none"> --> <input type="button" id="savetofile"
							class="btn btn-primary btn-block" value="Save To File"
							style="display: none">
							<div id="downloadlink" style="display: none">
								<a href="resources/tmpFiles/test.xml"
									class="btn btn-primary btn-block" download>Download</a>
							</div>
							<div id="summarylink" style="display: none">
								<a href="resources/tmpFiles/summary.pdf"
									class="btn btn-primary btn-block" download>Test Summary</a>
							</div>
						</td>
					</tr>
				</table>
			</div>



			<div class="row">
				<div id="output" class="col-lg-13 well" style="display: none">

				</div>
			</div>
			<div id="graph" class="col-lg-13 well" style="display: none">
				<div class="column-center">Time vs Response Time</div>
				<div class="column-left">Time vs Throughput</div>
				<div class="column-right">Time vs Error Rate</div>
			</div>
			<div class="row">
				<div class="col-lg-4">
					<div id="status" class="well alert alert-warning"
						style="display: none"></div>
				</div>
			</div>
			<div class="row" id="chart_div"></div>
	
</center>
	</div>
</body>
</html>