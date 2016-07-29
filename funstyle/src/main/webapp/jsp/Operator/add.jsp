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
<title>增加用户</title>
</head>
<body>
	<div class="easyui-panel" title="增加用户">
		<div style="padding:10px 60px 20px 60px">
			<form id="addOperatorForm" method="post"
				action="/esurfingadmin/addOperator.action"
				enctype="multipart/form-data">
				<table class="form-table">
					<tr>
						<td>编号:</td>
						<td><input class="easyui-textbox" disabled="disabled"
							type="text" name="operator.id" /></td>
					</tr>
					<tr>
						<td>名称:</td>
						<td><input class="easyui-textbox"
							data-options="prompt:'必填',required:true" name="operator.name" /></td>
					</tr>
					<tr>
						<td>类型:</td>
						<td>
							<select class="easyui-combobox" 
								name="operator.type"
								style="width: 100%"
								data-options="prompt:'必选',required:true,onSelect: operator.typeChange">
								<s:iterator value="@net.netca.esurfing.common.bo.Operator@createTypeOptionString()" id="key">
									<option  value="<s:property value="value.code"/>"><s:property value="value.description"/></option>
							    </s:iterator> 
							</select>
						</td>
					</tr>
					<tr>
						<td>签名证书:</td>
						<td><input class="easyui-filebox" name="signCertFile"
							data-options="prompt:'选择签名证书...',required:true"
							style="width:100%"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox"
							data-options="multiline:true" style="width:300px;height:100px"
							name="operator.memo" /></td>
					</tr>
					<tr>
						<td>权限:</td>
						<td>
							<table id="operatorRight" class="rightTable table table-bordered">
								<tr>
									<th>项目</th>
									<th>角色</th>
									<th>&nbsp;</th>
								</tr>
								<tr>
									<td>
										<select class="easyui-combobox uprojectSelect" data-options="editable:false">
											<option value="">--请选择---</option>
											<s:iterator value="#request.uprojects" var="uproject">
												<option value="<s:property value="#uproject.id"/>"><s:property value="#uproject.name"/></option>
										    </s:iterator> 
										</select>
									</td>
									<td>
										<select class="easyui-combobox roleSelect" data-options="multiple:true,editable:false">
											<s:iterator value="#request.roles" var="role">
												<option value="<s:property value="#role.id"/>"><s:property value="#role.name"/></option>
										    </s:iterator> 
										</select>
									</td>
									<td><a href="javascript:;" class="easyui-linkbutton" iconCls="icon-add" onClick="operator.addRelUProjectRole(this)">新增</a></td>
								</tr>
							</table>
							<div id="adminRight" style="display: none;">
								全部权限
							</div>
						</td>
					</tr>
					<tr>
						<td><a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok"
							onclick="operator.save()">保存</a></td>

					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
