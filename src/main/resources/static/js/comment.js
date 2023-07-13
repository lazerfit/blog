// Create Comment
$('.reply-btn').on('click',function (){
  const postId=document.getElementById("postId").value
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
    url:"/posts/comment/new",
    type: "POST",
    data: JSON.stringify(comment),
    contentType: "application/json; charset=utf-8"
  })
  .done(function (fragment) {
    $('#comment-table').replaceWith(fragment);
  })
});

// Delete Comment
