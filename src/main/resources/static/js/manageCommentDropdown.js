function manageCommentDropdown(element) {
  let commentId = element.nextElementSibling.getAttribute("data-commentId");
  const dropdownContent = document.querySelector(".comment-dropdown[data-commentId='" + commentId + "']");

  if (dropdownContent.style.display === "none") {
    dropdownContent.style.display = "block";
  } else {
    dropdownContent.style.display = "none";
  }

}
