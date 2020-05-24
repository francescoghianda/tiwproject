$(() =>
{
   $('.delete-campaign-form').on('response', function (e) {
       if(e.response.status === 200)
       {
           let item = $(this).closest(".list-item");
           item.animate({
               width: 0
           }, 300, "swing",  () => item.remove());
       }
   });
});