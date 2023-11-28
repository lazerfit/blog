function postEditFormSubmit() {
  const postId=document.getElementById('postId').value;
  const categoryTitle=document.getElementById('categorySelect').value;
  const title=document.getElementById('editForm-title').value;
  const content=document.getElementById('editForm-content').value;
  const tag=document.getElementById('input-tag').value;

  const data={
    "title":title,
    "content":content,
    "categoryTitle":categoryTitle,
    "tag":tag
  }
  $.ajax({
    url: 'https://gomdolog.store/post/edit/' + postId,
    type: 'POST',
    data: JSON.stringify(data),
    contentType: 'application/json; charset=utf-8',
    success: function () {
      $(window).off('beforeunload');
      window.location.href='/post/'+postId
    },
    error: function (xhr, status, error) {
      console.log(xhr,status,error)
    },
  })
}
