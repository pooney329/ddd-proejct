
create table member
(
    member_idx int unsigned auto_increment
        primary key,
    name       varchar(50)                        not null,
    created_at datetime default CURRENT_TIMESTAMP not null
);

---
create table friend
(
    idx        int unsigned auto_increment
        primary key,
    member_idx int unsigned                       not null,
    friend_idx int unsigned                       not null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    constraint uk_friend
        unique (member_idx, friend_idx)
);

create index idx_friend_friend
    on friend (friend_idx);

create index idx_friend_member
    on friend (member_idx);


---

create table friend_rank
(
    idx        int unsigned auto_increment
        primary key,
    member_idx int unsigned                       not null,
    friend_idx int unsigned                       not null,
    score      int                                not null,
    ranking    int                                not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint friend_rank_pk
        unique (member_idx, friend_idx)
);

create index idx_rank_member
    on friend_rank (member_idx);

create index idx_rank_member_ranking
    on friend_rank (member_idx, ranking);


---


create table score
(
    idx        int unsigned auto_increment
        primary key,
    member_idx bigint unsigned                    not null,
    score      int      default 0                 not null,
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create index idx_score_member
    on score (member_idx);



