var geocoder;
var map;

//product description page map initialize
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
	productAddressUpdate();
}

function autoCompleteCities(name) {
	var input = document.getElementById(name);
	var options = {
	  types: ['(cities)']
	};

	autocomplete = new google.maps.places.Autocomplete(input, options);
}

function map_callback(results, status) {
	if (status == google.maps.places.PlacesServiceStatus.OK) {
		var searchSuggest = [];
		for (var i = 0; i < results.length; i++) {
			searchSuggest[i] = results[i].name;
		}
		
		$("#searchCityInput").autocomplete({
		      source: searchSuggest
	    });
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
