$('#del-button').click(function () {
  if (confirm("정말 삭제하시겠습니까?")) {
    document.deletePost.submit()
  }
  else{
    return false;
  }
})
