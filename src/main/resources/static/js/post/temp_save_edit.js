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
  localStorage.clear();

  const form = document.querySelector('#post-save');
  form.submit();
});

