// Create Comment
$('.reply-btn').on('click',function (){
  const postId=document.getElementById("inputInfo").querySelector(".postId").value
  const username=document.getElementById("inputInfo").querySelector(".username").value
  const password=document.getElementById("inputInfo").querySelector(".password").value
  const content=document.getElementById("inputComment").querySelector("#comment").value

  console.log(postId);
  console.log(username);
  console.log(password);
  console.log(content);


  const comment = {
    'postId':postId,
    'username':username,
    'password':password,
    "content":content,
    "parentId":0
  };

  $.ajax({
    url:"/post/comment/new",
    type: "POST",
    data: JSON.stringify(comment),
    dataType:"html",
    contentType: "application/json; charset=utf-8"
  })
  .done(function (result) {
    $('#comment-table').replaceWith(result);
  })
});
