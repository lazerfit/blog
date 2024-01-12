document.addEventListener('DOMContentLoaded',()=>{
  const analytics = document.querySelector('.analyse');
  const userInfo = document.querySelector('.user-info');
  const users = document.querySelector('.new-users');
  const category = document.querySelector('.category');
  const recycleBin = document.querySelector('.recycle-bin');

  analytics.style.display = 'none';
  userInfo.style.display ='none'
  category.style.display ='none'
  users.style.display = 'grid';
  recycleBin.style.display = 'none';
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
const recycleBin = document.querySelector('.recycle-bin');
const userInfo = document.querySelector('.user-info');
const recycleBinBtn=document.querySelector('.recycle-bin-sidebar')
userBtn.addEventListener('click', () =>{
  analytics.style.display = 'none';
  categoryList.style.display = 'none';
  userInfo.style.display = 'none';
  users.style.display = 'grid';
  recycleBin.style.display = 'none';
  recycleBin.classList.remove('active')
  anaylseBtn.classList.remove('active');
  categoryBtn.classList.remove('active');
  userBtn.className = 'users active';
})

anaylseBtn.addEventListener('click', () =>{
  analytics.style.display = 'grid';
  users.style.display = 'none';
  categoryList.style.display = 'none';
  userInfo.style.display = 'none';
  recycleBin.style.display = 'none';
  recycleBin.classList.remove('active')
  userBtn.classList.remove('active');
  categoryBtn.classList.remove('active');
  anaylseBtn.className = 'analytics active';
})

categoryBtn.addEventListener('click', () =>{
  analytics.style.display = 'none';
  users.style.display = 'none';
  userInfo.style.display = 'none';
  categoryList.style.display='block'
  recycleBin.style.display = 'none';
  recycleBin.classList.remove('active')
  anaylseBtn.classList.remove('active');
  userBtn.classList.remove('active');
  categoryBtn.className = 'category-sidebar active'
})

recycleBinBtn.addEventListener('click', () =>{
  analytics.style.display = 'none';
  users.style.display = 'none';
  userInfo.style.display = 'none';
  recycleBin.style.display='block'
  categoryList.style.display = 'none';
  categoryBtn.classList.remove('active')
  anaylseBtn.classList.remove('active');
  userBtn.classList.remove('active');
  recycleBin.className = 'category-sidebar active'
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

  const data={
    "email":email,
    "role":role
  }

  $.ajax({
    url: '/admin/setting/account/edit/role',
    type: 'POST',
    data: JSON.stringify(data),
    contentType: 'application/json; charset=utf-8',
    headers: {
      'X-CSRF-TOKEN': _token
    },
  }).done(function () {
    alert('수정이 완료되었습니다.')
  }).fail(function (result) {
    const error = JSON.parse(result);
    alert(error.messages);
  });
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

function deletePost(button) {
  const id=button.getAttribute("data-id");

  if (confirm('정말 삭제하시겠습니까?')) {
    $.ajax({
      url: "/admin/setting/recycleBin/delete/" + id,
      type: "POST",
      headers: {
        'X-CSRF-TOKEN': _token
      },
    }).done(function () {
      document.location.reload();
    });
  } else {
    return false;
  }
}

function restorePost(button) {
  const id = button.getAttribute('data-id');
  const title=button.getAttribute('data-title');
  const content = button.getAttribute('data-content');
  const tag = button.getAttribute('data-tag');
  const category = button.getAttribute('data-category');
  const thumbnail = button.getAttribute('data-thumbnail');
  if (confirm('복원하시겠습니까?')) {
    const data = {
      "title": title,
      "content": content,
      "tags": isNull(tag) ? '없음' : tag,
      "category": isNull(category) ? '카테고리 없음' : category,
      "thumbnail": thumbnail,
      "views":0
    };

    $.ajax({
      url:"/recycleBin/restore",
      data:JSON.stringify(data),
      type:'POST',
      contentType: "application/json; charset=utf-8",
      headers: {
        'X-CSRF-TOKEN': _token
      },
    }).done(function () {
      $.ajax({
        url: "/admin/setting/recycleBin/delete/" + id,
        type: "POST",
        headers: {
          'X-CSRF-TOKEN': _token
        },
      }).done(function () {
        document.location.reload();
      });
    });
  } else {
    return false;
  }
}

function isNull(value) {
  return value === null ? 1 : 0;
}
