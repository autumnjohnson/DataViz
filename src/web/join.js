d3.transition.prototype.join = d3.selection.prototype.join;

d3.selection.prototype.join = function(sel2, pred) {
  // your code goes here
  var n = this[0];
  var m = sel2[0];

  var result = [];
  for (var i = 0; i < n.length; i++) {
  	var isInJoin = false;
  	for (var j = 0; j < m.length; j++) {
  		var l = d3.select(n[i]);
  		var r = d3.select(m[j]);
  		// console.log(m[j]);
  		if (pred(l, r)) {
  			isInJoin = true;
  			// break;
  		}
  	}	
  	if (isInJoin) {
  		result.push(n[i]);
  	}
  }

  return d3.selectAll(result);
}
