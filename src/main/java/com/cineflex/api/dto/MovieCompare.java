// com/cineflex/api/dto/PieStatDTO.java
package com.cineflex.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieCompare {
    private String name;   // genre or bucket name
    private Long value;    // count
}
