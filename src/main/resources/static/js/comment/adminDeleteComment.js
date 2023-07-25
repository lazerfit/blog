function adminDeleteComment() {
  const commentId=document.getElementById('deleteCommentId').value;
  const postId=document.getElementById('deletePostId').value;

  $('#deleteCommentModal').modal('hide')

  $.ajax({
    url: "/post/admin/comment/delete",
    type:'POST',
    data:{
      "commentId":commentId,
      "postId":postId
    },
    dataType:"html",
  }).done(function (result) {
    $('#comment-table').replaceWith(result);
  })
}
