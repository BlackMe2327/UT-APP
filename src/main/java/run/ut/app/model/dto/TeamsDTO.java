package run.ut.app.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import run.ut.app.model.domain.Teams;
import run.ut.app.model.dto.base.OutputConverter;
import run.ut.app.model.enums.TeamsStatusEnum;

/**
 * <p>
 * TeamsDTO
 * </p>
 *
 * @author wenjie
 * @since 2020-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="TeamsDTO对象", description="")
public class TeamsDTO extends BaseDTO implements OutputConverter<TeamsDTO, Teams> {

    private Long id;

    private String name;

    private String logo;

    @ApiModelProperty(value = "团队描述，关于团队表，暂时不额外附加经历表了，经历也都写在这")
    private String description;

    private String tagIds;

    @ApiModelProperty(value = "0-不招人 1-招募中")
    private TeamsStatusEnum status;

}
