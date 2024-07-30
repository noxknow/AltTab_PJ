package com.ssafy.alttab.security.redis.repository;

import com.ssafy.alttab.security.redis.entity.BlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
