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

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script src="http://malsup.github.com/jquery.form.js"></script>
<script type="text/javascript" src="resources/js/home.js"></script>
<link rel="stylesheet" type="text/css" href="resources/css/home.css">

</head>
<body>
	<div class="container">
		<center>
			<div class="row">

				<br> <img src="resources/images/heading.png"><br> <br>
				<input id="crtestplan" type="image"
					src="resources/images/testplan.png" alt="Create TestPlan">
				<input id="uptestplan" type="image"
					src="resources/images/upload.png" alt="Upload TestPlan">
			</div>
		</center>
		<center>
		<div id="uploadform" class="row" style="display: none">
		<form id="form2" method="post" action="/LoadGen/upload" enctype="multipart/form-data">
  <!-- File input -->    
  <input name="file2" id="file2" type="file" /><br/>
</form>
 
<!-- <button value="Submit" onclick="uploadJqueryForm()" >Upload</button><i>Using JQuery Form Plugin</i><br/> -->
<button value="Submit" onclick="uploadFormData()" >Upload</button>
 
<div id="result"></div></div>
			<!-- <div id="uploadform" class="row" style="display: none">
				<input id="uploadFile" class="disableInputField"
					placeholder="Choose File" disabled="disabled" /> <label
					class="fileUpload"> <input id="uploadBtn" type="file"
					class="upload" /> <span class="uploadBtn">Browse</span>


				</label> <input id="openfile" type="image" src="resources/images/open.png"
					alt="Open">
			</div> -->
			<!-- <table id="uploadtable"
						class="table table-striped table-bordered table-condensed well"
						style="width: 500px">
						
						<tr>
							<td>Open File :</td>
							<td><input type='file' class='form-control' /></td>
							<td><input id="start" type="image"
							src="resources/images/start.png" alt="Open"></td>
						</tr>
</table>
 -->
	
	</center>
	<center>
		<div id="params" class="row" style="display: none">


			<%-- <div class="col-lg-5">
				<div class="form-group">
					<form:form method="POST" action="/LoadGen/loadgen"> --%>

			<table id="paramtable"
				class="table table-striped table-bordered table-condensed well"
				style="width: 500px">



				<tr>
					<th colspan="2" class="ui-helper-center">LoadGen Parameters</th>
				</tr>
				<tr>
					<td>Request Rate(reqs/sec):</td>
					<td><input type='text' class='form-control'
						placeholder='Enter Request Rate' /></td>
				</tr>


				<tr>
					<td>Duration(secs):</td>
					<td><input type='text' class='form-control'
						placeholder='Enter Duration' /></td>
				</tr>


				<!-- <tr>
								<td colspan="2"><input id="start" type="image"
									src="resources/images/start.png" alt="Start"></td>
							</tr> -->

			</table>
			<%-- </form:form> 
					</div>
					--%>



		</div>
	</center>


	<div class="row">

		<div id="features" class="col-lg-3" style="display: none">

			<table
				class="table table-striped table-bordered table-condensed well">
				<tr>
					<td><input id="httpreq" type="image"
						src="resources/images/httpreq.png" alt="Http Request"
						width="250px"></td>
				</tr>
				<tr>
					<td><input id="consttimer" type="image"
						src="resources/images/timer.png" alt="Constant Timer"
						width="250px"></td>
				</tr>
				<tr>
					<td><input id="regexex" type="image"
						src="resources/images/regexext.png" alt="Regex Extractor"
						width="250px"></td>
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
				<thead>
					<tr>
						<th>Test Feature</th>
						<th>Parameters</th>
						<th></th>
					</tr>
				</thead>

				<tbody id="tbody"></tbody>
			</table>

			<!-- </div> -->
		</div>
	</div>

	<center>

		<div id="buttons" class="row" style="display: none">
			<table
				class="table table-striped table-bordered table-condensed well"
				style="width: 300px">
				<tr>
					<td><input id="start" type="image"
						src="resources/images/start.png" alt="Start Test"></td>
					<td><input id="savetofile" type="image"
						src="resources/images/save.png" alt="Save To File"></td>
				</tr>
			</table>
		</div>

	</center>
	<div class="row">
		<div class="col-lg-4">
			<div id="status" class="well" style="display: none"></div>
		</div>
	</div>
	<div class="row">
		<div id="output" class="col-lg-11 well" style="display: none"></div>
	</div>

	</div>
</body>
</html>