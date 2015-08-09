/*
 * This code is licensed under the Reciprocal Public License 1.5 (RPL1.5)http://www.opensource.org/licenses/rpl1.5
 * 
 * Copyright (c) 2012 Gotham Digital Science, LLC -- All Rights Reserved
 *
 */


package com.gdssecurity.pmd.rules;




import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.StringMultiProperty;

import com.gdssecurity.pmd.SecurityRuleViolation;
import com.gdssecurity.pmd.Utils;


public class BaseSecurityRule extends AbstractJavaRule {

	protected Set<String> sources = null;	
    protected Set<String> unsafeTypes = null;
    protected Set<String> safeTypes = null;
	
    private final PropertyDescriptor<String[]> sourceDescriptor = new StringMultiProperty(
            "sources", "TODO",
            new String[] {
            "javax.servlet.http.HttpServletRequest.getParameter" }, 1.0f, '|');
    
    private final PropertyDescriptor<String[]> unsafeTypesDescriptor = new StringMultiProperty(
            "unsafeTypes",
            "types that could create a potential SQLi exposure when concatenated to a SQL statement",
            new String[] {
            		"java.lang.String", 
            		"java.lang.StringBuilder", 
            		"java.lang.StringBuffer"
            }, 
            1.0f,
            '|');
    

    // Ignoring Numeric types by default
    private final PropertyDescriptor<String[]> safeTypesDescriptor = new StringMultiProperty(
            "safeTypes",
            "types that may be considered safe to ignore.",
            new String[] { 
            	"java.lang.Byte",
            	"java.lang.Short",            	
            	"java.lang.Integer", 
            	"java.lang.Long",
            	"byte",
            	"short",
            	"int",
            	"long"
            },
            1.0f, 
            '|');
	
	public BaseSecurityRule() {
		super();
		this.propertyDescriptors.add(this.sourceDescriptor);
    	this.propertyDescriptors.add(this.unsafeTypesDescriptor);
    	this.propertyDescriptors.add(this.safeTypesDescriptor);    	
	}



    protected void init() {
        if (this.sources == null) {
            this.sources = Utils.arrayAsSet(getProperty(this.sourceDescriptor));
        }
        if (this.unsafeTypes == null) {
            this.unsafeTypes = Utils.arrayAsSet(getProperty(this.unsafeTypesDescriptor));
        }

        if (this.safeTypes == null) {
            this.safeTypes = Utils.arrayAsSet(getProperty(this.safeTypesDescriptor));
        }    	
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
    	
        if (rpt.getViolationTree().size() > 0) {
            for (Iterator<RuleViolation> i = rpt.iterator(); i.hasNext();) {
                RuleViolation ruleViolation = i.next();
	    		
                if (ruleViolation instanceof SecurityRuleViolation) {
                    SecurityRuleViolation secRuleViolation = (SecurityRuleViolation) ruleViolation;	    		
			        	
                    if (rule.getName().equals(secRuleViolation.getRule().getName())
                            && ctx.getSourceCodeFilename().equals(secRuleViolation.getJavaFileName())
                            && simpleNode.getBeginLine() == secRuleViolation.getJavaBeginLine()
                            && simpleNode.getEndLine()  == secRuleViolation.getJavaEndLine()) {
                        isNewSecurityViolation = false;
                    }
                }
            }   
        }
    	
        if (isNewSecurityViolation) {            
            rpt.addRuleViolation(new SecurityRuleViolation(rule, ctx, simpleNode, message, variableName));
        } 	
    }
   
    
}
