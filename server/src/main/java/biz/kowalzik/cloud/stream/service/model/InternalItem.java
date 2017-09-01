package biz.kowalzik.cloud.stream.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalItem implements Serializable {

    private Long id;
    private String name;
    private BigDecimal value;
    private double weight;
    private double height;
}
