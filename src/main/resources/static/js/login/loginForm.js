const main = {
  init: function () {
    const _this = this;
    $('#signIn').on('click', function () {
      _this.signIn();
    });
  },

  signIn: function () {
    const data={
      "email":$('#username').val(),
      "password":$('#password').val(),
      "remember":$('#remember').val()
    };

    $.post(
        '/auth/login', JSON.stringify(data), function () {
          window.location.href = "/";
        }
    );
  },
}

main.init();
