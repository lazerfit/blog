$(document).ready(function (){
  $('.summernote').summernote({
    // 에디터 높이
    height: 500,
    // 에디터 한글 설정
    lang: "ko-KR",
    // 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
    focus : true,
    tabSize: 2,
    prettifyHtml: true,
    toolbar:[
      // 글꼴 설정
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
      ['insert',['picture','link','video']],
      // 코드보기, 확대해서보기, 도움말
      ['view', ['codeview','fullscreen', 'help']],

      ['highlight',['highlight']],
    ],
  });

  $("button[data-toggle='dropdown']").each(function (index) {
    $(this).removeAttr("data-toggle")
    .attr("data-bs-toggle", "dropdown"); });
});


