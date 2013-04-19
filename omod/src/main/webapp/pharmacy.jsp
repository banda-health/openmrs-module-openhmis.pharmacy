<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="Add Orders" otherwise="/login.htm" redirect="/module/openhmis/pharmacy/pharmacy.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/openhmis/pharmacy/js/screen/entry.js" />

<h2><openmrs:message code="openhmis.pharmacy.prescriptionEntry" /></h2>

<div id="patient-view">
	<div id="patient-details" style="display: none;">
	</div>
	<div id="find-patient" style="display: none;">
		<openmrs:portlet id="findPatient" url="findPatient" parameters="size=full|hideAddNewPatient=true|showIncludeVoided=false" />
		<!-- Make sure that the global "doSelectionHandler" is hijacked -->
		<script type="text/javascript">window.doSelectionHandler = function(index, data) {
			curl([openhmis.url.backboneBase + 'js/openhmis'], function(openhmis) { openhmis.doSelectionHandler(index,data); });
		};</script>
	</div>
</div>

<input type="submit" id="saveOrders" value="Create Prescriptions" />

<%@ include file="/WEB-INF/template/footer.jsp"%>