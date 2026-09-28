package cn.iocoder.yudao.module.system.controller.app.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - Banner Response VO")
@Data
public class AppBannerRespVO {

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "图片地址")
    private String picUrl;

    @Schema(description = "跳转链接")
    private String url;

    @Schema(description = "展示位置")
    private Integer position;

    @Schema(description = "排序")
    private Integer sort;
}
