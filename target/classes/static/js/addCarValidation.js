$(document).ready(function() {

$('#brand').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[a-zA-Z- ]{2,20}$");
		var is_brand = pattern.test(input.val());
		if(is_brand){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#model').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[a-zA-Z- 0-9]{1,20}$");
		var is_model = pattern.test(input.val());
		if(is_model){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
		    input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#year').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^(19|20)[0-9]{2}$");
		var is_year = pattern.test(input.val());
		if(is_year){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
		    input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#price').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("(^[0-9]{1,5}$|^[0-9]{1,5}[.][0-9]{1,2}$)");
		var is_price = pattern.test(input.val());
		if(is_price){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
		    input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

		$('#submit button').click(function(event){
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