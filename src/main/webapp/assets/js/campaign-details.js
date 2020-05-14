
function Slider(id, transition, speed, easing)
{
    let _slider = $(`#${id}`);
    let _leftArrow = _slider.find(".left-arrow");
    let _rightArrow = _slider.find(".right-arrow");
    let _indexText = _slider.find(".index");
    let _slides = _slider.find(".slide");
    let _current = 0;
    let _lock = false;
    let _onSlideChange;
    let _asyncTransition = false;
    let _easing = easing ? easing : 'linear';
    let _speed = speed ? speed : 250;

    let _hiddenPropLeft = {opacity: 0};
    let _hiddenPropRight = {opacity: 0};
    let _visiblePropLeft = {opacity: 1};
    let _visiblePropRight = {opacity: 1};

    if(transition === 'slideX')
    {
        _hiddenPropLeft = {right: 0, left: '-100%'};
        _hiddenPropRight = {left: '100%', right: 0};
        _visiblePropLeft = {right: '100%', left: 0};
        _visiblePropRight = {left: 0, right: '100%'};
        _asyncTransition = true;
    }

    _slides.each(i => $(_slides[i]).css(i === 0 ? _visiblePropLeft : _hiddenPropLeft));

    this.getCurrentIndex = function () {
        return _current;
    }

    this.updateSlide = function () {
        _slides = _slider.find(".slide");
    }

    this.onSlideChange = function (callback) {
        _onSlideChange = callback;
    };

    this.slide = function (delta) {
        if(_lock)return;
        _lock = true;

        let newIndex = _current + delta;
        if(newIndex >= _slides.length)newIndex = 0;
        else if(newIndex < 0)newIndex = _slides.length-1;

        let currentElement = $(_slides.get(_current));
        let newElement = $(_slides.get(newIndex));

        _current = newIndex;

        let hideAnimationProp = delta > 0 ? _hiddenPropRight : _hiddenPropLeft;
        let showAnimationProp = delta > 0 ? _visiblePropRight : _visiblePropLeft;

        if(_asyncTransition)
        {
            currentElement.animate(hideAnimationProp, _speed, _easing);
            newElement.css(delta > 0 ? _hiddenPropLeft : _hiddenPropRight).animate(showAnimationProp, _speed, _easing, function () {
                if(_indexText)_indexText.text(`${newIndex+1} / ${_slides.length}`);
                _lock = false;
                _onSlideChange(newIndex, newElement);
            })
        }
        else
        {
            currentElement.animate(hideAnimationProp, _speed, _easing, function () {
                newElement.css(delta > 0 ? _hiddenPropLeft : _hiddenPropRight).animate(showAnimationProp, _speed, _easing, function () {
                    if(_indexText)_indexText.text(`${newIndex+1} / ${_slides.length}`);
                    _lock = false;
                    _onSlideChange(newIndex, newElement);
                })
            });
        }
    }

    _leftArrow.on('click', () => this.slide(1));
    _rightArrow.on('click', () => this.slide(-1));
}

$(() =>
{

    let sliders = $(".slider");
    sliders.each(i =>
    {
        let slider = new Slider(sliders[i].id);
        slider.onSlideChange((index, slide) =>
        {
            console.log(slide);
        })
    });
    /*$('.right-arrow').on('click', function () {
        let annotationList = $(this).closest('.wcontent').find('.annotations');
        displayAnnotation(annotationList, +1, $(this).closest('.nav-controller').find('.counter'))
    });

    $('.left-arrow').on('click', function () {
        let annotationList = $(this).closest('.wcontent').find('.annotations');
        displayAnnotation(annotationList, -1, $(this).closest('.nav-controller').find('.counter'))
    });


    function displayAnnotation(annotationList, delta, indexElem)
    {
        if(displayAnnotation.lock)return;
        displayAnnotation.lock = true;

        let current = annotationList.data('current');
        let items = annotationList.find('li');
        let toDisplay = current + delta;
        if(toDisplay < 0)toDisplay = items.length-1;
        if(toDisplay >= items.length)toDisplay = 0;
        annotationList.data('current', toDisplay);

        let currentElement = $(items.get(current));
        let toDisplayElement = $(items.get(toDisplay));

        currentElement.animate({
            opacity: 0
        }, 200, function () {
            currentElement.css('transform', 'translateX(100%)');
            toDisplayElement.css('opacity', 0).css('transform', 'translateX(0)');
            toDisplayElement.animate({opacity: 1}, 200, function () {
                if(indexElem)indexElem.text(`${toDisplay+1} / ${items.length}`);
                displayAnnotation.lock = false;
            })
        })
    }*/


    let lat = 45.4645075;
    let lon = 9.1895711;
    let zoom = 10;

    let map = L.map('map').setView([lat, lon], zoom);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    /*
    L.tileLayer('https://tiles.stadiamaps.com/tiles/osm_bright/{z}/{x}/{y}{r}.png', {
        maxZoom: 20,
        attribution: '&copy; <a href="https://stadiamaps.com/">Stadia Maps</a>, &copy; <a href="https://openmaptiles.org/">OpenMapTiles</a> &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors'
    });
     */

    map.on('click', function (e) {
        $('#input-latitude').val(e.latlng.lat);
        $('#input-longitude').val(e.latlng.lng);

        $.get(`https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${e.latlng.lat}&lon=${e.latlng.lng}`, function(data){
            let municipality = data.address.city;
            if(!municipality)municipality = data.address.town;
            if(!municipality)municipality = data.address.village;
            if(!municipality)municipality = data.address.county;
            if(!municipality)municipality = data.address.province;

            $('#input-municipality').val(municipality);
            $('#input-region').val(data.address.state);
        });
    });

    $(".delete-image-btn").on('click', function () {
        let campaignId = $('#campaign-id').val();
        let imageId = $(this).data('image_id');
        fetch(`/campaign/remove_image?id=${campaignId}&image_id=${imageId}`).then(response =>
        {
            if(response.ok)
            {
                $(this).parents('li').remove();
                if(!$('#campaign-image-list ul li')[0])location.reload();
            }
        });
    });

    $(".map-target-btn").on('click', function () {
        let image = $(this).closest('li');
        let longitude = image.data('longitude');
        let latitude = image.data('latitude');
        map.flyTo([latitude, longitude], 16);
    })

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
        let hasAnnotations = image.dataset.hasAnnotations;
        let window = $(`.window[data-image-id=${imageId}]`);

        L.marker([image.dataset.latitude, image.dataset.longitude]).addTo(map).on('click', () =>
        {
            window.removeClass("hidden");
            $('.window').css('z-index', 20);
            window.css('z-index', 21);
        }).setIcon(hasAnnotations === 'true' ? greenMarkerIcon : redMarkerIcon);

        fetch(`/get-image?id=${imageId}`).then(async response =>
        {
            let imageHash = await response.text();
            $(image).find('img').attr('src', imageHash).removeAttr('style');
            window.find(".wcontent .image img").attr('src', imageHash).removeAttr('style');
        })
    });

}

/*function loadImages(map) {
    let url = new URL(window.location.href);
    let searchParams = new URLSearchParams(url.search);
    let id = searchParams.get('id');
    fetch(`/campaign/images?id=${id}&media=false`).then(async response =>
    {
        let json = await response.json();
        let imageList = $('#campaign-image-list ul');

        for(let image of json.images)
        {
            let button = json.campaignStatus === 'CREATED' ? `<button class="delete-image-btn" data-image_id="${image.id}"></button>` : "";

            let card = $(`<li>
                                <img style="width: 24px" src="/assets/images/puff.svg">
                                ${button}
                              </li>`);

            fetch(`/get-image?id=${image.id}`).then(async response =>
            {
                let imageHash = await response.text();
                card.find('img').attr('src', imageHash).removeAttr('style');
            });

            L.marker([image.location.latitude, image.location.longitude]).addTo(map).on('click', () =>
            {
                $('.popup').removeClass("hidden");
            });

            imageList.append(card);
        }
    });
}*/