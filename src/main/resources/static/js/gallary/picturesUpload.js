function pictures_upload() {
    var carId = $("#carId");
    var employee_Id = $("#employee_Id");
    var lendTime = $("#lendTime");

    $.ajax({
        cache : true,
        type : "POST",
        url : context + '/conditions/submitJSON.html',
        data : $('#addConditionsForm').serialize(),
        async : true,
        error : function(request) {
            $('#addErrorMsg').html("<font color='red'>Failed!</font>");
        },
        success : function(data) {
            if (data.success) {
                history.go(0);
            } else {
                $('#addErrorMsg').html("<font color='red'> " + data.msg + "</font>");
            }
        },
        dataType : "json"
    });
    return false;
}