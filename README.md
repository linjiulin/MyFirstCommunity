## 菱格社区  
  
## 资料    
[bilibili视频](https://www.bilibili.com/video/BV1r4411r7au)  
[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content/)  
[Bootsrtap网站](https://v3.bootcss.com/)  
[Bootsrtap 文档](https://v3.bootcss.com/components/?#wells)  
[GitHub OAuth 文档](https://docs.github.com/en/developers/apps/creating-an-oauth-app)  
[Mybatis 文档](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)    
[Spring database](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)  
[bootstrap按钮下拉菜单](https://v3.bootcss.com/components/#btn-dropdowns)  
[thymeleaf文档](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)  
[Spring拦截器](https://docs.spring.io/spring-framework/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-config-interceptors)

## 工具  
[H2数据库](http://www.h2database.com/html/main.html)  
[LomBok支持](https://projectlombok.org/)  
[spring-boot devtools](https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-devtools.html)  
[JQuery](https://code.jquery.com/jquery-3.5.1.min.js)  
[ApiPost-页面模拟请求插件](https://chrome.google.com/webstore/detail/tabbed-postman-rest-clien/coohjcphdfgbiolnekdpbcijmhambjff)  
[JSONEditOnline-在线编辑JSON](http://jsoneditoronline.cn/)  
[CommentJS-js时间格式转换](http://momentjs.cn/)  
[Markdown编辑器](http://editor.md.ipandao.com/)
## 脚本  
```sql
-- auto-generated definition
create table USER
(
    ID           INT auto_increment
        primary key,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    BIO          VARCHAR(256),
    AVATAR       VARCHAR(100)
);


```    

```sql
-- auto-generated definition
create table DISCUSSION
(
    ID            INT auto_increment
        primary key,
    TITLE         VARCHAR(50),
    DESCRIPTION   TEXT,
    GMT_CREATE    BIGINT,
    GMT_MODIFIED  BIGINT,
    COMMENT_COUNT INT default 0,
    VIEW_COUNT    INT default 0,
    LIKE_COUNT    INT default 0,
    TAG           VARCHAR(256),
    CREATOR       INT
);


```    

```sql
-- auto-generated definition
create table COMMENT
(
    ID            INT auto_increment,
    PARENT_ID     INT not null,
    TYPE          INT not null,
    COMMENTATOR   INT,
    GMT_CREATE    BIGINT,
    GMT_MODIFIED  BIGINT,
    LIKE_COUNT    BIGINT default 0,
    CONTENT       VARCHAR(1024),
    COMMENT_COUNT INT    default 0,
    constraint COMMENT_PK
        primary key (ID)
);


```
```sql
-- auto-generated definition
create table NOTIFICATION
(
    ID            INT auto_increment
        primary key,
    NOTIFIER      INT           not null,
    RECEIVER      INT           not null,
    OUTER_ID      INT           not null,
    TYPE          INT           not null,
    GMT_CREATE    BIGINT        not null,
    STATUS        INT default 0 not null,
    NOTIFIER_NAME VARCHAR(50)   not null,
    OUTER_TITLE   VARCHAR(50)   not null
);


```
```bash  
mvn flyway:migrate
```