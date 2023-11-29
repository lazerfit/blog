function deleteCategory() {
  const editModal = document.getElementById("deleteModal");
  const categoryId = editModal.querySelector("#editId").value;
  const _token=$('input[name="_csrf"]').val()
  $.ajax({
    url:"/admin/setting/category/delete/"+categoryId,
    type:"POST",
    headers: {
      'X-CSRF-TOKEN': _token
    },
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




