function deleteCategory() {
  const editModal = document.getElementById("editModal");
  const categoryId = editModal.querySelector("#editId").value;

  $.ajax({
    url:"/admin/setting/category/delete/"+categoryId,
    type:"POST",
    success: setTimeout(function (){
      location.reload();
    },500)
  })
}

function transferCategoryId(categoryId) {
  const editModal = document.getElementById("editModal");
  const editId = editModal.querySelector("#editId");
  editId.value = categoryId;
}
