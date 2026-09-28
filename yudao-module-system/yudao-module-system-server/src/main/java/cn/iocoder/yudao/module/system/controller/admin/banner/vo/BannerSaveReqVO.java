package cn.iocoder.yudao.module.system.controller.admin.banner.vo;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Schema(description = "管理后台 - Banner 创建/修改字段")
@Data
public class BannerSaveReqVO {

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 100)
    private String title;

    @Schema(description = "图片地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 1024)
    private String picUrl;

    @Schema(description = "跳转链接，可为空；由客户端解析站内路径或链接")
    @Size(max = 1024)
    private String url;

    @Schema(description = "展示位置，1 为首页，其他正整数由接入方约定", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Positive
    private Integer position;

    @Schema(description = "排序，越小越靠前", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @PositiveOrZero
    private Integer sort;

    @Schema(description = "状态：0 启用，1 停用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @Schema(description = "内部备注")
    @Size(max = 500)
    private String memo;
}
