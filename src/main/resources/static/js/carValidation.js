$(document).ready(function() {

 $('#file-upload0').on('change', function() {
    var input = $(this);
    var numb = $(this)[0].files[0].size/1024/1024/10;
    numb = numb.toFixed(2);
    if(numb > 2){
        $("#btnSubmit").attr("disabled", true);
        input.next('.validator').html('<label class="small text-danger"> File is grater than 10 MB! </label>');
    } else {
        $("#btnSubmit").attr("disabled", false);
        input.next('.validator').html('<label class="small text-success"> Ok. </label>');
    }
});

 $('#file-upload1').on('change', function() {
    var input = $(this);
    var numb = $(this)[0].files[0].size/1024/1024/10;
    numb = numb.toFixed(2);
    if(numb > 2){
        $("#btnSubmit").attr("disabled", true);
        input.next('.validator').html('<label class="small text-danger"> File is grater than 10 MB! </label>');
    } else {
        $("#btnSubmit").attr("disabled", false);
        input.next('.validator').html('<label class="small text-success"> Ok. </label>');
    }
});

 $('#file-upload2').on('change', function() {
    var input = $(this);
    var numb = $(this)[0].files[0].size/1024/1024/10;
    numb = numb.toFixed(2);
    if(numb > 2){
        $("#btnSubmit").attr("disabled", true);
        input.next('.validator').html('<label class="small text-danger"> File is grater than 10 MB! </label>');
    } else {
        $("#btnSubmit").attr("disabled", false);
        input.next('.validator').html('<label class="small text-success"> Ok. </label>');
    }
});

 $('#file-upload3').on('change', function() {
    var input = $(this);
    var numb = $(this)[0].files[0].size/1024/1024/10;
    numb = numb.toFixed(2);
    if(numb > 2){
        $("#btnSubmit").attr("disabled", true);
        input.next('.validator').html('<label class="small text-danger"> File is grater than 10 MB! </label>');
    } else {
        $("#btnSubmit").attr("disabled", false);
        input.next('.validator').html('<label class="small text-success"> Ok. </label>');
    }
});

$('#brand').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[a-zA-Z- ]{2,20}$");
		var is_brand = pattern.test(input.val());
		if(is_brand){
		    $("#btnSubmit").attr("disabled", false);
		    input.removeClass("invalid").addClass("valid");
		    input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
			$("#btnSubmit").attr("disabled", true);
			input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#model').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^[a-zA-Z- 0-9]{1,20}$");
		var is_model = pattern.test(input.val());
		if(is_model){
		    $("#btnSubmit").attr("disabled", false);
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
		    $("#btnSubmit").attr("disabled", true);
		    input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#year').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("^(19|20)[0-9]{2}$");
		var is_year = pattern.test(input.val());
		if(is_year){
		    $("#btnSubmit").attr("disabled", false);
		    input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
		    $("#btnSubmit").attr("disabled", true);
		    input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
});

$('#price').on('blur', function() {
		var input = $(this);
		var pattern = new RegExp("(^[0-9]{1,5}$|^[0-9]{1,5}[.][0-9]{1,2}$)");
		var is_price = pattern.test(input.val());
		if(is_price){
		    $("#btnSubmit").attr("disabled", false);
		     input.removeClass("invalid").addClass("valid");
			input.next('.validator').html('<label class="small text-success"> Ok. </label>');
		}
		else{
		    $("#btnSubmit").attr("disabled", true);
		    input.removeClass("valid").addClass("invalid");
			input.next('.validator').html('<label class="small text-danger"> Invalid! </label>');
		}
    });
});




