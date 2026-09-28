package cn.iocoder.yudao.module.system.service.banner;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.framework.tenant.config.TenantProperties;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.tenant.core.db.TenantDatabaseInterceptor;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.system.controller.admin.banner.vo.*;
import cn.iocoder.yudao.module.system.dal.mysql.banner.BannerMapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.List;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.BANNER_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import({BannerServiceImpl.class, BannerServiceImplTest.Config.class})
class BannerServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BannerService bannerService;
    @Resource
    private BannerMapper bannerMapper;

    @TestConfiguration(proxyBeanMethods = false)
    static class Config {
        @Bean
        @Lazy(false)
        TenantLineInnerInterceptor bannerTenantInterceptor(MybatisPlusInterceptor interceptor) {
            var inner = new TenantLineInnerInterceptor(new TenantDatabaseInterceptor(new TenantProperties()));
            MyBatisUtils.addInterceptor(interceptor, inner, 0);
            return inner;
        }

        @Bean
        static MethodValidationPostProcessor methodValidationPostProcessor() {
            return new MethodValidationPostProcessor();
        }
    }

    @BeforeEach
    void setTenant() {
        TenantContextHolder.setTenantId(1L);
    }

    @AfterEach
    void clearTenant() {
        TenantContextHolder.clear();
    }

    private BannerSaveReqVO request(String title, int position, int sort, int status) {
        return new BannerSaveReqVO().setTitle(title).setPicUrl("https://example.com/banner.png")
                .setUrl("/pages/home").setMemo("内部备注").setPosition(position).setSort(sort).setStatus(status);
    }

    @Test
    void createUpdateDelete() {
        Long id = bannerService.createBanner(request("首页", 1, 0, 0));
        assertEquals(1L, bannerService.getBanner(id).getTenantId());
        var update = BeanUtils.toBean(request("修改", 2, 5, 1), BannerUpdateReqVO.class).setId(id);
        update.setUrl(null);
        update.setMemo(null);
        bannerService.updateBanner(update);
        var saved = bannerService.getBanner(id);
        assertEquals("修改", saved.getTitle());
        assertEquals(2, saved.getPosition());
        assertEquals(5, saved.getSort());
        assertEquals(1, saved.getStatus());
        assertEquals("", saved.getUrl());
        assertEquals("", saved.getMemo());
        bannerService.deleteBanner(id);
        assertNull(bannerService.getBanner(id));
        assertServiceException(() -> bannerService.updateBanner(update), BANNER_NOT_EXISTS);
        assertServiceException(() -> bannerService.deleteBanner(id), BANNER_NOT_EXISTS);
    }

    @Test
    void appFiltersAndSorts() {
        Long last = bannerService.createBanner(request("最后", 1, 9, 0));
        Long first = bannerService.createBanner(request("第一", 1, 1, 0));
        Long newer = bannerService.createBanner(request("同序较新", 1, 1, 0));
        bannerService.createBanner(request("停用", 1, 0, 1));
        bannerService.createBanner(request("其他位置", 2, 0, 0));
        Long deleted = bannerService.createBanner(request("删除", 1, 0, 0));
        bannerService.deleteBanner(deleted);
        assertEquals(List.of(newer, first, last), bannerService.getEnabledBannerList(1)
                .stream().map(b -> b.getId()).toList());
        assertTrue(bannerService.getEnabledBannerList(99).isEmpty());
    }

    @Test
    void pageFiltersAndPaginates() {
        bannerService.createBanner(request("首页一", 1, 3, 0));
        Long first = bannerService.createBanner(request("首页二", 1, 1, 0));
        bannerService.createBanner(request("其他", 1, 0, 0));
        bannerService.createBanner(request("首页停用", 1, 0, 1));
        bannerService.createBanner(request("首页其他位置", 2, 0, 0));
        var req = new BannerPageReqVO().setTitle("首页").setPosition(1).setStatus(0);
        req.setPageSize(1);
        var page = bannerService.getBannerPage(req);
        assertEquals(2L, page.getTotal());
        assertEquals(1, page.getList().size());
        assertEquals(first, page.getList().getFirst().getId());
    }

    @Test
    void tenantCannotReadOrModifyOtherTenant() {
        Long otherId = bannerService.createBanner(request("租户一", 1, 0, 0));
        TenantContextHolder.setTenantId(2L);
        assertNull(bannerService.getBanner(otherId));
        assertTrue(bannerService.getEnabledBannerList(1).isEmpty());
        assertEquals(0L, bannerService.getBannerPage(new BannerPageReqVO()).getTotal());
        var update = BeanUtils.toBean(request("越权修改", 1, 0, 0), BannerUpdateReqVO.class).setId(otherId);
        assertServiceException(() -> bannerService.updateBanner(update), BANNER_NOT_EXISTS);
        assertServiceException(() -> bannerService.deleteBanner(otherId), BANNER_NOT_EXISTS);
        Long ownId = bannerService.createBanner(request("租户二", 1, 0, 0));
        assertEquals(2L, bannerMapper.selectById(ownId).getTenantId());
        TenantContextHolder.setTenantId(1L);
        assertEquals("租户一", bannerService.getBanner(otherId).getTitle());
        assertNull(bannerService.getBanner(ownId));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(ConstraintViolationException.class,
                () -> bannerService.createBanner(request(" ", 1, 0, 0)));
        assertThrows(ConstraintViolationException.class,
                () -> bannerService.createBanner(request("标题", 1, 0, 2)));
        assertThrows(ConstraintViolationException.class,
                () -> bannerService.createBanner(request("标题", 0, 0, 0)));
        assertThrows(ConstraintViolationException.class,
                () -> bannerService.createBanner(request("标题", 1, -1, 0)));
        assertThrows(ConstraintViolationException.class,
                () -> bannerService.createBanner(request("标题", 1, 0, 0).setPicUrl("")));
        assertThrows(ConstraintViolationException.class,
                () -> bannerService.createBanner(request("长".repeat(101), 1, 0, 0)));
        assertThrows(ConstraintViolationException.class,
                () -> bannerService.updateBanner(BeanUtils.toBean(request("标题", 1, 0, 0), BannerUpdateReqVO.class)));
        assertThrows(ConstraintViolationException.class, () -> bannerService.getEnabledBannerList(0));
    }
}
