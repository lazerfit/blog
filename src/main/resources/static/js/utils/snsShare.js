const url=document.URL
const text=encodeURIComponent(document.getElementById('postFindById_title').value)
const size="width=450,height=500"
function twitter() {
  window.open('https://twitter.com/intent/tweet?url='+url+'&text='+text,twitter,size);
}

function facebook() {
  window.open('https://www.facebook.com/sharer/sharer.php?u='+encodeURIComponent(url),facebook,size);
}
