package com.ssafy.alttab.solution.document;

import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "solutions")
public class Solution {
    @Id
    private String id;

    private String studyId;
    private String problemId;
    private List<Block> blocks;
}