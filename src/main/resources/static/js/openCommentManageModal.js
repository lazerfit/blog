function openManageModal(link) {
  $('#manageCommentModal').modal('show');
  const dropdownContent=link.closest(".dropdown-content");
  let commentId = link.getAttribute('data-commentId');
  dropdownContent.style.display="none";
  document.getElementById('editId').value=commentId;
}
