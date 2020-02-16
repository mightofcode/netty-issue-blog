package com.mocyx.yinwangblog.blog.entity.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuesDto {
    private List<IssueDto> nodes;
}
