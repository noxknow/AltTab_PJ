package com.ssafy.alttab.solution.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "solutions")
public class Solution {
//    @Id
//    private SolutionId id;
//    private String studyId;
//    private String problemId;
//    private List<Block> blocks;
    private String studyId;
    private String problemId;
    private List<Block> blocks;
}