function backToMainModel(){
  $('#deleteCommentModal').modal('hide');
  $('#manageCommentModal').modal('show');

  const id = document.getElementById('test').value;

  console.log(id);
}

function userDeleteComment() {

  const id=document.getElementById('comment-id').value;
  const password=document.getElementById('comment-pwd').value;

  $.ajax({
    url: "/posts/comment/manage/delete",
    type: "POST",
    data: {
      "commentId": id,
      "password": password
    }
  }).done(function (result){
    console.log(result)
    if (result === "성공") {
      window.close();
    }

  })
}


