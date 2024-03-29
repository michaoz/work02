//const createRoute = (function() {
const createRoute = (function () {
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
	let spotCntNo = 0;
	// map
	var map = L.map('mapContainer');
	// current geoJson layerGroup on the map
	// var searchedPointGeoLayerGroup = new L.layerGroup();
	var pointGeoLayerGroup = new L.layerGroup();	
	var lineGeoLayerGroup = new L.layerGroup();
	// targeted geoLayer map
	var searchedGeoLayerMap = new Map();
	// var routeGeoLayerList = new Array();

	/** global variable end****************/
	
	// create the searched-result records
	const createSearchResultRecord = (function(jsonResultData, recordNum) {
		var searchedLeafletId = searchedGeoLayerMap.get(jsonResultData["place_id"])._leaflet_id + 1;
		var pointLeafletId = Object.keys(pointGeoLayerGroup._layers)[recordNum - 1];
		if (pointLeafletId == undefined) {
			pointLeafletId = searchedLeafletId;
		}
		
		let tdNo = '<td class="js-search-result-record-no' + jsonResultData["place_id"] + '-' + recordNum 
		           + ' css-search-result-record-no">' + recordNum + '</td>';
		let tdAddress = '<td class="js-search-result-address">' + jsonResultData["display_name"] + '</td>';
		let tdLatLon = '<td style="display: none" class="js-search-result-lanlon">' + jsonResultData["lat"] + '_' + jsonResultData["lon"] + '</td>';
		let tdLeafletId = '<td style="display: none" class="js-search-result-leafletid">' + searchedLeafletId + '_' + pointLeafletId + '</td>';
		/*
		let tdCheckbock = '<td class="css-search-result-checkbox-td">'
     	    + '<input type="checkbox" class="js-search-result-checkbox-'
			+ jsonResultData["place_id"] 
     	    + ' css-search-result-checkbox"'
		    + ' value="' + jsonResultData["place_id"]
		    + '"/></td>';
		*/
		let tdAddButton = '<td id="css-search-result-button-td">'
			+ '<button type="button" '
			+ 'name="' + jsonResultData["place_id"] + '"'
			+ ' class="js-search-result-button-' + jsonResultData["place_id"] 
		    + ' css-search-result-button"'
	        + ' value="' + jsonResultData["place_id"]
            + '"></button></td>';
            
		var tbody = '<tbody>'
			    + '<tr>'
		        + tdNo
			    + tdAddress
			    + tdLatLon
			    + tdLeafletId
		     /* + tdCheckbock */
			    + tdAddButton
			    + '</tr>'
			    + '</tbody>';
		return tbody;
	});

	// 	create spot list records
	const createSpotListRecord = (function(recordNum, addressText, placeId, latLonText, pointLeafletId) {
		recordNum++;

		var searchedLeafletId = searchedGeoLayerMap.get(Number(placeId.split(DELIMITER_COLON)[1]))._leaflet_id + 1;
		if (pointLeafletId == undefined || pointLeafletId == null) {
			pointLeafletId = searchedLeafletId;
		}
		
		//let addressNumPattern = /^\d{3}-\d{4}/;
		const addressNumPattern = /^\d+$|^\d+-\d+/;
		let addressNumIdx = 0;
		let spotName, city, address = '';
		// アドレスをカンマで区切って配列にする
		if (addressText) {
			var adressList = addressText.split(/,/);
			
			/* set Spot List's columns */
			// Spot Name
			spotName = adressList[0].replace(/\s+/g, '');
			
			// City Name
			city = adressList[adressList.length - 2].replace(/\s+/g, '') 
			                   ? adressList[adressList.length - 2].replace(/\s+/g, '')
			        		   : '';  // undefinedの場合
			/// search numbers and make a list
			var matchedList = adressList.filter(
					x => addressNumPattern.test(x.replace(/\s+/g, '')));
			if (matchedList.length != 0) {
				var addressNum = matchedList.sort(
						(y1, y2) => adressList.indexOf(y1) - adressList.indexOf(y2))[0];
				addressNumIdx = adressList.indexOf(addressNum);
			}
			if (addressNumIdx > 0) {
				city = adressList[addressNumIdx - 1].replace(/\s+/g, '')
				           ? adressList[addressNumIdx - 1].replace(/\s+/g, '')
				           : '';  // undefinedの場合
			}
			
			// Address
			// 空白のない文字列として連結し、全ての文字列に関して空白を排除する。
			address = addressText.replace(/\s+/g, '');
		}	
		//alert(spotName + "||" + city + "||" + address);
		
		var tdNo = '<td id="spot-list-record-no-' + placeId + '" >' + recordNum + '</td>';
		var tdSortLeader = '<td><span></span>' + '</td>';
		var tdSpotName = '<td id="spot-list-record-content-spotName-' + placeId + '">' 
		                 + spotName + '</td>';
		var tdCity = '<td id="spot-list-record-content-city-' + placeId + '">'
		             + city + '</td>';
		var tdAddress = '<td id="spot-list-record-content-address-' + placeId + '">'
                        + address + '</td>';
//		var latLon = '<td style="display: none" id="spot-list-record-content-latLon-' + searchedLeafletId + '">'
		var latLon = '<td style="display: none" id="spot-list-record-content-latLon-' + placeId + '">'
                     + latLonText + '</td>';
		var tdLeafletId = '<td style="display: none" id="spot-list-record-content-leafletid">' + searchedLeafletId + '_' + pointLeafletId + '_' + placeId + '</td>';		
        var tdEmitChbox = '<td>' + '<input type="checkbox" id="spot-list-record-content-emit-' + placeId + '" '
                          + 'class="css-list-chbox" value=' + placeId + '>'
                          + '<label for="spot-list-record-content-emit-' + placeId + '" class="css-list-chbox-label">✓</label>'
                          + '</td>';

		var tr = '<tr id="' +  recordNum + '">'  
			+ tdSortLeader
			+ tdEmitChbox
			+ tdNo 
			+ tdSpotName 
			+ tdCity 
			+ tdAddress
			+ latLon
			+ tdLeafletId
			+ '</tr>';

		return tr;
	});

	// create delete list records
	const createDeleteListRecord = (function(recordNum, target, placeId, pLeafletId) {
		recordNum++;

		var searchedLeafletId = searchedGeoLayerMap.get(Number(placeId.split(DELIMITER_COLON)[1]))._leaflet_id + 1;
//		var pointLeafletId = Object.keys(pointGeoLayerGroup._layers)[recordNum - 1];
		if (pLeafletId == undefined) {
			pLeafletId = searchedLeafletId;
		}
		
		var spotName = target.children('td[id^="spot-list-record-content-spotName-"]')[0].textContent;
		var city = target.children('td[id^="spot-list-record-content-city-"]')[0].textContent;
		var address = target.children('td[id^="spot-list-record-content-address-"]')[0].textContent;
		var latLon = target.children('td[id^="spot-list-record-content-latLon-"]')[0].textContent;

		var tdNo = '<td id="delete-list-record-no-' + placeId + '" >' + recordNum + '</td>';
        /*
		var tdSpotName = '<td>' + '<input type="text" id="delete-list-record-content-spotName-' + recordNum 
		                 + '" value=' + spotName + '>' + '</td>';
		var tdCity = '<td style="display: none">' + '<input type="text" id="delete-list-record-content-city-' + recordNum + '" value=' + city + '>' + '</td>';
		var tdAddress = '<td style="display: none">' + '<input type="text" id="delete-list-record-content-address-' + recordNum + '" value=' + address + '>' + '</td>';
        */
		var tdSpotName = '<td id="delete-list-record-content-spotName-' + placeId + '">' 
		                 + spotName + '</td>';
        var tdCity = '<td style="display: none" id="delete-list-record-content-city-' + placeId + '">'
                     + city + '</td>';
        var tdAddress = '<td style="display: none" id="delete-list-record-content-address-' + placeId + '">'
                        + address + '</td>';
        var tdLatLon = '<td style="display: none" id="delete-list-record-content-latLon-' + placeId + '">'
                        + latLon + '</td>';
		var tdLeafletId = '<td style="display: none" id="delete-list-record-content-leafletid">' + searchedLeafletId + '_' + pLeafletId + '_' + placeId + '</td>';
		var tdAddChbox = '<td>' + '<input type="checkbox" id="delete-list-record-content-emit-' + placeId + '" '
		            + 'class="css-list-chbox" value=' + placeId + '>'
	                + '<label for="delete-list-record-content-emit-' + placeId + '" class="css-list-chbox-label">✓</label>'
		            + '</td>';
		      
		var tbody = '<tbody id="" >' 
			+ tdAddChbox
			+ tdNo
			+ tdSpotName
			+ tdCity
			+ tdAddress
			+ tdLatLon
			+ tdLeafletId
			+ '</tbody>';
		
		return tbody;
	});

	// add an record to the spot list when the button's clicked
	const addSpotListRecode = (function() {
		$('.js-add-recode').click(function() {
			var spotListRecordNo = $('td[id^="spot-list-record-no"]').last().text();
			if (!spotListRecordNo) {
				spotListRecordNo = 0;
			}
			var tr = createSpotListRecord(spotListRecordNo, null, null, null);
			$('#spot-list-table').append(tr);  // $('table[id="spot-list-table"]').append(tbody);

		});
	});
	
	// send to delete list: 
	// send the checked recode to the delete list when the button's clicked
	const sendToDeleteList = (function() {
		$('.js-to-delete-list').click(function() {
			// hide the sort pane and clear the route on map
			commonClear(HIDE_SORT_TRUE, CLEAR_ROUTE_TRUE);
			
			var emitRecodeList = $('[id^="spot-list-record-content-emit-"]');
			var deleteTrList = new Array();
			emitRecodeList.each(function() {
				if($(this).prop('checked')) {
					var targetTr = $(this).closest('tr');
					deleteTrList.push(targetTr);
				}
			});
			$.each(deleteTrList, function(idx, deleteTargetTr) {
				var deletedTarget = delHtml(deleteTargetTr);
				delHtml(deleteTargetTr.closest('tbody'));
				
				var deleteListRecordNo = $('td[id^="delete-list-record-no"]').last().text();
				if (!deleteListRecordNo) {
					deleteListRecordNo = 0;
				}
				// Get placeId
				var deletedTds = Array.from(deletedTarget[0].children);  // HTMLCOllectionを配列に変換
				var deletedTd = deletedTds.filter(tdElm => tdElm.id.startsWith('spot-list-record-no-'));
				var placeId = deletedTd[0].id.substring(
						deletedTd[0].id.lastIndexOf('-', deletedTd[0].id.length) + 1
						, deletedTd[0].id.length
						);  // id part=placeId
				var pLeafletId = deletedTds.filter(tdElm => tdElm.id.startsWith('spot-list-record-content-leafletid'))[0].textContent.split('_')[1];
				
				var tbody = createDeleteListRecord(deleteListRecordNo, deletedTarget, placeId, pLeafletId);
				addHtml($('#delete-list-table'), tbody);
			});
			
			// reassign numbers to the recodes
			var spotRecodeNumTds = $('td[id^=spot-list-record-no-]');
			//var spotTableMaxRowNum = document.getElementById('spot-list-table').rows.length;
			
			// reassign recode No
			spotRecodeNumTds.sort(function(a, b) {
				if (Number(a.textContent) < Number(b.textContent)) {
					return -1;
				} else {
					return 1;
				}
			});	
			$.each(spotRecodeNumTds, function(idx, td) {
				td.textContent = idx + 1;
				td.parentElement.id = td.textContent;
			});
			
			// remove its marker from the map
			var newLayersFeatureList = new Array();  // a list for new layers' features
			var pointGeoLayers = $.extend({}, pointGeoLayerGroup._layers);
			pointGeoLayerGroup.clearLayers();
			
			for (var pLeafletId in pointGeoLayers) {
				var newLayerFlg = true;  // creating a new layer: true / false
				
				// get info of pointGeoLayer
				var pointGeoLayer = pointGeoLayers[pLeafletId];
				
				var lat = pointGeoLayer._layers[pLeafletId - 1]._latlng.lat;
				var lon = pointGeoLayer._layers[pLeafletId - 1]._latlng.lng;
					
				// get info of deleted layer
				for (let i = 0; i < deleteTrList.length; i++) {
					var targetDeleteTd = deleteTrList[i].children('[id^="spot-list-record-content-latLon-"]');
					var deleteLat = targetDeleteTd[0].textContent.split("_")[0];
					var deleteLon = targetDeleteTd[0].textContent.split("_")[1];
					var deleteLeafletId = deleteTrList[i].children('[id^="spot-list-record-content-leafletid"]').text();
					deleteLeafletId = deleteLeafletId.split('_')[1];
					
					// remove the layer if it's the deleting target
					if (lat == Number(deleteLat) && lon == Number(deleteLon) && pLeafletId == Number(deleteLeafletId)) {
						newLayerFlg = false;
						break;
					}
					
//					for (let key of searchedGeoLayerMap.keys()) {
//						if (lat == Number(deleteLat) && lon == Number(deleteLon) && pLeafletId == Number(deleteLeafletId)) {
//							newLayerFlg = false;
//						} else {
//							newLayerFlg 
//						}
//					}
					
//					if (newLayerFlg) {
//						newLayersFeatureList.push(pointGeoLayer._layers[pLeafletId - 1]);				
//					}
				}
				if (newLayerFlg) {
//					newLayersFeatureList.push(pointGeoLayer._layers[pLeafletId - 1].feature);				
					newLayersFeatureList.push(pointGeoLayer._layers[pLeafletId - 1]);				
				}
			}
			if (newLayersFeatureList.length > 0) {
				var a = null;
				newLayersFeatureList.forEach(newLayer => a = newLayer);
				// register a new layers
				newLayersFeatureList.forEach(newLayer => controlGeoInfo(newLayer.feature, null, null, null, newLayer._leaflet_id))				
			}			
		});
	});
	
	// Sync PointGeoLayer leaflet_id on HTML when the layers on map were edited
	const syncPLeafletIdOnHtml = (function(pLeafletId, geoLayer) {
		$('[id = "spot-list-record-content-leafletid"]').each(function() {
			var leafletIdText = $(this).text();
			if (leafletIdText.split('_')[1] == (pLeafletId + 1)) {
				$(this).text(leafletIdText.split('_')[0] + '_' + geoLayer._leaflet_id + '_' + leafletIdText.split('_')[2]);
			}
		});
	});
		
	// sort numbers
	const sortNumber = (function(targetList) {
		targetList.sort(function(a, b) {
			if (Number(a) < Number(b)) {
				return -1;
			} else {
				return 1;
			}
		});
	})
	
	// display sort pane of Spot List
	const dsiplaySortSpotList = (function() {
		commonClear(null, CLEAR_ROUTE_TRUE);
		
		$('#spot-list-table').find('#header-table').children('th').first().css("display", "table-cell"); /* or .show() */
		$('#spot-list-table').find('tr').each(function() {
			$(this).find('td').first().fadeIn(200);
		});
		$('#spot-list-table').find('tbody').sortable({
			disabled: false,
			update: function() {
				$('td[id^="spot-list-record-no-"]').each(function(idx) {
					$(this).val(idx + 1);
					$(this)[0].textContent = idx + 1;  // rewrite td.textContent
					idx++;
				});
			}
		});
		// extract Ids from tr elements 
		var trIds = $('#spot-list-table').children('tbody').sortable("toArray");		
	});
	// hide sort pane of Spot List
	const hideSortSpotList = (function() {
		$('#spot-list-table').find('#header-table').children('th').first().css("display", "none");
		$('#spot-list-table').find('tr').each(function() {
			$(this).find('td').first().hide();
		});
		$('#spot-list-table').find('tbody').sortable({
			disabled: true
		});
	});
	
	// back to recode:
	// send the checked recode back to the spot list when the button's clicked
	const sendBackToSpotList = (function() {
		$('.js-back-to-recode').click(function() {
			// hide the sort pane and clear the route on map
			commonClear(HIDE_SORT_TRUE, CLEAR_ROUTE_TRUE);
			
			var backToRecodeList = $('[id^="delete-list-record-content-emit-"]');
			var backToSpotList = new Array();
			backToRecodeList.each(function() {
				if($(this).prop('checked')) {
					backToSpotList.push($(this).closest('tr'));
				}
			});
			$.each(backToSpotList, function(idx, backToTarget) {
				var backedToTarget = delHtml(backToTarget);	
				delHtml(backToTarget.closest('tbody'));		
				var addressText = backedToTarget.children('td[id^="delete-list-record-content-address-"]')[0].textContent;			
				
				// placeIdの取得
				var backedTds = Array.from(backedToTarget[0].children);  // HTMLCOllectionを配列に変換
				var backedTd = backedTds.filter(tdElm => tdElm.id.startsWith('delete-list-record-no-'));
				var placeId = backedTd[0].id.substring(
						        backedTd[0].id.lastIndexOf('-', backedTd[0].id.length) + 1
						        , backedTd[0].id.length
						      );  // id部分=placeIdの取得
				//var placeId = backToTarget.children('td[id^="delete-list-record-content-emit-"]');
				
				var spotListRecordNo = $('td[id^="spot-list-record-no"]').last().text();
				if (!spotListRecordNo) {
					spotListRecordNo = 0;
				}
				
				var latLonText = backedTds.filter(tdElm => tdElm.id.startsWith('delete-list-record-content-latLon-'))[0].textContent;
				var pointLeafletId = backedTds.filter(tdElm => tdElm.id.startsWith('delete-list-record-content-leafletid'))[0].textContent.split('_')[1];
				
				var tr = createSpotListRecord(spotListRecordNo, addressText, placeId, latLonText, pointLeafletId);
				addHtml($('#spot-list-table'), tr);
				
			});
			
			// reassign numbers to the recodes
			var deleteRecodeNums = $('td[id^="delete-list-record-no-"]');
			deleteRecodeNums.sort(function(a, b) {
				if (Number(a.textContent) < Number(b.textContent)) {
					return -1;
				} else {
					return 1;
				}
			});	
			$.each(deleteRecodeNums, function(idx, num) {
				num.textContent = idx + 1;
			});
			
			
			// add its marker back to the map
			var newLayersFeatureList = new Array();  // a list for new layers' features
			var pointGeoLayers = $.extend({}, pointGeoLayerGroup._layers);
//			pointGeoLayerGroup.clearLayers();
			/*
			for (let pLeafletId in pointGeoLayers) {
				var newLayerFlg = true;  // creating a new layer: true / false
				
				// get info of pointGeoLayer
				var pointGeoLayer = pointGeoLayers[pLeafletId];
				var id = pointGeoLayer._leaflet_id;
				var lat = pointGeoLayer._layers[id - 1]._latlng.lat;
				var lon = pointGeoLayer._layers[id - 1]._latlng.lng;
					
				// get info of deleted layer
				var deleteLat, deleteLon;
				*/
				backToSpotList.forEach(function(backedToTarget) {
					var spotLat = backedToTarget.children('[id^="delete-list-record-content-latLon-"]')[0].textContent.split('_')[0];
					var spotLon = backedToTarget.children('[id^="delete-list-record-content-latLon-"]')[0].textContent.split('_')[1];
					var addressText = backedToTarget.children('td[id^="delete-list-record-content-address-"]')[0].textContent;
					var pLeafletId = backedToTarget.children('td[id="delete-list-record-content-leafletid"]').text().split('_')[1];

					controlGeoInfo(null, spotLat, spotLon, addressText, Number(pLeafletId) - 1);
				});
				/*
				if (newLayerFlg) {
					newLayersFeatureList.push(pointGeoLayer._layers[id - 1].feature);				
				}
			}
			if (newLayersFeatureList.length > 0) {
				// register a new layers
				newLayersFeatureList.forEach(newLayerFeature => controlGeoInfo(newLayerFeature, null, null, null))				
			}*/
		});
	});
	
	// add HTML element to the htmlElm
	const addHtml = (function(htmlElm, target) {
		htmlElm.append(target);
		return target;
	});
	
	// remove HTML element
	const delHtml = (function(target) {
		var deletedElm = target.remove();
		return deletedElm
	});

	// add search results to the spot list
	const addToSpotList = (function() {
		// add to list
		$(document).off('click.name1', '[class^="js-search-result-button-"]');
		$(document).on('click.name1', '[class^="js-search-result-button-"]', function(){
			// hide the sort pane and clear the route on map
			commonClear(HIDE_SORT_TRUE, CLEAR_ROUTE_TRUE);
			
			// get data from search results
			var address = $(this).parent().prevAll('.js-search-result-address').text();
			var placeId = spotCntNo + ':' + $(this).attr('name');
			var latLonText = $(this).parent().prevAll('.js-search-result-lanlon').text();
			var sLeafletId = $(this).parent().prevAll('.js-search-result-leafletid').text().split("_")[0];			
			var spotListRecordNo = $('td[id^="spot-list-record-no"]').last().text();
			if (!spotListRecordNo) {
				spotListRecordNo = 0;
			}

			/** create spot list record */
			var pointLeafletId = Object.keys(pointGeoLayerGroup._layers)[spotListRecordNo];
			var tr = createSpotListRecord(spotListRecordNo, address, placeId, latLonText, pointLeafletId);
			addHtml($('#spot-list-table'), tr);  //$('#spot-list-table').append(tbody);  // $('table[id="spot-list-table"]').append(tbody);
			
			// spotCntNoを更新
			spotCntNo++;
			
			/** create layers */
			// copy pointGeoLayerGroup._layers to a new object 
			var pointGeoLayers = $.extend({}, pointGeoLayerGroup._layers);
			if (pointGeoLayers != null && pointGeoLayers.length != 0) {
				// clear the pointGeoLayerGroup if there's no records at spot list
				if ($('#spot-list-table').find('tr').length <= 2) {
					pointGeoLayerGroup.clearLayers();					
				}
			}

			var lat = latLonText.split("_")[0];
			var lon = latLonText.split("_")[1];
			for (let key of searchedGeoLayerMap.keys()) {
				var searchedLat = searchedGeoLayerMap.get(key)._latlng.lat;
				var searchedLon = searchedGeoLayerMap.get(key)._latlng.lng;
				var searchedLeafletId = searchedGeoLayerMap.get(key)._leaflet_id + 1;		
				var pLeafletId = searchedGeoLayerMap.get(key)._leaflet_id;

				if (searchedLat.toString() == lat && searchedLon.toString() == lon && searchedLeafletId.toString() == sLeafletId) {
					controlGeoInfo(searchedGeoLayerMap.get(key).feature, searchedLat, searchedLon, null, pLeafletId);
				}
			}
			
//			/** create spot list record */
//			var pointLeafletId = Object.keys(pointGeoLayerGroup._layers)[spotListRecordNo - 1];
//			var tr = createSpotListRecord(spotListRecordNo, address, placeId, latLonText, pointLeafletId);
//			addHtml($('#spot-list-table'), tr);  //$('#spot-list-table').append(tbody);  // $('table[id="spot-list-table"]').append(tbody);
//			
//			// spotCntNoを更新
//			spotCntNo++;
		});
	});

    // search for the place (ajax)
	const searchPlace = (function() {
		// search button
		$('#search-button').on('click', function() {
//		$('#search-button').click(function() {
			var jsonResultDataList = new Array();
			var resultPlaceIdList = new Array();
			
			// delete the old searched results
			delHtml($('#search-result-table').children('tbody'));
			
			var requestContent = $('input[id="content-to-search"]').val();
			//alert(requestContent);
			
			const MAP_URL = "https://nominatim.openstreetmap.org/search";
			const HEROKU_PROXY = "https://immense-atoll-08173.herokuapp.com/";
			var params = "?format=json" + "&q=" + requestContent;
			var url = MAP_URL + params;

			const ALLOWED_METHODS = [
			    'GET',
			    'POST',
			    'PUT',
			    'PATCH',
			    'DELETE',
			    //'HEAD',
			    'OPTIONS'
			];
			const ALLOWED_ORIGINS = [
				MAP_URL
			];
			const ALLOWED_HEADERS = [
				//"Accept, Content-Type, Content-Length, Accept-Encoding, X-Requested-With, Origin, X-Csrftoken, X-CSRF-Token, Authorization"
				'Content-type',
				'Accept',
				'X-Custom-Header'
			];
			
			//var headers = {"x-csrf-token": $('input[name=_csrf]').val()};
			var headers = {"Access-Control-Allow-Origin": ALLOWED_ORIGINS,
					       //"Content-Type" : "application/json;charset=UTF-8", 
					       "Access-Control-Allow-Methods" : ALLOWED_METHODS.join(','),
					       "Access-Control-Allow-Headers" : ALLOWED_HEADERS.join(',')
					       };

			$.ajax({	
				headers:headers, // 現場のソース													
				url:url,
				type:"GET",  // 現場のソースはpost
				//data:requestContent, // 送信するデータ
				dataType: 'json', // 応答のデータの種類 (xml/html/script/json/jsonp/text)
				//jsonpCallback: 'callback',
				timespan:1000,  // 通信のタイムアウトの設定(ミリ秒)
				// async: false, 同期にする場合はasync:falseを追加する
				processData:false, // 現場のソース
				contentType:false, // 現場のソース
				context:this, // 現場のソース
				
				//  引数：response　通信で取得したデータ
				//  引数：textStatus　通信結果のステータス
				//  引数：jqXHR　XMLHttpRequestオブジェクト
				}).done(function(response,textStatus,jqXHR) {
					console.log("Api Response: " + jqXHR.status 
							+ "(" + textStatus + ")"); // 例：200, success

					// レスポンスをJSON形式の文字列に変換
					var strResponse = JSON.stringify(response);
					console.log(strResponse);
					var jsonStrResponseList = strResponse.slice(1, -1).split('},');
					$.each(jsonStrResponseList, function(idx, jsonStrResponse) {
						if(idx != (jsonStrResponseList.length - 1)) {
							jsonStrResponse = jsonStrResponse + '}';							
						}
						// JSON形式の文字列をJSONオブジェクトに変換
						var result = JSON.parse(jsonStrResponse);
						jsonResultDataList.push(result);
					});
					$.each(jsonResultDataList, function(idx, jsonResult) {
						console.log(jsonResult["place_id"]);
						resultPlaceIdList.push(jsonResult["place_id"]);
					});
					
					if (resultPlaceIdList.length != 0) {
						showOnMap(jsonResultDataList);
						displayResult(jsonResultDataList, resultPlaceIdList);
					}
					// add an event of add-to-spot-list event
					addToSpotList();

				}).fail(function(jqXHR, textStatus, errorThrown ) {
				    console.log(jqXHR.status); //例：404
				    console.log(textStatus); //例：error
				    console.log(errorThrown); //例：NOT FOUND

			    // alwaysは成功/失敗に関わらず実行される
				}).always(function(){
			        console.log("api done");
			});
		});
	});
	
	// display the result of the search
	const displayResult = (function(jsonResultDataList, resultPlaceIdList) {		
		var searchResultTbodies = null;
		var displayRecordNum = 0;
		$.each(jsonResultDataList, function(idx, jsonResultData) {
			displayRecordNum++;
			searchResultTbodies = searchResultTbodies + createSearchResultRecord(jsonResultData, displayRecordNum)
		});
		$('#search-result-table').append(searchResultTbodies);
	});	

	// reassign recode numbers by toArray
	
	
	// sort Spot List:
	// handle the sort pane of Spot List
	const handleSortPane = (function() {
		$('[class^="js-to-sort-spot-list"]').click(function() {
			if ($('div[class^="js-draw-route"]').children('input[name="check"]').prop('checked')) {
				// clear the route on map
				$('.js-draw-route').trigger('click');				
			}

			
			if ($('#spot-list-table').find('#header-table').children('th').first().is(':hidden')) {
				dsiplaySortSpotList();
			} else {
				hideSortSpotList();
			}
       	});
	});

	const initMap = (function(address, latitude, longitude) {
	  //var map = L.map('mapContainer').setView([51.505, -0.09], VIEW_ZOOM_LEVEL);
	  map.setView([latitude, longitude], VIEW_ZOOM_LEVEL);
		
	  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
	      maxZoom: 19,
	      attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
	  }).addTo(map);

	  /* add a marker <- ordinary way if you don't use geoJson */
	  // L.marker([35.69, 139.69]).addTo(map); // Tokyo'S lat, lon

	  controlGeoInfo(null, latitude, longitude, address, null);
	});
		
	// show geo data on map
	const showOnMap = (function(jsonResultDataList) {
		// remove old layers/markers on map
		pointGeoLayerGroup.clearLayers();			
		searchedGeoLayerMap = new Map();
		
		$.each(jsonResultDataList, function(idx, data) {
			map.setView([data.lat, data.lon], VIEW_ZOOM_LEVEL);
			var geoLayer = controlGeoInfo(null, data.lat, data.lon, data.display_name, null);
			//searchedGeoLayerMap.set(geoLayer._leaflet_id - 1, geoLayer._layers[geoLayer._leaflet_id - 1]);
			searchedGeoLayerMap.set(data.place_id, geoLayer._layers[geoLayer._leaflet_id - 1]);
		})
	});
	
	// add a marker of the geoJson format
	const controlGeoInfo = (function(feature, latitude, longitude, address, pLeafletId) {
		var geojsonFeature;
		
		if (feature == null) {
			geojsonFeature = [{
				"type": "Feature",
				"geometry": {
				    "type": "Point",
				    "coordinates": [longitude, latitude]     // [longtitude, latitude]
				},
				"properties": {
				 "popupContent": address
				}
			}];			
		} else {
			geojsonFeature = feature;
		}

		var geoLayer = L.geoJson(geojsonFeature, {
			onEachFeature: function(feature, layer) {
				if (feature && feature.properties && feature.properties.popupContent) {
				  	layer.bindPopup(feature.properties.popupContent);			
				}
		    }
		});
		
		pointGeoLayerGroup.addTo(map);  // make a point geoLayer group
		pointGeoLayerGroup.addLayer(geoLayer);
		
		// 引数のpLeafletIdは以前のleafletIdなので、これとhtmlを突き合わせて合うものに対して適切な最新のleaflet_idを再付与する
		syncPLeafletIdOnHtml(pLeafletId, geoLayer);
		
		return geoLayer;
	});

	// draw route: draw a route on map
	const drawRoute = (function() {
		$('.js-draw-route').click(function() {
			// hide sort pane
			commonClear(HIDE_SORT_TRUE, null);			
			
			$('.js-draw-route').toggleClass('checked');
			if(!$(this).children('input[name="check"]').prop("checked")) {
				$(".js-draw-route input").prop("checked", true);
				
				var latLonList = new Array();
				var coordinates = new Array();
				
				$('[id^="spot-list-record-content-latLon-"]').each(function(idx, latLonTd) {
					latLonList.push(
							{
								latitude: latLonTd.textContent.split("_")[0], 
								longitude: latLonTd.textContent.split("_")[1]
							}
					)
				});
				
				latLonList.forEach(latLon => coordinates.push([Number(latLon.longitude), Number(latLon.latitude)]));
				addLineGeoLayerGroup(coordinates);
			} else {
				$(".js-draw-route input").prop("checked", false);
				
				lineGeoLayerGroup.clearLayers();
			}
		})
	});
	
	const addLineGeoLayerGroup = (function(coordinates) {
		var lines = {
				"type": "FeatureCollection",
				"features": [
					{
						"type": "Feature",
						"properties:": {
							"popupContent": "<p>route</p>"
						},
						"geometry": {
							"type": "LineString",
							"coordinates": coordinates
						}
					}
				]
		}
		
		var lineLayer = L.geoJSON(lines, {
			onEachFeature: function onEachFeature(feature, layer) {
				if(feature.properties && feature.properties.popupContent) {
			          layer.bindPopup(feature.properties.popupContent);
			    }
			}
		})
 		lineGeoLayerGroup.addTo(map);  // make a line geoLayer group
		lineGeoLayerGroup.addLayer(lineLayer);
	});
	
	const commonClear = (function(hideSortFlg, clearRouteFlg) {
		if (hideSortFlg) {
			hideSortSpotList();			
		}
		if (clearRouteFlg) {
			if ($('div[class^="js-draw-route"]').children('input[name="check"]').prop('checked')) {
				// clear the route on map
				$('.js-draw-route').trigger('click');				
			}					
		}
	})
	
	/* confirm the created route */
	const confirm = (function() {
		$('.js-confirm').click(function() {
			// manipulate the toggle
			$(this).toggleClass('active');
			
			$('.body-make-new-plans').slideToggle();
			$('.map-search-results').slideToggle();
			
			// when the class is 'active', copy spot list to the created route
			if ($(this).attr('class').endsWith('active')) {
				// clear the table
				delHtml($('.body-create-route tbody'));
				
				// copy spot list to the created route
				$.each($('#spot-list-table tbody').children('tr'), function() {
					// add a new tr
					var newTr = '<tr id="' + $(this).attr('id') + '"></tr>';
					var editTable = $('.body-create-route table').append(newTr);
					
					// create the copy source and edit its ids
					var copySource = $(this).children('[id^="spot-list-record-"]').slice(0, 4).clone();
					copySource.each(function() {
						var replaced = $(this).attr('id').replace('spot-list-record', 'created-route');
						$(this).attr('id', replaced);
					})
					
					// copy spot list and add to the new tr
					editTable.find('#' + $(this).attr('id')).append(copySource);				
				});
			}			
		});
	});
	
	const setEvent = (function() {
		addSpotListRecode();
		sendToDeleteList();
		sendBackToSpotList();
		searchPlace();
		handleSortPane();
		drawRoute();
		confirm();
	});
	
	$(function(){
		initMap(DEFAULT_ADDRESS, LATITUDE, LONGITUDE);
		setEvent();
		$('#search-button').trigger('click');
		//dsiplaySortSpotList($('#spot-list-table').find('tbody'));
		location.href="#";
	});
});
/*
	return {
		init: function() {
			addToSpotList();
			searchPlace();			
	    }
	};	
})();
*/
/* TODO
1 検索した地域を全て地図上にピン止め
-- 再検索するとspot listの地点のピンが削除されるので、削除されないようにする。
        また、再検索時はルート線をoffにするようにする。
-- ルート線のスタート地点とゴール地点を別色のピンにするなど分かりやすくする。
-- かつ、削除時にすでに引いた地図上のルート線が消えないようにする（削除処理の際に再度線を引く？）
-- titleでツールチップを導入する。、また、toggleを使用して、文字列省略されている箇所をクリックで全表示できるようにする。 
   ― tooltipと、pタグで固定のtooltipを作成
2　地図上マーカー色分け
-- searched result にある地名：グレーのマーカー
-- spot list にある地名: 青のマーカー
-- delete list にある地名：マーカーを消すor薄いグレーのマーカー
-- spot list の地点を線で結ぶ
3 destination listの機能
- 選択したら 地図上のピン止めにフォーカス
4 spot list のソート機能（好きな順に並べる事ができるように）
5
** not neccesary but better if fixed for usability
・ 検索結果を地図上のタブとかに一覧に出す。必要な時だけ開けるようにする。
・ 

*********************************
* 改修履歴
-- 削除リストからsot listに戻ってからルート線を引けなくなっているので修正する。
-- かつ、順番が変わっているがその順にルート線が引かれるか確認する
*********************************
*/

$(function(){
    createRoute();
});