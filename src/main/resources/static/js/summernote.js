$(document).ready(function (){
  $('.summernote').summernote({
    // 에디터 높이
    height: 500,
    // 에디터 한글 설정
    lang: "ko-KR",
    // 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
    focus : true,
  });

  $("button[data-toggle='dropdown']").each(function (index) {
    $(this).removeAttr("data-toggle")
    .attr("data-bs-toggle", "dropdown"); });
});


