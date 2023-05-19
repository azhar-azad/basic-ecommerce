package com.azad.basicecommerce.model.auth;

import com.azad.basicecommerce.common.generics.GenericApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class AppUser extends GenericApiModel {

    @NotNull(message = "First name cannot be empty.")
    @Size(min = 1, max = 50, message = "First name length has to be between 1 to 50 characters.")
    protected String firstName;

    @NotNull(message = "Last name cannot be empty.")
    @Size(min = 1, max = 50, message = "Last name length has to be between 1 to 50 characters.")
    protected String lastName;

    @NotNull(message = "Email cannot be empty.")
    @Email(message = "Please provide a valid email",
            regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    protected String email;

    @NotNull(message = "Username cannot be empty")
    @Size(min = 2, message = "Username has to be more that 2 characters")
    protected String username;

    @NotNull(message = "Password cannot be empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    protected LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    protected LocalDateTime modifiedAt;
}
