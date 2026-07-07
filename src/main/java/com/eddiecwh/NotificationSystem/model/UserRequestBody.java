package com.eddiecwh.NotificationSystem.model;

import lombok.Data;;

@Data
public class UserRequestBody {
    String firstName;
    String lastName;
    String email;
    String phone;
}
