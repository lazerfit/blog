function manageCategory() {
  window.location.href="/admin/setting/category"
}

function manageAccount() {
  window.location.href="/admin/setting/account"
}

function openPasswordModal() {
  const user_email = document.getElementById('user-email').innerText;
  const input_email=document.getElementById('account-email');
  input_email.value=user_email;

  $('#change-password-modal').modal('show');
}

function passwordCheck() {
  if (document.getElementById('new-password').value === document.getElementById('new-password-check').value) {
    document.getElementById('password-check').innerHTML = "비밀번호가 일치합니다.";
    document.getElementById('password-check').style.color = 'blue';
  } else {
    document.getElementById('password-check').innerHTML = "비밀번호가 일치하지 않습니다.";
    document.getElementById('password-check').style.color = 'red';
  }
}

function saveChanges() {
  const email = document.getElementById('account-email').value;
  const origin_password = document.getElementById('origin-password').value;
  const new_password = document.getElementById('new-password').value;
  const _token=$('input[name="_csrf"]').val()

  const data={
    "email":email,
    "originPassword":origin_password,
    "newPassword":new_password
  }

  if (document.getElementById('new-password').value === document.getElementById(
      'new-password-check').value) {
    $.ajax({
      url: '/admin/setting/account/edit/password',
      method: 'POST',
      data: JSON.stringify(data),
      headers: {
        'X-CSRF-TOKEN': _token
      },
      contentType: 'application/json; charset=utf-8',
    }).done(function () {
      $('#change-password-modal').modal('hide')
      $.ajax({
        url: '/logout',
        method: 'POST',
        headers: {
          'X-CSRF-TOKEN': _token
        },
      }).done(function () {
        window.location.href = '/'
      }).fail(function (response) {
        const error = JSON.parse(response.responseText);
        alert(error.message)
      });
    }).fail(function (response) {
      const error = JSON.parse(response.responseText)
      alert(error.message)
    });
  }
  else {
    alert("새 비밀번호가 일치하지 않습니다.")
  }
}
