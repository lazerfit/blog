const main = {
  init: function () {
    const _this = this;
    $('#login-submit').on('click', function () {
      _this.signIn();
    });
  },

  signIn: function () {
    const data={
      "email":$('#username').val(),
      "password":$('#password').val(),
    };

    $.post(
        '/auth/login', JSON.stringify(data), function () {
          window.location.href = "/";
        }
    )
    .fail(function (response) {
      const error=JSON.parse(response.responseText)
      alert(error.message)
    });
  },
}

main.init();
