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
[ApiPost](edge://extensions/?id=lbapjkpcenjdeddcdgodfaljhkphfmjp)  
[JSONEditOnline](http://jsoneditoronline.cn/)  
[CommentJS](http://momentjs.cn/)
## 脚本  
```sql
    create table USER
    (
        ID           INT auto_increment,
        ACCOUNT_ID   VARCHAR(100),
        NAME         VARCHAR(50),
        TOKEN        CHAR(36),
        GMT_CREATE   BIGINT,
        GMT_MODIFIED BIGINT,
        constraint USER_PK
            primary key (ID)
    );  
```    

```sql
    alter table USER
	add BIO VARCHAR(256);
```    

```sql
   create table discussion
   (
        ID int auto_increment,
        TITLE VARCHAR(50),
        DESCRIPTION TEXT,
        GMT_CREATE BIGINT,
        GMT_MODIFIED BIGINT,
        CREATOR int,
        COMMENT_COUNT int default 0,
        VIEW_COUNT int default 0,
        LIKE_COUNT int default 0,
        TAG VARCHAR(256),
        constraint discussion_pk
            primary key (ID)
   );
```
```sql
    create table comment
    (
        id BIGINT auto_increment,
        parent_id BIGINT not null,
        type int not null,
        commentator int,
        gmt_create BIGINT,
        gmt_modified BIGINT,
        like_count BIGINT default 0,
        constraint comment_pk
            primary key (id)
);
```
```bash  
mvn flyway:migrate
```