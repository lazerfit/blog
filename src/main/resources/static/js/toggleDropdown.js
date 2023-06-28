function toggleDropdown() {
  var dropdownContent = document.getElementById("dropdownContent");
  dropdownContent.classList.toggle("show");
}

window.addEventListener("click", function (event) {
  if (!event.target.matches(".profile")) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    for (var i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains("show")) {
        openDropdown.classList.remove("show");
      }
    }
  }
});
