
function editCategory() {
  const editModal = document.getElementById("editModal");
  const categoryId = editModal.querySelector("#categoryId");

  const title=document.getElementById('editModal').querySelector('#editFloatingInput').value;
  const listOrder=document.getElementById('editModal').querySelector('#editFloatingNumber').value;

  const formData={'title':title, 'listOrder':listOrder};

  console.log(formData)

  $.ajax({
    url: "/admin/setting/category/edit/"+categoryId.value,
    type: "POST",
    data: JSON.stringify(formData),
    dataType:'json',
    contentType: 'application/json; charset=utf-8',
    success:
      setTimeout(function () {
        window.location.reload();
      }, 100)
  });

}

function transferCategoryIdToEditModal(categoryId, categoryTitle, categoryListOrder) {
  const editModal = document.getElementById("editModal");
  const categoryIdInput = editModal.querySelector("#categoryId");
  categoryIdInput.value = categoryId;

  const titleInput = editModal.querySelector("#editFloatingInput");
  titleInput.value = categoryTitle;

  const listOrderInput = editModal.querySelector("#editFloatingNumber");
  listOrderInput.value = categoryListOrder;
}




