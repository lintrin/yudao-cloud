package cn.iocoder.yudao.module.system.dal.dataobject.banner;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 租户内的 Banner 配置。 */
@TableName("system_banner")
@KeySequence("system_banner_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class BannerDO extends TenantBaseDO {

    /** 编号 */
    private Long id;

    /** 标题 */
    private String title;

    /** 图片地址 */
    private String picUrl;

    /** 跳转链接 */
    private String url;

    /** 展示位置 */
    private Integer position;

    /** 排序 */
    private Integer sort;

    /** 状态 */
    private Integer status;

    /** 内部备注 */
    private String memo;
}
