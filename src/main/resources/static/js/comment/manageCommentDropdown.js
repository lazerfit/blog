function manageCommentDropdown(element) {
  let commentId = element.nextElementSibling.getAttribute("data-commentId");
  const dropdownContent = document.querySelector(".comment-dropdown[data-commentId='" + commentId + "']");

  if (dropdownContent.style.display === "none") {
    dropdownContent.style.display = "block";
  } else {
    dropdownContent.style.display = "none";
  }
}

function openManageComment(element) {
  const commentId = element.parentElement.getAttribute('data-commentId');
  const dropdownContent = document.querySelector(".comment-dropdown[data-commentId='" + commentId + "']");
  dropdownContent.style.display="none";
  $('#authCheckModal').modal('show');
  const inputCommentId = document.getElementById('commentId');
  inputCommentId.value=commentId;
}

function openDeleteCommentModal(element) {
  const commentId = $(element).closest('.dropdown-content').attr(
      'data-commentId');
  const dropdownContent = document.querySelector(".comment-dropdown[data-commentId='" + commentId + "']");
  const postId = element.getAttribute('data-postId');
  dropdownContent.style.display="none"

  const inputCommentId = document.getElementById('deleteCommentId');
  const inputPostId = document.getElementById('deletePostId');
  inputCommentId.value=commentId;
  inputPostId.value=postId;

  $('#deleteCommentId').val(commentId);
  $('#deleteCommentModal').modal('show');
}
