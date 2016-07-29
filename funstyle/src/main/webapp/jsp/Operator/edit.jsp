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
<title>编辑用户</title>

</head>
<body>
	<div class="easyui-panel" title="编辑用户">
		<div style="padding:10px 60px 20px 60px">
			<form id="editOperatorForm<s:property value="#parameters.id"/>" method="post"
				action="/esurfingadmin/modifyOperator.action" enctype="multipart/form-data">
				<div style="display:none;">
					<input class="easyui-textbox" type="hidden" name="operator.id" />
				</div>
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
								style="width: 100%" disabled="disabled"
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
							data-options="prompt:'选择签名证书...'"
							style="width:100%">
							<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-tip"
							onclick="operator.downloadOperatorCert('<s:property value="#parameters.id"/>')">查看证书</a>	
						</td>
					</tr>
					<tr>
						<td>权限:</td>
						<td>
							<s:if test="#request.operator.type == 1">
								<table class="rightTable table table-bordered">
									<tr>
										<th>项目</th>
										<th>角色</th>
										<th>&nbsp;</th>
									</tr>
									<s:iterator value="#request.selectedUprojectRights" var="rel">
										<tr>
											<td>
												<s:property value="#rel.uprojectName"/>
												<input type='hidden' uprojectId='<s:property value="#rel.uprojectId"/>'>
											</td>
											<td>
												<s:property value="#rel.rightNames"/>
												<input type='hidden' name='operator.uprojectIdRightIds' value='<s:property value="#rel.uprojectIdRightId"/>'>
											</td>
											<td>
												<a href="javascript:;" class="easyui-linkbutton" onClick="operator.delRelUProjectRole(this)">删除</a>
											</td>
										</tr>
									</s:iterator>
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
							</s:if>
							<s:if test="#request.operator.type == 2">
								<div>
									全部权限
								</div>
							</s:if>
						</td>
					</tr>
					<tr>
						<td>创建时间:</td>
						<td><input class="easyui-textbox" name="operator.createTime" disabled="disabled" /></td>
					</tr>
					<tr>
						<td>最后修改时间:</td>
						<td><input class="easyui-textbox" name="operator.lastModifyTime" disabled="disabled" /></td>
					</tr>
					<tr>
						<td>是否冻结:</td>
						<td><select class="easyui-combobox" name="operator.status" disabled="disabled"
							data-options="prompt:'必选',required:true" style="width: 100%">
								<option value="0">否</option>
								<option value="1">是</option>
						</select></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox"
							data-options="multiline:true" style="width:300px;height:100px"
							name="operator.memo" /></td>
					</tr>
					<tr>
						<td><a href="javascript:;" class="easyui-linkbutton" iconCls="icon-ok"
							onclick="operator.update('<s:property value="#parameters.id"/>')">保存</a></td>

					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
