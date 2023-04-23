//const createRoute = (function() {
const createRoute = (function () {
	/** global variable *******************/

	// The default address when the page is read.
	const DEFAULT_ADDRESS = "6 Southwark St, London SE1 1TQ Ingland";
	const LATITUDE = 51.505;
	const LONGITUDE = -0.09;
	const VIEW_ZOOM_LEVEL = 14;
	
	// -- spot-list recode number count
	//let spotListRecordNo = 1;
	let spotCntNo = 0;
	// map
	var map = L.map('mapContainer');
	// current geoJson layerGroup on the map
	var geoLayerGroup = new L.layerGroup();	

	/** global variable end****************/
	
	// create the searched-result records
	const createSearchResultRecord = (function(jsonResultData, recordNum) {
		let tdNo = '<td class="js-search-result-record-no' + jsonResultData["place_id"] + '-' + recordNum 
		           + ' css-search-result-record-no">' + recordNum + '</td>';
		let tdAddress = '<td class="js-search-result-address">' + jsonResultData["display_name"] + '</td>';
		let tdLatLon = '<td style="display: none" class="js-search-result-lanlon">' + jsonResultData["lat"] + '_' + jsonResultData["lon"] + '</td>';
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
		     /* + tdCheckbock */
			    + tdAddButton
			    + '</tr>'
			    + '</tbody>';
		return tbody;
	});

	// 	create spot list records
	const createSpotListRecord = (function(recordNum, addressText, placeId, latLon) {		
		recordNum++;
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
		
		var tdSortLeader = '<td><span></span>' + '</td>';
		var tdNo = '<td id="spot-list-record-no-' + placeId + '" >' + recordNum + '</td>';
		var tdSpotName = '<td id="spot-list-record-content-spotName-' + placeId + '">' 
		                 + spotName + '</td>';
		var tdCity = '<td id="spot-list-record-content-city-' + placeId + '">'
		             + city + '</td>';
		var tdAddress = '<td id="spot-list-record-content-address-' + placeId + '">'
                        + address + '</td>';
		var latLon = '<td style="display: none" id="spot-list-record-content-latLon-' + placeId + '">'
                     + latLon + '</td>';
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
			+ '</tr>';

		return tr;
	});

	// create delete list records
	const createDeleteListRecord = (function(recordNum, target, placeId) {
		recordNum++;
				
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
			hideSortSpotList();
			
			var emitRecodeList = $('[id^="spot-list-record-content-emit-"]');
			var deleteList = new Array();
			emitRecodeList.each(function() {
				if($(this).prop('checked')) {
					var targetTr = $(this).closest('tr');
					deleteList.push(targetTr);
				}
			});
			$.each(deleteList, function(idx, deleteTargetTr) {
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
				
				var tbody = createDeleteListRecord(deleteListRecordNo, deletedTarget, placeId);
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
			hideSortSpotList();
			
			var backToRecodeList = $('[id^="delete-list-record-content-emit-"]');
			var spotList = new Array();
			backToRecodeList.each(function() {
				if($(this).prop('checked')) {
					spotList.push($(this).closest('tr'));
				}
			});
			$.each(spotList, function(idx, backToTarget) {
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
				
				var latLon = backedTds.filter(tdElm => tdElm.id.startsWith('delete-list-record-content-latLon-'));
				
				var tr = createSpotListRecord(spotListRecordNo, addressText, placeId, latLon);
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
		});
	});
	
	// add HTML element to the htmlElm
	const addHtml = (function(htmlElm, target) {
		htmlElm.append(target);
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
			hideSortSpotList();
			
			// get data from search results
			var address = $(this).parent().prevAll('.js-search-result-address').text();
			var placeId = spotCntNo + ':' + $(this).attr('name');
			var latLon = $(this).parent().prevAll('.js-search-result-lanlon').text();
			
			var spotListRecordNo = $('td[id^="spot-list-record-no"]').last().text();
			if (!spotListRecordNo) {
				spotListRecordNo = 0;
			}
			var tr = createSpotListRecord(spotListRecordNo, address, placeId, latLon);
			addHtml($('#spot-list-table'), tr);  //$('#spot-list-table').append(tbody);  // $('table[id="spot-list-table"]').append(tbody);
			
			// spotCntNoを更新
			spotCntNo++;
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
						displayResult(jsonResultDataList, resultPlaceIdList);
						showOnMap(jsonResultDataList);
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
		var recordNum = 0;
		$.each(jsonResultDataList, function(idx, jsonResultData) {
			recordNum++;
			searchResultTbodies = searchResultTbodies + createSearchResultRecord(jsonResultData, recordNum)
		});
		$('#search-result-table').append(searchResultTbodies);
	});	

	// reassign recode numbers by toArray
	
	
	// sort Spot List:
	// handle the sort pane of Spot List
	const handleSortPane = (function() {
		$('[class^="js-to-sort-spot-list"]').click(function() {
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

	  controlGeoInfo(map, latitude, longitude, address);
	});
		
	// show geo data on map
	const showOnMap = (function(jsonResultDataList) {		
		// remove old layers/markers
		geoLayerGroup.clearLayers();
		
		$.each(jsonResultDataList, function(idx, data) {
			map.setView([data.lat, data.lon], VIEW_ZOOM_LEVEL);
			controlGeoInfo(map, data.lat, data.lon, data.display_name)
		})
	});
	
	// add a marker of the geoJson format
	const controlGeoInfo = (function(map, latitude, longitude, address) {
		var geojsonFeature = [{
			"type": "Feature",
			"geometry": {
			    "type": "Point",
			    "coordinates": [longitude, latitude]     // [longtitude, latitude]
			},
			"properties": {
			 "popupContent": address
			}
		}];

		geoLayer = L.geoJson(geojsonFeature, {
			onEachFeature: function(feature, layer) {
				if (feature && feature.properties && feature.properties.popupContent) {
				  	layer.bindPopup(feature.properties.popupContent);			
				}
		    }
		});
		geoLayerGroup.addTo(map);  // make a geoLayer group
		geoLayerGroup.addLayer(geoLayer);
	});

	// draw a route on map
	const drawRoute = (function() {
		$('.js-draw-route').click(function() {
			var latLonList = new Array();
			var coordinates = new Array();
			// todo 
			// https://www.key-p.com/blog/staff/archives/104043
			$('[id^="spot-list-record-content-latLon-"]').each(function(idx, latLonTd) {
				var latlonText = latLonTd.textContent;
				latLonList.push(
						{
							latitude: latlonText.substring(0, latlonText.indexOf('_')), 
							longitude: latlonText.substring(latlonText.indexOf('_') + 1, latlonText.len)
						}
				)
			});
			
			latLonList.forEach(latLon => coordinates.push([latLon.longitude, latLon.latitude]));
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
								"coordinates": [
									coordinates
								]
							}
						}
					]
			}
			
			L.geoJSON(lines, {
				onEachFeature: function onEachFeature(feature, layer) {
					if(feature.properties && feature.properties.popupContent) {
				          layer.bindPopup(feature.properties.popupContent);
				    }
				}
			}).addTo(map);
		})
	});
	
	const setEvent = (function() {
		addSpotListRecode();
		sendToDeleteList();
		sendBackToSpotList();
		searchPlace();
		handleSortPane();
		//drawRoute();
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
*/

$(function(){
    createRoute();
});