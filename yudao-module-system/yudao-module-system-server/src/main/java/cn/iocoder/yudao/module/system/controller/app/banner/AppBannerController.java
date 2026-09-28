package cn.iocoder.yudao.module.system.controller.app.banner;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.app.banner.vo.AppBannerRespVO;
import cn.iocoder.yudao.module.system.service.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - Banner")
@RestController
@RequestMapping("/system/banner")
@Validated
public class AppBannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    @Operation(summary = "按位置获得启用的 Banner 列表")
    @PermitAll
    public CommonResult<List<AppBannerRespVO>> getBannerList(@RequestParam("position") @Positive Integer position) {
        return success(BeanUtils.toBean(bannerService.getEnabledBannerList(position), AppBannerRespVO.class));
    }
}
