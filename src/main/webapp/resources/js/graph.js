/*$(function() {

var n = 40,
    random = d3.random.normal(0, .2),
    data = d3.range(n).map(random);
var margin = {top: 20, right: 20, bottom: 20, left: 40},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;
var x = d3.scale.linear()
    .domain([0, n - 1])
    .range([0, width]);
var y = d3.scale.linear()
    .domain([-1, 1])
    .range([height, 0]);
var line = d3.svg.line()
    .x(function(d, i) { return x(i); })
    .y(function(d, i) { return y(d); });
var svg = d3.select("body").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
svg.append("defs").append("clipPath")
    .attr("id", "clip")
  .append("rect")
    .attr("width", width)
    .attr("height", height);
svg.append("g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + y(0) + ")")
    .call(d3.svg.axis().scale(x).orient("bottom"));
svg.append("g")
    .attr("class", "y axis")
    .call(d3.svg.axis().scale(y).orient("left"));
var path = svg.append("g")
    .attr("clip-path", "url(#clip)")
  .append("path")
    .datum(data)
    .attr("class", "line")
    .attr("d", line);
tick();

});
 */

/*$.ajax({
 type : "POST",
 url : '/LoadGen/graph',
 dataType : "HTML",
 success : function(data) {
 console.log(data);

 },
 complete : function() {
 // Schedule the next request when the current one's complete
 setTimeout(graph, 1000);
 }
 });*/
function graph(num) {
	
	$("#graph").show();
	d3.selectAll("svg").remove();
	
	//Initialization
	var maxt=0;
	var maxr=0;
	var maxe=0;
	var margin = {
		top : 20,
		right : 20,
		bottom : 20,
		left : 40
	};
	
	var widtht=parseInt(d3.select(".column-left").style("width"))- margin.left - margin.right;
	var heightt=parseInt(d3.select(".column-left").style("height"))- margin.top
	- margin.bottom;
	
	var svgt = d3.selectAll(".column-left").append("svg").attr("width",
			widtht + margin.left + margin.right).attr("height",
			heightt + margin.top + margin.bottom).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");
	
	var widthr=parseInt(d3.select(".column-center").style("width"))- margin.left - margin.right;
	var heightr=parseInt(d3.select(".column-center").style("height"))- margin.top
	- margin.bottom;
	
	var svgr = d3.selectAll(".column-center").append("svg").attr("width",
			widthr + margin.left + margin.right).attr("height",
			heightr + margin.top + margin.bottom).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");
	
	var widthe=parseInt(d3.select(".column-right").style("width"))- margin.left - margin.right;
	var heighte=parseInt(d3.select(".column-right").style("height"))- margin.top
	- margin.bottom;
	
	var svge = d3.selectAll(".column-right").append("svg").attr("width",
			widthe + margin.left + margin.right).attr("height",
			heighte + margin.top + margin.bottom).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");
	
	//Initialize Axis
	
	var n = 30;// random = d3.random.normal(50, 5);
	var datat = d3.range(n) .map(function() { return 0; });
	var datar = d3.range(n) .map(function() { return 0; });
	var datae = d3.range(n) .map(function() { return 0; });
	
	//Throughput
		
	var xt = d3.scale.linear().domain([ 0, n - 1 ]).range([ 0, widtht ]);
	var yt = d3.scale.linear().domain([ 0, 300 ]).range([ heightt, 0 ]);
	var linet = d3.svg.line().x(function(d, i) {
		return xt(i);
	}).y(function(d, i) {
		return yt(d);
	});

	svgt.append("defs").append("clipPath").attr("id", "clip").append("rect")
			.attr("width", widtht).attr("height", heightt);
	svgt.append("g").attr("class", "x axis").attr("transform",
			"translate(0," + yt(0) + ")").call(
			d3.svg.axis().scale(xt).orient("bottom"));
	svgt.append("g").attr("class", "y axis").call(
			d3.svg.axis().scale(yt).orient("left"));
	var patht = svgt.append("g").attr("clip-path", "url(#clip)").append("path")
			.datum(datat).attr("class", "line").attr("d", linet);
	
	//Response Time
		
	var xr = d3.scale.linear().domain([ 0, n - 1 ]).range([ 0, widthr ]);
	var yr = d3.scale.linear().domain([ 0, 100 ]).range([ heightr, 0 ]);
	var liner = d3.svg.line().x(function(d, i) {
		return xr(i);
	}).y(function(d, i) {
		return yr(d);
	});
	
	svgr.append("defs").append("clipPath").attr("id", "clip").append("rect")
			.attr("width", widthr).attr("height", heightr);
	svgr.append("g").attr("class", "x axis").attr("transform",
			"translate(0," + yr(0) + ")").call(
			d3.svg.axis().scale(xr).orient("bottom"));
	svgr.append("g").attr("class", "y axis").call(
			d3.svg.axis().scale(yr).orient("left"));
	var pathr = svgr.append("g").attr("clip-path", "url(#clip)").append("path")
			.datum(datar).attr("class", "line").attr("d", liner);

	//Error Rate
	
	var xe = d3.scale.linear().domain([ 0, n - 1 ]).range([ 0, widthe ]);
	var ye = d3.scale.linear().domain([ 0, 100 ]).range([ heighte, 0 ]);
	var linee = d3.svg.line().x(function(d, i) {
		return xe(i);
	}).y(function(d, i) {
		return ye(d);
	});
	
	svge.append("defs").append("clipPath").attr("id", "clip").append("rect")
			.attr("width", widthe).attr("height", heighte);
	svge.append("g").attr("class", "x axis").attr("transform",
			"translate(0," + ye(0) + ")").call(
			d3.svg.axis().scale(xe).orient("bottom"));
	svge.append("g").attr("class", "y axis").call(
			d3.svg.axis().scale(ye).orient("left"));
	var pathe = svge.append("g").attr("clip-path", "url(#clip)").append("path")
			.datum(datae).attr("class", "line").attr("d", linee);
	
	tick();
	var prevtime="";
	var time="";
	function tick() {
		// push a new data point onto the back
		var value=getData(num);
		
		if(value.length>0){
			var res = value.split(" ");
			tpt= parseInt(res[0]);
			resp = parseInt(res[1]);
			err= parseInt(res[2]);
			time=res[3];
			datat.push(tpt);
			datar.push(resp);
			datae.push(err);
		}
		//console.log(time+" "+prevtime)
			if(time!=prevtime){
			prevtime=time;
			if(tpt>=maxt)
				maxt=tpt+20;

			var xnt = d3.scale.linear().domain([0,30]).range([0,widtht]);
            var ynt = d3.scale.linear().domain([0,maxt]).range([heightt,0]).nice();
            
            var linet = d3.svg.line().x(function(d, i) {
        		return xnt(i);
        	}).y(function(d, i) {
        		return ynt(d);
        	});

		 	var xAxist = d3.svg.axis().scale(xnt).orient("bottom");
	        var yAxist = d3.svg.axis().scale(ynt).orient("left");

	        svgt.selectAll("g.y.axis")
	            .call(yAxist)

	        svgt.selectAll("g.x.axis")
	            .call(xAxist);
		
	        
	        if(resp>=maxr)
				maxr=resp+10;
        	var xnr = d3.scale.linear().domain([0,30]).range([0,widthr]);
            var ynr = d3.scale.linear().domain([0,maxr]).range([heightr,0]).nice();

            liner = d3.svg.line().x(function(d, i) {
        		return xnr(i);
        	}).y(function(d, i) {
        		return ynr(d);
        	});
            
		 	var xAxisr = d3.svg.axis().scale(xnr).orient("bottom");
	        var yAxisr = d3.svg.axis().scale(ynr).orient("left");

	        svgr.selectAll("g.y.axis")
	            .call(yAxisr)

	        svgr.selectAll("g.x.axis")
	            .call(xAxisr);
	        
	        
	        if(err>=maxe)
				maxe=err+20;
        	var xne = d3.scale.linear().domain([0,30]).range([0,widthe]);
            var yne = d3.scale.linear().domain([0,maxe]).range([heighte,0]).nice();
            
            linee = d3.svg.line().x(function(d, i) {
        		return xne(i);
        	}).y(function(d, i) {
        		return yne(d);
        	});

		 	var xAxise = d3.svg.axis().scale(xne).orient("bottom");
	        var yAxise = d3.svg.axis().scale(yne).orient("left");

	        svge.selectAll("g.y.axis")
	            .call(yAxise)

	        svge.selectAll("g.x.axis")
	            .call(xAxise);
	        
		  // redraw the line, and slide it to the left
		patht
	      .attr("d", linet)
	      .attr("transform", null)
	      .style("stroke", "red")
	      .transition()
	      .duration(1000)
	      .ease("linear")
	      .attr("transform", "translate(" + xnt(-1) + ",0)");
	      pathr
		      .attr("d", liner)
		      .attr("transform", null)
		      .style("stroke", "red")
		      .transition()
		      .duration(1000)
		      .ease("linear")
		      .attr("transform", "translate(" + xnr(-1) + ",0)");
		  pathe
		      .attr("d", linee)
		      .attr("transform", null)
		      .style("stroke", "red")
		      .transition()
		      .duration(1000)
		      .ease("linear")
		      .attr("transform", "translate(" + xne(-1) + ",0)")
		      .each("end", tick);
		  // pop the old data point off the front
		  if(value.length>0){
			  datat.shift();
			  datar.shift();
			  datae.shift();
		  }
			}

	}
	
	function getData(row){
		
		//return 100;
		var val="";
		$
		.ajax({
			type : "POST",
			url : '/LoadGen/graph',
			data : {
				rownum : row
			},
			async : false,
			cache : false,
			timeout : 30000,
			success : function(value) {
				//console.log(value);
				val=value;
				/*var res = value.split(" ");
				val = parseInt(res[1]);
				console.log(val);*/
				//return 100;
			},
			error : function(e) {
				console.log(e);
				//return 0;
			}
		});
		/*if (val === parseInt(val, 10))
			return val;
		else
			return -1;*/
		return val;
	}
}
