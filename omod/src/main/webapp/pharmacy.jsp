<%@ include file="/WEB-INF/template/include.jsp"%>
<%--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 2.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  ~ License for the specific language governing rights and limitations
  ~ under the License.
  ~
  ~ Copyright (C) OpenMRS, LLC.  All Rights Reserved.
  --%>
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