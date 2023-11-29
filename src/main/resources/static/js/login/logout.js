function logout() {
  const _token=$('input[name="_csrf"]').val()

  $.ajax({
    url:"/logout",
    type:"POST",
    headers:{
      'X-CSRF-TOKEN': _token
    },
    success: function (response){
      window.location.href='/'
    },
    error:function (error) {
      console.error('Logout error:',error)
    }
  })}
