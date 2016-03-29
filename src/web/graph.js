var errorMessage = "unable to create";
function drawGraph() {
    $.ajax({
        type: 'POST',
        url: '../parser',
        // encode for server to parse
        data: $( "#graphType option:selected" ).text() + ":::" + document.getElementById("query").value.toLowerCase(),

        success: function(msg){
            var type = $( "#graphType option:selected" ).text();
            makeGraph(msg, type);
            document.getElementById("descriptionBody").innerHTML = msg;
            if (msg == errorMessage) {
                document.getElementById("descriptionBody").classList.add("error");
            } else {
                document.getElementById("descriptionBody").classList.remove("error");
            }
        }
    });

    function makeGraph(file, type) {
        var errorMessage = '<div class=\"error\">Graph not available for this query</div>';
        $('#graph').hide();
        var query = query = document.getElementById("query").value.toLowerCase();
        if (type == "pie") {
            var graphNode = document.getElementById("graph");
            graphNode.innerHTML = '';

            // visualizations hardcoded for specific queries hardcoded
            // because our language is only for descriptions of graphs
            // these are examples of graphs you could create with these descriptions
            if (query == "number of github repositories by programming paradigm") {
                pieChart("../Data/pie_chart_data.tsv");
            } else if (query == "number of people by state") {
                pieChart("../Data/pie_chart_state_data.tsv");
            } else {
                graphNode.innerHTML = errorMessage;
            }
        } else if (type == "scatter") {
            var graphNode = document.getElementById("graph");
            graphNode.innerHTML = '';
            if (query == "creation year versus number of repos of language") {
                scatterplot();
            } else {
                graphNode.innerHTML = errorMessage;
            }
        } else if (type == "bar") {
            var graphNode = document.getElementById("graph");
            graphNode.innerHTML = '';
            if (query == "number of students by grade") {
                barGraph();
            } else {
                graphNode.innerHTML = errorMessage;
            }
        }
        $("#graph").fadeIn(1000);
    }
}

document.getElementById("submitbutton").onclick = drawGraph;