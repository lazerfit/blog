document.addEventListener('DOMContentLoaded',()=>{
  const analytics = document.querySelector('.analyse');
  const userInfo = document.querySelector('.user-info');
  const users = document.querySelector('.new-users');
  const category = document.querySelector('.category');

  analytics.style.display = 'none';
  userInfo.style.display ='none'
  category.style.display ='none'
  users.style.display = 'grid';
})
const _token = document.getElementById('csrf').value;

// Dark-mode Toggle
const sideMenu = document.querySelector('aside');
const menuBtn = document.getElementById('menu-btn');
const closeBtn = document.getElementById('close-btn');

const darkMode = document.querySelector('.dark-mode');


menuBtn.addEventListener('click',() =>{
  sideMenu.style.display = 'block';
});

closeBtn.addEventListener('click', () =>{
  sideMenu.style.display = 'none';
})

darkMode.addEventListener('click', () =>{
  document.body.classList.toggle('dark-mode-variables');
  darkMode.querySelector('span:nth-child(1)').classList.toggle('active');
  darkMode.querySelector('span:nth-child(2)').classList.toggle('active');
})

// Sidebar-Toggle
const userBtn = document.querySelector('.users');
const anaylseBtn=document.querySelector('.analytics');
const categoryBtn = document.querySelector('.category-sidebar');
const analytics = document.querySelector('.analyse');
const users = document.querySelector('.new-users');
const categoryList = document.querySelector('.category');
const userInfo = document.querySelector('.user-info');
userBtn.addEventListener('click', () =>{
  analytics.style.display = 'none';
  categoryList.style.display = 'none';
  userInfo.style.display = 'none';
  users.style.display = 'grid';
  anaylseBtn.classList.remove('active');
  categoryBtn.classList.remove('active');
  userBtn.className = 'users active';
})

anaylseBtn.addEventListener('click', () =>{
  analytics.style.display = 'grid';
  users.style.display = 'none';
  categoryList.style.display = 'none';
  userInfo.style.display = 'none';
  userBtn.classList.remove('active');
  categoryBtn.classList.remove('active');
  anaylseBtn.className = 'analytics active';
})

categoryBtn.addEventListener('click', () =>{
  analytics.style.display = 'none';
  users.style.display = 'none';
  userInfo.style.display = 'none';
  categoryList.style.display='block'
  anaylseBtn.classList.remove('active');
  userBtn.classList.remove('active');
  categoryBtn.className = 'category-sidebar active'
})

// User-list Toggle
const userList=document.querySelector('.user-list');

userList.addEventListener('click',() =>{
  if (userInfo.style.display === 'none') {
    userInfo.style.display = 'block';
  } else {
    userInfo.style.display = 'none';
  }
})

// send EditRoleForm
function editRole(button) {
  const email = button.getAttribute('data-email')
  const role=button.parentNode.parentNode
  .querySelector('td:nth-child(3)').querySelector('select').value;

  console.log(email);
  console.log(role);
}

// add new row in the category table
function insertRow() {
  const table=document.querySelector('.category-table');

  const newRow=table.insertRow();

  const cell1 = newRow.insertCell(0);
  const cell2 = newRow.insertCell(1);
  const cell3 = newRow.insertCell(2);

  cell1.insertAdjacentHTML("afterbegin","<input type='text' class='row-id' value=''>");
  cell2.insertAdjacentHTML("afterbegin","<input type='text' class='order-num' value=''>");
  cell3.insertAdjacentHTML('afterbegin',"<a href=\"#\" class=\"category-edit-btn\" type=\"button\" onclick=\"submitCategoryOfInsertedRow(this)\">\n"
      + "                    <span class=\"material-icons-sharp\">\n"
      + "                      send\n"
      + "                    </span>\n"
      + "                    </a>");

}

function submitCategory(button) {
  const title=button.parentNode.parentNode.querySelector('td:nth-child(1)').firstElementChild.value;
  const listOrder = button.parentNode.parentNode.querySelector('td:nth-child(2)').firstElementChild.value;
  const id=button.getAttribute("data-id");

  const data={
    "title":title,
    "listOrder":listOrder,
    "id":id
  }

  $.ajax({
    url: '/admin/setting/category/edit',
    type: 'POST',
    data: JSON.stringify(data),
    headers: {
      'X-CSRF-TOKEN': _token
    },
    contentType: "application/json; charset=utf-8"
  }).done(function () {
    alert('수정이 완료되었습니다.')
    document.location.reload();
  }).fail(function (result) {
    const error = JSON.parse(result);
    alert(error.message);
  });
}

function deleteCategory(button) {
  const id=button.getAttribute("data-id");

  $.ajax({
    url: '/admin/setting/category/delete/' + id,
    type: 'POST',
    headers: {
      'X-CSRF-TOKEN': _token
    },
  }).done(function () {
    alert('삭제하였습니다.')
    document.location.reload();
  })
  .fail(function (result) {
    const error = JSON.parse(result);
    alert(error.messages);
  });
}

function submitCategoryOfInsertedRow(button) {
  const title = button.parentNode.parentNode.firstElementChild.firstElementChild.value;
  const listOrder = button.parentNode.parentNode.querySelector('td:nth-child(2)').firstElementChild.value;

  const data={
    "title":title,
    "listOrder":listOrder
  }

  $.ajax({
    url: '/admin/setting/category/edit',
    type: 'POST',
    data: JSON.stringify(data),
    headers: {
      'X-CSRF-TOKEN': _token
    },
    contentType: "application/json; charset=utf-8"
  }).done(function () {
    alert('수정이 완료되었습니다.')
  }).fail(function (result) {
    const error = JSON.parse(result);
    alert(error.message);
  });
}
