package com.battle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.QuestionWaitDao;

@Service
public class QuestionWaitService {

	@Autowired
	private QuestionWaitDao questionWaitDao;
}
