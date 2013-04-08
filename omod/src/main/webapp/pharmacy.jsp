<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="Add Orders" otherwise="/login.htm" redirect="/module/openhmis/pharmacy/pharmacy.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/openhmis/pharmacy/js/screen/entry.js" />

<h2><openmrs:message code="openhmis.pharmacy.prescriptionEntry" /></h2>


<%@ include file="/WEB-INF/template/footer.jsp"%>