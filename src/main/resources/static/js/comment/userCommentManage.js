function openCommentManage() {
  //close the modal
  $('#authCheckModal').modal('hide');

  // Open a new window
  const windowFeatures = 'width=500,height=550';
  const newWindow = window.open('', '댓글 수정/삭제', windowFeatures);

  // Submit the form in the new window
  document.getElementById('commentPasswordCheckForm').target = '댓글 수정/삭제';
  document.getElementById('commentPasswordCheckForm').submit();
}
