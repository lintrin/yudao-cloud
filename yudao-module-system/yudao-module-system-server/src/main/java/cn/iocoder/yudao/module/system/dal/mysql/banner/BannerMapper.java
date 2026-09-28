package cn.iocoder.yudao.module.system.dal.mysql.banner;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.banner.vo.BannerPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.banner.BannerDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BannerMapper extends BaseMapperX<BannerDO> {

    default PageResult<BannerDO> selectPage(BannerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BannerDO>()
                .likeIfPresent(BannerDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerDO::getPosition, reqVO.getPosition())
                .eqIfPresent(BannerDO::getStatus, reqVO.getStatus())
                .orderByAsc(BannerDO::getSort).orderByDesc(BannerDO::getId));
    }

    default List<BannerDO> selectEnabledList(Integer position) {
        return selectList(new LambdaQueryWrapperX<BannerDO>()
                .eq(BannerDO::getPosition, position)
                .eq(BannerDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(BannerDO::getSort).orderByDesc(BannerDO::getId));
    }
}
