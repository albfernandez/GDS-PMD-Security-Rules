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

package com.gdssecurity.pmd.rules.dfa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jaxen.JaxenException;

import com.gdssecurity.pmd.TypeUtils;
import com.gdssecurity.pmd.Utils;
import com.gdssecurity.pmd.rules.BaseSecurityRule;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.dfa.DataFlowNode;
import net.sourceforge.pmd.lang.dfa.VariableAccess;
import net.sourceforge.pmd.lang.dfa.pathfinder.CurrentPath;
import net.sourceforge.pmd.lang.dfa.pathfinder.DAAPathFinder;
import net.sourceforge.pmd.lang.dfa.pathfinder.Executable;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTAssignmentOperator;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTConditionalExpression;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTEqualityExpression;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.ASTVariableInitializer;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

public class DfaSecurityRule extends BaseSecurityRule implements Executable {

	private static final String UNKNOWN_TYPE = "UNKNOWN_TYPE";
	private Map<String, String> cacheReturnTypes = new HashMap<String, String>();
	private Set<String> annotatedGenerators = new HashSet<String>();
	private Set<String> annotatedSinks = new HashSet<String>();
	private Set<String> currentPathTaintedVariables;
	private Set<String> methodParametersTainted = new HashSet<String>();
	private Set<String> fieldsTainted = new HashSet<String>();

	private Map<String, Class<?>> fieldTypes;
        private Map<String, Class<?>> fieldTypesAll = new HashMap<>();
	private Map<String, Class<?>> methodParameterTypes = new HashMap<>();
	private Set<String> sinks;
	private Set<String> sanitizers;
	private Set<String> sinkAnnotations;
	private Set<String> generatorAnnotations;
	private Set<String> searchAnnotationsInPackages;

	private String[] searchAnnotationsInPackagesArray;

	private final PropertyDescriptor<List<String>> sinkDescriptor = 
			PropertyFactory.stringListProperty("sinks").delim('|')
			.emptyDefaultValue()
			.desc("List of sinks for potential unsafe data")
			.build();
			
	private final PropertyDescriptor<List<String>> sinkAnnotationsDescriptor = 
			PropertyFactory.stringListProperty("sink-annotations").delim('|')
			.emptyDefaultValue()
			.desc("List of annotattions you can use no annotate sink methods in your code")
			.build();

	private final PropertyDescriptor<List<String>> sanitizerDescriptor = 
			PropertyFactory.stringListProperty("sanitizers").delim('|')
			.emptyDefaultValue()
			.desc("List of methods that accept unsafe data and returns safe data")
			.build();

	private final PropertyDescriptor<List<String>> annotationsPackagesDescriptor = 
			PropertyFactory.stringListProperty("search-annotations-in-packages").delim('|')
			.emptyDefaultValue()
			.desc("TODO")
			.build();
			

	private final PropertyDescriptor<List<String>> generatorAnnotationsDescriptor = 
			PropertyFactory.stringListProperty("generator-annotations").delim('|')
			.emptyDefaultValue()
			.desc("TODO")
			.build();
			
	
	private final PropertyDescriptor<String> maxDataFlowsDescriptor = 
			PropertyFactory.stringProperty("max-dataflows")
			.defaultValue("30")
			.desc("TODO")
    		.build();
	
	private final PropertyDescriptor<String> maxLoopsDescriptor = 
			PropertyFactory.stringProperty("max-loops")
			.defaultValue("500")
			.desc("TODO")
    		.build();

	private RuleContext rc;
	private int methodDataFlowCount;

	private List<DataFlowNode> additionalDataFlowNodes = new ArrayList<DataFlowNode>();

	private int MAX_DATAFLOWS = 30;
	private int MAX_LOOPS = 500;
	private boolean generator = false;
	private boolean initialized = false;
	
	private HashMap<Class<?>, ClassInfo> cacheType = new HashMap<>();

	public DfaSecurityRule() {
		super();
		definePropertyDescriptor(this.sinkDescriptor);
		definePropertyDescriptor(this.sanitizerDescriptor);
		definePropertyDescriptor(this.sinkAnnotationsDescriptor);
		definePropertyDescriptor(this.generatorAnnotationsDescriptor);
		definePropertyDescriptor(this.annotationsPackagesDescriptor);
		definePropertyDescriptor(this.maxDataFlowsDescriptor);
		definePropertyDescriptor(this.maxLoopsDescriptor);
	}

	@Override
	protected void init() {
		super.init();
		if (!this.initialized) {
			init2();
			this.initialized = true;
		}
	}
	private void init2() {
		this.sinks = getConfig(this.sinkDescriptor);
		this.sanitizers = getConfig(this.sanitizerDescriptor);
		this.sinkAnnotations = getConfig(this.sinkAnnotationsDescriptor);
		this.generatorAnnotations = getConfig(this.generatorAnnotationsDescriptor);
		this.searchAnnotationsInPackages = getConfig(this.annotationsPackagesDescriptor);
		this.searchAnnotationsInPackagesArray = this.searchAnnotationsInPackages.toArray(new String[0]);
		try {
			this.MAX_DATAFLOWS = Integer.parseInt(getProperty(this.maxDataFlowsDescriptor));
		}
		catch (Exception e) {
			this.MAX_DATAFLOWS = 30;
		}
		try {
			this.MAX_LOOPS = Integer.parseInt(getProperty(this.maxLoopsDescriptor));
		}
		catch (Exception e) {
			this.MAX_LOOPS = 500;
		}
	}

	@Override
	public void execute(CurrentPath currentPath) {

		this.methodDataFlowCount++;
		if (this.currentPathTaintedVariables == null) {
			this.currentPathTaintedVariables = new HashSet<String>();
			this.currentPathTaintedVariables.addAll(this.fieldsTainted);
			this.currentPathTaintedVariables.addAll(this.methodParametersTainted);
		}

		if (this.methodDataFlowCount < MAX_DATAFLOWS) {
			for (Iterator<DataFlowNode> iterator = currentPath.iterator(); iterator.hasNext();) {
				DataFlowNode iDataFlowNode = iterator.next();
				Node node = iDataFlowNode.getNode();
				if (node != null) {
					Class<?> nodeClass = node.getClass();
					if (nodeClass == ASTMethodDeclaration.class || nodeClass == ASTConstructorDeclaration.class) {
						clearTaintedAndVariablesList();
						this.generator = isGeneratorThisMethodDeclaration(node);
						if (!isSinkThisMethodDeclaration(node)) {
 							addMethodParamsToTaintedVariables(node);
						}
						addClassFieldsToTaintedVariables(node);
						this.currentPathTaintedVariables.addAll(this.fieldsTainted);
						this.currentPathTaintedVariables.addAll(this.methodParametersTainted);
					} else if (nodeClass == ASTVariableDeclarator.class || nodeClass == ASTStatementExpression.class || nodeClass == ASTExpression.class) {
						handleDataFlowNode(iDataFlowNode);
					}
					else if (nodeClass == ASTReturnStatement.class) {
						handeReturnNode(node, iDataFlowNode);
					}
				}
			}

			if (!this.additionalDataFlowNodes.isEmpty()) {
				DataFlowNode additionalRootNode = this.additionalDataFlowNodes.remove(0);

				DAAPathFinder daaPathFinder = new DAAPathFinder(additionalRootNode, this, MAX_DATAFLOWS, MAX_LOOPS);
				this.methodDataFlowCount = 0;
				daaPathFinder.run();
			}

		}
	}

	private void clearTaintedAndVariablesList() {
		this.currentPathTaintedVariables = new HashSet<String>();
		this.methodParameterTypes = new HashMap<String, Class<?>>();
		this.methodParametersTainted = new HashSet<String>();
		this.fieldTypes = new HashMap<String, Class<?>>();
		this.fieldsTainted = new HashSet<String>();
	}

	protected boolean isSanitizerMethod(String type, String method) {
 		return this.sanitizers.contains(type + "." + method);
	}

	private boolean isSink(String objectTypeAndMethod) {
		return this.sinks.contains(objectTypeAndMethod) || this.annotatedSinks.contains(objectTypeAndMethod);
	}
	private boolean isSink(String objectType, String objectMethod) {
		return isSink(objectType + "." + objectMethod);
	}
	private boolean isGenerator(String objectTypeAndMethod) {
		return this.annotatedGenerators.contains(objectTypeAndMethod);
	}
	private boolean isGenerator(String objectType, String objectMethod) {
		return isGenerator(objectType + "." + objectMethod);
	}

	private boolean isGeneratorThisMethodDeclaration(Node node) {
		String seek = getCurrentMethodName(node);
		if (!StringUtils.isBlank(seek)) {
			return isGenerator(seek);
		}

		return false;
	}
	private boolean isSinkThisMethodDeclaration(Node node) {
		String seek = getCurrentMethodName(node);
		if (!StringUtils.isBlank(seek)) {
			return isSink(seek);
		}

		return false;
	}
	
	private String getCurrentMethodName(Node node) {
		Class<?> type = getJavaType(node);
		if (type == null) {
			return "";
		}
		String methodName = UNKNOWN_TYPE;
		if (node.getClass() == ASTMethodDeclaration.class) {
			Node declarator = node.getFirstChildOfType(ASTMethodDeclarator.class);
			if (declarator == null) {
				return "";
			}
			methodName = declarator.getImage();			
			return type.getCanonicalName() + "." + methodName;
		}
		if (node.getClass() == ASTConstructorDeclaration.class) {
			methodName = node.getImage();
			return type.getCanonicalName() + "." + methodName;
		}

		return "";
	}

	private boolean isTaintedVariable(String variable) {
		return this.currentPathTaintedVariables.contains(variable);
	}

	@Override
	public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
		if (node != null && node.getType() != null && !StringUtils.isBlank(node.getType().getCanonicalName())) {
			populateCache(node.getType(),node.getType().getCanonicalName());
		}
		return super.visit(node, data);
	}
	
	@Override
	public Object visit(ASTConstructorDeclaration astConstructorDeclaration, Object data) {
		ASTClassOrInterfaceDeclaration astClass = astConstructorDeclaration
				.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
		if (astClass == null) {
			return data;
		}
		this.rc = (RuleContext) data;
		processReturnStatements(astConstructorDeclaration);
		processThrowsStatements(astConstructorDeclaration);
		runFinder(astConstructorDeclaration);
		return data;

	}

	@Override
	public Object visit(ASTMethodDeclaration astMethodDeclaration, Object data) {

		ASTClassOrInterfaceDeclaration astClass = astMethodDeclaration
				.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
		if (astClass == null) {
			return data;
		}

		this.rc = (RuleContext) data;

		processReturnStatements(astMethodDeclaration);
		processThrowsStatements(astMethodDeclaration);

		runFinder(astMethodDeclaration);

		super.visit(astMethodDeclaration, data);

		return data;
	}

	private void runFinder(Node astMethodDeclaration) {
		DataFlowNode rootDataFlowNode = astMethodDeclaration.getDataFlowNode().getFlow().get(0);

		this.methodDataFlowCount = 0;

		DAAPathFinder daaPathFinder = new DAAPathFinder(rootDataFlowNode, this, MAX_DATAFLOWS, MAX_LOOPS);

		daaPathFinder.run();

	}

	private void processReturnStatements(Node node) {
		processDataFlow(node, "./Block/BlockStatement//TryStatement/CatchStatement//ReturnStatement");
	}

	private void processThrowsStatements(Node node) {
		processDataFlow(node, "./Block/BlockStatement//TryStatement/CatchStatement//ThrowStatement");
	}

	private void processDataFlow(Node node, String xpath) {
		try {

			List<? extends Node> statements = node.findChildNodesWithXPath(xpath);
			if (statements == null || statements.isEmpty()) {
				return;
			}
			int i = 0;
			for (DataFlowNode current : node.getDataFlowNode().getFlow()) {
				for (Node statement : statements) {
					if (current.equals(statement.getDataFlowNode())) {
						DataFlowNode next = node.getDataFlowNode().getFlow().get(i + 1);
						this.additionalDataFlowNodes.add(next);
					}
				}
				i++;
			}

		} catch (JaxenException e) { // NOPMD
			//
		}
	}
	

	private void handeReturnNode(Node node, DataFlowNode iDataFlowNode) {
		
		handleVariableReference(iDataFlowNode);
		if (isTainted(node) && this.generator && !isSafeType(this.getType(node))) {
			addSecurityViolation(this, this.rc, node, getMessage(), "");

		}
		
	}


	
	
	private void addClassFieldsToTaintedVariables(Node node) {



		ASTClassOrInterfaceBody astBody = node.getFirstParentOfType(ASTClassOrInterfaceBody.class);
		if (astBody == null) {
			return;
		}
		
		Class<?> thisClass = getJavaType(astBody);
		if (cacheType.containsKey(thisClass)){
			ClassInfo info = cacheType.get(thisClass);
			this.fieldTypes.putAll(info.fieldTypes);
			this.fieldsTainted.addAll(info.fieldsTainted);
			return;
		}

		List<ASTClassOrInterfaceBodyDeclaration> declarations = astBody
				.findChildrenOfType(ASTClassOrInterfaceBodyDeclaration.class);
		
		for (ASTClassOrInterfaceBodyDeclaration declaration : declarations) {			
			ASTFieldDeclaration field = declaration.getFirstChildOfType(ASTFieldDeclaration.class);
			if (field != null) {
				Class<?> type = field.getType();
				ASTVariableDeclarator declarator = field.getFirstChildOfType(ASTVariableDeclarator.class);
				ASTVariableDeclaratorId name1 = declarator.getFirstChildOfType(ASTVariableDeclaratorId.class);
				if (name1 != null) {
					String name = name1.getName();
					this.fieldTypes.put(name, type);
					if (!field.isFinal() && isUnsafeType(field.getType())) {
						this.fieldsTainted.add("this." + name);
					}
				}
			}
		}
		
		ClassInfo info = new ClassInfo();
		info.fieldTypes.putAll(this.fieldTypes);
		info.fieldsTainted.addAll(this.fieldsTainted);
		cacheType.put(thisClass, info);

	}

	private void addMethodParamsToTaintedVariables(Node node) {

		ASTFormalParameters formalParameters = null;
		if (node == null) {
			return;
		}
			
		if (node.getClass() == ASTMethodDeclaration.class) {
			ASTMethodDeclarator declarator = node.getFirstChildOfType(ASTMethodDeclarator.class);
			formalParameters = declarator.getFirstChildOfType(ASTFormalParameters.class);
		} else if (node.getClass() == ASTConstructorDeclaration.class) {
			formalParameters = node.getFirstChildOfType(ASTFormalParameters.class);
		}
		if (formalParameters == null) {
			return;
		}
		List<ASTFormalParameter> parameters = formalParameters.findChildrenOfType(ASTFormalParameter.class);
		for (ASTFormalParameter parameter : parameters) {
			ASTType type = parameter.getTypeNode();
			ASTVariableDeclaratorId name1 = parameter.getFirstChildOfType(ASTVariableDeclaratorId.class);
			String name = name1.getName();
			if (name != null && type != null) {
				this.methodParameterTypes.put(name, type.getType());
			}
			if (name != null && isUnsafeType(type)) {
				this.methodParametersTainted.add(name);
			}
		}
	}

	private void handleDataFlowNode(DataFlowNode iDataFlowNode) {
		List<VariableAccess> variableAcces = iDataFlowNode.getVariableAccess();
		for (VariableAccess access : variableAcces) {
			if (access.isDefinition()) {
				String variableName = access.getVariableName();
				handleVariableDefinition(iDataFlowNode, variableName);
				//handleVariableReference(iDataFlowNode);
				return;
			}
		}
		handleVariableReference(iDataFlowNode);
	}

	private void handleVariableReference(DataFlowNode iDataFlowNode) {

		Node simpleNode = iDataFlowNode.getNode();
		if (isStringConcatenation(simpleNode)) {			
			handleStringConcatenation(iDataFlowNode, simpleNode);			
		}
		else if (isMethodCall(simpleNode)) {

			topToBottom(simpleNode);
			bottomToTop(simpleNode);
		}
		
	}
	
	private void topToBottom(Node simpleNode) {
		Class<?> type = null;
		String method = "";

		Node astMethod = null;
		if (simpleNode.getFirstDescendantOfType(ASTAssignmentOperator.class) == null) {
			astMethod = simpleNode.getFirstDescendantOfType(ASTPrimaryExpression.class);
		} else {
			astMethod = simpleNode.getFirstDescendantOfType(ASTExpression.class);
		}
		method = getMethod(astMethod);
		if (StringUtils.isBlank(method)) {
			astMethod = astMethod.getFirstDescendantOfType(ASTPrimaryExpression.class);
			method = getMethod(astMethod);
		}
		type = getJavaType(astMethod);

		if (isStringBuilderAppend(type, method)) {
			analizeStringBuilderAppend(simpleNode);
		}

		if (isSink(type, method)) {
			analyzeSinkMethodArgs(simpleNode);
		}
	}
	
	
	private void bottomToTop(Node simpleNode) {
		List<Node> methodCalls = findMethodCalls(simpleNode);
		for (Node n : methodCalls) {
			Class<?> type = null;
			String method = null;
			Node astMethod = null;
			
			int pos = n.getIndexInParent();
			if (pos >= 2) {
				Node previous = n.getParent().getChild(pos -1);
				Node previousPrevious = n.getParent().getChild(pos -2);
				astMethod = previous;
				
				method = astMethod.getImage();
				
				if (previousPrevious instanceof AbstractJavaTypeNode) {
					AbstractJavaTypeNode sf = (AbstractJavaTypeNode) previousPrevious;
					type = sf.getType();
				}
				
				if (isStringBuilderAppend(type, method)) {
					analizeStringBuilderAppend(simpleNode);
				}

				if (isSink(type, method)) {
					analyzeSinkMethodArgs(n);
				}
			}
		}
	}
	
	private List<Node> findMethodCalls(Node node) {
		List<ASTArguments> arguments = node.findDescendantsOfType(ASTArguments.class);
		List<Node> methods = new ArrayList<>(arguments.size());
		for (ASTArguments args: arguments) {
			if (args.size() > 0) {
				methods.add(args.jjtGetParent());
			}
		}
		
		return methods;
	}

	private boolean isStringBuilderAppend(Class<?> type, String methodName) {
		return isStringBuilder(type) && ("insert".equals(methodName) || "append".equals(methodName));
	}

	private boolean isStringBuilder(Class<?> type) {
		if (type == null) {
			return false;
		}
		return
				type == StringBuilder.class || 
				type == StringBuffer.class || 
				"java.lang.AbstractStringBuilder".equals(type.getCanonicalName());		
	}

	private void handleStringConcatenation(DataFlowNode iDataFlowNode, Node simpleNode) {
		Node node = simpleNode.getChild(0);
		if (node.getNumChildren() > 0) {
			node = node.getChild(0);
			if (node.getClass() == ASTPrimaryPrefix.class && node.getNumChildren() > 0) {
				Node name = node.getChild(0);
				if (name.getClass() == ASTName.class) {
					handleVariableDefinition(iDataFlowNode, name.getImage());
				}
			}
		}
	}

	private boolean isStringConcatenation(Node simpleNode) {
		if(simpleNode.getNumChildren() > 2) {
			Node asign = simpleNode.getChild(1);
			if (asign.getClass() == ASTAssignmentOperator.class) {
				return "+=".equals(asign.getImage());
			}
		}
		return false;
	}

	private boolean isSink(Class<?> type, String methodName) {
		if (type == null) {
			return false;
		}
		return isSink(type.getCanonicalName(), methodName);

	}

	private boolean analizeTypeWithReflectionForAnnotations(Class<?> type) {
		if (this.searchAnnotationsInPackagesArray.length == 0) {
			return false;
		}
		if (type == null || type.getPackage() == null) {
			return false;
		}
		String packageName = type.getPackage().getName();
		return StringUtils.startsWithAny(packageName, this.searchAnnotationsInPackagesArray);
	}

	private void analizeStringBuilderAppend(Node simpleNode) {
		ASTName name = simpleNode.getFirstDescendantOfType(ASTName.class);
		if (name == null) {
			return;
		}

		String varName = getVarName(name);

		if (this.isTaintedVariable(varName)) {
			return;
		}
		if (isTainted(simpleNode)) {
			this.currentPathTaintedVariables.add(varName);
		}

	}

	private void analyzeSinkMethodArgs(Node simpleNode) {
		if (isAnyArgumentTainted(simpleNode)) {
			addSecurityViolation(this, this.rc, simpleNode, getMessage(), "");
		}

	}

	private boolean isAnyArgumentTainted(Node simpleNode) {
		ASTArgumentList argListNode = simpleNode.getFirstDescendantOfType(ASTArgumentList.class);
		if (argListNode != null) {
			if (isSanitized(argListNode)) {
				return false;
			}
			int numChildren = argListNode.getNumChildren();
			for (int i = 0; i < numChildren; i++) {
				Node argument = argListNode.getChild(i);
				if (isTainted(argument)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSanitized(ASTArgumentList argListNode) {
		try {
			ASTArguments arguments = (ASTArguments) argListNode.getParent();
			ASTPrimarySuffix suffix = (ASTPrimarySuffix) arguments.getParent();
			ASTPrimaryExpression exp = (ASTPrimaryExpression) suffix.getParent();
			String method = getMethod(exp);
			Class<?> type = getJavaType(exp);
			String typeName = type.getName();
			if (isSink(typeName, method)) {
				return false;
			}
			if (isSanitizerMethod(typeName, method) || isSafeType(getReturnType(exp, typeName, method))) {
				return true;
			}
		}
		catch (Exception e) {
			return false;
		}
		return false;
	}

	private boolean isMethodCall(Node node) {
		ASTArguments arguments = node.getFirstDescendantOfType(ASTArguments.class);
		return arguments != null;
	}

	private void handleVariableDefinition(DataFlowNode iDataFlowNode, String variable) {
		Node simpleNode = iDataFlowNode.getNode();
		Class<?> clazz = String.class;

		Node primaryExpression = simpleNode.getChild(0);
		if (primaryExpression != null) {
			if (primaryExpression.getClass() == ASTPrimaryExpression.class) {
				Node primaryPrefix = primaryExpression.getChild(0);
				if (primaryPrefix != null && primaryPrefix.getClass() == ASTPrimaryPrefix.class) {
					clazz = ((ASTPrimaryPrefix) primaryPrefix).getType();
				}
			}
			if (primaryExpression.getClass() == ASTVariableDeclaratorId.class && simpleNode.getNumChildren() > 1) {
				Node initializer = simpleNode.getChild(1);
				if (initializer != null && initializer.getClass() == ASTVariableInitializer.class) {
					clazz = ((ASTVariableDeclaratorId) primaryExpression).getType();
				}
			}
		}
		// dando la vuelta se optimizaría, pero no se puede ya que nos saltariamos las validaciones
		// de los posibles sinks que hubiese en la parte derecha de la expresion 
		if (isTainted(simpleNode) && isUnsafeType(clazz)) {
			this.currentPathTaintedVariables.add(variable);
		}
		List<ASTArguments> arguments = simpleNode.findDescendantsOfType(ASTArguments.class);
		for (ASTArguments a: arguments) {
			checkMethod(a.getParent().getParent());
		}
		
	}
	
	private void checkMethod(Node node) {
		String method = getMethod(node);
		if(StringUtils.isBlank(method)) {
			// It's a method call but cannot determinate method name, strange
			return;
		}
		String type = getType(node);
		
		if (isSink(type, method)) {
			analyzeSinkMethodArgs(node);
		} 
		
	}

	private boolean isTainted(Node node2) {
		List<ASTPrimaryExpression> primaryExpressions = getExp(node2);
		boolean tainted = false;
   		for (ASTPrimaryExpression node : primaryExpressions) {
			if (node.getNumChildren() == 1 && node.getChild(0).getNumChildren() == 1 && node.getChild(0).getChild(0).getClass() == ASTLiteral.class){
				continue;
			}
			if (node.getNumChildren() == 1 && node.getChild(0).getNumChildren() == 1 && node.getChild(0).getChild(0).getClass() == ASTExpression.class){
				boolean t1 = isTainted(node);
				tainted = tainted || t1;
				continue;
			}
			if (node.getParent().getClass() == ASTConditionalExpression.class && node.getParent().getChild(0) == node) {
				isTainted(node);
				continue;
			}
			if (node.getParent().getClass() == ASTEqualityExpression.class) {
				isTainted(node);
				continue;
			}

			if (isMethodCall(node)) {
				String method = getMethod(node);
				if(StringUtils.isBlank(method)) {
					// It's a method call but cannot determinate method name, strange
					continue;
				}
				String type = getType(node);
				
				if (isSink(type, method)) {
					analyzeSinkMethodArgs(node);
				} 
				if (isSanitizerMethod(type, method) || isGenerator(type, method) || isSafeType(getReturnType(node, type, method))) {
					continue;
				} 
				else if (isSource(type, method) || isUsedOverTaintedVariable(node) || isAnyArgumentTainted(node)) {
					return true;
				}
				


			} else if (node.hasDescendantOfType(ASTName.class)) {
				List<ASTName> astNames = node.findDescendantsOfType(ASTName.class);
				if (analyzeVariable(astNames)) {
					return true;
				}
			} else if (isUsedOverTaintedVariable(node)) {
				return true;
			}
			boolean childsTainted = isTainted(node);
			if (childsTainted) {
				return true;
			}
		}
		return tainted;

	}

	private boolean isUsedOverTaintedVariable(Node node) {
		ASTPrimaryPrefix prefix = node.getFirstChildOfType(ASTPrimaryPrefix.class);
		ASTPrimarySuffix suffix = node.getFirstChildOfType(ASTPrimarySuffix.class);
		if ((prefix == null || prefix.getImage() == null) && suffix != null && suffix.getImage() != null) {
			String fieldName = suffix.getImage();
			if (this.currentPathTaintedVariables.contains("this." + fieldName)) {
				return true;
			}
		}
		if (prefix != null) {
			ASTName astName = prefix.getFirstChildOfType(ASTName.class);
			if (astName != null) {
				String varName = getVarName(astName);
				return isTaintedVariable(varName);
			}
		}
		return false;
	}

	private String getVarName(ASTName name) {
		String varName = name.getImage();
		if (varName.startsWith("this.")) {
			varName = StringUtils.removeStart(varName, "this.");
		} else if (varName.contains(".")) {
			varName = StringUtils.split(varName, ".")[0];
		}
		if (varName.indexOf('.') != -1) {
			varName = varName.substring(varName.indexOf('.') + 1);
		}
		if (isField(name)) {
			varName = "this." + varName;
		}
		return varName;
	}

	private String getReturnType(Class<?> clazz, String realType, String methodName) {
		if (!this.cacheReturnTypes.containsKey(realType)) {
			populateCache(clazz, realType);
		}
		String retVal = this.cacheReturnTypes.get(realType + "." + methodName);
		if (StringUtils.isBlank(retVal)) {
			return UNKNOWN_TYPE;
		}
		return retVal;
	}

	private void populateCache(Class<?> clz, String realType) {
 		if (this.cacheReturnTypes.containsKey(realType)) {
			return;
		}
		this.cacheReturnTypes.put(realType, realType);
		Class<?> clazz = clz;
		if (clazz == null) {
			clazz = TypeUtils.INSTANCE.getClassForName(realType);
		}
		if (clazz == null) {
			return;
		}
		
		boolean analizeAnnotations = analizeTypeWithReflectionForAnnotations(clazz); 
		for (Method method : Utils.getMethods(clazz)) {
			Class<?> returnType = method.getReturnType();
			String methodName = method.getName();
			String key = clazz.getCanonicalName() + "." + methodName;

			String returnTypeName = UNKNOWN_TYPE;
			if (returnType != null) {
				returnTypeName = returnType.getCanonicalName();
			}
			String old = this.cacheReturnTypes.get(key);
			if (old == null || StringUtils.equals(old, returnTypeName)) {
				this.cacheReturnTypes.put(key, returnTypeName);
			}
			// else {
			// // various return types for same method
			// cacheReturnTypes.put(key, UNKNOWN_TYPE);
			// }

			if (analizeAnnotations) {
				registerAnnotations(key, method.getAnnotations());
			}
		}
		for (Constructor<?> constructor: Utils.getConstructors(clazz)) {
			String methodName = clazz.getSimpleName();
			String key = clazz.getCanonicalName() + "." + methodName;
			String retunTypeName = clazz.getCanonicalName();
			this.cacheReturnTypes.put(key, retunTypeName);
			if (analizeAnnotations) {
				registerAnnotations(key, constructor.getAnnotations());
			}
		}
		
	}

	private void registerAnnotations(String key, Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (this.sinkAnnotations.contains(annotation
					.annotationType().getCanonicalName())) {
				this.annotatedSinks.add(key);
			}
			if (this.generatorAnnotations.contains(annotation
					.annotationType().getCanonicalName())) {
				this.annotatedGenerators.add(key);
			}
		}
	}

	private String getReturnType(Node node, String type, String methodName) {
		String realType = type;

		Class<?> clazz = null;
		if (StringUtils.isBlank(realType) || UNKNOWN_TYPE.equals(realType)) {
			clazz = getContainingType(node);
			if (clazz != null) {
				realType = clazz.getCanonicalName();
			}
		}
		if (StringUtils.isBlank(realType) || UNKNOWN_TYPE.equals(realType)) {
			return UNKNOWN_TYPE;
		}
		return getReturnType(clazz, realType, methodName);

	}

	private List<ASTPrimaryExpression> getExp(Node node2) {

		List<ASTPrimaryExpression> expressions = new ArrayList<ASTPrimaryExpression>();
		int numChildren = node2.getNumChildren();
		for (int i = 0; i < numChildren; i++) {
			Node child = node2.getChild(i);
			if (child.getClass() == ASTPrimaryExpression.class) {
				expressions.add((ASTPrimaryExpression) child);
			} else {
				expressions.addAll(getExp(child));
			}
		}

		return expressions;
	}

	private String getMethod(Node node) {
		if (node == null) {
			return "";
		}
		String method = getFullMethodName(node);
		if (method.indexOf('.') != -1) {
			method = method.substring(method.lastIndexOf('.') + 1);
		}
		return method;
	}

	private String getFullMethodName(Node node) {
		ASTClassOrInterfaceType astClass = node.getFirstChildOfType(ASTClassOrInterfaceType.class);
		if (astClass != null) {
			return astClass.getImage();
		}
		ASTPrimaryPrefix prefix = node.getFirstChildOfType(ASTPrimaryPrefix.class);

		if (prefix != null) {
			ASTName astName = prefix.getFirstChildOfType(ASTName.class);
			if (astName != null && astName.getImage() != null) {
				return astName.getImage();
			}
			ASTAllocationExpression constructor = prefix.getFirstChildOfType(ASTAllocationExpression.class);
			if (constructor != null) {
				ASTArguments constructorArguments = constructor.getFirstChildOfType(ASTArguments.class);
				if (constructorArguments != null && constructorArguments.getArgumentCount() > 0) {
					Class<?> type = constructor.getType();
					if (type != null) {
						return type.getSimpleName();
					}
				}
			}
		}
		if (prefix == null) {
			ASTName astName = node.getFirstDescendantOfType(ASTName.class);
			if (astName != null && astName.getImage() != null) {
				return astName.getImage();
			}
		}
		StringBuilder mName = new StringBuilder();
		List<ASTPrimarySuffix> suffixes = node.findChildrenOfType(ASTPrimarySuffix.class);
		for (ASTPrimarySuffix suffix : suffixes) {
			if (!suffix.hasDescendantOfType(ASTArguments.class) && suffix.getImage() != null) {
				if (mName.length() > 0) {
					mName.append(".");
				}
				mName.append(suffix.getImage());
			}
		}
		return mName.toString();
	}

	private String getType(Node node) {

		String cannonicalName = UNKNOWN_TYPE;
		Class<?> type = null;

		try {
			type = getJavaType(node);
			if (type != null) {
				cannonicalName = type.getCanonicalName();
			}
			return cannonicalName;
		} catch (Exception ex1) {
			return cannonicalName;
		}

	}
	


	private Class<?> getJavaType(Node node) {

		Class<?> type = null;
		if (node == null) {
			return null;
		}
		Class<?> nodeClass = node.getClass();
		if (nodeClass == ASTExpression.class) {
			ASTPrimaryExpression primaryExpression = node.getFirstChildOfType(ASTPrimaryExpression.class);
			if (primaryExpression != null) {
				ASTName astName = primaryExpression.getFirstChildOfType(ASTName.class);
				if (astName != null) {
					type = astName.getType();
				}
			}
		} else if (nodeClass == ASTPrimaryExpression.class) {
			ASTClassOrInterfaceType astClass = node.getFirstChildOfType(ASTClassOrInterfaceType.class);
			if (astClass != null) {
				type = astClass.getType();
			} else {
				ASTPrimaryPrefix prefix = node.getFirstChildOfType(ASTPrimaryPrefix.class);				
				ASTName astName = prefix.getFirstChildOfType(ASTName.class);
				if (astName != null) {					
					NameDeclaration dec = astName.getNameDeclaration();
					if (dec != null && dec.getNode() instanceof ASTVariableDeclaratorId) {
						ASTVariableDeclaratorId declarator = (ASTVariableDeclaratorId) dec.getNode();
						type = declarator.getType();
					}
				
					if (type == null) {						
						String parameterName = astName.getImage();
						if (parameterName.indexOf('.') > 0) {
							parameterName = parameterName.substring(0, parameterName.lastIndexOf('.'));
							type = TypeUtils.INSTANCE.getClassForName(parameterName, astName);						
						}
						if (type == null) {
							parameterName = astName.getImage();
							if (parameterName.indexOf('.') > 0) {
								parameterName = parameterName.substring(0, parameterName.indexOf('.'));
							}
							if (this.methodParameterTypes.containsKey(parameterName)) {
								type = this.methodParameterTypes.get(parameterName);
							} else {
								type = getTypeFromAttribute(node, parameterName);
							}
						}
					}
				} else {
					ASTPrimarySuffix suffix = node.getFirstChildOfType(ASTPrimarySuffix.class);
					ASTAllocationExpression constructor = prefix.getFirstChildOfType(ASTAllocationExpression.class);
					if (constructor != null && suffix == null) {
						type = constructor.getType();						
					}
					if (type == null && suffix != null) {						
						if (this.fieldTypes.containsKey(suffix.getImage())) {
							type = this.fieldTypes.get(suffix.getImage());
						} else {
							type = getTypeFromAttribute(node, suffix.getImage());
						}
					}

				}
			}
		} else if (nodeClass == ASTName.class) {
			type = ((ASTName) node).getType();
		} else if (nodeClass == ASTMethodDeclaration.class || nodeClass == ASTConstructorDeclaration.class) {
			type = getContainingType(node);
		}
		if (type != null) {
			populateCache(type, type.getCanonicalName());
		}
		return type;

	}

	private Class<?> getTypeFromAttribute(Node node, String attributeName) {
		Class<?> type = getContainingType(node);


		if (type != null) {
			
			List<Class<?>> inheritanceList = getInheritance(type);

			for (Class<?> clazz : inheritanceList) {
                                Class<?> t1 = this.fieldTypesAll.get(clazz.getCanonicalName() + "." + attributeName);
                                if (t1 != null) {
                                    return t1;
                                }
                                List<Field> fields = FieldUtils.getAllFieldsList(clazz);
                                for (Field field: fields) {
                                    this.fieldTypesAll.put(clazz.getCanonicalName() + "." + field.getName(), field.getType());
                                }
                                t1 = this.fieldTypesAll.get(clazz.getCanonicalName() + "." + attributeName);
                                if (t1 != null) {
                                    return t1;
                                } 				
			}
			
		}


		return type;
	}
        


	private List<Class<?>> getInheritance(Class<?> declaringClass) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Class<?> aux = declaringClass;
		while (aux != null) {
			list.add(aux);
			aux = aux.getSuperclass();
		}
		return list;
	}

	private Class<?> getContainingType (Node node) {
		ASTClassOrInterfaceDeclaration astClass = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
		if (astClass != null) {
			return astClass.getType();
		}
		return null;
	}
	
	private boolean analyzeVariable(List<ASTName> listOfAstNames) {
		for (ASTName name : listOfAstNames) {
			String var = getVarName(name);

			if (isTaintedVariable(var) || isSource(getType(name), var)) {
				return true;
			}
		}
		return false;
	}

	private boolean isField(ASTName name) {
		NameDeclaration declaration = name.getNameDeclaration();
		return declaration != null && !declaration.getNode().getParentsOfType(ASTFieldDeclaration.class).isEmpty();
	}
	
	private static class ClassInfo {
		private HashMap<String, Class<?>> fieldTypes = new HashMap<>();
		private HashSet<String> fieldsTainted = new HashSet<>();
	}

}
