package run.ut.app.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import run.ut.app.model.domain.BaseEntity;
import run.ut.app.model.domain.TeamsMembers;
import run.ut.app.model.dto.base.OutputConverter;

/**
 * <p>
 * TeamsMembersDTO
 * </p>
 *
 * @author wenjie
 * @since 2020-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="TeamsMembersDTO 对象", description="")
public class TeamsMembersDTO extends BaseDTO implements OutputConverter<TeamsMembersDTO, TeamsMembers> {

    private Long id;

    private Long teamId;

    private Long uid;

    @ApiModelProperty(value = "0-队员 1-队长")
    private Integer isLeader;

}