package com.revature.ai;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.services.AIService;

import junit.framework.TestCase;

public class AITest extends TestCase {
	
	AIService aiService = new AIService();
	
	@Test
	public void testAI() {
		aiService.getPredictedValue();
	}
	
}
