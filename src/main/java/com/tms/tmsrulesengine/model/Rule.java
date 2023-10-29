package com.tms.tmsrulesengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rule {
	
	private String condition;
    private String action;
    private  int priority;
  
}
