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

    const _token=$('input[name="_csrf"]').val()

    $.ajax({
      url: '/auth/login/',
      method: 'post',
      data: JSON.stringify(data),
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
