/**
 * 
 */


$(document).ajaxStart(function() {
//	console.log("ajax start");
	$(".syncflag").show();	
});

$(document).ajaxStop(function(){
//	console.log("ajax stop");
	$(".syncflag").hide();
});


Array.prototype.insert = function (index, item) {
	this.splice(index, 0, item);
};

Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] == obj) {
            return true;
        }
    }
    return false;
}

function sortByKey(array, key, sortReverse) {
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0)) * sortReverse;
    });
}