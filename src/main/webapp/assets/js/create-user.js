import * as Utils from './utils.js'

$(() =>
{
    $('#role-input').on('change', function ()
    {
        if(this.value === 'MANAGER')
        {
            $('#worker-section').slideUp();
            $('#photo-input').prop("required", false);
            $('#worker-section').find('input').prop("disabled", true);
        }
        else
        {
            $('#worker-section').slideDown();
            $('#photo-input').prop("required", true);
            $('#worker-section').find('input').prop("disabled", false);
        }
    });

    $('.photo-selector input:file').on('change', function ()
    {
        let inputFileElem = $(this);
        if(this.files.length <= 0)return;

        let reader = new FileReader();
        reader.onloadend = function () {
            inputFileElem.parent(".photo-selector").children(".photo-selector-preview").attr("src", reader.result).css("display", "block")
            $("#photo-hidden").val(reader.result);
        };
        reader.readAsDataURL(this.files[0]);
    });
});


