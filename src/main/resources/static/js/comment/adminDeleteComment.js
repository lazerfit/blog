function adminDeleteComment() {
  const commentId=document.getElementById('deleteCommentId').value;
  const postId=document.getElementById('deletePostId').value;
  const _token=$('input[name="_csrf"]').val()
  $('#deleteCommentModal').modal('hide')

  $.ajax({
    url: "/post/admin/comment/delete",
    type:'POST',
    data:{
      "commentId":commentId,
      "postId":postId
    },
    headers: {
      'X-CSRF-TOKEN': _token
    },
    dataType:"html",
  }).done(function (result) {
    $('#comment-table').replaceWith(result);
  })
}
