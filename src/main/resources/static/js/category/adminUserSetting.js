const main = {
  init: function () {
    const _this = this;
      document.querySelector('#login-submit').addEventListener('click',
          function () {
        _this.signIn();
          });
  },

  signIn: function () {
    const data={
      "email":document.querySelector('#username').value,
      "password":document.querySelector('#password').value,
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
