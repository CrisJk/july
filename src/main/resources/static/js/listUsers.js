/**
 * Created by Crow on 2016/6/10.
 */
function search()
{
    var nickname = document.getElementById("for_search").value;
    //alert(nickname);
    window.open('listUserByNickname?nickname=' + nickname);
    //window.location.href = 'listUserByNickname?nickname=' + nickname;
    return false;
}


