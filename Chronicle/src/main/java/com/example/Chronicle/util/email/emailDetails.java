package com.example.Chronicle.util.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class emailDetails {
    private String recipient;
    private String subject;
    private String body;
}
