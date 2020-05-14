import * as Utils from "./utils.js";

$(() =>
{
   $('.input-container .password-eye').on('click', function () {
       $(this).toggleClass('toggled');
       let input = $(this).parents('.input-container').find('input[type=password], input[type=text]');
       input.attr('type', $(this).hasClass('toggled') ? 'text' : 'password');
   });

    $('.input-container .input-lock').on('click', function () {
        $(this).toggleClass('toggled');
        let input = $(this).parents('.input-container').find(':input');
        $(this).hasClass('toggled') ? input.removeAttr('readonly') : input.attr('readonly', 'readonly');
    });

    $('.profile-picture').on('click', function (e)
    {
        $('.user-menu').toggleClass('visible');
        e.stopPropagation();
    })

    $(window).on('click', function () {
        $('.user-menu').removeClass('visible');
    })

    let zoomedImage;

    $('img.zommable').on('mouseover', function () {
        let img = $(this).attr('src');
        zoomedImage = `<div class="image-zoom"><img src="${img}"></div>`;
        $('body').append(zoomedImage)
    });

    $('img.zoomable').on('mouseexit', function () {
        zoomedImage.delete();
    })

    $('form').submit(function (event) {

        event.preventDefault();

        let popupId = $(this).data('popup');
        if(popupId)
        {
            let popup = $(`#${popupId}`);
            let form = $(this);
            popup.on('close', function (e) {
                console.log($(this));
                if(e.popupResult.ok())postForm(form);
            });
            popup.addClass('show');
        }
        else postForm($(this));
    });

    function PopupResult(result)
    {
        this.ok = function () {
            return result === 'ok';
        }
        this.result = result;
    }

    $('.popup .ok-btn, .popup .cancel-btn, .popup .popup-close-btn').on('click', function () {
        let popup = $(this).closest('.popup');
        let closeEvent = $.Event('close');
        closeEvent.popupResult = new PopupResult($(this).val());
        popup.trigger(closeEvent);
        if(closeEvent.isDefaultPrevented())return;
        popup.removeClass('show');
    });

    function postForm(form)
    {
        Utils.postFormIfValid(form).then(response => {
            form.find('.form-error').removeClass('visible');
            if(response.responseURL)window.location.href = response.responseURL;
        })
            .catch(response => form.find('.form-error').addClass('visible'));
    }

    /*$('form')[0].querySelectorAll('input, select').forEach(element => {
        element.addEventListener('blur', () => {
            Utils.checkElementValidity(element);
        });
    });*/

    $('form').find('input, select').on('blur', function () {
        Utils.checkElementValidity($(this).get(0));
    })

    $('.photo-selector input:file').on('change', function ()
    {
        let inputFileElem = $(this);
        if(this.files.length <= 0)return;

        let reader = new FileReader();
        reader.onloadend = function () {
            inputFileElem.parent(".photo-selector").find(".photo-preview").attr("src", reader.result).css("display", "block")
            $('#photo-hidden').val(reader.result);
        };
        reader.readAsDataURL(this.files[0]);
    });

    $('.input-file-button input:file').on('change', function ()
    {
        if(this.files.length <= 0)return;

        let container = $(this).parents('.input-file-button');
        let previewId = $(this).data('preview_id');

        console.log($(`#${previewId}`));

        let reader = new FileReader();
        reader.onloadend = function () {
            container.find('input[type=hidden]').val(reader.result);
            $(`#${previewId} img`).attr('src', reader.result);
            $(`#${previewId}`).removeClass('empty');
        };
        reader.readAsDataURL(this.files[0]);
    });

    $('.floating').on('click', function ()
    {
        $(this).addClass('expanded');
    });

    $('.floating-close').on('click', function (e)
    {
        let floatingId = $(this).data('floating_id');
        $(`#${floatingId}`).removeClass('expanded');
        e.stopPropagation();
    });

    $('.window-close-btn').on('click', function () {
        $(this).closest('.window').addClass("hidden");
    });


    let draggable = null;
    let dragOffsetX = 0;
    let dragOffsetY = 0;

    $('.wnav').on('mousedown', function (e) {
        $(this).css('cursor', 'grabbing');
        let popup = $(this).closest('.window');
        popup.addClass('draggable');
        let currentOffset = popup.offset();
        dragOffsetX = e.pageX - currentOffset.left;
        dragOffsetY = e.pageY - currentOffset.top;
        draggable = popup;
    });

    $('.wnav').on('mouseup', function () {
        $(this).css('cursor', 'grab');
        let popup = $(this).closest('.window');
        popup.removeClass('draggable');
        draggable = null;
    });

    $('.wnav').on('dblclick', function () {
        let popup = $(this).closest('.window');
        let height = $(window).height()-32;
        let width = $(window).width()-32;

        popup.animate({
            width: width,
            height: height,
            left: 16,
            top: 16
        }, 200);
    })

    $('.window').on('mousedown', function () {
        $('.window').css('z-index', 20);
        $(this).css('z-index', 21);
    })

    $(window).on('mousemove', function (e) {
        if(draggable)
        {
            e.preventDefault();
            draggable.offset({
                top: e.pageY - dragOffsetY,
                left: e.pageX - dragOffsetX
            })
        }
    });


    $('.list-close-btn').on('click', function () {
        $(this).toggleClass('toggled');
        $(this).closest('.list-container').toggleClass('closed');
    });


    let marquee = $(".overflow-marquee");
    marquee.each(index =>
    {
        let marqueeElement = $(marquee[index]);
        marqueeElement.data('width', marqueeElement.width()+10);
        if(marqueeElement.width() > marqueeElement.parent().width())marqueeElement.data("overflow", "true");
    });

    $(window).on('resize', function () {
        console.log("resize");
        marquee.each(index =>
        {
            let marqueeElement = $(marquee[index]);
            marqueeElement.data('width', marqueeElement.width()+10);
            if(marqueeElement.width() > marqueeElement.parent().width())marqueeElement.data("overflow", "true");
            else{
                marqueeElement.data("overflow", "false");
                marqueeElement.css("text-indent", 0);
            }
        });
    })

    marquee.parent().on('mouseenter', function () {
        let marqueeElement = $(this).find(".overflow-marquee");
        if(marqueeElement.data("overflow") === "true")startMarquee(marqueeElement);
    })

    marquee.parent().on('mouseleave', function () {
        $(this).find(".overflow-marquee").stop();
    })

    function startMarquee($element){
        let width = $element.data("width");

        let currentTextIndent = $element.css("text-indent");
        currentTextIndent = currentTextIndent.substring(0, currentTextIndent.length-2);
        let animationSpeed;
        if(currentTextIndent < 0)
        {
            currentTextIndent = Math.abs(currentTextIndent);
            animationSpeed = (width-currentTextIndent)*10;
        }
        else animationSpeed = width*10;

        $element.animate({
            'text-indent': -width
        }, animationSpeed, "linear", () => marqueeLoop($element));
    }

    function marqueeLoop($element) {
        let width = $element.data("width");
        $element.css("text-indent", `${width}px`).animate({
            'text-indent': -width
        }, width*10*2, "linear", () => marqueeLoop($element));
    }
});