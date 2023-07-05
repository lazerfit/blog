function deleteCategory() {
  const editModal = document.getElementById("deleteModal");
  const categoryId = editModal.querySelector("#editId").value;

  $.ajax({
    url:"/admin/setting/category/delete/"+categoryId,
    type:"POST",
    success: setTimeout(function (){
      location.reload();
    },500)
  })
}

function transferCategoryIdTodeleteModal(categoryId) {
  const deleteModal = document.getElementById("deleteModal");
  const editId = deleteModal.querySelector("#editId");
  editId.value = categoryId;
}

function transferCategoryIdToEditModal(categoryId, categoryTitle, categoryListOrder) {
  const editModal = document.getElementById("editModal");
  const categoryIdInput = editModal.querySelector("#categoryId");
  categoryIdInput.value = categoryId;
  console.log(categoryId)

  const titleInput = editModal.querySelector("#editFloatingInput");
  titleInput.value = categoryTitle;
  console.log(categoryTitle)

  const listOrderInput = editModal.querySelector("#editFloatingNumber");
  listOrderInput.value = categoryListOrder;
  console.log(categoryListOrder)
}




