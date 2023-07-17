function openManageComment(element) {
  const commentId = element.parentElement.getAttribute('data-commentId');
  const dropdownContent = document.querySelector(".comment-dropdown[data-commentId='" + commentId + "']");
  dropdownContent.style.display="none";
  $('#authCheckModal').modal('show');
  const inputCommentId = document.getElementById('commentId');
  inputCommentId.value=commentId;

}

function openDeleteModal(elem) {
  $('#deleteCommentModal').modal('show');
/*  const id = elem.getAttribute('data-commentId');
  const inputId = document.getElementById('comment-id');
  inputId.value=id;*/
}


function openEditModal() {
  $('#editCommentModal').modal('show')
  $('#manageCommentModal').modal('hide')
  const commentId = document.getElementById('commentId').value;

  const deleteModal = document.getElementById('editCommentModal');
  let commentIdInput = deleteModal.querySelector('#editCommentId');
  commentIdInput.value=commentId;
}
