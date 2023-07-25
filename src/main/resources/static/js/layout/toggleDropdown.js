function toggleDropdown(dropdownId) {
  const dropdownContent = document.getElementById(dropdownId);
  dropdownContent.classList.toggle("show");
}

window.addEventListener("click", function (event) {
  if (!event.target.matches(".profile")) {
    const dropdowns = document.getElementsByClassName("dropdown-content");
    for (let i = 0; i < dropdowns.length; i++) {
      const openDropdown = dropdowns[i];
      if (openDropdown.classList.contains("show")) {
        openDropdown.classList.remove("show");
      }
    }
  }
});
