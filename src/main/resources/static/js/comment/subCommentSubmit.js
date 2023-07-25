function subCommentSubmit() {

  const parentId=document.getElementById('parentId').value;
  const postId=document.getElementById('postId').value;
  const username=document.getElementById('username').value;
  const password=document.getElementById('password').value;
  const content=document.getElementById('content-area').value;

  const data={
    "username":username,
    "content":content,
    "parentId":parentId,
    "postId":postId,
    "password":password
  }

  console.log(data)

  $.ajax({
    url:"/post/comment/subComment/new",
    type:"POST",
    data:JSON.stringify(data),
    contentType:"application/json; charset=utf-8"
  }).done(function (){
    window.close();
  })
}
