/**
 * Created by Crow on 2016/6/10.
 */
function search()
{
    var nickname = document.getElementById("for_search").value;
    var type = document.getElementById("search_type").value;
    alert(nickname);
    if (type == 1) {
        window.location('listUserByNickname?nickname=' + nickname);
        // window.open('listUserByNickname?nickname=' + nickname);
    }
    else {
        window.open('fullTextSearch?content=' + nickname);
    }
    //window.location.href = 'listUserByNickname?nickname=' + nickname;
    return false;
}


