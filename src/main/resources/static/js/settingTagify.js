const input=document.querySelector('input[name=tags]');
const tagify=new Tagify(input,{
  maxTags:10,
  backspace:"edit",
  whitelist:["Spring","Spring data","Spring boot","spring boot","Spring security","spring","spring data","spring security","Java","java","MySql"],
  dropdown:{
    enable: 1,
    fuzzySearch: false,
    position: 'text',
    caseSensitive: true
  },
  editTags: {
    clicks: 2
  }
})

