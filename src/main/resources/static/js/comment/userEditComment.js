function cancel() {
  $('#editCommentModal').modal('hide');
}

function userEditComment() {
  const id=document.getElementById('commentId').value;
  const content = document.getElementById('content-area').value;
  const password=document.getElementById('edit-password').value;

  $.ajax({
    url: "/post/comment/manage/edit",
    type: "POST",
    data: {
      "id": id,
      "content": content,
      "password": password
    }
  }).done(function () {
    window.close();
  }).fail(function () {
    alert("비밀번호가 올바르지 않습니다.")
  });
}



