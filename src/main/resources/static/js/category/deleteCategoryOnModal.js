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

function transferCategoryIdToDeleteModal(categoryId) {
  const deleteModal = document.getElementById("deleteModal");
  const editId = deleteModal.querySelector("#editId");
  editId.value = categoryId;
}




