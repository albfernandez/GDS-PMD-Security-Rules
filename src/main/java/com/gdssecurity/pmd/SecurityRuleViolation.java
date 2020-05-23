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

package com.gdssecurity.pmd;


import java.io.File;
import java.util.Comparator;
import java.util.List;

import com.gdssecurity.pmd.smap.SmapFileReader;
import com.gdssecurity.pmd.smap.SmapResolver;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;


public class SecurityRuleViolation implements Comparator<RuleViolation>, RuleViolation {
    
    private Rule rule;
    private String description;
    private String fileName; 

    private String className;
    private String methodName;
    private String variableName;

    private String packageName;
    private int beginLine; 
    private int endLine; 

    private int beginColumn;
    private int endColumn;
    private boolean isSuppressed = false;
	
    private String javaFileName; 
    private int javaBeginLine; 
    private int javaEndLine; 
   
    @Override
	public int compare(RuleViolation r1, RuleViolation r2) {
        if (!r1.getFilename().equals(r2.getFilename())) {
            return r1.getFilename().compareTo(r2.getFilename());
        }

        if (r1.getBeginLine() != r2.getBeginLine()) {
            return r1.getBeginLine() - r2.getBeginLine();
        }

        if (r1.getDescription() != null && r2.getDescription() != null
                && !r1.getDescription().equals(r2.getDescription())) {
            return r1.getDescription().compareTo(r2.getDescription());
        }

        if (r1.getBeginLine() == r2.getBeginLine()) {
            return 1;
        }

        return r1.getBeginLine() - r2.getBeginLine();
    }
    
    	
    public SecurityRuleViolation(Rule rule, RuleContext ctx, Node node, String specificMsg, String variable) {
        this.rule = rule;
        this.javaFileName = ctx.getSourceCodeFilename();
        this.variableName = variable;
       
        if (node != null) {
            if (node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class)
                    == null) {
	            
                this.className = "";
            } else {            	
            	this.className = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class).getSimpleName();
            }
	        
            String qualifiedName = null;
            List<ASTClassOrInterfaceDeclaration> parents = node.getParentsOfType(
                    ASTClassOrInterfaceDeclaration.class);

            for (ASTClassOrInterfaceDeclaration parent : parents) {            	
                if (qualifiedName == null) {
                    qualifiedName = parent.getSimpleName();
                } else {
                    qualifiedName = parent.getSimpleName() + "$" + qualifiedName;
                }
            }
            ASTMethodDeclaration method = node.getFirstParentOfType(ASTMethodDeclaration.class);
            if (method != null) {
            	this.methodName = method.getName();
            }
            ASTCompilationUnit compilationUnit = node.getFirstParentOfType(ASTCompilationUnit.class);
            if (compilationUnit != null && compilationUnit.getPackageDeclaration()!= null){
            	this.packageName = compilationUnit.getPackageDeclaration().getPackageNameImage();
            }


            this.javaBeginLine = node.getBeginLine(); 
            this.javaEndLine = node.getEndLine(); 

            if (this.javaFileName.indexOf("WEB-INF") > 0) {
                int webRootDirPos = this.javaFileName.indexOf("WEB-INF");
                String webRootDirName = this.javaFileName.substring(0,
                        webRootDirPos);
				
                int dot = this.javaFileName.lastIndexOf(".");
                String smapFileName = this.javaFileName.substring(0, dot)
                        + ".class.smap";
                SmapFileReader r = new SmapFileReader(new File(smapFileName));
                SmapResolver resolver = new SmapResolver(r);
				
                this.fileName = webRootDirName
                        + resolver.getJspFileName(this.javaBeginLine, 0); 
                this.beginLine = resolver.unmangle(this.javaBeginLine, 0); 
                this.endLine = resolver.unmangle(this.javaEndLine, 0); 
            } else {
                this.fileName = this.javaFileName;
                this.beginLine = this.javaBeginLine;
                this.endLine = this.javaEndLine;
            }

            if ("".equals(specificMsg)) {
                this.description = "No message for rule violation. Code snippet: "
                        + Utils.getCodeSnippet(this.fileName, this.beginLine, this.endLine);
            } else {
                this.description = specificMsg;
            }
	
            
        } else {
            this.className = "";
            this.methodName = "";
            this.packageName = "";
            this.fileName = "";
        }
    }

    @Override
	public Rule getRule() {
        return this.rule;
    }
	
    @Override
	public boolean isSuppressed() {
        return this.isSuppressed;
    }
	
    @Override
	public int getBeginColumn() {
        return this.beginColumn;
    }
	
    @Override
	public int getEndColumn() {
        return this.endColumn;
    }
	
    @Override
	public String getDescription() {
        return this.description;
    }
	
    @Override
	public String getFilename() {
        return this.fileName;
    }
	
    @Override
	public String getClassName() {
        return this.className;
    }
	
    @Override
	public String getMethodName() {
        return this.methodName;
    }
	
    @Override
	public String getPackageName() {
        return this.packageName;
    }
	
    @Override
	public int getBeginLine() {
        return this.beginLine;
    }
	
    @Override
	public int getEndLine() {
        return this.endLine;
    }
	
    @Override
	public String getVariableName() {
        return this.variableName;
    }
	
    @Override
    public String toString() {
        return getFilename() + ":" + getRule() + ":" + getDescription() + ":"
                + this.beginLine;
    }
	
    public int getJavaBeginLine() {
        return this.javaBeginLine;
    }
	
    public int getJavaEndLine() {
        return this.javaEndLine;
    }
    
    public String getJavaFileName() {
        return this.javaFileName;
    }
    

}
