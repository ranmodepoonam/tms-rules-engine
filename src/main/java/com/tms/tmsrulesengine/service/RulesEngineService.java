package com.tms.tmsrulesengine.service;

import java.io.File;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.tmsrulesengine.model.Rule;
import com.tms.tmsrulesengine.model.User;


@Service
public class RulesEngineService {

	private List<Rule> rules;

	@PostConstruct
	public void init() throws IOException {
		// Load JSON rules from a file or a database
		ObjectMapper objectMapper = new ObjectMapper();
//		rules = objectMapper.readValue(
//				new File("/tms-rules-engine/src/main/resources/jsonRules.json"),
//				new TypeReference<List<Rule>>() {
//				});
//		System.out.println("Rules : "+rules);
		
		File jsonFile = new ClassPathResource("jsonRules.json").getFile();
		rules = objectMapper.readValue(jsonFile, new TypeReference<List<Rule>>() {});
		System.out.println("Rules: " + rules);
		 // Sort the rules based on priority in descending order
        Collections.sort(rules, Comparator.comparingInt(Rule::getPriority).reversed());
        System.out.println("********init()********@postconstruct******************");
	}

	public String executeRules(User data) {
		for (Rule rule : rules) {
			ExpressionParser parser = new SpelExpressionParser();
			StandardEvaluationContext context = new StandardEvaluationContext(data);
			
			System.out.println("context : "+parser.parseExpression(rule.getCondition()).getValue(context, Boolean.class));

			if (parser.parseExpression(rule.getCondition()).getValue(context, Boolean.class)) {
				// Perform the action based on the rule
				System.out.println("***********"+rule.getAction());
				return rule.getAction();
			}
		}

		// Default action if no rule matches
		return "DEFAULT_ACTION";
	}

}
