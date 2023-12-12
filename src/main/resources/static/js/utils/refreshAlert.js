document.addEventListener('DOMContentLoaded', function () {
  const beforeUnloadHandler = (event) => {
    // Recommended
    event.preventDefault();

    // Included for legacy support, e.g. Chrome/Edge < 119
    event.returnValue = '변경사항이 저장되지 않을 수 있습니다.';
  };

  window.addEventListener('beforeunload',beforeUnloadHandler);

  document.querySelector('#post-save').addEventListener('click', function () {
    window.removeEventListener('beforeunload',beforeUnloadHandler);
  });
});
