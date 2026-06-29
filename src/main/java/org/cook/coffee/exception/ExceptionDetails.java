package org.cook.coffee.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDetails {

    private String message;
    private String description;
    private LocalDateTime timeStamp;

}
