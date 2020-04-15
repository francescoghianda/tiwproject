$(() =>
{
    $('#add-campaign-btn').on('click', () =>
    {
        $('.new-campaign-form').addClass('visible');
    })

    $('#close-new-campaign').on('click', () =>
    {
        $('.new-campaign-form').removeClass('visible');
    })

    $('#formCampaign').on('click', function ()
    {
        console.log('click');
        $('#createCampaign').css('display','block');
    })
});