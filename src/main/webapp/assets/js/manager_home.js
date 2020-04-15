$(() =>
{
    $('#add-campaign-btn').on('click', function ()
    {
        $(this).addClass('expanded');
    })

    $('#close-new-campaign').on('click', function (e)
    {
        $('#add-campaign-btn').removeClass('expanded');
        e.stopPropagation();
    })

    $('.profile-picture').on('click', function (e)
    {
        $('.user-menu').toggleClass('visible');
        e.stopPropagation();
    })

    $(window).on('click', function () {
        $('.user-menu').removeClass('visible');
    })

    $('#formCampaign').on('click', function ()
    {
        console.log('click');
        $('#createCampaign').css('display','block');
    })
});