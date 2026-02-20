package com.idgovern.param;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class TestVo {

    @NotBlank
    private String msg;

    @NotNull(message = "id cannot be null")
    @Max(value = 10, message = "id cannot be greater than 10")
    @Min(value = 0, message = "id is at least greater than or equal to 0")
    private Integer id;

    private List<String> str;
}
