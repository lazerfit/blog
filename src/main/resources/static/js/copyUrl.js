let clip = document.getElementById('clipboard');
clip.addEventListener('click',function () {
  navigator.clipboard.writeText(window.location.href).then(() =>
      alert('Url 주소가 복사되었습니다.'))
  })

