package cn.iocoder.yudao.module.system.controller.admin.banner.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - Banner 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class BannerPageReqVO extends PageParam {

    @Schema(description = "标题，模糊匹配")
    @Size(max = 100)
    private String title;

    @Schema(description = "展示位置")
    @Positive
    private Integer position;

    @Schema(description = "状态：0 启用，1 停用")
    @InEnum(CommonStatusEnum.class)
    private Integer status;
}
