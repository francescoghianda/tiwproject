import * as Utils from './utils.js'

$(() =>
{
    $('#role-input').on('change', function ()
    {
        let workerSection = $('#worker-section');
        if(this.value === 'MANAGER')
        {
            workerSection.slideUp();
            workerSection.find('input, select').prop("disabled", true);
        }
        else
        {
            workerSection.slideDown();
            workerSection.find('input, select').prop("disabled", false);
        }
    });
});


