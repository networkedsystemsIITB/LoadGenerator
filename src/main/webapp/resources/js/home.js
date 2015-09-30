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

	$('#normalcrtest').click(function() {

		$('#normaltesthome').hide();
		$('#normaltestplan').show();

	});

	$('#normalopentest').click(function() {

		$('#normaltesthome').hide();
		$('#normaluploadForm').show();

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
		$('#features').show();
		
		  $('#saveplan').show();
		$('#savenormalplan').show();
		 
		$('#buttons').hide();
		$('#start').hide();
		$('#stop').hide();

		$('#savetofile').hide();
		$("#downloadlink").hide();

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
		$('#features').show();
		
		 $('#saveplan').show(); 
		 $('#saverandomplan').show();
		 
		$('#buttons').hide();
		$('#start').hide();
		$('#stop').hide();

		$('#savetofile').hide();
		$("#downloadlink").hide();

		$("#httpreq").off().on("click", AddHttpReq);
		$("#consttimer").off().on("click", AddConstTimer);
		$("#regexex").off().on("click", AddRegexEx);

		/* $("#testtable").resizable(); */
		/* $('#randomtestplan').hide(); */
		$("#testplanhome").hide();
		
	});

	$('#savenormaltestplan').click(function() {
		$('params').hide();
		$('#normalparamtable').hide();
		$('#features').hide();
		$('#saveplan').hide();
		$('#savenormalplan').hide();
		/* $('#normaltestplan').show(); */
		$('#testplanhome').show();
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#start").off().on("click", NormalStart);
		$("#savetofile").off().on("click", NormalSaveToFile);
		SaveNormalPlan();

	});

	$('#saverandomtestplan').click(function() {
		
		$('#features').hide();
		$('#saveplan').hide();
		$('#saverandomplan').hide();
		/* $('#randomtestplan').show(); */
		$('#testplanhome').show();
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#start").off().on("click", RandomStart);
		$("#savetofile").off().on("click", RandomSaveToFile);
		
		SaveRandomPlan();

	});

	/*
	 * $(':checkbox').change(function() { // do stuff here. It will fire on any
	 * checkbox change
	 * 
	 * alert("hi"); if ($("#delay").prop('checked') == true) {
	 * $('#delaybox').show(); } else { $('#delaybox').hide(); } });
	 */
	$("#normaluploadForm").submit(function(event) {
		event.preventDefault();

		var fileData = new FormData($('#normaluploadForm')[0]);
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
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
			/*
			 * $("#ttable
			 * tr:last").children("td:nth-child(1)").children("select:nth-child(2)").change(function () {
			 * var id =
			 * $(this).parents('tr:first').children("td:nth-child(2)").children("div:nth-child(2)");
			 * if(this.value==="POST"){ id.show(); } else{ id.hide(); }
			 * //alert(id); });
			 */
			/*
			 * $("#ttable tbody")
			 * 
			 * .append( "<tr>" + "<td><label class='control-label'>HTTP
			 * Request </label>&nbsp;&nbsp;<select id='reqtype'
			 * class='form-control' onchange='getval(this);'
			 * style='max-width:40%; display:inline;'><option value='GET'
			 * selected>GET</option><option value='POST'>POST</option></select></td>" + "<td><input
			 * type='text' class='form-control' placeholder='Enter URL'/>" + "<input
			 * type='text' id='postparams' class='form-control'
			 * placeholder='Enter params'/></td>" + "<td><input type='image'
			 * src='resources/images/save.jpeg' width=25 height=25
			 * class='btnSave'><input type='image'
			 * src='resources/images/delete.png' width=25 height=25
			 * class='btnDelete'/></td>" + "</tr>");
			 */
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
					// talbes.hide();
					// table.children("")
					// tables.$('td:nth-child(3)').hide();
					var tabledata = {
						"url" : tdUrl.html(),
						"httpType" : httpval,
						"rownum" : par.index(),
						"postBody" : rawbody.val(),
						"postParams" : tableparam

					};

					$.toJSON(tabledata);
					// alert(JSON.stringify(tabledata));
					/*
					 * var tabledatacomb = tableparam.concat(tabledata);
					 * alert(JSON.stringify(tabledatacomb)); var tabdata =
					 * JSON.stringify(tabledatacomb);
					 */
					/* alert(JSON.stringify(tabledata)); */
					$.ajax({
						type : "POST",
						url : "/LoadGen/httppostreq",
						/*
						 * contentType : 'application/json; charset=utf-8',
						 * dataType : 'json',
						 * 
						 * data : JSON.stringify(tabledata),
						 */
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
					/* } *//*
							 * else {
							 * 
							 * var tabledata = table.tableToJSON(); var
							 * actualObj = JSON.parse(jsonText); actualObj+={
							 * "url ":tdUrl.html(),
							 * "httpType":httpType.val(),"rownum":par.index()};
							 * tabledata.url=tdUrl.html();
							 * tabledata.httpType=httpType.val();
							 * tabledata.rownum=par.index();
							 * 
							 * //alert(actualData);
							 * alert(JSON.stringify(actualObj)); }
							 */

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

	function NormalStart() {
		$('#start').hide();
		$('#stop').show();
		$('#addnormaltestplan').hide();

		$("#stop").off().on("click", NormalStop);
		$('#output').show();
		output();
		$.ajax({
			type : "POST",
			url : "/LoadGen/normalloadgen",

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
		output();
		$.ajax({
			type : "POST",
			url : "/LoadGen/randomloadgen",
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
		output();
		$.ajax({
			type : "POST",
			url : "/LoadGen/randomfileloadgen",

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
function checkChange() {
	if ($("#delay").prop('checked') == true) {
		$('#delaybox').show();
	} else {
		$('#delaybox').hide();
	}
};
function output() {
	//drawChart();
	$.ajax({
		type : "POST",
		url : '/LoadGen/output',
		dataType : "HTML",
		success : function(data) {
			//drawChart();
			$('#output').html(data);
			/*$('#outtable').
			drawChart();*/
		},
		complete : function() {
			// Schedule the next request when the current one's complete
			setTimeout(output, 1000);
		}
	});
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