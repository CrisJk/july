/**
 * Created by Crow on 2016/6/5.
 */
function follow( aim_user_email ,type,current_user_email)
{
    $.ajax({
        url: "/followControlInJson",
        type:"GET",
        data:{
            "aim_user_email":aim_user_email,
            "type":type,
            "current_user_email":current_user_email
        },
        dataType:"json",
        async : false,
        cache:true,
        success:function(data){
            //alert("success");
            location.reload();
            //alert(data.success);
        },
        error:function(data){
            alert("error");
            //alert(data.success);
        }
    });
}
