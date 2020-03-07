  $(document).ready(function() {
  $('#exampleModal').on('show.bs.modal', function (event) {
  var button = $(event.relatedTarget) // Button that triggered the modal
  var orderId = button.data('order');
  var isAdmin = button.data('admin');
  var price = button.data('price');
  var presentDate = new Date();
  var startDate = new Date(button.data('start_date'));
  var difference = new Date(startDate - presentDate);
  var daysToStart = Math.round(difference/1000/60/60/24) + 1;
  var penaltyDays;
  var priceToPay;
  var page;

  if (daysToStart == 1) {
        priceToPay = (price * 0.2).toFixed(2);
        penaltyDays = 20;
  }else if (daysToStart == 2) {
        priceToPay = (price * 0.15).toFixed(2);
        penaltyDays = 15;
  }else if (daysToStart == 3) {
        priceToPay = (price * 0.1).toFixed(2);
        penaltyDays = 10;
  } else {
        priceToPay = 0
  }

  var modal = $(this);
  var information;
  if (priceToPay == 0) {
    information = ('Are you sure that you want to cancel this reservation?');
  } else {
    information = ('Please be informed that your reservation starts in '+daysToStart+ ' days. In case of cancellation this reservation, the penalty will be imposed equals ' +priceToPay +'zl. ('+penaltyDays+'% of '+price+'zl.)');
  }

    modal.find('.data').text(information);

    if (isAdmin == true) {
        page = ('/orders/cancel/'+orderId+'/admin');
    } else {
        page = ('/orders/cancel/'+orderId);
    }


    $('#cancel').click(function(){
          $('#form').attr('action', page);
    });
});

});

