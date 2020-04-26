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

    $('form').submit(function (event) {

        event.preventDefault();
        Utils.postFormIfValid($(this)).then(response => {
            $(this).find('.form-error').removeClass('visible');
            if(response.responseURL)window.location.href = response.responseURL;
        })
            .catch(response => $(this).find('.form-error').addClass('visible'));

        /*Utils.checkFormValidity($(this)[0]).then(valid =>
        {
            if(valid)Utils.postForm($(this).attr('action'), $(this)).then(response =>
            {
                $(this).find('.form-error').removeClass('visible');
                if(response.responseURL)window.location.href = response.responseURL;
            }).catch(response => $(this).find('.form-error').addClass('visible'));
        });*/

    });

    $('form')[0].querySelectorAll('input, select').forEach(element => {
        element.addEventListener('blur', () => {
            Utils.checkElementValidity(element);
        });
    });

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

    $('.popup-close-btn').on('click', function () {
        $(this).closest('.popup').addClass("hidden");
    });


    let draggable = null;
    let dragOffsetX = 0;
    let dragOffsetY = 0;

    $('.pnav').on('mousedown', function (e) {

        $(this).css('cursor', 'grabbing');
        let popup = $(this).closest('.popup');
        popup.addClass('draggable');
        let currentOffset = popup.offset();
        dragOffsetX = e.pageX - currentOffset.left;
        dragOffsetY = e.pageY - currentOffset.top;
        draggable = popup;
    });

    $('.pnav').on('mouseup', function () {
        $(this).css('cursor', 'grab');
        let popup = $(this).closest('.popup');
        popup.removeClass('draggable');
        draggable = null;
    });

    $('.popup').on('mousedown', function () {
        $('.popup').css('z-index', 20);
        $(this).css('z-index', 21);
    })

    $(window).on('mousemove', function (e) {
        if(draggable)
        {
            draggable.offset({
                top: e.pageY - dragOffsetY,
                left: e.pageX - dragOffsetX
            })
        }
    });

});