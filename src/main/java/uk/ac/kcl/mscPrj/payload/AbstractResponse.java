package uk.ac.kcl.mscPrj.payload;

import org.springframework.http.HttpStatus;

public abstract class AbstractResponse {
    public abstract HttpStatus getStatus();
}
