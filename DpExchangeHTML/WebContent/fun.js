var scale = 1;
var fromValue;
var toValue;
//获取缩放比例
function getScaleSize() {
	var fromList = document.getElementById('list_from');
	fromValue = fromList.options[fromList.selectedIndex].value;
	var toList = document.getElementById('list_to');
	toValue = toList.options[toList.selectedIndex].value;
	scale = toValue / fromValue;
	scale = scale.toFixed(2);
	document.getElementById('scale_value').innerHTML = "Scale: "+scale;
}

function compute() {
	var instr = document.getElementById('str_in').value;
	var lines = instr.split('\n');
	var sb = "";
	for ( var i = 0; i < lines.length; i++) {
		var line = lines[i];
		var tmpStr = "";
		if (line.indexOf("<dimen") !== -1) {
			if (line.indexOf("dp") !== -1) {
				tmpStr = line.substring(line.indexOf(">") + 1, line
						.lastIndexOf("dp"));
				tmpStr = line.replace(tmpStr, Math.round(tmpStr*scale));
			} else if (line.indexOf("sp") != -1) {
				tmpStr = line.substring(line.indexOf(">") + 1, line
						.lastIndexOf("sp"));
				tmpStr = line.replace(tmpStr, Math.round(tmpStr*scale));
			}
			sb = sb + tmpStr + "\n";
		} else if (line.indexOf("<!--") !== -1) {
			if (line.indexOf(fromValue) !== -1) {
				tmpStr = line.replace(fromValue, toValue);
			}
			sb = sb + tmpStr + "\n";
		} else {
			sb = sb + line + "\n";
		}
	}
	document.getElementById('str_out').value = sb;
}

$(function() {
    var $tb1 = $('textarea[id$=str_in]');
    var $tb2 = $('textarea[id$=str_out]');
    $tb1.scroll(function() {
        $tb2.scrollTop($tb1.scrollTop());
    });
});