package cn.iocoder.yudao.module.system.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - Banner Response VO")
@Data
public class BannerRespVO {

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

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "内部备注")
    private String memo;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
