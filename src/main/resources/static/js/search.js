$(document).ready(function () {
  $('#searchKeyword').keyup(function (event) {
    if (event.which === 13) {
      $('#submit').click();
    }
  });
});
