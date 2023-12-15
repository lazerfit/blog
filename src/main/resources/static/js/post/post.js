// const postId=document.querySelector('.post-id').value;
document.addEventListener('DOMContentLoaded', function () {
  const postId = document.querySelector('input[name="post-id"]').value;

  let visitedPosts = localStorage.getItem('visitedPosts');
  visitedPosts = visitedPosts ? JSON.parse(visitedPosts) : [];

  const expirationTime=localStorage.getItem('expirationTime');
  const oneDayMilliseconds = 24 * 60 * 60 * 1000;

  if (!Array.isArray(visitedPosts)) {
    visitedPosts = [];
  }

  if (!visitedPosts.includes(postId)) {
    increasePostViewCount(postId);
    visitedPosts.push(postId);
    localStorage.setItem('visitedPosts', JSON.stringify(visitedPosts));
  }

  if (!expirationTime || new Date().getTime() - parseInt(expirationTime)
      >= oneDayMilliseconds) {
    localStorage.removeItem('visitedPosts');
    localStorage.setItem('expirationTime', new Date().getTime().toString());
  }

  function increasePostViewCount(postId) {

    $.ajax({
      url: '/increase-view/' + postId,
      type: 'GET'
    }).fail(function (result) {
      const error = JSON.parse(result);
      console.log(error.messages)
    });
  }
});


