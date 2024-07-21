package uk.ac.kcl.mscPrj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
@Data
@AllArgsConstructor
public class GetReportsDTO {
    private Map<String, Integer> reports;
}
