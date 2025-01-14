import * as Utils from "./utils.js";
import * as Main from "./main.js";


$(() =>
{
    let lat = 45.4645075;
    let lon = 9.1895711;
    let zoom = 10;

    let map = L.map('map').setView([lat, lon], zoom);

    /*L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);*/

    L.tileLayer('https://tiles.stadiamaps.com/tiles/osm_bright/{z}/{x}/{y}{r}.png', {
        maxZoom: 18,
        attribution: '&copy; <a href="https://stadiamaps.com/">Stadia Maps</a>, &copy; <a href="https://openmaptiles.org/">OpenMapTiles</a> &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors'
    }).addTo(map);

    $(".map-target-btn").on('click', function () {
        let image = $(this).closest('li');
        let longitude = image.data('longitude');
        let latitude = image.data('latitude');
        map.flyTo([latitude, longitude], 16);
    });

    loadImages(map);
});



function loadImages(map) {

    let redMarkerIcon = new L.Icon({
        iconUrl: '/assets/images/marker-icon-2x-red.png',
        shadowUrl: '/assets/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    let greenMarkerIcon = new L.Icon({
        iconUrl: '/assets/images/marker-icon-2x-green.png',
        shadowUrl: '/assets/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    let imageList = $('#campaign-image-list ul li').toArray();

    imageList.forEach(image =>
    {
        let imageId = image.dataset.imageId;
        let hasAnnotation = image.dataset.hasAnnotation;
        let popup = $(`.popup[id=popup-${imageId}]`);

        let marker = L.marker([image.dataset.latitude, image.dataset.longitude]).addTo(map).on('click', () =>
        {
            popup.addClass('show');
        });

        marker.setIcon(hasAnnotation === 'true' ? greenMarkerIcon : redMarkerIcon)

        fetch(`/get-image?id=${imageId}`).then(async response =>
        {
            let imageHash = await response.text();
            $(image).find('img').attr('src', imageHash).removeAttr('style');
            popup.find(".pcontent .image-container .image img").attr('src', imageHash).removeAttr('style');

            popup.on('close',   (e) => {
                if(!e.popupResult.ok())return;
                if(popup.attr('annotation-present') === 'true')return;
                let form = popup.find('.pcontent .annotation-form');

                form.on('response', function (responseEvent) {
                    if(responseEvent.response.status === 200)
                    {
                        popup.removeClass('show');
                        form.find('fieldset').attr('disabled', true);
                        marker.setIcon(greenMarkerIcon);
                    }
                })

                Main.postForm(form);

                e.preventDefault();
            });
        })
    });

}