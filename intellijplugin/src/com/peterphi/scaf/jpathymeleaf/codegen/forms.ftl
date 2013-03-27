<#-- @ftlvariable name="entity" type="com.peterphi.scaf.jpathymeleaf.codegen.PersistentMember" -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
	<title>${entity.name} Includes</title>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<link href="bootstrap.min.css" rel="stylesheet" media="screen"/>
</head>

<body id="top">
<div class="container">


<#macro stringField formId, field>
<#-- @ftlvariable name="field" type="com.peterphi.scaf.jpathymeleaf.codegen.PersistentMember" -->
	<input
			type="text"
			id="${formId}"
			name="${field.name}"
			placeholder=""
			th:value="${"$"}{entity.${field.name}}"/>
</#macro>
<#macro numberField formId, field>
<#-- @ftlvariable name="field" type="com.peterphi.scaf.jpathymeleaf.codegen.PersistentMember" -->
	<input
			type="number"
			id="input_${field.name}"
			name="${field.name}"
			placeholder=""
			th:value="${"$"}{entity.${field.name}}"/>
</#macro>

<#macro booleanField formId, field>
<#-- @ftlvariable name="field" type="com.peterphi.scaf.jpathymeleaf.codegen.PersistentMember" -->
	<input
			id="${formId}"
			name="${field.name}"
			type="checkbox"
			th:checked="${"$"}{entity.${field.name}}"/>
</#macro>

<#macro enumField formId, field>
<#-- @ftlvariable name="field" type="com.peterphi.scaf.jpathymeleaf.codegen.PersistentMember" -->
	<select id="${formId}" name="${field.name}">
	<#-- iterate over enum values as enumVal -->
		<option
				th:each="enumVal: ${"$"}{@${field.typeName}@values()}"
				th:selected="${"$"}{entity.${field.name}} == ${"$"}{enumVal}"
				th:value="${"$"}{enumVal.name()}"
				th:text="${"$"}{enumVal.toString()}">
			enumVal
		</option>
	</select>

</#macro>

<#macro htmlField field>
<#-- @ftlvariable name="field" type="com.peterphi.scaf.jpathymeleaf.codegen.PersistentMember" -->
	<!-- ${field.name} -->
	<div class="control-group">
		<label class="control-label" for="input_${field.name}">${field.name}</label>

		<div class="controls">
			<#if field.string>
				<@stringField formId="input_${field.name}" field=field />
			<#elseif field.number>
				<@numberField formId="input_${field.name}" field=field />
			<#elseif field.boolean>
				<@booleanField formId="input_${field.name}" field=field />
			<#elseif field.enum>
				<@enumField formId="input_${field.name}" field=field />
			<#else>
				<@stringField formId="input_${field.name}" field=field />
			</#if>

			<!-- Help / Warning text -->
			<span class="help-inline"></span>
		</div>
	</div>
</#macro>

	<h2>Render ${entity.name} as Definition List</h2>

	<dl class="dl-horizontal" th:fragment="render_dd">
	<#list entity.fields as field>
		<#if !field.collection>
			<dt>${field.name}</dt>
			<dd th:text="${"$"}{entity.${field.name}}">${field.name} value</dd>
		<#else>
			<!-- Ignored collection field ${field.name} -->
		</#if>
	</#list>
	</dl>

	<h2>Render ${entity.name} as Editable Form</h2>

	<form class="form-horizontal" method="POST" th:fragment="render_form">
	<#list entity.fields as field>
		<#if !field.collection>
			<@htmlField field=field />
		<#else>
			<!-- Ignored collection field ${field.name} -->
		</#if>
	</#list>
		<div class="control-group">
			<div class="controls">
				<button type="submit" class="btn btn-primary">Submit</button>
				<button type="reset" class="btn">Reset</button>
			</div>
		</div>
	</form>


	<h2>Render List of ${entity.name} as Table</h2>


	<table class="table table-striped" th:fragment="render_list_table">
		<thead>
		<tr>
		<#list entity.fields as field>
			<#if !field.collection>
				<th>${field.name}</th>
			</#if>
		</#list>
		</tr>
		</thead>
		<tbody>
		<tr th:each="entity: ${"$"}{entities}">
		<#list entity.fields as field>
			<#if !field.collection>
				<td th:text="${"$"}{entity.${field.name}}">${field.name}</td>
			</#if>
		</#list>
		</tr>
		</tbody>
	</table>
</div>
</body>
</html>
