//const confirmPlans = (function() {
const confirmPlans = (function () {
	/** global variable *******************/

	// The default address when the page is read.
	const DEFAULT_ADDRESS = "6 Southwark St, London SE1 1TQ Ingland";
	const LATITUDE = 51.505;
	const LONGITUDE = -0.09;
	const VIEW_ZOOM_LEVEL = 15;
	
	const HIDE_SORT_TRUE = true;
	const CLEAR_ROUTE_TRUE = true;
	
	/* delimiter */
	// colon for:
	// - search no. and place id
	const DELIMITER_COLON = ':';
	
	
	// -- spot-list recode number count
	// let spotListRecordNo = 1;
	let luggageCntNo = 0;
	// current geoJson layerGroup on the map
	// var searchedPointGeoLayerGroup = new L.layerGroup();
	// targeted geoLayer map
	var searchedGeoLayerMap = new Map();
	// var routeGeoLayerList = new Array();

	/** global variable end****************/
	
	// method right before submit
	const beforeSubmit = (function() {
		$('input[id^="submit"]').click(function() {
			var url = '';

			if ($(this).attr('id') == 'submit') {
				// if the button is not submit button
				url = '/work02/travel/tripPlans/createRoute/prepLuggage/confirmPlans/resultTripPlans';
			} else {
				if ($(this).attr('id') == 'submit-back') {
					// if the button is back button
					url = '/work02/travel/tripPlans/createRoute/prepLuggage?back';
				}
			}
			// rewrite the action
			$('form').attr('action', url);				
			$('form').submit();
		});
	});
	
	const setEvent = (function() {
		beforeSubmit();
	});
	
	$(function(){
		setEvent();
		$('#search-button').trigger('click');
		//dsiplaySortSpotList($('#luggage-list-table').find('tbody'));
		location.href="#";
	});
});

$(function(){
    confirmPlans();
});