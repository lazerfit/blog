document.addEventListener("DOMContentLoaded", () =>{
  const innerHTMLElem = document.getElementById('rightColumn').innerHTML;
  document.getElementById('aside-content').insertAdjacentHTML('beforebegin',innerHTMLElem);
})

const offcanvasElement = document.querySelector('.offcanvas');
const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
let button = document.getElementById('offcanvas-toggler');

button.addEventListener("click", () => {
  offcanvas.show();
})
