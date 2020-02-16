package com.mocyx.yinwangblog.blog.entity.gql;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GqlQuery {
    private String query;
}
