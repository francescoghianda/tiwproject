$(() =>
{
    $('#add-campaign-btn').on('click', function ()
    {
        $(this).addClass('expanded');
    })

    $('#close-new-campaign').on('click', function (e)
    {
        console.log("remove");
        $('#add-campaign-btn').removeClass('expanded');
        e.stopPropagation();
    })

    $('#formCampaign').on('click', function ()
    {
        console.log('click');
        $('#createCampaign').css('display','block');
    })
});