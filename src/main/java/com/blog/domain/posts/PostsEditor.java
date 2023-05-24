package com.blog.domain.posts;

/*
record 쓰는 이유 :
PostsEditor 만든 이유 :
Builder Design Pattern :
 */

public record PostsEditor(String title, String content) {

    public static PostsEditor.PostsEditorBuilder builder() {
        return new PostsEditor.PostsEditorBuilder();
    }

    public static class PostsEditorBuilder {
        private String title;
        private String content;

        public PostsEditorBuilder() {
        }

        public PostsEditorBuilder title(String title) {
            if (title != null) {
                this.title=title;
            }
            return this;
        }

        public PostsEditorBuilder content(String content) {
            if (content != null) {
                this.content = content;
            }
            return this;
        }

        public PostsEditor build() {
            return new PostsEditor(this.title,this.content);
        }
    }
}
