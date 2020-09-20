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

## 工具  
[H2数据库](http://www.h2database.com/html/main.html)  
  
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