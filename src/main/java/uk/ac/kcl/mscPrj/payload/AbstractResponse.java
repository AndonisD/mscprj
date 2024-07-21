package uk.ac.kcl.mscPrj.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public abstract class AbstractResponse {
    @JsonIgnore
    HttpStatus httpStatus;
    public abstract HttpStatus getStatus();
}
