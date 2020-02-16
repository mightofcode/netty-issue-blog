package com.mocyx.yinwangblog.blog.entity.issue;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String bodyHTML;
    private AuthorDto author;
    private String createdAt;
}
