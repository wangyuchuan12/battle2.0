package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleMemberRank;

public interface BattleMemberRankDao extends CrudRepository<BattleMemberRank, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleMemberRank findOneByMemberId(String memberId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleMemberRank> findAllByRoomId(String roomId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	Page<BattleMemberRank> findAllByRoomId(String roomId, Pageable pageable);

}
