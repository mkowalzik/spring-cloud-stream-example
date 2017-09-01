package biz.kowalzik.cloud.stream.service.apistream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemMessage {

    private Long id;

    @NotNull
    @Pattern(regexp = "[A-Za-z]+")
    private String name;

    private BigDecimal value;

    @Max(10)
    private double weight;

    @Min(100)
    private double height;

}
