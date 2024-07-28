package com.ssafy.alt_tab.redis.repository;

import com.ssafy.alt_tab.redis.entity.BlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
