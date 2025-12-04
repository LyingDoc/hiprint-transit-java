//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.dromara.pdf.pdfbox.core.ext.convertor.html;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.impl.driver.Driver;
import com.microsoft.playwright.options.Margin;
import com.microsoft.playwright.options.ScreenshotType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import io.netty.util.concurrent.FastThreadLocal;
import lombok.Generated;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dromara.pdf.pdfbox.core.base.Document;
import org.dromara.pdf.pdfbox.core.base.PageSize;
import org.dromara.pdf.pdfbox.core.component.Image;
import org.dromara.pdf.pdfbox.core.enums.HorizontalAlignment;
import org.dromara.pdf.pdfbox.core.enums.VerticalAlignment;
import org.dromara.pdf.pdfbox.core.ext.convertor.AbstractConvertor;
import org.dromara.pdf.pdfbox.handler.PdfHandler;
import org.dromara.pdf.pdfbox.support.Constants;
import org.dromara.pdf.pdfbox.util.IdUtil;
import org.dromara.pdf.pdfbox.util.ImageUtil;
import org.dromara.pdf.pdfbox.util.UnitUtil;

public class HtmlConvertor extends AbstractConvertor {
    protected static final FastThreadLocal<Page> THREAD_LOCAL;
    protected static final ThreadPoolExecutor POOL;
    protected Integer dpi;
    protected PageSize pageSize;
    protected Long requestTimeout;
    protected PageLoadState pageState;
    protected Float marginTop;
    protected Float marginBottom;
    protected Float marginLeft;
    protected Float marginRight;
    protected Float scale;
    protected Boolean isLandscape;
    protected Boolean isIncludeBackground;

    public HtmlConvertor(Document document) {
        super(document);
    }

    public void setMargin(float margin) {
        this.marginTop = margin;
        this.marginBottom = margin;
        this.marginLeft = margin;
        this.marginRight = margin;
    }

    static {
        initDriver();
        THREAD_LOCAL = new FastThreadLocal<>();
        POOL = HtmlConvertor.DefaultThreadPool.createPool();
    }
    public Document toPdf(File file) {
        return this.toPdf(file.getAbsolutePath());
    }

    public Document toPdf(String url) {
        this.init();
        return this.convertToPdf(url);
    }

    protected static void initDriver() {
        try {
            Log log = LogFactory.getLog(HtmlConvertor.class);
            if (log.isInfoEnabled()) {
                log.info("Initializing browser driver...");
            }

            Driver driver = Driver.ensureDriverInstalled(Collections.emptyMap(), false);
            if (Objects.isNull(System.getProperty("x-easypdf.playwright.url"))) {
                if (log.isInfoEnabled()) {
                    log.info("Checking and install chromium browser...");
                }

                ProcessBuilder pb = driver.createProcessBuilder();
                pb.command().addAll(Arrays.asList("install", "chromium", "--with-deps", "--no-shell"));
                String version = Playwright.class.getPackage().getImplementationVersion();
                if (version != null) {
                    pb.environment().put("PW_CLI_DISPLAY_VERSION", version);
                }

                pb.inheritIO();
                Process process = pb.start();
                process.waitFor();
            } else if (log.isInfoEnabled()) {
                log.info("Skipped chromium browser check because remote playwright is enabled...");
            }

            System.setProperty("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "true");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] toPdfBytes(File file) {
        return this.toPdfBytes(file.getAbsolutePath());
    }

    public byte[] toPdfBytes(String url) {
        this.init();
        return this.convertToPdfBytes(url);
    }

    public Document toPdfWithImage(File file) {
        return this.toPdfWithImage(file.getAbsolutePath());
    }

    public Document toPdfWithImage(String url) {
        this.init();
        BufferedImage bufferedImage = this.convertToImage(url);
        return this.imageToPdf(bufferedImage);
    }

    public BufferedImage toImage(File file) {
        return this.toImage(file.getAbsolutePath());
    }

    public BufferedImage toImage(String url) {
        this.init();
        return this.convertToImage(url);
    }

    public byte[] toImageBytes(File file) {
        return this.toImageBytes(file.getAbsolutePath());
    }

    public byte[] toImageBytes(String url) {
        this.init();
        return this.convertToImageBytes(url);
    }

    protected void init() {
        if (Objects.isNull(this.dpi)) {
            this.dpi = 96;
        }

        if (Objects.isNull(this.pageSize)) {
            this.pageSize = PageSize.A4;
        }

        if (Objects.isNull(this.isIncludeBackground)) {
            this.isIncludeBackground = true;
        }

        if (Objects.isNull(this.requestTimeout)) {
            this.requestTimeout = 60000L;
        }

        if (Objects.isNull(this.pageState)) {
            this.pageState = PageLoadState.DOMCONTENTLOADED;
        }

        if (Objects.isNull(this.marginTop)) {
            this.marginTop = 0.0F;
        }

        if (Objects.isNull(this.marginBottom)) {
            this.marginBottom = 0.0F;
        }

        if (Objects.isNull(this.marginLeft)) {
            this.marginLeft = 0.0F;
        }

        if (Objects.isNull(this.marginRight)) {
            this.marginRight = 0.0F;
        }

        if (Objects.isNull(this.scale)) {
            this.scale = 1.0F;
        }

        if (Objects.isNull(this.isLandscape)) {
            this.isLandscape = Boolean.FALSE;
        }

    }

    protected Document convertToPdf(String url) {
        return PdfHandler.getDocumentHandler().load(this.convertToPdfBytes(url));
    }

    public String toHtml(String url, String domId) {
        this.init();
        return StringUtils.toEncodedString(this.convert(url, (page) -> page.querySelector(domId).innerHTML().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    protected BufferedImage convertToImage(String url) {
        return ImageUtil.read(this.convertToImageBytes(url));
    }

    @SneakyThrows
    public Document toPdfWithContent(String htmlContent) {
        this.init();
        Path path = Paths.get(Constants.TEMP_FILE_PATH, String.join(".", IdUtil.get(), "html"));
        Files.write(path, htmlContent.getBytes());

        Document var3;
        try {
            var3 = this.convertToPdf(path.toAbsolutePath().toString());
        } finally {
            Files.deleteIfExists(path);
        }
        return var3;
    }

    protected byte[] convertToPdfBytes(String url) {
        Page.PdfOptions options = (new Page.PdfOptions())
                .setWidth(UnitUtil.pt2px(this.dpi, this.pageSize.getWidth()) + "px")
                .setHeight(UnitUtil.pt2px(this.dpi, this.pageSize.getHeight()) + "px")
                .setPrintBackground(this.isIncludeBackground)
                .setOutline(true)
                .setScale((double) this.scale)
                .setLandscape(this.isLandscape)
                .setMargin((new Margin())
                        .setLeft(this.marginLeft + "px")
                        .setRight(this.marginRight + "px")
                        .setTop(this.marginTop + "px")
                        .setBottom(this.marginBottom + "px"));
        return this.convert(url, (page) -> page.pdf(options));
    }

    protected Document imageToPdf(BufferedImage sourceImage) {
        Document document = PdfHandler.getDocumentHandler().create();
        document.setMarginTop(this.marginTop);
        document.setMarginBottom(this.marginBottom);
        document.setMarginLeft(this.marginLeft);
        document.setMarginRight(this.marginRight);
        org.dromara.pdf.pdfbox.core.base.Page page = new org.dromara.pdf.pdfbox.core.base.Page(document);
        float scale = page.getWithoutMarginWidth() / (float)sourceImage.getWidth();

        for(BufferedImage image : ImageUtil.splitForVertical(sourceImage, (int)(page.getWithoutMarginHeight() / scale))) {
            Image component = new Image(document.getCurrentPage());
            component.setImage(ImageUtil.scale(image, image.getWidth() * 2, image.getHeight() * 2, 1));
            component.setScale(scale * this.scale / 2.0F);
            component.setHorizontalAlignment(HorizontalAlignment.CENTER);
            component.setVerticalAlignment(VerticalAlignment.CENTER);
            component.render();
        }

        document.appendPage(page);
        return document;
    }

    protected Page getBrowserPage() {
        Page page = THREAD_LOCAL.get();
        if (Objects.isNull(page)) {
            page = this.initBrowserPage();
        }
        return page;
    }

    protected byte[] convertToImageBytes(String url) {
        return this.convert(url, (page) -> page.screenshot((new Page.ScreenshotOptions())
                .setType(ScreenshotType.PNG)
                .setFullPage(true)
                .setOmitBackground(false)));
    }

    @SneakyThrows
    protected byte[] convert(String url, Function<Page, byte[]> function) {
        return POOL.submit(() -> {
            long begin = 0L;
            long end;
            Page page = this.getBrowserPage();
            String navigateUrl = this.getNavigateUrl(url);
            if (this.log.isInfoEnabled()) {
                begin = System.currentTimeMillis();
                this.log.info("Loading page: " + navigateUrl);
            }

            page.navigate(navigateUrl);
            page.waitForLoadState(this.pageState.getState(), (new Page.WaitForLoadStateOptions())
                    .setTimeout((double) this.requestTimeout));
            page.evaluate("document.fonts.ready.then(() => { window.isFontLoaded = true; });");
            page.waitForFunction("window.isFontLoaded === true");
            if (this.log.isInfoEnabled()) {
                end = System.currentTimeMillis();
                this.log.info("Loaded page: " + (end - begin) + " ms");
                begin = end;
                this.log.info("Converting page...");
            }

            byte[] bytes = function.apply(page);
            if (this.log.isInfoEnabled()) {
                end = System.currentTimeMillis();
                this.log.info("Converted page: " + (end - begin) + " ms");
            }
            return bytes;
        }).get(5L, TimeUnit.MINUTES);
    }

    protected Page initBrowserPage() {
        long begin = 0L;
        long end;
        if (this.log.isInfoEnabled()) {
            begin = System.currentTimeMillis();
            this.log.info("Initializing browser...");
        }

        String remoteUrl = System.getProperty("x-easypdf.playwright.url");
        Playwright playwright = Playwright.create();
        Browser browser;
        if (Objects.isNull(remoteUrl)) {
            browser = playwright.chromium().launch((new BrowserType.LaunchOptions()).setChannel("chromium"));
        } else {
            browser = playwright.chromium().connect("ws://" + remoteUrl);
        }

        if (this.log.isInfoEnabled()) {
            end = System.currentTimeMillis();
            this.log.info("Initialized browser: " + (end - begin) + " ms");
            begin = end;
            this.log.info("Initializing page...");
        }

        Page newPage = browser.newPage();
        THREAD_LOCAL.set(newPage);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            THREAD_LOCAL.remove();
            playwright.close();
            this.log.info("Close browser successfully");
        }, "HtmlConvertor-ShutdownHook"));
        if (this.log.isInfoEnabled()) {
            end = System.currentTimeMillis();
            this.log.info("Initialized page: " + (end - begin) + " ms");
        }
        return newPage;
    }

    @Generated
    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    @Generated
    public void setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
    }

    @Generated
    public void setRequestTimeout(Long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    @Generated
    public void setPageState(PageLoadState pageState) {
        this.pageState = pageState;
    }

    @Generated
    public void setMarginTop(Float marginTop) {
        this.marginTop = marginTop;
    }

    @Generated
    public void setMarginBottom(Float marginBottom) {
        this.marginBottom = marginBottom;
    }

    @Generated
    public void setMarginLeft(Float marginLeft) {
        this.marginLeft = marginLeft;
    }

    @Generated
    public void setMarginRight(Float marginRight) {
        this.marginRight = marginRight;
    }

    @Generated
    public void setScale(Float scale) {
        this.scale = scale;
    }

    @Generated
    public void setIsLandscape(Boolean isLandscape) {
        this.isLandscape = isLandscape;
    }

    @Generated
    public void setIsIncludeBackground(Boolean isIncludeBackground) {
        this.isIncludeBackground = isIncludeBackground;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof HtmlConvertor)) {
            return false;
        } else {
            HtmlConvertor other = (HtmlConvertor)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$dpi = this.dpi;
                Object other$dpi = other.dpi;
                if (this$dpi == null) {
                    if (other$dpi != null) {
                        return false;
                    }
                } else if (!this$dpi.equals(other$dpi)) {
                    return false;
                }

                Object this$requestTimeout = this.requestTimeout;
                Object other$requestTimeout = other.requestTimeout;
                if (this$requestTimeout == null) {
                    if (other$requestTimeout != null) {
                        return false;
                    }
                } else if (!this$requestTimeout.equals(other$requestTimeout)) {
                    return false;
                }

                Object this$marginTop = this.marginTop;
                Object other$marginTop = other.marginTop;
                if (this$marginTop == null) {
                    if (other$marginTop != null) {
                        return false;
                    }
                } else if (!this$marginTop.equals(other$marginTop)) {
                    return false;
                }

                Object this$marginBottom = this.marginBottom;
                Object other$marginBottom = other.marginBottom;
                if (this$marginBottom == null) {
                    if (other$marginBottom != null) {
                        return false;
                    }
                } else if (!this$marginBottom.equals(other$marginBottom)) {
                    return false;
                }

                Object this$marginLeft = this.marginLeft;
                Object other$marginLeft = other.marginLeft;
                if (this$marginLeft == null) {
                    if (other$marginLeft != null) {
                        return false;
                    }
                } else if (!this$marginLeft.equals(other$marginLeft)) {
                    return false;
                }

                Object this$marginRight = this.marginRight;
                Object other$marginRight = other.marginRight;
                if (this$marginRight == null) {
                    if (other$marginRight != null) {
                        return false;
                    }
                } else if (!this$marginRight.equals(other$marginRight)) {
                    return false;
                }

                Object this$scale = this.scale;
                Object other$scale = other.scale;
                if (this$scale == null) {
                    if (other$scale != null) {
                        return false;
                    }
                } else if (!this$scale.equals(other$scale)) {
                    return false;
                }

                Object this$isLandscape = this.isLandscape;
                Object other$isLandscape = other.isLandscape;
                if (this$isLandscape == null) {
                    if (other$isLandscape != null) {
                        return false;
                    }
                } else if (!this$isLandscape.equals(other$isLandscape)) {
                    return false;
                }

                Object this$isIncludeBackground = this.isIncludeBackground;
                Object other$isIncludeBackground = other.isIncludeBackground;
                if (this$isIncludeBackground == null) {
                    if (other$isIncludeBackground != null) {
                        return false;
                    }
                } else if (!this$isIncludeBackground.equals(other$isIncludeBackground)) {
                    return false;
                }

                Object this$pageSize = this.pageSize;
                Object other$pageSize = other.pageSize;
                if (this$pageSize == null) {
                    if (other$pageSize != null) {
                        return false;
                    }
                } else if (!this$pageSize.equals(other$pageSize)) {
                    return false;
                }

                Object this$pageState = this.pageState;
                Object other$pageState = other.pageState;
                if (this$pageState == null) {
                    return other$pageState == null;
                } else return this$pageState.equals(other$pageState);
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof HtmlConvertor;
    }

    @Generated
    public int hashCode() {
        int result = super.hashCode();
        Object $dpi = this.dpi;
        result = result * 59 + ($dpi == null ? 43 : $dpi.hashCode());
        Object $requestTimeout = this.requestTimeout;
        result = result * 59 + ($requestTimeout == null ? 43 : $requestTimeout.hashCode());
        Object $marginTop = this.marginTop;
        result = result * 59 + ($marginTop == null ? 43 : $marginTop.hashCode());
        Object $marginBottom = this.marginBottom;
        result = result * 59 + ($marginBottom == null ? 43 : $marginBottom.hashCode());
        Object $marginLeft = this.marginLeft;
        result = result * 59 + ($marginLeft == null ? 43 : $marginLeft.hashCode());
        Object $marginRight = this.marginRight;
        result = result * 59 + ($marginRight == null ? 43 : $marginRight.hashCode());
        Object $scale = this.scale;
        result = result * 59 + ($scale == null ? 43 : $scale.hashCode());
        Object $isLandscape = this.isLandscape;
        result = result * 59 + ($isLandscape == null ? 43 : $isLandscape.hashCode());
        Object $isIncludeBackground = this.isIncludeBackground;
        result = result * 59 + ($isIncludeBackground == null ? 43 : $isIncludeBackground.hashCode());
        Object $pageSize = this.pageSize;
        result = result * 59 + ($pageSize == null ? 43 : $pageSize.hashCode());
        Object $pageState = this.pageState;
        result = result * 59 + ($pageState == null ? 43 : $pageState.hashCode());
        return result;
    }

    protected String getNavigateUrl(String url) {
        try {
            return (new URL(url)).toString();
        } catch (MalformedURLException var3) {
            return Paths.get(url).toUri().toString();
        }
    }

    protected static class DefaultThreadPool {
        protected static ThreadPoolExecutor createPool() {
            String coreSize = System.getProperty("x-easypdf.thread.core.size", "1");
            String maxSize = System.getProperty("x-easypdf.thread.max.size", "1");
            String keepAliveTime = System.getProperty("x-easypdf.thread.keep.alive.time", "60");
            String queueSize = System.getProperty("x-easypdf.thread.queue.size", "2000");
            return new ThreadPoolExecutor(Integer.parseInt(coreSize),
                    Integer.parseInt(maxSize),
                    Integer.parseInt(keepAliveTime),
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(Integer.parseInt(queueSize)),
                    new DefaultThreadFactory(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    protected static class DefaultThreadFactory implements ThreadFactory {
        private final ThreadGroup group = Thread.currentThread().getThreadGroup();
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        DefaultThreadFactory() {
        }

        public Thread newThread(Runnable r) {
            String namePrefix = "playwrightPool-thread-";
            Thread t = new DefaultThread(this.group,
                    r,
                    namePrefix + this.threadNumber.getAndIncrement(),
                    0L);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }

            if (t.getPriority() != 5) {
                t.setPriority(5);
            }

            return t;
        }
    }

    protected static class DefaultThread extends Thread {
        public DefaultThread(ThreadGroup group,
                             Runnable target,
                             String name,
                             long stackSize) {
            super(group, target, name, stackSize);
        }
    }
}
