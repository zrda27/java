<%--
  Created by IntelliJ IDEA.
  User: zrd
  Date: 2016/7/3
  Time: 12:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<title>查询用户</title>
</head>
<body>
	<div class="easyui-panel" title="查询条件"">
		<div style="padding:10px 60px 20px 60px">
			<form id="selectOperatorByPageForm" method="post"
				action="/esurfingadmin/selectOperatorByPage.action">
				<table class="form-table">
					<tr>
						<td>名称:</td>
						<td><input class="easyui-textbox" type="text"
							name="operator.name" /></td>
					</tr>
					<tr>
						<td>类型:</td>
						<td>
							<select class="easyui-combobox" 
								name="operator.type"
								style="width: 100%">
								<option value="">--未选择--</option>
								<s:iterator value="@net.netca.esurfing.common.bo.Operator@createTypeOptionString()" id="key">
									<option  value="<s:property value="value.code"/>"><s:property value="value.description"/></option>
							    </s:iterator> 
							</select>
						</td>
					</tr>
					<tr>
						<td>状态</td>
						<td>
							<select class="easyui-combobox" 
								name="operator.status"
								style="width: 100%">
								<option value="">--未选择--</option>
								<option value="0">正常</option>
								<option value="1">冻结</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><a href="javascript:;" class="easyui-linkbutton"
							iconCls="icon-ok" onclick="operator.select()">查询</a></td>
						<td><a href="javascript:;" class="easyui-linkbutton"
							iconCls="icon-undo"
							onclick="clearForm('selectOperatorByPageForm')">清空</a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<table id="selectOperatorResult" class="easyui-datagrid" title="查询结果"
		toolbar="#operatorToolbar"
		data-options="pagination:true,singleSelect:true,collapsible:true,fitColumns:true,url:'/esurfingadmin/selectOperatorByPage.action',method:'post'">
		<thead>
			<tr>
				<th data-options="field:'id'">编号</th>
				<th data-options="field:'name'">名称</th>
				<th data-options="field:'signCertDigest'">签名证书摘要</th>
				<th data-options="field:'typeName'">类型</th>
				<th data-options="field:'status'">冻结(是:1 否:0)</th>
				<th data-options="field:'lastModifyTime'">修改时间</th>
			</tr>
		</thead>
	</table>
	<div id="operatorToolbar">
		<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-edit"
			plain="true" onclick="operator.gotoEdit()">编辑</a> <a
			href="javascript:;" class="easyui-linkbutton" iconCls="icon-remove"
			plain="true" onclick="operator.freeze()">冻结/解冻</a>
	</div>
</body>
</html>
