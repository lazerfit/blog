function openSubCommentWindow(elem) {
  const id=$(elem).closest('.comment-id').attr('data-commentId')
  const postId=document.getElementById('commentContent').getAttribute('data-postId')
  window.open('/post/comment/subComment/new?commentId='+id+'&postId='+postId,'대댓글','width=500,height=500')
}
