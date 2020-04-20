import * as Utils from "./utils.js";

$(() =>
{
   $('.input-container .password-eye').on('click', function () {
       $(this).toggleClass('toggled');
       let input = $(this).parents('.input-container').find('input[type=password], input[type=text]');
       input.attr('type', $(this).hasClass('toggled') ? 'text' : 'password');
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
        Utils.checkFormValidity($(this)[0]).then(valid =>
        {
            if(valid)Utils.postForm($(this).attr('action'), $(this)).then(response =>
            {
                $(this).find('.form-error').removeClass('visible');
                if(response.responseURL)window.location.href = response.responseURL;
            }).catch(response => $(this).find('.form-error').addClass('visible'));
        });

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

});