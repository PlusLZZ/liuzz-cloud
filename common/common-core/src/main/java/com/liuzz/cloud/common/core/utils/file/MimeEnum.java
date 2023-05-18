package com.liuzz.cloud.common.core.utils.file;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * mime类型大全
 */
@Getter
public enum MimeEnum {
    AAC("aac", "audio/aac"),

    ABW("abw", "application/x-abiword"),

    ARC("arc", "application/x-freearc"),

    AVI("avi", "video/x-msvideo"),

    AZW("azw", "application/vnd.amazon.ebook"),

    BIN("bin", "application/octet-stream"),

    BMP("bmp", "image/bmp"),

    BZ("bz", "application/x-bzip"),

    BZ2("bz2", "application/x-bzip2"),

    CSH("csh", "application/x-csh"),

    CSS("css", "text/css"),

    CSV("csv", "text/csv"),

    DOC("doc", "application/msword"),

    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),

    EOT("eot", "application/vnd.ms-fontobject"),

    EPUB("epub", "application/epub+zip"),

    GZ("gz", "application/gzip"),

    GIF("gif", "image/gif"),

    HTM("htm", "text/html"),

    HTML("html", "text/html"),

    ICO("ico", "image/vnd.microsoft.icon"),

    ICS("ics", "text/calendar"),

    JAR("jar", "application/java-archive"),

    JPEG("jpeg", "image/jpeg"),

    JPG("jpg", "image/jpeg"),

    JS("js", "text/javascript"),

    JSON("json", "application/json"),

    JSONLD("jsonld", "application/ld+json"),

    MID("mid", "audio/midi"),

    MIDI("midi", "audio/midi"),

    MJS("mjs", "text/javascript"),

    MP3("mp3", "audio/mpeg"),

    MPEG("mpeg", "video/mpeg"),

    MPKG("mpkg", "application/vnd.apple.installer+xml"),

    ODP("odp", "application/vnd.oasis.opendocument.presentation"),

    ODS("ods", "application/vnd.oasis.opendocument.spreadsheet"),

    ODT("odt", "application/vnd.oasis.opendocument.text"),

    OGA("oga", "audio/ogg"),

    OGV("ogv", "video/ogg"),

    OGX("ogx", "application/ogg"),

    OPUS("opus", "audio/opus"),

    OTF("otf", "font/otf"),

    PNG("png", "image/png"),

    PDF("pdf", "application/pdf"),

    PHP("php", "application/x-httpd-php"),

    PPT("ppt", "application/vnd.ms-powerpoint"),

    PPTX("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),

    RAR("rar", "application/vnd.rar"),

    RTF("rtf", "application/rtf"),

    SH("sh", "application/x-sh"),

    SVG("svg", "image/svg+xml"),

    SWF("swf", "application/x-shockwave-flash"),

    TAR("tar", "application/x-tar"),

    TIF("tif", "image/tiff"),

    TIFF("tiff", "image/tiff"),

    TS("ts", "video/mp2t"),

    TTF("ttf", "font/ttf"),

    TXT("txt", "text/plain"),

    VSD("vsd", "application/vnd.visio"),

    WAV("wav", "audio/wav"),

    WEBA("weba", "audio/webm"),

    WEBM("webm", "video/webm"),

    WEBP("webp", "image/webp"),

    WOFF("woff", "font/woff"),

    WOFF2("woff2", "font/woff2"),

    XHTML("xhtml", "application/xhtml+xml"),

    XLS("xls", "application/vnd.ms-excel"),

    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),

    XML("xml", "application/xml"),

    XUL("xul", "application/vnd.mozilla.xul+xml"),

    ZIP("zip", "application/zip"),

    MIME_3GP("3gp", "video/3gpp"),

    MIME_3GP_WITHOUT_VIDEO("3gp", "audio/3gpp2"),

    MIME_3G2("3g2", "video/3gpp2"),

    MIME_3G2_WITHOUT_VIDEO("3g2", "audio/3gpp2"),

    MIME_7Z("7z", "application/x-7z-compressed"),
    /**
     * exe使用默认二进制流
     */
    EXE("exe", "application/octet-stream"),
    /**
     * 默认为二进制流
     */
    DEFAULT("default", "application/octet-stream");

    private final String type;

    private final String mime;

    MimeEnum(String type, String mime) {
        this.type = type;
        this.mime = mime;
    }

    /**
     * 根据文件名或者带路径的文件名获取mime
     *
     * @param path 文件名/带路径
     * @return MimeEnum
     */
    public static MimeEnum get(String path) {
        if (StringUtils.hasText(path)) {
            String fileType = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
            for (MimeEnum mime : values()) {
                if (StrUtil.equalsIgnoreCase(fileType, mime.getType())) {
                    return mime;
                }
            }
        }
        return DEFAULT;
    }

}
