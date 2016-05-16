$(function() {
	/*google.load('visualization', '1.0', {
		'packages' : [ 'corechart' ]
	});

	// Set a callback to run when the Google Visualization API is loaded.
	google.setOnLoadCallback(drawChart);*/
	/* google.load('visualization', '1.1', {packages: ['line']});
	    google.setOnLoadCallback(drawChart);*/
	/* Clones */

	var homeClone = $("#home").clone();
	var paramsClone = $("#params").clone();
	var featuresClone = $("#features").clone();
	var dbFeaturesClone = $("#dbfeatures").clone();

	/* count for id creation */

	var hrefcount = 0;
	var saved = true;
	var savedparam = true;

	$('#logo').click(function() {
		location.reload();
		$("#home").replaceWith(homeClone.clone());

	});

	$('#normaltest').click(function() {

		$('#testtypes').hide();
		$('#normaltesthome').show();

	});

	$('#randomtest').click(function() {

		$('#testtypes').hide();
		$('#randomtesthome').show();

	});

	$('#dbtest').click(function() {

		$('#testtypes').hide();
		$('#dbtesthome').show();

	});

	$('#normalcrtest').click(function() {

		$('#normaltesthome').hide();
		$('#normaltestplan').show();

	});

	$('#normalopentest').click(function() {

		$('#normaltesthome').hide();
		$('#normaluploadForm').show();

	});

	$('#dbcrtest').click(function() {

		$('#dbtesthome').hide();
		$('#dbtestplan').show();

	});

	$('#dbopentest').click(function() {

		$('#dbtesthome').hide();
		$('#dbuploadForm').show();

	});

	$('#randomcrtest').click(function() {

		$('#randomtesthome').hide();
		$('#randomtestplan').show();

	});

	$('#randomopentest').click(function() {

		$('#randomtesthome').hide();
		$('#randomuploadForm').show();

	});

	$('#addnormaltestplan').click(function() {
		$("#params").replaceWith(paramsClone.clone());
		$("#features").replaceWith(featuresClone.clone());
		$('#params').show();
		$('#normalparamtable').show();
		$('#randomparamtable').hide();
		$('#dbparamtable').hide();
		$('#features').show();
		$('#dbfeatures').hide();

		$('#saveplan').show();
		$('#savenormalplan').show();

		$('#buttons').hide();
		$('#start').hide();
		$('#stop').hide();

		$('#savetofile').hide();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#httpreq").off().on("click", AddHttpReq);
		$("#consttimer").off().on("click", AddConstTimer);
		$("#regexex").off().on("click", AddRegexEx);

		// $("#testtable").resizable();
		$("#testplanhome").hide();

	});
	$('#addrandomtestplan').click(function() {

		$("#features").replaceWith(featuresClone.clone());
		$('#params').show();
		$('#randomparamtable').show();
		$('#normalparamtable').hide();
		$('#dbparamtable').hide();
		$('#features').show();
		$('#dbfeatures').hide();

		$('#saveplan').show();
		$('#saverandomplan').show();

		$('#buttons').hide();
		$('#start').hide();
		$('#stop').hide();

		$('#savetofile').hide();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#httpreq").off().on("click", AddHttpReq);
		$("#consttimer").off().on("click", AddConstTimer);
		$("#regexex").off().on("click", AddRegexEx);

		/* $("#testtable").resizable(); */
		/* $('#randomtestplan').hide(); */
		$("#testplanhome").hide();

	});

	$('#adddbtestplan').click(function() {
		$("#params").replaceWith(paramsClone.clone());
		$("#features").replaceWith(featuresClone.clone());
		$("#dbfeatures").replaceWith(dbFeaturesClone.clone());
		$('#params').show();
		$('#dbparamtable').show();
		$('#normalparamtable').hide();
		$('#randomparamtable').hide();
		$('#features').hide();

		$('#dbfeatures').show();

		$('#saveplan').show();
		$('#savedbplan').show();

		$('#buttons').hide();
		$('#start').hide();
		$('#stop').hide();

		$('#savetofile').hide();
		$("#downloadlink").hide();
		$("#summarylink").hide();
		$("#testplanhome").hide();
		$("#dbquery").off().on("click", AddQuery);

	});

	$('#savenormaltestplan').click(function() {
		$('params').hide();
		$('#normalparamtable').hide();
		$('#features').hide();
		$('#saveplan').hide();
		$('#savenormalplan').hide();
		$('#testplanhome').show();
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#start").off().on("click", NormalStart);
		$("#savetofile").off().on("click", NormalSaveToFile);
		SaveNormalPlan();

	});

	$('#saverandomtestplan').click(function() {

		$('#features').hide();
		$('#saveplan').hide();
		$('#saverandomplan').hide();
		$('#testplanhome').show();
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#start").off().on("click", RandomStart);
		$("#savetofile").off().on("click", RandomSaveToFile);

		SaveRandomPlan();

	});

	$('#savedbtestplan').click(function() {
		$('params').hide();
		$('#dbparamtable').hide();
		$('#dbfeatures').hide();
		$('#saveplan').hide();
		$('#savedbplan').hide();
		$('#testplanhome').show();
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#start").off().on("click", DbStart);
		$("#savetofile").off().on("click", DbSaveToFile);
		SaveDbPlan();

	});

	$("#normaluploadForm").submit(function(event) {
		event.preventDefault();

		var fileData = new FormData($('#normaluploadForm')[0]);
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#start").off().on("click", NormalStart);
		$("#savetofile").off().on("click", NormalSaveToFile);

		$.ajax({
			url : "/LoadGen/normaluploadFile",
			type : "POST",
			data : fileData,
			processData : false,
			contentType : false,
			cache : false,

			success : function() {

				$("#status").html('File Uploaded');
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function() {

				$("#status").html('File failed to upload');
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	});

	$("#randomuploadForm").submit(function(event) {
		event.preventDefault();

		var fileData = new FormData($('#randomuploadForm')[0]);
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#start").off().on("click", RandomFileStart);
		$("#savetofile").off().on("click", RandomSaveToFile);

		$.ajax({
			url : "/LoadGen/randomuploadFile",
			type : "POST",
			data : fileData,
			processData : false,
			contentType : false,
			cache : false,

			success : function() {

				$("#status").html('File Uploaded');
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function() {

				$("#status").html('File failed to upload');
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	});

	$("#dbuploadForm").submit(function(event) {
		event.preventDefault();

		var fileData = new FormData($('#dbuploadForm')[0]);
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#summarylink").hide();

		$("#start").off().on("click", DbStart);
		$("#savetofile").off().on("click", DbSaveToFile);

		$.ajax({
			url : "/LoadGen/dbuploadFile",
			type : "POST",
			data : fileData,
			processData : false,
			contentType : false,
			cache : false,

			success : function() {

				$("#status").html('File Uploaded');
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function() {

				$("#status").html('File failed to upload');
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	});

	$('input[type=file]').bootstrapFileInput();
	$('.file-inputs').bootstrapFileInput();

	/*
	 * function getval(sel) {
	 * 
	 * var id = sel.parents('tr:first').children("td:nth-child(2)").children(
	 * "div:nth-child(2)"); if (this.value === "POST") { id.show(); } else {
	 * id.hide(); } }
	 */
	/* function delaySelect(){ */
	/* $('#delay').change(function () { */

	function SaveNormalPlan() {

		var tab = $("#normalparamtable");
		var reqrate = tab.children().children("tr:nth-child(2)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var duration = tab.children().children("tr:nth-child(3)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		if ($("#delay").prop('checked') == true) {
			var startDelay = tab.children().children("tr:nth-child(4)")
					.children("td:nth-child(2)").children("input:nth-child(1)")
					.val();
			/*
			 * #normalparamtable > tbody:nth-child(1) > tr:nth-child(4) >
			 * td:nth-child(2) > input:nth-child(1)
			 */
		} else {
			var startDelay = 0;

		}
		$.ajax({
			type : "POST",
			url : "/LoadGen/savenormaltestplan",
			data : {
				reqRate : reqrate,
				duration : duration,
				startDelay : startDelay

			},
			success : function(response) {
				// we have the response

				$("#status").html("TestPlan Saved");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");

			},
			error : function(e) {
				$("#status").html("TestPlan failed to save");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;
	function SaveRandomPlan() {

		$.ajax({
			type : "POST",
			url : "/LoadGen/saverandomtestplan",

			success : function(response) {
				// we have the response

				$("#status").html("TestPlan Saved");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");

			},
			error : function(e) {
				$("#status").html("TestPlan failed to save");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;

	function SaveDbPlan() {

		var tab1 = $("#dbparamtable");
		var tab2 = $("#dbconfigtable");
		var tab3 = $("#dbquerytable");

		var reqrate = tab1.children().children("tr:nth-child(2)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var duration = tab1.children().children("tr:nth-child(3)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		if ($("#dbdelay").prop('checked') == true) {
			var startDelay = tab1.children().children("tr:nth-child(4)")
					.children("td:nth-child(2)").children("input:nth-child(1)")
					.val();

		} else {
			var startDelay = 0;

		}
		
		var i=0;
		var rowCount = $('#dbquerytable tr').length;
		//console.log(rowCount);
		var dburl = tab2.children().children("tr:nth-child(1)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var dbdriver = tab2.children().children("tr:nth-child(2)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var uname = tab2.children().children("tr:nth-child(3)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var pwd = tab2.children().children("tr:nth-child(4)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var maxconnections=tab2.children().children("tr:nth-child(5)").children(
		"td:nth-child(2)").children("input:nth-child(1)").val();
		/*var queries = tab3.children().children(
		"td:nth-child(1)").children("input:nth-child(1)").map(function(){
		       return $(this).val();
		   }).get();
		console.log(queries);*/
		/*var queries=[];
		for(j=1;j<rowCount;j++){
			//console.log("hallo");
			var dbquery=tab3.children().children("tr:nth-child("+j+")").children(
			"td:nth-child(1)").children("input:nth-child(1)").val();
			queries.push(dbquery);
			//console.log(dbquery);
		}*/
		//console.log(queries);
		var datas=reqrate+"---"+duration+"---"+startDelay+"---"+dburl+"---"+dbdriver+"---"+uname+"---"+pwd+"---"+maxconnections;
		for(j=1;j<rowCount;j++){
			//console.log("hallo");
			var dbquery=tab3.children().children("tr:nth-child("+j+")").children(
			"td:nth-child(1)").children("input:nth-child(1)").val();
			
		datas+="---"+dbquery;
		}
		
		/*var dbdata = {
				"reqRate" : parseInt(reqrate),
				"duration" : parseInt(duration),
				"startDelay" : parseInt(startDelay),
				"dbUrl" : dburl,
				"dbDriver" : dbdriver,
				"uname" : uname,
				"passwd" : pwd,
				"maxConnections" : parseInt(maxconnections)
				};

			$.toJSON(dbdata);
		*/
		//console.log(datas);
		$.ajax({
			type : "POST",
			url : "/LoadGen/savedbtestplan",
			data : {
				dbdatas : datas
			},
			success : function(response) {
				// we have the response

				$("#status").html("TestPlan Saved");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");

			},
			error : function(e) {
				$("#status").html("TestPlan failed to save");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;

	function AddHttpReq() {
		if (saved === true) {
			/*$('#saveplan').hide();
			$('#savenormalplan').hide();*/
			$('#testtable').show();
			/* onchange='getval(this);' */
			var uniq1 = hrefcount++;

			var uniq2 = hrefcount++;
			var uniq3 = hrefcount++;

			var uniq4 = hrefcount++;
			var addRow = "<tr><td style='vertical-align: middle;'><label class='control-label'>Http Request</label>&nbsp;&nbsp;<select id='reqtype' class='form-control' "
					+ "style='max-width:40%; display:inline;'><option value='GET' selected>GET</option><option value='POST'>POST</option></select></td>"
					+ "<td><div id='urlbox' class='ui-helper-center'><input type='text' class='form-control' placeholder='Enter URL'/></div>"
					+ "<div id='postparams' style='display: none'>"
					+

					"<ul class='nav nav-tabs'>"
					+ "<li class='active'><a id ='a1' data-toggle='tab' href=''>POST Params</a></li>"
					+ "<li><a id='a2' data-toggle='tab' href='' >POST Body</a></li>"
					+ "</ul>"
					+

					"<div class='tab-content'>"
					+ "<div id='div1' class='tab-pane fade in active'>"
					+ "<table cellpadding='10' cellspacing='10' width='100%' >"
					+ "<tr><td class='ui-helper-center'><b>Param Name</b>"
					+ "</td><td class='ui-helper-center'><b>Param Value</b></td></tr>"
					+ "</table>"
					+ "<input type='image' class='parAdd' src='resources/images/plus.png' width=25 height=25>"
					+ "</div>"
					+ "<div id='div2' class='tab-pane fade'>"
					+ "<textarea width='100%' cols='42' rows='5' name='postdata'>"
					+ "</textarea>"
					+ "</div>"
					+

					"</div>"
					+ "</div>"
					+

					"</td><td style='vertical-align: middle;'><input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'>"
					+ "<input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/></td></tr>";
			/* $('#ttable tr:last').after(addRow); */

			$('#ttable > tbody:last-child').append(addRow);
			$(".parAdd").off().on("click", AddParam);
			$(".parDelete").off().on("click", DeleteParam);
			/* $(".parEdit").off().on("click", EditParam); */
			$(".parSave").off().on("click", SaveParam);

			$('#div1').attr("id", uniq1);
			$('#div2').attr("id", uniq2);
			$('#a1').attr("id", uniq3);
			$('#a2').attr("id", uniq4);
			var url1 = '#' + uniq1;
			var url2 = '#' + uniq2;
			$('#' + uniq3).attr("href", url1);
			$('#' + uniq4).attr("href", url2);
			/*
			 * #tbody > tr:nth-child(1) > td:nth-child(2) > div:nth-child(2) >
			 * div:nth-child(2)
			 */
			$('select')
					.on(
							'change',
							function() {
								var id = $(this).parents('tr:first').children(
										"td:nth-child(2)").children(
										"div:nth-child(2)");
								if (this.value === "POST") {
									id.show();
								} else {
									id.hide();
								}
							});
		
			$(".btnSave").off().on("click", Save);
			$(".btnDelete").off().on("click", Delete);
			/* $('#postparams').hide(); */
			saved = false;
		} else {
			$("#status").html("Save Feature before adding");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	}
	;
	function AddConstTimer() {
		if (saved === true) {
			/*	$('#saveplan').hide();
				$('#savenormalplan').hide();*/
			$('#testtable').show();
			var addRow = "<tr><td style='vertical-align: middle;'><label class='control-label'>Constant Timer</label></td> "
					+ "<td class='ui-helper-center'><input type='text' class='form-control' placeholder='Enter Time(millisecs)'/></td> "
					+ "<td style='vertical-align: middle;'><input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'><input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/></td></tr>";
			/* $('#ttable tr:last').after(addRow); */
			$('#ttable > tbody:last-child').append(addRow);
			/*
			 * $("#ttable tbody") .append( "<tr>" + "<td><label
			 * class='control-label'>Constant Timer</label></td>" + "<td><input
			 * type='text' class='form-control' placeholder='Enter
			 * Time(millisecs)'/></td>" + "<td><input type='image'
			 * src='resources/images/save.jpeg' width=25 height=25
			 * class='btnSave'><input type='image'
			 * src='resources/images/delete.png' width=25 height=25
			 * class='btnDelete'/></td>" + "</tr>");
			 */
			$(".btnSave").off().on("click", Save);
			$(".btnDelete").off().on("click", Delete);
			saved = false;
		} else {
			$("#status").html("Save Feature before adding");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	}
	;
	function AddRegexEx() {
		if (saved === true) {
			/*$('#saveplan').hide();
			$('#savenormalplan').hide();*/
			$('#testtable').show();
			var addRow = "<tr><td style='vertical-align: middle;'><label class='control-label'>Regex Extractor</label><div >"
					+ "<input type='checkbox' name='globalregex' id='globalregex'/>"
					+

					"<label class='control-label'> Global Regex</label>"
					+ "</div></td>"
					+ "<td><table id='regextable' cellpadding='10' cellspacing='10' width='100%'>"
					+ "<tr><td class='ui-helper-center'><b>Reference Name</b>"
					+ "</td><td class='ui-helper-center'><b>Regex</b></td></tr>"
					+ "<tr><td class='ui-helper-center'><input type='text' class='form-control' placeholder='Enter Reference Name'/>"
					+ "</td><td class='ui-helper-center'><input type='text' class='form-control' placeholder='Enter Regex'/></td></tr></table></td>"
					+ "<td style='vertical-align: middle;'><input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'><input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/></td></tr>";

			$('#ttable > tbody:last-child').append(addRow);

			$(".btnSave").off().on("click", Save);
			$(".btnDelete").off().on("click", Delete);
			saved = false;
		} else {
			$("#status").html("Save Feature before adding");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}

	}
	;

	function AddQuery() {
			$('#querytable').show();
			var addRow = "<tr><td class='ui-helper-center'><input type='text' class='form-control' placeholder='Enter Query'/></td></tr>";
			$('#dbquerytable > tbody:last-child').append(addRow);
		};
	
	function AddParam() {
		if (savedparam === true) {
			var tab = $(this).parent().children("table:nth-child(1)");

			var newRow = "<tr><td class='ui-helper-center'><input type='text' class='form-control' placeholder='Param Name'/></td>"
					+ "<td class='ui-helper-center'><input type='text' class='form-control' placeholder='Param Value'/></td><td style='vertical-align: middle;'>"
					+ "<input type='image' src='resources/images/save.jpeg' width=25 height=25 class='parSave'>"
					+ "<input type='image' src='resources/images/delete.png' width=25 height=25 class='parDelete'/></td></tr>";
			tab.append(newRow);
			$(".parDelete").off().on("click", DeleteParam);
			/* $(".parEdit").off().on("click", EditParam); */
			$(".parSave").off().on("click", SaveParam);
			savedparam = false;
		} else {
			$("#status").html("Save Param before adding");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	}
	;
	function EditParam() {
		if (savedparam === true) {
			var tab = $(this).parent().parent();
			var tdName = tab.children("td:nth-child(1)");
			var tdValue = tab.children("td:nth-child(2)");
			var tdButtons = tab.children("td:nth-child(3)");
			tdName
					.html("<input type='text' class='form-control' placeholder='Param Name' value='"
							+ tdName.html() + "'/>");
			tdValue
					.html("<input type='text' class='form-control' placeholder='Param Value' value='"
							+ tdValue.html() + "'/>");
			tdButtons
					.html("<input type='image' src='resources/images/save.jpeg' width=25 height=25 class='parSave'/>");
			$(".parSave").off().on("click", SaveParam);
			savedparam = false;
		} else {
			$("#status").html("Save Param before editing");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	}
	;
	function SaveParam() {
		savedparam = true;
		var tab = $(this).parent().parent();
		var tdName = tab.children("td:nth-child(1)");
		var tdValue = tab.children("td:nth-child(2)");
		var tdButtons = tab.children("td:nth-child(3)");

		tdButtons
				.html("<input type='image' src='resources/images/delete.png' width=25 height=25 class='parDelete'/>"
						+ "<input type='image' src='resources/images/edit.jpeg' width=25 height=25 class='parEdit'/>");
		tdName.html(tdName.children("input[type=text]").val());
		tdValue.html(tdValue.children("input[type=text]").val());
		$(".parDelete").off().on("click", DeleteParam);
		$(".parEdit").off().on("click", EditParam);

	}
	;
	function DeleteParam() {

		var tab = $(this).parent().parent();
		var num = tab.index();

		if (tab.parent().children('tr').length == (num + 1))
			savedparam = true;
		tab.remove();
	}
	;
	function Save() {
		if (savedparam === true) {
			/*	$('#saveplan').show();
				$('#savenormalplan').show();*/
			saved = true;
			var par = $(this).parent().parent();
			var tdName = par.children("td:nth-child(1)").children(
					"label:nth-child(1)");

			var tdButtons = par.children("td:nth-child(3)");

			tdButtons
					.html("<input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/><input type='image' src='resources/images/edit.jpeg' width=25 height=25 class='btnEdit'/>");
			$(".btnEdit").off().on("click", Edit);
			$(".btnDelete").off().on("click", Delete);

			if (tdName.html() === "Http Request") {
				/*
				 * #ttable > thead:nth-child(2) > tr:nth-child(2) >
				 * td:nth-child(2) > input:nth-child(1)
				 */

				var tdUrl = par.children("td:nth-child(2)").children(
						"div:nth-child(1)");

				var httpType = par.children("td:nth-child(1)").children(
						"select:nth-child(2)");
				var httpval = httpType.val();
				tdUrl.html(tdUrl.children("input[type=text]").val());

				if (httpType.val() === "GET") {
					httpType.prop("disabled", true);
					$.ajax({
						type : "POST",
						url : "/LoadGen/httpgetreq",
						data : {
							url : tdUrl.html(),
							httpType : httpval,
							rownum : par.index()
						},
						success : function(response) {
							// we have the response
							$("#status").html("Http Request Added");
							$("#status").show();
							$("#status").delay(2000).fadeOut("slow");

						},
						error : function(e) {

							$("#status").html("Http Request failed to add");
							$("#status").show();
							$("#status").delay(2000).fadeOut("slow");
						}
					});

				} else {

					httpType.prop("disabled", true);
					var tables = par.children("td:nth-child(2)").children(
							"div:nth-child(2)").children("div:nth-child(2)")
							.children("div:nth-child(1)").children(
									"table:nth-child(1)");
					/* table.children("td") */
					/* if ((table).find("tr").not("thead tr").length == 1) { */
					var rawbody = par.children("td:nth-child(2)").children(
							"div:nth-child(2)").children("div:nth-child(2)")
							.children("div:nth-child(2)").children(
									"textarea:nth-child(1)");
					var plus = par.children("td:nth-child(2)").children(
							"div:nth-child(2)").children("div:nth-child(2)")
							.children("div:nth-child(1)").children(
									"input:nth-child(2)");
					plus.hide();
					// alert(rawbody.val());

					var tableparam = tables.tableToJSON();
					var postparam = par.children("td:nth-child(2)").children(
							"div:nth-child(2)");
					postparam.hide();

					var tabledata = {
						"url" : tdUrl.html(),
						"httpType" : httpval,
						"rownum" : par.index(),
						"postBody" : rawbody.val(),
						"postParams" : tableparam

					};

					$.toJSON(tabledata);
					$.ajax({
						type : "POST",
						url : "/LoadGen/httppostreq",
						
						data : {
							tabdata : JSON.stringify(tabledata)
						},

						success : function(response) {
							// we have the response
							$("#status").html("Http Request Added");
							$("#status").show();
							$("#status").delay(2000).fadeOut("slow");

						},
						error : function(e) {
							alert(e.responseText);
							$("#status").html("Http Request failed to add");
							$("#status").show();
							$("#status").delay(2000).fadeOut("slow");
						}
					});
				

				}
			} else if (tdName.html() === "Constant Timer") {
				var tdTime = par.children("td:nth-child(2)");

				tdTime.html(tdTime.children("input[type=text]").val());

				$.ajax({
					type : "POST",
					url : "/LoadGen/consttimer",
					data : {
						time : tdTime.html(),
						rownum : par.index()
					},
					success : function(response) {
						// we have the response
						$("#status").html("Constant Timer Added");
						$("#status").show();
						$("#status").delay(2000).fadeOut("slow");

					},
					error : function(e) {
						$("#status").html("Constant Timer failed to add");
						$("#status").show();
						$("#status").delay(2000).fadeOut("slow");
					}
				});
			} else if (tdName.html() === "Regex Extractor") {
				var tdRegexName = par.children("td:nth-child(2)").children()
						.children("tbody:nth-child(1)").children(
								"tr:nth-child(2)").children("td:nth-child(1)");
				var tdRegex = par.children("td:nth-child(2)").children()
						.children("tbody:nth-child(1)").children(
								"tr:nth-child(2)").children("td:nth-child(2)");

				var global = par.children("td:nth-child(1)").children(
						"div:nth-child(2)").children("input:nth-child(1)");

				var globalvalue;
				if (global.prop('checked') == true) {
					globalvalue = 1;
				} else {
					globalvalue = 0;
				}
				tdRegexName
						.html(tdRegexName.children("input[type=text]").val());
				tdRegex.html(tdRegex.children("input[type=text]").val());
				global.prop("disabled", true);
				$.ajax({
					type : "POST",
					url : "/LoadGen/regexextractor",
					data : {
						refName : tdRegexName.html(),
						regex : tdRegex.html(),
						global : globalvalue,
						rownum : par.index()
					},
					success : function(response) {
						// we have the response
						$("#status").html("Regex Added");
						$("#status").show();
						$("#status").delay(2000).fadeOut("slow");
					},
					error : function(e) {
						$("#status").html("Regex failed to add");
						$("#status").show();
						$("#status").delay(2000).fadeOut("slow");
					}
				});

			}
		} else {
			$("#status").html("Save Param First");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	}
	;
	function Edit() {
		if (saved === true) {
			/*	$('#saveplan').hide();
				$('#savenormalplan').hide();*/
			var par = $(this).parent().parent();

			var tdName = par.children("td:nth-child(1)").children(
					"label:nth-child(1)");

			var tdButtons = par.children("td:nth-child(3)");
			var num = par.index();
			// alert(num);
			if (tdName.html() === "Http Request") {
				var tdUrl = par.children("td:nth-child(2)").children(
						"div:nth-child(1)");
				var tables = par.children("td:nth-child(2)").children(
						"div:nth-child(2)").children("div:nth-child(2)")
						.children("div:nth-child(1)").children(
								"table:nth-child(1)");

				tdUrl
						.html("<input type='text' class='form-control' placeholder='Enter URL' id='txtParam' value='"
								+ tdUrl.html() + "'/>");
				var httpType = par.children("td:nth-child(1)").children(
						"select:nth-child(2)");

				httpType.prop("disabled", false);
				/*
				 * var plus = par.children("td:nth-child(2)").children(
				 * "div:nth-child(2)").children("div:nth-child(2)")
				 * .children("div:nth-child(1)").children(
				 * "input:nth-child(2)"); plus.show();
				 */
				if (httpType.val() === "POST") {
					var postparam = par.children("td:nth-child(2)").children(
							"div:nth-child(2)");
					postparam.show();
				}
				// tables.show();
			} else if (tdName.html() === "Constant Timer") {
				var tdTime = par.children("td:nth-child(2)");
				tdTime
						.html("<input type='text' class='form-control' placeholder='Enter Time(millisecs)'id='txtParam' value='"
								+ tdTime.html() + "'/>");

			} else if (tdName.html() === "Regex Extractor") {
				var tdRegexName = par.children("td:nth-child(2)").children()
						.children("tbody:nth-child(1)").children(
								"tr:nth-child(2)").children("td:nth-child(1)");
				var tdRegex = par.children("td:nth-child(2)").children()
						.children("tbody:nth-child(1)").children(
								"tr:nth-child(2)").children("td:nth-child(2)");
				tdRegexName
						.html("<input type='text' class='form-control' placeholder='Enter Reference Name' id='txtParam' value='"
								+ tdRegexName.html() + "'/>");
				tdRegex
						.html("<input type='text' class='form-control' placeholder='Enter Regex' id='txtParam' value='"
								+ tdRegex.html() + "'/>");
				var global = par.children("td:nth-child(1)").children(
						"div:nth-child(2)").children("input:nth-child(1)");
				global.prop("disabled", false);

			}
			tdButtons
					.html("<input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'/>");
			$(".btnSave").off().on("click", Save);
			// $(".btnEdit").bind("click", Edit);
			// $(".btnDelete").bind("click", Delete);
			saved = false;
		} else {
			$("#status").html("Save Feature before editing");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	}
	;

	function Delete() {
		var par = $(this).parent().parent();
		var num = par.index();
		if ($('#tbody').children('tr').length == num + 1) {
			saved = true;
			savedparam = true;
		}
		par.remove();
		if ($('#tbody').children('tr').length == 0) {
			$('#testtable').hide();
			/*	$('#saveplan').hide();
				$('#savenormalplan').hide();*/
		}
		// alert(num);
		$.ajax({
			type : "POST",
			url : "/LoadGen/remove",
			data : {
				rownum : num
			},
			success : function(response) {
				// we have the response
				$("#status").html("Deleted");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("Failed to delete");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;
	var prevData = "";
	var flag = 0;
	var testStartTime = 0;

	function NormalStart() {
		$('#start').hide();
		$('#stop').show();
		$('#addnormaltestplan').hide();
		var d = new Date();
		testStartTime = d.getTime();
		$("#stop").off().on("click", NormalStop);
		$('#output').show();
		flag = 0;
		prevData = "";
		normaloutput();
		$.ajax({
			type : "POST",
			url : "/LoadGen/normalloadgen",
			timeout : 10000,
			success : function(response) {
				// we have the response

				$("#status").html("LoadGen Started");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("LoadGen failed to start");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}

		});

	}
	;
	function RandomStart() {

		var tab = $("#randomparamtable");
		var maxreqrate = tab.children().children("tr:nth-child(2)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var maxduration = tab.children().children("tr:nth-child(3)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var epoch = tab.children().children("tr:nth-child(4)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();

		$('#start').hide();
		$('#stop').show();
		$("#stop").off().on("click", RandomStop);
		$('#output').show();
		$('#addrandomtestplan').hide();
		flag = 0;
		prevData = "";
		randomoutput();
		$.ajax({
			type : "POST",
			url : "/LoadGen/randomloadgen",
			timeout : 10000,
			data : {

				maxreqRate : maxreqrate,
				maxduration : maxduration,
				epoch : epoch
			},

			success : function(response) {
				// we have the response

				$("#status").html("LoadGen Started");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {

				$("#status").html("LoadGen failed to start");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;

	function RandomFileStart() {

		$('#start').hide();
		$('#stop').show();
		$("#stop").off().on("click", RandomStop);
		$('#addrandomtestplan').hide();
		$('#output').show();
		flag = 0;
		prevData = "";
		randomoutput();
		$.ajax({
			type : "POST",
			url : "/LoadGen/randomfileloadgen",
			timeout : 10000,
			success : function(response) {
				// we have the response

				$("#status").html("LoadGen Started");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {

				$("#status").html("LoadGen failed to start");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;
	function DbStart() {
		$('#start').hide();
		$('#stop').show();
		$('#adddbtestplan').hide();
		var d = new Date();
		testStartTime = d.getTime();
		$("#stop").off().on("click", DbStop);
		$('#output').show();
		flag = 0;
		prevData = "";
		dboutput();
		$.ajax({
			type : "POST",
			url : "/LoadGen/dbloadgen",
			timeout : 10000,
			success : function(response) {
				// we have the response

				$("#status").html("LoadGen Started");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {

				$("#status").html("LoadGen failed to start");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}

		});

	}
	;
	function normaloutput() {
		$.ajax({
			type : "POST",
			url : '/LoadGen/output',
			dataType : "HTML",
			success : function(data) {
				if (data == prevData) {
					CreateSummary();
					NormalStop();
					flag = 1;
				}
				$('#output').html(data);
				prevData = data;
				$(".btnGraph").off().on("click", CallGraph);
			},
			complete : function() {
				// Schedule the next request when the current one's complete
				if (flag == 0)
					setTimeout(normaloutput, 1000);
			}
		});
	}
	;
	function randomoutput() {

		$.ajax({
			type : "POST",
			url : '/LoadGen/output',
			dataType : "HTML",
			success : function(data) {
				if (data == prevData) {
					CreateSummary();
					RandomStop();
					flag = 1;
				}
				$('#output').html(data);
				prevData = data;
				$(".btnGraph").off().on("click", CallGraph);
			},
			complete : function() {
				// Schedule the next request when the current one's complete
				if (flag == 0)
					setTimeout(randomoutput, 1000);
			}
		});
	}
	;

	function dboutput() {
		$.ajax({
			type : "POST",
			url : '/LoadGen/output',
			dataType : "HTML",
			success : function(data) {
				if (data == prevData) {
					CreateSummary();
					DbStop();
					flag = 1;
				}
				$('#output').html(data);
				prevData = data;
				$(".btnGraph").off().on("click", CallGraph);
			},
			complete : function() {
				// Schedule the next request when the current one's complete
				if (flag == 0)
					setTimeout(dboutput, 1000);
			}
		});
	}
	;
	function NormalStop() {
		$('#stop').hide();
		$('#start').show();
		$("#start").off().on("click", NormalStart);
		$.ajax({
			type : "POST",
			url : "/LoadGen/stop",

			success : function(response) {

				$("#status").html("LoadGen Stopped");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("LoadGen failed to stop");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;
	function RandomStop() {
		$('#stop').hide();
		$('#start').show();
		$("#start").off().on("click", RandomStart);
		$.ajax({
			type : "POST",
			url : "/LoadGen/stop",

			success : function(response) {

				$("#status").html("LoadGen Stopped");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("LoadGen failed to stop");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;

	function DbStop() {
		$('#stop').hide();
		$('#start').show();
		$("#start").off().on("click", DbStart);
		$.ajax({
			type : "POST",
			url : "/LoadGen/stop",

			success : function(response) {

				$("#status").html("LoadGen Stopped");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("LoadGen failed to stop");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;

	function CallGraph() {
		var par = $(this).parent().parent();
		graph(par.index(), testStartTime);
	}
	;

	function CreateSummary() {

		$.ajax({
			type : "POST",
			async : false,
			url : "/LoadGen/createsummary",

			success : function(response) {
				// we have the response
				$("#buttons").show();
				$("#start").show();
				$("#stop").hide();
				$("#downloadlink").hide();
				$("#savetofile").hide();
				$("#summarylink").show();
				$("#status").html("Summary Generated");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("Summary failed to generate");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;

	function NormalSaveToFile() {

		$.ajax({
			type : "POST",
			url : "/LoadGen/normalsavetofile",

			success : function(response) {
				// we have the response
				$("#buttons").show();
				$("#start").show();
				$("#stop").hide();
				$("#downloadlink").show();
				$("#savetofile").hide();
				$("#summarylink").hide();
				$("#status").html("Test Saved to file");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("Test failed to save");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});

	}
	;
	function RandomSaveToFile() {
		var tab = $("#randomparamtable");
		var maxreqrate = tab.children().children("tr:nth-child(2)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var maxduration = tab.children().children("tr:nth-child(3)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();
		var epoch = tab.children().children("tr:nth-child(4)").children(
				"td:nth-child(2)").children("input:nth-child(1)").val();

		$.ajax({
			type : "POST",
			url : "/LoadGen/randomsavetofile",
			data : {

				maxreqRate : maxreqrate,
				maxduration : maxduration,
				epoch : epoch
			},

			success : function(response) {
				// we have the response
				$("#buttons").show();
				$("#start").show();
				$("#stop").hide();
				$("#downloadlink").show();
				$("#savetofile").hide();
				$("#summarylink").hide();
				$("#status").html("Test Saved to file");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			},
			error : function(e) {
				$("#status").html("Test failed to save");
				$("#status").show();
				$("#status").delay(2000).fadeOut("slow");
			}
		});
	}
	;
});

function DbSaveToFile() {

	$.ajax({
		type : "POST",
		url : "/LoadGen/dbsavetofile",

		success : function(response) {
			// we have the response
			$("#buttons").show();
			$("#start").show();
			$("#stop").hide();
			$("#downloadlink").show();
			$("#savetofile").hide();
			$("#summarylink").hide();
			$("#status").html("Test Saved to file");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		},
		error : function(e) {
			$("#status").html("Test failed to save");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	});

};
function checkChange() {
	//alert("hi");
	if ($("#delay").prop('checked') == true) {
		$('#delaybox').show();
	} else {
		$('#delaybox').hide();
	}
	if ($("#dbdelay").prop('checked') == true) {
		$('#dbdelaybox').show();
	} else {
		$('#dbdelaybox').hide();
	}
};

/*function drawChart() {

 // Create the data table.
 //console.log("start");
 var data = new google.visualization.DataTable();
 data.addColumn('number', 'Day');
 data.addColumn('number', 'Guardians of the Galaxy');
 data.addColumn('number', 'The Avengers');
 data.addColumn('number', 'Transformers: Age of Extinction');

 data.addRows([
 [1,  37.8, 80.8, 41.8],
 [2,  30.9, 69.5, 32.4],
 [3,  25.4,   57, 25.7],
 [4,  11.7, 18.8, 10.5],
 [5,  11.9, 17.6, 10.4],
 [6,   8.8, 13.6,  7.7],
 [7,   7.6, 12.3,  9.6],
 [8,  12.3, 29.2, 10.6],
 [9,  16.9, 42.9, 14.8],
 [10, 12.8, 30.9, 11.6],
 [11,  5.3,  7.9,  4.7],
 [12,  6.6,  8.4,  5.2],
 [13,  4.8,  6.3,  3.6],
 [14,  4.2,  6.2,  3.4]
 ]);

 var options = {
 chart: {
 title: 'Box Office Earnings in First Two Weeks of Opening',
 subtitle: 'in millions of dollars (USD)'
 },
 width: 900,
 height: 500
 };
 //.log("middle");
 var chart = new google.charts.Line(document.getElementById('chart_div'));

 chart.draw(data, options);
 // console.log("end");
 }*/