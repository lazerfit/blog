function backToMainModel(){
  $('#deleteCommentModal').modal('hide');
  $('#manageCommentModal').modal('show');
  const id = document.getElementById('test').value;
  console.log(id);
}

function userDeleteComment(elem) {
  const id=document.getElementById('commentId').value;
  const password=document.getElementById('comment-pwd').value;
  console.log(id);
  console.log(password);
  const data={
    "commentId": id,
    "password": password
  }
  $.ajax({
    url: "/post/comment/manage/delete",
    type: "POST",
    data: JSON.stringify(data),
    contentType: 'application/json; charset=utf-8'
  }).done(function () {
    window.close();
  }).fail(function () {
    alert("비밀번호가 올바르지 않습니다.")
  });
}


