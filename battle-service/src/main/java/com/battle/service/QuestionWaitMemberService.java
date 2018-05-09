package com.battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.QuestionWaitMemberDao;

@Service
public class QuestionWaitMemberService {

	@Autowired
	private QuestionWaitMemberDao questionWaitMemberDao;
}
