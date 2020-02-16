package com.mocyx.yinwangblog.blog.entity.issue;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto {
    private String bodyHTML;
    private AuthorDto author;
    private String title;
    private CommentsDto comments;
    private String createdAt;
    private String id;
    private String databaseId;
}
