package run.ut.app.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * TeamsRecruitmentsTags
 * </p>
 *
 * @author wenjie
 * @since 2020-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TeamsRecruitmentsTags 对象", description = "")
@Deprecated
public class TeamsRecruitmentsTags extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long teamRecruitmentId;

    private Integer tagId;

}