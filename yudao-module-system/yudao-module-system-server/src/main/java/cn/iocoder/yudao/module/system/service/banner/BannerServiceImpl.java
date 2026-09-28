package cn.iocoder.yudao.module.system.service.banner;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.module.system.dal.mysql.banner.BannerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.BANNER_NOT_EXISTS;

@Service
@Validated
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    @Override
    public Long createBanner(BannerSaveReqVO reqVO) {
        BannerDO banner = toBanner(reqVO);
        // 创建只允许数据库分配主键，租户和审计字段由框架填充。
        banner.setId(null);
        bannerMapper.insert(banner);
        return banner.getId();
    }

    @Override
    public void updateBanner(BannerUpdateReqVO reqVO) {
        BannerDO banner = toBanner(reqVO);
        banner.setId(reqVO.getId());
        if (bannerMapper.updateById(banner) == 0) {
            throw exception(BANNER_NOT_EXISTS);
        }
    }

    @Override
    public void deleteBanner(Long id) {
        if (bannerMapper.deleteById(id) == 0) {
            throw exception(BANNER_NOT_EXISTS);
        }
    }

    @Override
    public BannerDO getBanner(Long id) {
        return bannerMapper.selectById(id);
    }

    @Override
    public PageResult<BannerDO> getBannerPage(BannerPageReqVO reqVO) {
        return bannerMapper.selectPage(reqVO);
    }

    @Override
    public List<BannerDO> getEnabledBannerList(Integer position) {
        return bannerMapper.selectEnabledList(position);
    }

    private BannerDO toBanner(BannerSaveReqVO reqVO) {
        BannerDO banner = BeanUtils.toBean(reqVO, BannerDO.class);
        // 全量修改时，省略可选字段也应清空旧值，避免 MyBatis 跳过 null 字段。
        banner.setUrl(reqVO.getUrl() == null ? "" : reqVO.getUrl());
        banner.setMemo(reqVO.getMemo() == null ? "" : reqVO.getMemo());
        return banner;
    }
}
