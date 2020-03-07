$(document).ready(function() {

	$('#btnSubmit').click(function(event){
			var brand = $('#brand');
			var model = $('#model');
			var year = $('#year');
			var price = $('#price');

        if(!brand.hasClass('valid') || !model.hasClass('valid') || !year.hasClass('valid') || !price.hasClass('valid')){
                $('#errorSubmit').html('<p class="bg-danger text-white text-center">There are errors in form!</p>');
				event.preventDefault();
			}
		});

});