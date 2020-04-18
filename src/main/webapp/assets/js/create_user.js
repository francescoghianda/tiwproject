import * as Utils from './utils.js'

$(() =>
{
    $('#registration-form')[0].querySelectorAll('input, select').forEach(element => {
        element.addEventListener('blur', () => {
            Utils.checkElementValidity(element);
        });
    });

    $('#role-input').on('change', function ()
    {
        if(this.value === 'MANAGER')
        {
            $('#worker-section').slideUp();
            //$('#photo-input').prop("required", false);
            $('#worker-section').find('input, select').prop("disabled", true);
        }
        else
        {
            $('#worker-section').slideDown();
            //$('#photo-input').prop("required", true);
            $('#worker-section').find('input, select').prop("disabled", false);
        }
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

    $('#form-submit-btn').on('click', function ()
    {
        let form = $('#registration-form');

        Utils.checkFormValidity(form[0]).then(valid =>
        {
            if(valid)
            {
                Utils.postForm('/create_user', form).then(response =>
                {
                    if(response.status >= 400) Utils.showError($('#user-creation-error'));
                    else location.replace('/');
                });
            }
        });

    })
});


