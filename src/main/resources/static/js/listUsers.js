/**
 * Created by Crow on 2016/6/10.
 */
function searchUser()
{
    var nickname = document.getElementById("for_search").value;
    window.open('listUserByNickname?nickname=' + nickname);
    return false;
}

function searchArticle()
{
    var nickname = document.getElementById("for_search").value;
    window.open('fullTextSearch?content=' + nickname);
    return false;
}


