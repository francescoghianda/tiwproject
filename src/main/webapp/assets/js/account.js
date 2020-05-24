$(() =>
{
    let photo = $('#image-preview').attr('src');

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


    $('#change-photo-bnt').on('click', function () {
        $(this).hide();
        $('#image-form-buttons').show();
    });

    $('#generate-image-btn').on('click', function () {
        fetch('/generate-user-picture').then(response => response.text()).then(image =>
        {
            $('#image-form input[type=hidden]').val(image);
            $(`#image-preview`).attr('src', image);
            $('#save-image-btn').attr('disabled', false);
        });
    });

    $('#cancel-image-btn').on('click', function () {
        $('#image-preview').attr('src', photo);
        $('#image-form-buttons').hide();
        $('#save-image-btn').attr('disabled', true);
        $('#change-photo-bnt').show();
    });

    $('#select-image-btn').on('click', function () {
        $('#image-form input[type=file]').trigger('click');
    });

    $('#image-form input[type=file]').on('change', function () {
        if(this.files.length <= 0)return;

        let reader = new FileReader();
        reader.onloadend = function () {
            $('#image-form input[type=hidden]').val(reader.result);
            $(`#image-preview`).attr('src', reader.result);
        };
        reader.readAsDataURL(this.files[0]);

        $('#save-image-btn').attr('disabled', false);
    });

    $('#image-form').on('response', function (e) {
        if(e.response.status === 200)
        {
            $('#image-form-buttons').hide();
            $('#save-image-btn').attr('disabled', false);
            $('#change-photo-bnt').show();
            photo = $(`#image-preview`).attr('src');
            $('.navbar .user-image img').attr('src', photo);
            $('.navbar .profile-picture').attr('src', photo);
        }
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