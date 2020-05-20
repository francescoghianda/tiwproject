$(() =>
{
    let photo;

    function hideInfoForm()
    {
        $('#account-info-form').hide();
        $('#account-info').show();
    }

    function showInfoForm()
    {
        $('#account-info').hide();
        $('#account-info-form').show();
    }

    $('#edit-btn').on('click', function () {
        showInfoForm();
    });

    $('#cancel-btn').on('click', function () {
        hideInfoForm();
        disablePasswordChange();
    });


    $('#image-form a').on('click', function () {
        $('#image-form input[type=file]').trigger('click');
    });

    $('#image-form input[type=file]').on('change', function () {
        if(this.files.length <= 0)return;

        photo = $('#image-form img').attr('src');

        let reader = new FileReader();
        reader.onloadend = function () {
            $('#image-form input[type=hidden]').val(reader.result);
            $(`#image-form img`).attr('src', reader.result);
        };
        reader.readAsDataURL(this.files[0]);

        $('#image-form a').hide();
        $('#image-form > div > div').show();
    });

    $('#image-form').on('response', function (e) {
        if(e.response.status === 200)
        {
            $('#image-form > div > div').hide();
            $('#image-form a').show();
            let image = $(`#image-form img`).attr('src');
            $('.navbar .user-image img').attr('src', image);
            $('.navbar .profile-picture').attr('src', image);
        }
    });

    $('#image-form > div > div > button').on('click', function () {
        $('#image-form img').attr('src', photo);
        $('#image-form > div > div').hide();
        $('#image-form a').show();
    });

    $('#account-info-form').on('response', function (e) {
        if(e.response.status === 200)
        {
            $('#email-span').text($('#email-input').val());
            $('#exp-lvl-span').text($('#exp-lvl-input option:selected').text());
            hideInfoForm();
            disablePasswordChange();
        }
    });

    let passwordInput = $('#password-input');
    let passwordInputContainer = passwordInput.closest('.input-container');

    let passwordConfirm = $('#password-confirmation-input');
    let passwordConfirmContainer = passwordConfirm.closest('.input-container');

    $('#change-password-btn').on('click', function () {
        enablePasswordChange();
    });

    $('#disable-change-password-btn').on('click', function () {
        disablePasswordChange();
    });

    function enablePasswordChange() {
        $('#change-password-btn').hide();
        $('#disable-change-password-btn').show();
        passwordInput.val('').attr('disabled', false);
        passwordInputContainer.attr('disabled', false);
        passwordConfirm.val('').attr('disabled', false);
        passwordConfirmContainer.attr('disabled', false);
    }

    function disablePasswordChange() {
        $('#disable-change-password-btn').hide();
        $('#change-password-btn').show();
        passwordInput.val('******').attr('disabled', true);
        passwordInputContainer.attr('disabled', true);
        passwordConfirm.val('******').attr('disabled', true);
        passwordConfirmContainer.attr('disabled', true);
    }
});