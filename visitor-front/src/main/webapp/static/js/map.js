var geocoder;
var map;
function mapInitialize() {
	geocoder = new google.maps.Geocoder();
	var latlng = new google.maps.LatLng(-34.397, 150.644);
	var mapOptions = {
		center: latlng,
		zoom: 8,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
}

function getPos() {
	var address = document.getElementById("address").value;
	geocoder.geocode( 
		{'address': address}, 
		function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				map.setCenter(results[0].geometry.location);
				var marker = new google.maps.Marker({
					map: map,
					position: results[0].geometry.location
				});
			} else {
				alert("Geocode was not successful for the following reason: " + status);
			}
		}
	);
}

function hideCities(node) {
	$("#productCityPart").hide();
}

function showCities(node) {
	$("#productCityPart").show();
}

function map_callback(results, status) {
	if (status == google.maps.places.PlacesServiceStatus.OK) {
		$("#destinationPart").html("");
		for (var i = 0; i < results.length; i++) {
			$("#destinationPart").append('<li onclick="setPosValue(this)">'+results[i].name+'</li>');
		}
	}
}

function displayPosResult(node) {
	var valueT = $(node).val();
	var valueO = $.trim(valueT);
	if (valueO.length > 3) {
		var request = {
			query: valueO
		};
		service = new google.maps.places.PlacesService(map);
		service.textSearch(request, map_callback);
	}
}

function setPosValue(node) {
	var valueT = $(node).html();
	
	var inputNode = $("#searchCityInput");
	inputNode.val(valueT);
	hideCities(inputNode);
}