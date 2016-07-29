/**
 * 用于天翼u盾的增删改查
 * 
 * 关于简单增删改查模块的约定 *
	增删改查页面的位置及命名：
	/js/{moduleId}.js
	/jsp/{moduleId}/add.jsp
	/jsp/{moduleId}/edit.jsp
	/jsp/{moduleId}/select.jsp
	
	分页查询表单Id：select{moduleId}ByPageForm
	查询结果表格Id：select{moduleId}Result
	添加表单Id：add{moduleId}Form
	编辑表单Id：edit{moduleId}Form
	表单的属性：{moduleId}首字母小写.xx
	
	分页查询action：select{moduleId}ByPage.action
	冻结/解冻action：freeze{moduleId}.action
	修改action：modify{moduleId}.action
	增加action：add{moduleId}.action
 */
(function(){
	var _appName = "esurfingadmin";
	
	/**
	 * 构造方法
	 * moduleId 模块id
	 * moduleName 模块名称
	 * nameField row的名称字段，默认为name
	 */
	function CURD(moduleId, moduleName, nameField){
		nameField = nameField ? nameField : "name";
		
		this.addformId = "add" + moduleId + "Form";
		this.dataTableId = "select" + moduleId + "Result";
		this.editFormId = "edit" + moduleId + "Form";
		this.editPage = "/" + _appName + "/jsp/" + moduleId + "/" + "edit.jsp";
		this.selectFormId = "select" + moduleId + "ByPageForm";
		this.freezeUrl = "freeze" + moduleId + ".action";
		this.objectName = moduleId.substring(0,1).toLowerCase() + moduleId.substring(1);
		this.selectTabTitle = "查询" + moduleName;
		
		var _this = this;
		
		/**
		 * 增加
		 */
		function _saveOrUpdate(formId){
			$('#'+formId).form('submit',{
				onSubmit:function(){
					return $(this).form('enableValidation').form('validate');
				},
				success:function(data){
					var obj = eval('(' + data + ')');
					if((obj.success == null)||(obj.success.length == 0)){
						alert(obj.error);
					}else{
						alert(obj.success);
						var tab = $('#contentTabs').tabs('getSelected');
						var index = $('#contentTabs').tabs('getTabIndex',tab);
						setTimeout(function(){
							$('#contentTabs').tabs('close', index);
							if ($('#contentTabs').tabs('exists', _this.selectTabTitle)){
								$('#contentTabs').tabs('select', _this.selectTabTitle);
								$('#' + _this.dataTableId).datagrid('reload');
							}
						}, 1000);
					}
				}
			
			});
		};
		
		/**
		 * 增加
		 */
		this.save = function(){
			_saveOrUpdate(_this.addformId);
		};
		
		/**
		 * 更新
		 */
		this.update = function(id){
			_saveOrUpdate(_this.editFormId + id);
		};
		
		/**
		 * 冻结
		 */
		this.freeze = function(){
			var row = $('#' + _this.dataTableId).datagrid('getSelected');
			if(!row){
				alert("请先选择要冻结/解冻的项！");
				return;
			}
			var data = {};
			data[_this.objectName + "."+"id"] = row.id;
			$.messager.confirm('冻结/解冻', '确定要冻结/解冻吗？', function(r){
				if (r){
					$.ajax({
				  		type: "post",
				  		dataType: "json",
				  		async : false,
				  		data:data,
				  		url: _this.freezeUrl,
				  		success: function (data) {
				  			if(data.success){
				  				alert(data.success);
				  				$('#' + _this.dataTableId).datagrid('reload');
				  			}else{
				  				errAlert(data.error);
				  			}
				  		}
				  	});
				}
			});
		};
		/**
		 * 进入编辑
		 */
		this.gotoEdit = function(onload){
			var row = $('#' + _this.dataTableId).datagrid('getSelected');
			if(!row){
				alert("请先选择要编辑的项！");
				return;
			}
			var title = "编辑" + moduleName + "[" + row.id + (row[nameField] ? "-" + row[nameField] : "") + "]";
			var newrow = new Object();
			for(var key in row){
				if(isJson(row[key])){
					newrow[_this.objectName + "."+key] =JSON.stringify(row[key]); 
				}
				else{
				newrow[_this.objectName + "."+key] = row[key];
				}
			}
			if ($('#contentTabs').tabs('exists', title)){
			    $('#contentTabs').tabs('select', title);
			} else {
			    $('#contentTabs').tabs('add',{
			      title:title,
			      href: _this.editPage + "?id=" + row.id,
			      closable:true,
			      onLoad: function(){
			    	  _this.beforeFillDataToEdit(newrow);
			    	  $("#" + _this.editFormId + row.id).form("load", newrow);
			    	  _this.afterEnterEdit(row);
			      }
			    });
			}
		};
		/**
		 * 进入编辑后调用
		 */
		this.afterEnterEdit = function(row){
			//空实现，根据具体模块写代码
		};
		/**
		 * 填充编辑数据之前调用
		 */
		this.beforeFillDataToEdit = function(row){
		};
		/**
		 * 查询
		 */
		this.select = function(){
			var queryParams = getFormParams(_this.selectFormId);
		    $('#' + _this.dataTableId).datagrid({
		        method: "post",
		        url: $("#" + _this.selectFormId).attr("action"),
		        queryParams: queryParams,
		        onLoadSuccess: _this.afterSelect
		    });
		};
		/**
		 * 查询回调
		 */
		this.afterSelect = function(data){
			
		};
	};
	
	window.CURD = CURD;
})(window);