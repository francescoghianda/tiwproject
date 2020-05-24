$(() =>
{
    Array.from($('.list-container')).forEach(list => new List($(list)));
});

function List($listContainer)
{
    let list = $listContainer.find('.list');
    let items = $listContainer.find('.list > .list-item');
    let itemsArray = Array.from(items);

    $listContainer.find('.list-header > .list-filter').on('change', function () {
        let selected = $(this).find('option:selected').val();
        if(selected === 'all')items.css('display', '');
        else itemsArray.forEach(item =>
            $(item).css('display', $(item).attr('data-filter') === selected ? '' : 'none'));
        items.css('margin-bottom', '');
        list.find('.list-item:visible:last').css('margin-bottom', '16px');
    });

    $listContainer.find('.list-header > .list-close-btn').on('click', function () {
        $(this).toggleClass('toggled');
        $(this).closest('.list-container').toggleClass('closed');
    });
}