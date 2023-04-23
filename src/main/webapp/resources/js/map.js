

/* set its view to our chosen geographical coordinates and a zoom level */
/* var map = L.map('mapid', {
  center: [35.66572, 139.73100],
  zoom: 17,
}); */
var map = L.map('map').setView([35.66572, 139.73100], 17)
/*
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);
*/
var map = L.map('map').setView([51.505, -0.09], 13);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);
