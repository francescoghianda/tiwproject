import * as Utils from "./utils.js";

$(() =>
{
    $('#login-btn').on('click', function ()
    {
        let form = $('#login-form');

        Utils.checkFormValidity(form[0]).then(valid =>
        {
            if(valid)
            {
                Utils.postForm('/login', form).then(response =>
                {
                    if(response.status >= 400)console.log("error") ;//Utils.showError($('#user-creation-error'));
                    else location.replace('/');
                });
            }
        });

    })
});