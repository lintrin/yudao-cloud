package cn.iocoder.yudao.module.system.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - Banner 修改 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class BannerUpdateReqVO extends BannerSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Positive
    private Long id;
}
