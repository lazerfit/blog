document.addEventListener('DOMContentLoaded', function () {
  $('.summernote').summernote({
    // 에디터 높이
    height: 500,
    // 에디터 한글 설정
    lang: "ko-KR",
    // 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
    focus : true,
  });

  const title=document.querySelector('#inputDefault');
  const categoryTitle=document.querySelector('#exampleSelect1');
  const tags=document.querySelector('#tags');

  let loadedData=JSON.parse(localStorage.getItem('temp_save'))

  if (loadedData !== null) {
    categoryTitle.value=loadedData.categoryTitle;
    title.value=loadedData.title;
    tagify.addTags(loadedData.tags);
    tags.value=loadedData.tags;
    $('#summernote').summernote('code', loadedData.content);
  }

  function saveData() {
    let data={
      'categoryTitle':categoryTitle.value,
      'title':title.value,
      'content':$('#summernote').summernote('code'),
      'tags': tags.value
    };

    const json = JSON.stringify(data);

    localStorage.setItem('temp_save',json);
  }

  setInterval(saveData,30000)
});

document.querySelector('#post-save').addEventListener('click', function () {
  const title=document.querySelector('#inputDefault');
  const categoryTitle=document.querySelector('#exampleSelect1');
  const tags=document.querySelector('#tags');
  const _token=document.querySelector('#csrf');
  const postId=document.querySelector('#postId').value;

  const postSave={
    'categoryTitle':categoryTitle.value,
    'title':title.value,
    'content':$('#summernote').summernote('code'),
    'tags':tags.value,
  }

  localStorage.clear();

  $.ajax({
    url: '/post/edit/'+postId,
    type: 'POST',
    data: JSON.stringify(postSave),
    contentType: 'application/json; charset=utf-8',
    headers: {
      'X-CSRF-TOKEN': _token.value
    }
  }).done(function () {
    window.location.href = "/post/"+postId
  }).fail(function (response) {
    const error = JSON.parse(response.responseText)
    alert(error.message)
  });

});
