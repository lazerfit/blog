if (!Kakao.isInitialized()) {
  Kakao.init("9e41201e9d97eaf62b4bbd228be0c380");
}

const postId = document.querySelector('input[name="post-id"]').value;
const linkUrl=window.location.href;
const title = document.querySelector('meta[name="title"]').getAttribute('content');
const content = document.querySelector('meta[name="description"]').getAttribute('content');
const thumbTag = document.querySelector('meta[property="og:image"]');
const thumbImg= thumbTag ? thumbTag.getAttribute('content') :
'https://drive.google.com/uc?export=download&id=10N4Yv5PgVo4j5o0pVUYxmSxWUTrJjYKm';

const sendKakao = function () {
  Kakao.Share.sendScrap({
    // container: '#kakaotalk-sharing-btn',
    requestUrl: linkUrl,
    templateId: 101768,
    templateArgs:{
      THU: thumbImg,
      title: title,
      description: content,
      url: "post/"+postId
    }
  });

  Kakao.Link.cleanup();
};



