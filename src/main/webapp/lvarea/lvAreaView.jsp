<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jb.model.TlvArea" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');		
	});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false">
		<table class="table table-hover table-condensed">
				<tr>	
					<th><%=TlvArea.ALIAS_CODE%></th>	
					<td>
						${lvArea.code}							
					</td>							
					<th><%=TlvArea.ALIAS_NAME%></th>	
					<td>
						${lvArea.name}							
					</td>							
				</tr>		
				<tr>	
					<th><%=TlvArea.ALIAS_PARENT_CODE%></th>	
					<td>
						${lvArea.parentCode}							
					</td>							
				</tr>		
		</table>
	</div>
</div>