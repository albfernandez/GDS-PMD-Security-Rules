/*
(C) Copyright  2014-2015 Alberto Fernández <infjaf@gmail.com>
(C) Copyright  2012      Gotham Digital Science, LLC -- All Rights Reserved
 
Unless explicitly acquired and licensed from Licensor under another
license, the contents of this file are subject to the Reciprocal Public
License ("RPL") Version 1.5, or subsequent versions as allowed by the RPL,
and You may not copy or use this file in either source code or executable
form, except in compliance with the terms and conditions of the RPL.

All software distributed under the RPL is provided strictly on an "AS
IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND
LICENSOR HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT
LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific
language governing rights and limitations under the RPL. 

This code is licensed under the Reciprocal Public License 1.5 (RPL1.5)
http://www.opensource.org/licenses/rpl1.5

*/


package com.gdssecurity.pmd.rules;




import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.gdssecurity.pmd.SecurityRuleViolation;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;



public class BaseSecurityRule extends AbstractJavaRule {

	protected Set<String> sources = null;	
	protected Set<String> sourceTypes = null;
    protected Set<String> unsafeTypes = null;
    protected Set<String> safeTypes = null;
	
    private final PropertyDescriptor<List<String>> sourceDescriptor = 
    		PropertyFactory.stringListProperty("sources")
    		.delim('|')
    		.desc("sources of posibble dangerous data")
    		.defaultValues(
    			"javax.servlet.ServletRequest.getParameter",
    			"javax.servlet.ServletRequest.getParameterMap",
    			"javax.servlet.ServletRequest.getParameterNames",
    			"javax.servlet.ServletRequest.getParameterValues",
    			"javax.servlet.http.HttpServletRequest.getParameter",
    			"javax.servlet.http.HttpServletRequest.getParameterMap",
    			"javax.servlet.http.HttpServletRequest.getParameterNames",
    			"javax.servlet.http.HttpServletRequest.getParameterValues",
    			"javax.servlet.http.HttpServletRequest.getHeader",
    			"javax.servlet.http.HttpServletRequest.getHeaders",
    			"javax.servlet.http.HttpServletRequest.getHeaderNames"
    		)
    		.build();
    
    private final PropertyDescriptor<List<String>> sourceTypesDescriptor = 
    		PropertyFactory.stringListProperty("sourceTypes")
    		.delim('|')
    		.desc("sources (classes) of posibble dangerous data")
    		.defaultValues(
    			"javax.servlet.ServletRequest",
    			"javax.servlet.http.HttpServletRequest"
    		)
    		.build();
    
   
    private final PropertyDescriptor<List<String>> unsafeTypesDescriptor = 
    		PropertyFactory.stringListProperty("unsafeTypes").delim('|')
    		.defaultValues(
    			"java.lang.String", 
            	"java.lang.StringBuilder", 
            	"java.lang.StringBuffer",
            	"java.lang.AbstractStringBuilder")
    		.desc("types that could create a potential SQLi exposure when concatenated to a SQL statement")
    		.build(); 

    // Ignoring Numeric types by default
    private final PropertyDescriptor<List<String>> safeTypesDescriptor =
    		PropertyFactory.stringListProperty("safeTypes").delim('|')
    		.defaultValues(
    				"java.lang.Byte",
                	"java.lang.Short",            	
                	"java.lang.Integer", 
                	"java.lang.Long",
                	"java.lang.Float",
                	"java.lang.Double",
                	"java.lang.Boolean",
                	"byte",
                	"short",
                	"int",
                	"long",
                	"float",
                	"double",
                	"boolean"
    				)
    		.desc("types that may be considered safe to ignore.")
    		.build();
    
    private boolean initialized = false;
	
	public BaseSecurityRule() {
		super();
		definePropertyDescriptor(this.sourceDescriptor);
		definePropertyDescriptor(this.sourceTypesDescriptor);
    	definePropertyDescriptor(this.unsafeTypesDescriptor);
    	definePropertyDescriptor(this.safeTypesDescriptor);    	
	}



	protected void init() {
		if (!this.initialized) {
			this.sources = getConfig(this.sourceDescriptor);
			this.sourceTypes = getConfig(this.sourceTypesDescriptor);
			this.unsafeTypes = getConfig(this.unsafeTypesDescriptor);
			this.safeTypes = getConfig(this.safeTypesDescriptor);
			this.initialized = true;
		}
	}
	
	protected final Set<String> getConfig(PropertyDescriptor<List<String>> descriptor) {
		Set<String> ret = new HashSet<String>();
		List<String> props = getProperty(descriptor);
		for (String value: props) {
			if (!StringUtils.isBlank(value)) {
				ret.add(value.trim());
			}
		}
		
		return ret;
	}
    
    
    protected boolean isSafeType(ASTType type) {
    	 if (type == null) {
    		 return false;
    	 }
    	 return isSafeType(type.getType());
    }
    protected boolean isSafeType(Class<?> type) {
    	if (type == null) {
    		return false;
    	}
    	if (type.isArray()) {
    		return isSafeType(type.getComponentType().getCanonicalName());
    	}
    	return isSafeType(type.getCanonicalName());
    }
    protected boolean isSafeType(String type) {
    	if (type == null) {
    		return false;
    	}
    	return this.safeTypes.contains(type);
    }

    
    protected boolean isUnsafeType(ASTType type) {
    	if (type == null) {
    		return true;
    	}
    	return isUnsafeType(type.getType());
    }
    protected boolean isUnsafeType(Class<?> type) {
    	if (type == null) {
    		return true;
    	}
    	if (type.isArray()) {
    		return isUnsafeType(type.getComponentType().getCanonicalName());
    	}
    	return isUnsafeType(type.getCanonicalName());
    }
    protected boolean isUnsafeType(String type) {
    	if (type == null) {
    		return true;
    	}
    	return this.unsafeTypes.contains(type);
    }
    
    protected boolean isSource(String type, String method) {
    	return 
    			this.sources.contains(type + "." + method) || 
    			this.sources.contains ("*." + method) || 
    			this.sources.contains(type + ".*");
    }
    
    protected boolean isSource(String type) {
    	return this.sourceTypes.contains(type);
    }
    
    @Override
	public void start(RuleContext ctx) {
    	init();
    	super.start(ctx);
    }
	
	@Override
	public void apply(List<? extends Node> list, RuleContext rulecontext) {
		init();
        super.apply(list, rulecontext);
    }


    
    protected final void addSecurityViolation(Rule rule, RuleContext ctx, Node simpleNode, String message, String variableName) {
        Report rpt = ctx.getReport();       
        boolean isNewSecurityViolation = true;
    	

        for (Iterator<RuleViolation> i = rpt.iterator(); i.hasNext();) {
            RuleViolation ruleViolation = i.next();
    		
            if (ruleViolation != null && ruleViolation.getClass() == SecurityRuleViolation.class) {
                SecurityRuleViolation secRuleViolation = (SecurityRuleViolation) ruleViolation;	    		
		        	
                if (rule.getName().equals(secRuleViolation.getRule().getName())
                        && ctx.getSourceCodeFile().getAbsolutePath().equals(secRuleViolation.getJavaFileName())
                        && simpleNode.getBeginLine() == secRuleViolation.getJavaBeginLine()
                        && simpleNode.getEndLine()  == secRuleViolation.getJavaEndLine()) {
                    isNewSecurityViolation = false;
                }
            }
        }   

    	
        if (isNewSecurityViolation) {            
            rpt.addRuleViolation(new SecurityRuleViolation(rule, ctx, simpleNode, message, variableName));
        } 	
    }
   
    
}
