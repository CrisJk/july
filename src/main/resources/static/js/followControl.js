/**
 * Created by Crow on 2016/6/5.
 */
function follow( aim_user_email ,type)
{
    alert(aim_user_email);
    $.ajax({
        url: "/followControlInJson",
        type:"GET",
        data:{
            "aim_user_email":aim_user_email,
            "type":type
        },
        dataType:"json",
        async : false,
        cache:true,
        success:function(data){
            setTimeout(function() {
                // After 5s delay
                alert(data.message);
            }, 5000);
            //alert("success");
           // alert(data);
        },
        error:function(data){
            setTimeout(function() {
                // After 5s delay
                alert(data.message);
            }, 5000);
            //alert("error");
            //alert(data);
        }
    });
}
