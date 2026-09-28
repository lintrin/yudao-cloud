package cn.iocoder.yudao.module.system.service.banner;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.banner.BannerDO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public interface BannerService {

    Long createBanner(@NotNull @Valid BannerSaveReqVO reqVO);

    void updateBanner(@NotNull @Valid BannerUpdateReqVO reqVO);

    void deleteBanner(@NotNull @Positive Long id);

    BannerDO getBanner(@NotNull @Positive Long id);

    PageResult<BannerDO> getBannerPage(@NotNull @Valid BannerPageReqVO reqVO);

    List<BannerDO> getEnabledBannerList(@NotNull @Positive Integer position);
}
