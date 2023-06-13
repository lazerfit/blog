$(document).ready(function () {
  // Warning
  $(window).on('beforeunload', function(){
    return "Any changes will be lost";
  });

  // Form Submit
  $(document).on("submit", "form", function(event){
    // disable warning
    $(window).off('beforeunload');
  });
});
