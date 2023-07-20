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

// Delete Comment
function deleteComment() {

  $('#deleteCommentModal').modal('hide')

  let commentId=document.getElementById('deleteCommentId').value;
  let postId=document.getElementById('deletePostId').value;

  console.log(commentId);
  console.log(postId);

  $.ajax({
    url:"/post/admin/comment/delete",
    type:"POST",
    data:{
      "commentId":commentId,
      "postId":postId
    }
  }).done(function (result){
    $('#comment-table').replaceWith(result);
  }).fail(function (){
    window.location.reload();
  })

function userDelete() {

  $('#deleteCommentModal').modal('hide')

  const commentId=document.getElementById('deleteCommentId').value;

  $.ajax({
    url: "/post/comment/manage/delete",
    type: "POST"
  })

}

}
