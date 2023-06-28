function logout() {
  $.ajax({
    url:"/logout",
    type:"POST",
    success: function (response){
      window.location.href='/'
    },
    error:function (error) {
      console.error('Logout error:',error)
    }
  })}
