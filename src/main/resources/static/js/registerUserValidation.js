$(document).ready(function() {

$('#firstName').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[a-zA-Z -]{3,25}$");
		var is_firstName = pattern.test(input.val());
		if(is_firstName){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#lastName').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[a-zA-Z -]{3,25}$");
		var is_lastName = pattern.test(input.val());
		if(is_lastName){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#email').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp(".+\@.+\..+");
		var is_email = pattern.test(input.val());
		if(is_email){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#password').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp(".{8,20}");
		var is_password = pattern.test(input.val());
		if(is_password){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Password must contain: at least one lowercase letter and uppercase and digit and special character and size >= 8. </label>');
		}
});

$('#mobileNumber').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[0-9]{0,10}$");
		var is_mobileNumber = pattern.test(input.val());
		if(is_mobileNumber){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#city').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[a-zA-Z]{0,10}$");
		var is_city = pattern.test(input.val());
		if(is_city){
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});


		$('#submit button').click(function(event){
			var firstName = $('#firstName');
			var lastName = $('#lastName');
			var email = $('#email');
			var password = $('#password');

        if(!firstName.hasClass('valid') || !lastName.hasClass('valid') || !email.hasClass('valid') || !password.hasClass('valid')){
                $('#errorSubmit').html('<p class="bg-danger text-white text-center">There are errors in form!</p>');
				event.preventDefault();
			}
		});

});