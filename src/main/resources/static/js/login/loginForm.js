const main = {
  init: function () {
    const _this = this;
    document.querySelector('#login-submit').addEventListener('click',
        function () {
      _this.signIn();
        });
  },

  signIn: function () {
    const data = {
      "email": document.querySelector('#username').value,
      "password": document.querySelector('#password').value,
    };

    const _token = document.querySelector('input[name="_csrf"]').value;

    $.ajax({
      url: '/auth/login',
      method: 'post',
      data: JSON.stringify(data),
      contentType:'application/json; charset=utf-8',
      headers: {
        'X-CSRF-TOKEN': _token
      }
    }).done(function () {
      window.location.href="/"
    })
    .fail(function (response) {
      const error = JSON.parse(response.responseText)
      alert(error.message)
    });
  },
}

main.init();
