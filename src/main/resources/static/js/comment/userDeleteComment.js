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
    contentType:'application/json; charset=utf-8'
  }).done(function (result){
    console.log(result)
    if (result === "성공") {
      window.close();
    }
  })
}


