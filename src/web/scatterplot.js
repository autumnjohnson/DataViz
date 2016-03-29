function scatterplot(file) {
    getData();
};

function getData() {
    // d3.json("data.json", function(e, d) {
    d3.json("../Data/data.json", function(e, d) {
        data = d;
        initPlot();
        drawPlot();
    });
}

// constants and global variables
var data;
var width;
var height;
var XDIM = 1000;
var YDIM = 500;
var yPadding = 100000;
var xPadding = 20;

function elementsOverlap(sel1, sel2) {
  // bounding rectangles
  var r1 = sel1.node().getBoundingClientRect();
  var r2 = sel2.node().getBoundingClientRect();
   	 
   // http://stackoverflow.com/a/2752369
   return (r1.left <= r2.right &&  r2.left <= r1.right && 
               r1.top <= r2.bottom && r2.top <= r1.bottom);
}
function logCircleOutsidePlot(d) {
	var cr = this.getBoundingClientRect();
	console.log(
    "Circle in (%d, %d, %d, %d) is outside plot",
     cr.left, cr.right, cr.top, cr.bottom
  );
}
function negate(pred) {
  return function() {
    return !pred.apply(this, arguments);
  };
}
var elementsDontOverlap = negate(elementsOverlap);
// log circles
function logOverlappingCircle(d) {
  var cr = this.getBoundingClientRect();
 	 
  console.log(
    "Circle in (%d, %d, %d, %d) is obscured",
     cr.left, cr.right, cr.top, cr.bottom
  );
} 

// Block on the last "end" event (from http://stackoverflow.com/a/20773846)
function endall(transition, callback) { 
  if (transition.size() === 0) { callback() }
    var n = 0; 
    transition.each(function() { ++n; }) 
      .each("end", function() { if (!--n) callback.apply(this, arguments); }); 
}

// Call a callback, blocking on the end of all transitions
d3.transition.prototype.callOnEndAll = function(callback) {
  return this.call(endall, callback);
};

function drawPlot() {
    var paradigms = [];
    var arrs = _.pluck(data, "paradigms");
    var unique = _.union.apply(_, arrs);
    d3.select("#paradigmSelect").selectAll("option").data(unique).enter().append("option").text(function(d) {
        return d;
    }).attr("value", function(d) {
        return d;
    });


    // function that is fired on change event from option select
    var selectData = function() {
        // var filterOn = d3.select("#paradigmSelect").node().value;

        // // get data that contains the filter condition
        // var newData = _.filter(data, function(d) {
        //     return _.contains(d.paradigms, filterOn)
        // });
        
        var key = function(d) {
                return d.name;
        }
            // update data
        var dataAdded = d3.select("svg").selectAll(".datapoint").data(data, key);

        // fade out and remove old data
        dataAdded.exit().style("opacity", 1).transition().duration(500).style("opacity", 0).each("end", function() {
            d3.select(this).remove();
        });

        // calculate new graph scale
        var miny = d3.min(newData, function(d) {
            return d.nbRepos
        });
        var maxy = d3.max(newData, function(d) {
            return d.nbRepos
        });
        var minx = d3.min(newData, function(d) {
            return d.year
        });
        var maxx = d3.max(newData, function(d) {
            return d.year
        });

        var x = d3.scale.linear().range([0, width]).domain([minx - xPadding, maxx + xPadding]);
        var y = d3.scale.log().range([height, 0]).domain([miny, maxy + yPadding]);

        var formatxAxis = d3.format('.0f');
        var xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(formatxAxis).ticks(10);
        var yAxis = d3.svg.axis().scale(y).orient("right").ticks(5);

        // translate updated data on transition
        var updateTransition = dataAdded.transition().delay(500).duration(500).attr("transform", function(d) {
            return "translate(" + x(d.year) + "," + y(d.nbRepos) + ")";
        })
        updateTransition.callOnEndAll(function() {
        	d3.selectAll("circle").join(d3.selectAll("text"), elementsOverlap).each(logOverlappingCircle);
        	d3.selectAll("circle").join(d3.select("svg"), elementsDontOverlap).each(logCircleOutsidePlot);
        });	

        // rescale graph on transition
        d3.select(".yaxis").transition().delay(500).duration(500).call(yAxis)
        d3.select(".xaxis").transition().delay(500).duration(500).call(xAxis);

        // create new nodes on transition
        var gNode = dataAdded.enter().append("g").classed("datapoint", true);
        gNode.attr("transform", function(d) {
            return "translate(" + x(d.year) + "," + y(d.nbRepos) + ")";
        });
        var addTransition = gNode.style("opacity", 0).transition().delay(1000).duration(500).style("opacity", 1).each("start", function() {
            var me = d3.select(this);
            me.append("circle").attr("r", 5).attr("fill", "red");
            me.append("text").attr("dx", function(d) {
                return -10 - (d.name.length * 8)
            }).text(function(d) {
                return d.name
            });
        });
        addTransition.callOnEndAll(function() {
        	d3.selectAll("circle").join(d3.selectAll("text"), elementsOverlap).each(logOverlappingCircle);
        	d3.selectAll("circle").join(d3.select("svg"), elementsDontOverlap).each(logCircleOutsidePlot);
        });

    };
    d3.select("#paradigmSelect").on("change", selectData);

    // call for initial condition before any events have fired
    selectData();
}

function initPlot() {
    var margin = {
        top: 20,
        right: 10,
        bottom: 20,
        left: 10
    };
    width = XDIM - margin.left - margin.right;
    height = YDIM - margin.top - margin.bottom;
    var svg = d3.select("#graph").append("svg").attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom);

    var g = svg.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    g.append("g").attr("class", "xaxis").attr("transform", "translate(0," + height + ")");

    g.append("g")
        .attr("class", "yaxis");
    svg.append("text").attr("text-anchor", "end").attr("x", width).attr("y", height + 10).text("Year").style("font-size", "12px");
    svg.append("text").attr("text-anchor", "end").attr("y", 0).attr("dy", ".75em").attr("transform", "rotate(-90)").text("Number of Repos").style("font-size", "12px");
}