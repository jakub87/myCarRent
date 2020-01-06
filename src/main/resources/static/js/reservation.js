$(document).ready(function() {

$('#startDate').change(function()
{
    calculate();
});

$('#endDate').change(function()
{
    calculate();
});

function calculate()
{
        var startDate = new Date($('#startDate').val());
        var endDate = new Date($('#endDate').val());
        var presentDate = new Date();
        presentDate.setDate(presentDate.getDate() - 1); // minus the date
        var carPrice = $('#carPrice').val();


        var summary;
        var result;
        var cost;

        if( startDate < presentDate )
        {
              result = "Start date is in the past.";
        }
        else if (endDate < presentDate )
        {
            result = "End date is in the past.";
        }
        else if (endDate >= startDate)
        {
            var difference = new Date(endDate - startDate);
            var duration = Math.round(difference/1000/60/60/24) + 1;
            var price = carPrice * duration;
            result = "Duration: "+duration+" days.";
            cost = "Overall cost: "+price+"zl";
            summary = "Summary";
        }
        else{
              result = "Start date is later than end date.";
        }

        $('#summary').text(summary);
        $('#duration').text(result);
        $('#cost').text(cost);
}
});