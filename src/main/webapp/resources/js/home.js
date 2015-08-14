$(function() {
	var paramsClone = $("#params").clone();
	var featuresClone = $("#features").clone();
	/*$("#httpreq").off().on("click", AddHttpReq);
	$("#consttimer").off().on("click", AddConstTimer);
	$("#regexex").off().on("click", AddRegexEx);*/
	/*$("#start").off().on("click", Start);
	$("#savetofile").off().on("click", SaveToFile);
	$(".btnSave").off().on("click", Save);
	$(".btnDelete").off().on("click", Delete);
	$(".btnEdit").off().on("click", Edit);*/
	
	$('#addtestplan').click(function() {
		$("#params").replaceWith(paramsClone.clone());
		$("#features").replaceWith(featuresClone.clone());
		$('#params').show();
		$('#features').show();
		
		
		$('#saveplan').show();
		$('#buttons').hide();
		$('#start').hide();
		$('#stop').hide();
		
		$('#savetofile').hide();
		$("#downloadlink").hide();
		$('#testplan').hide();
		
	
		$("#httpreq").off().on("click", AddHttpReq);
		$("#consttimer").off().on("click", AddConstTimer);
		$("#regexex").off().on("click", AddRegexEx);
		
		$("#testtable").resizable();
		
	});
	$('#crtest').click(function() {

		$('#crtest').hide();
		$('#opentest').hide();
		$('#testplan').show();

	});
	$('#savetestplan').click(function() {
		$('#params').hide();
		$('#features').hide();
		$('#saveplan').hide();
		$('#testplan').show();
		$('#buttons').show();
		$('#start').show();
		$('#stop').hide();
		$('#savetofile').show();
		$("#downloadlink").hide();
		$("#start").off().on("click", Start);
		$("#savetofile").off().on("click", SaveToFile);
		SavePlan();

	});
	$('#opentest').click(function() {
		$('#openfile').show();
		$('#crtest').hide();
		$('#opentest').hide();
		
	});
	
	$("#uploadForm").submit(function (event) {
		 event.preventDefault();
		 
	
		
		var fileData = new FormData($('#uploadForm')[0]); 
       

        $.ajax({
            url: "/LoadGen/uploadFile",
            type: "POST",
            data : fileData,
            processData: false,
            contentType: false,
            cache : false,
            
           
            success: function () {
            	$('#buttons').show();
            	$('#start').show();
            	$('#stop').hide();
        		$('#savetofile').show();
        		$("#downloadlink").hide();
        		$("#start").off().on("click", Start);
        		$("#savetofile").off().on("click", SaveToFile);
                $("#status").html('File Uploaded');
    			$("#status").show();
    			$("#status").delay(2000).fadeOut("slow");
            },
            error: function(){
                
        $("#status").html('File failed to upload');
		$("#status").show();
		$("#status").delay(2000).fadeOut("slow");
            }
        });

        
    });

		$('input[type=file]').bootstrapFileInput();
		$('.file-inputs').bootstrapFileInput();
	
	event.preventDefault();
});

function SavePlan() {
	var tab = $("#paramtable");
	var reqrate = tab.children().children("tr:nth-child(2)").children(
			"td:nth-child(2)").children("input:nth-child(1)").val();
	var duration = tab.children().children("tr:nth-child(3)").children(
			"td:nth-child(2)").children("input:nth-child(1)").val();

	$.ajax({
		type : "POST",
		url : "/LoadGen/savetestplan",
		data : {
			reqRate : reqrate,
			duration : duration
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

};


function AddHttpReq() {
	$('#testtable').show();
	$("#ttable tbody")
		
			.append(
					"<tr>"
							+ "<td>Http Request</td>"
							+ "<td><input type='text' class='form-control' placeholder='Enter URL'/></td>"
							+ "<td><input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'><input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/></td>"
							+ "</tr>");
	$(".btnSave").off().on("click", Save);
	$(".btnDelete").off().on("click", Delete);

};
function AddConstTimer() {
	$('#testtable').show();
	$("#ttable tbody")
			.append(
					"<tr>"
							+ "<td>Constant Timer</td>"
							+ "<td><input type='text' class='form-control' placeholder='Enter Time(millisecs)'/></td>"
							+ "<td><input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'><input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/></td>"
							+ "</tr>");
	$(".btnSave").off().on("click", Save);
	$(".btnDelete").off().on("click", Delete);

};
function AddRegexEx() {
	$('#testtable').show();
	$("#ttable tbody")
			.append(
					"<tr>"
							+ "<td>Regex Extractor</td>"
							+ "<td><div><input type='text' class='form-control' placeholder='Enter Reference Name'/></div><div><input type='text' class='form-control' placeholder='Enter Regex'/></div></td>"
							+ "<td><input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'><input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/></td>"
							+ "</tr>");
	$(".btnSave").off().on("click", Save);
	$(".btnDelete").off().on("click", Delete);

};

function Save() {
	var par = $(this).parent().parent();

	var tdName = par.children("td:nth-child(1)");

	var tdButtons = par.children("td:nth-child(3)");
	tdButtons
			.html("<input type='image' src='resources/images/delete.png' width=25 height=25 class='btnDelete'/><input type='image' src='resources/images/edit.jpeg' width=25 height=25 class='btnEdit'/>");
	$(".btnEdit").off().on("click", Edit);
	$(".btnDelete").off().on("click", Delete);
	if (tdName.html() === "Http Request") {
		var tdUrl = par.children("td:nth-child(2)");
		tdUrl.html(tdUrl.children("input[type=text]").val());
		$.ajax({
			type : "POST",
			url : "/LoadGen/httpreq",
			data : {
				url : tdUrl.html(),
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
		var tdRegexName = par.children("td:nth-child(2)").children(
				"div:nth-child(1)");
		var tdRegex = par.children("td:nth-child(2)").children(
				"div:nth-child(2)");
		tdRegexName.html(tdRegexName.children("input[type=text]").val());
		tdRegex.html(tdRegex.children("input[type=text]").val());

		$.ajax({
			type : "POST",
			url : "/LoadGen/regexextractor",
			data : {
				refName : tdRegexName.html(),
				regex : tdRegex.html(),
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

};
function Edit() {
	var par = $(this).parent().parent();
	var tdName = par.children("td:nth-child(1)");

	var tdButtons = par.children("td:nth-child(3)");
	var num = par.index();
	// alert(num);
	if (tdName.html() === "Http Request") {

		var tdUrl = par.children("td:nth-child(2)");
		tdUrl
				.html("<input type='text' class='form-control' placeholder='Enter URL' id='txtParam' value='"
						+ tdUrl.html() + "'/>");
	} else if (tdName.html() === "Constant Timer") {
		var tdTime = par.children("td:nth-child(2)");
		tdTime
				.html("<input type='text' class='form-control' placeholder='Enter Time(millisecs)'id='txtParam' value='"
						+ tdTime.html() + "'/>");

	} else if (tdName.html() === "Regex Extractor") {
		var tdRegexName = par.children("td:nth-child(2)").children(
				"div:nth-child(1)");
		var tdRegex = par.children("td:nth-child(2)").children(
				"div:nth-child(2)");
		tdRegexName
				.html("<input type='text' class='form-control' placeholder='Enter Reference Name' id='txtParam' value='"
						+ tdRegexName.html() + "'/>");
		tdRegex
				.html("<input type='text' class='form-control' placeholder='Enter Regex' id='txtParam' value='"
						+ tdRegex.html() + "'/>");
	}
	tdButtons
			.html("<input type='image' src='resources/images/save.jpeg' width=25 height=25 class='btnSave'/>");
	$(".btnSave").off().on("click", Save);
	// $(".btnEdit").bind("click", Edit);
	// $(".btnDelete").bind("click", Delete);
};

function Delete() {
	var par = $(this).parent().parent();
	var num = par.index();

	par.remove();
	if ($('#tbody').children('tr').length == 0)
		$('#testtable').hide();
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

};
function Start() {

	
	$.ajax({
		type : "POST",
		url : "/LoadGen/loadgen",

		success : function(response) {
			// we have the response
			$('#start').hide();
			$('#stop').show();
			$("#stop").off().on("click", Stop);
			$("#status").html("LoadGen Started");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		},
		error : function(e) {
			$('#stop').show();
			$('#start').hide();
			$("#stop").off().on("click", Stop);
			$("#status").html("LoadGen failed to start");
			$("#status").show();
			$("#status").delay(2000).fadeOut("slow");
		}
	});

};
function Stop() {
	
	
	$.ajax({
		type : "POST",
		url : "/LoadGen/stop",
		
		success : function(response) {
		
			$('#stop').hide();
			$('#start').show();
			$("#start").off().on("click", Start);
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

};
function SaveToFile() {

	$.ajax({
		type : "POST",
		url : "/LoadGen/savetofile",
		
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

};
