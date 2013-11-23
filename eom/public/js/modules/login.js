/**
 * functions for login page.
 */

function doLogin() {
	var userId = $("#userId_input").get(0).value;
	var password = $("#password_input").get(0).value;
	var loginData = {
		userId : userId,
		password : password
	};

	$.ajax({
		type : "POST",
		url : "/doLogin",
		data : loginData,
		success : function(data, textStatus, jqXHR) {
				if(data.result == "success"){
					alert("login success");
				}else{
					alert(" login fail : " + data.message);
				}
		},
		dataType : "json"
	}).done(function(data, textStatus, jqXHR) {
		
	}).fail(function(jqXHR, textStatus, errorThrown) {
		alert("fail:" + errorThrown);
	});

}
