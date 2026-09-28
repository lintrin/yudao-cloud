package cn.iocoder.yudao.module.system.controller.admin.banner;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.system.service.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - Banner")
@RestController
@RequestMapping("/system/banner")
@Validated
public class BannerController {

    @Resource
    private BannerService bannerService;

    @PostMapping("/create")
    @Operation(summary = "创建 Banner")
    @PreAuthorize("@ss.hasPermission('system:banner:create')")
    public CommonResult<Long> createBanner(@Valid @RequestBody BannerSaveReqVO reqVO) {
        return success(bannerService.createBanner(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改 Banner，包括启停和排序")
    @PreAuthorize("@ss.hasPermission('system:banner:update')")
    public CommonResult<Boolean> updateBanner(@Valid @RequestBody BannerUpdateReqVO reqVO) {
        bannerService.updateBanner(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 Banner")
    @PreAuthorize("@ss.hasPermission('system:banner:delete')")
    public CommonResult<Boolean> deleteBanner(@RequestParam("id") @Positive Long id) {
        bannerService.deleteBanner(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得 Banner 详情")
    @PreAuthorize("@ss.hasPermission('system:banner:query')")
    public CommonResult<BannerRespVO> getBanner(@RequestParam("id") @Positive Long id) {
        return success(BeanUtils.toBean(bannerService.getBanner(id), BannerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得 Banner 分页")
    @PreAuthorize("@ss.hasPermission('system:banner:query')")
    public CommonResult<PageResult<BannerRespVO>> getBannerPage(@Valid BannerPageReqVO reqVO) {
        return success(BeanUtils.toBean(bannerService.getBannerPage(reqVO), BannerRespVO.class));
    }
}
