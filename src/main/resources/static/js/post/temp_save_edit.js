document.addEventListener('DOMContentLoaded', function () {
  $('.summernote').summernote({
    // 에디터 높이
    height: 500,
    // 에디터 한글 설정
    lang: "ko-KR",
    // 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
    focus : true,
    tabSize: 2,
    prettifyHtml: true,
    spellcheck: false,
    toolbar:[
      // 글꼴 설정
      ['style', ['style']],
      ['fontname', ['fontname']],
      // 글자 크기 설정
      ['fontsize', ['fontsize']],
      // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
      ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
      // 글자색
      ['color', ['forecolor','color']],
      // 표만들기
      ['table', ['table']],
      // 글머리 기호, 번호매기기, 문단정렬
      ['para', ['ul', 'ol', 'paragraph']],
      // 줄간격
      ['height', ['height']],
      // 그림첨부, 링크만들기, 동영상첨부
      ['insert',['picture','link']],
      // 코드보기, 확대해서보기, 도움말
      ['view', ['codeview','fullscreen', 'help']],

      ['highlight',['highlight']],
    ],
    fontSizes: [
      // 글자 크기 선택 옵션
      '8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72'
    ],
    styleTags: [
      // 스타일 태그 옵션
      'p',
      { title: 'Blockquote', tag: 'blockquote', className: 'blockquote', value: 'blockquote' },
      'pre',
      'h1', 'h2', 'h3', 'h4', 'h5', 'h6'
    ],
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

  const form = document.querySelector('.post-edit-from');
  form.submit();
});

