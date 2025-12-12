package dev.cong.v.springcomereme.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {


    private  String toMail;

    private  String subject;

    private  String body;

}
