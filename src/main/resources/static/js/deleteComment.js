function backToMainModel(){
  $('#deleteCommentModal').modal('hide');
  $('#manageCommentModal').modal('show');
}

function deleteComment() {

  $('#deleteCommentModal').modal('hide')

  let commentId=document.getElementById('deleteCommentId').value;
  let postId=document.getElementById('postId').value;

  $.ajax({
    url:"/posts/comment/delete",
    type:"POST",
    data:{
      "commentId":commentId,
      "postId":postId
    }
  }).done(function (result){
    $('#comment-table').replaceWith(result);
  })

}
