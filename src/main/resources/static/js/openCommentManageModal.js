function openManageModal(link) {
  $('#manageCommentModal').modal('show');
  const dropdownContent=link.closest(".dropdown-content");
  let username = link.getAttribute('data-username');
  let content = link.getAttribute('data-content');

  const element = document.querySelector('.comment-username');
  element.innerHTML=username;

  document.getElementById('comment-username').value=username;
  document.getElementById('content-area').value=content;

  dropdownContent.style.display="none";
}

function openDeleteModal() {
  $('#deleteCommentModal').modal('show')
  $('#manageCommentModal').modal('hide')
  const commentId = document.getElementById('commentId').value;
  const deleteModal = document.getElementById('deleteCommentModal');
  let commentIdInput = deleteModal.querySelector('#deleteCommentId');
  commentIdInput.value=commentId;
}

function openEditModal() {
  $('#editCommentModal').modal('show')
  $('#manageCommentModal').modal('hide')
  const commentId = document.getElementById('commentId').value;

  const deleteModal = document.getElementById('editCommentModal');
  let commentIdInput = deleteModal.querySelector('#editCommentId');
  commentIdInput.value=commentId;
}
