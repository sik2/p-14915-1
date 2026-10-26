package com.back.faq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("faq")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Faq {

    @Id // id가 null이면 INSERT, 값이 있으면 UPDATE. 값은 DB(identity)가 만든다.
    private Long id;

    private String question;

    private String answer;
}
